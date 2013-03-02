package ua.com.imalur.authographeer;

import ua.com.imalur.authographeer.utils.PhotoFileHelper;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class DrawActivity extends Activity {

	private AuthographView view;	
	private String photoPath;
	
	private static final String PAINT_WIDTH = "width";
	private static final String PAINT_COLOR = "color";
	SharedPreferences paintPreferences;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD)
        	setTitle(R.string.app_name);
        setContentView(R.layout.draw);
                       
        view = (AuthographView) findViewById(R.id.drawing_area);
        photoPath = getIntent().getStringExtra(MainScreen.EXTRA_PHOTOPATH);
        view.setBackgroundPath(photoPath);
    }
    /**
     * Загрузка последней кисти
     * @see android.app.Activity#onStart()
     */
    @Override
    protected void onStart() {
    	super.onStart();
    	paintPreferences = getPreferences(MODE_PRIVATE);
    	int color = paintPreferences.getInt(PAINT_COLOR, AuthographView.DEF_COLOR);
    	float width = paintPreferences.getFloat(PAINT_WIDTH, AuthographView.DEF_WIDTH);
    	view.setCurrentPaintParams(color, width);
    }
    /**
     * Сохранение текущей кисти
     * @see android.app.Activity#onStop()
     */
    @Override
    protected void onStop() {
    	super.onStop();
    	Paint paint = view.getCurrentPaint();
    	paintPreferences = getPreferences(MODE_PRIVATE);
    	Editor editor = paintPreferences.edit();
    	editor.putInt(PAINT_COLOR, paint.getColor());
    	editor.putFloat(PAINT_WIDTH, paint.getStrokeWidth());
    	editor.commit();
    }

    @Override
    public void onBackPressed() {
    	if (!view.isSaved())
    		showSaveDialog(); 
    	else
    		super.onBackPressed();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
    	switch(item.getItemId()){
    	case R.id.menu_pencil:
    		showPaintOptionsDialog( view.getCurrentPaint());
    		break;
    	case R.id.menu_undo:
    		view.undo();
    		view.setSaved(false);
    		break;
    	case R.id.menu_clear:
    		view.clear();
    		view.setSaved(false);
    		break;
    	case R.id.menu_save:
    		PhotoFileHelper.saveDrawingToExternalCard(this, view);
    		view.setSaved(true);
    		break;
    	}
    	return true;
    }

	private void showPaintOptionsDialog(Paint currentPaint) {
		PaintOptionsDialog dialog = 
				new PaintOptionsDialog(this, currentPaint);
		dialog.show();
	}
	
	private void showSaveDialog(){
		new AlertDialog.Builder(this)
		.setCancelable(false)
		.setTitle(getResources().getString(R.string.save_dialog_title))
		.setMessage(getResources().getString(R.string.save_dialog_message))
		.setPositiveButton(getResources().getString(android.R.string.yes), 
				new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which) {
						PhotoFileHelper.saveDrawingToExternalCard(DrawActivity.this, view);
			    		view.setSaved(true);
			    		DrawActivity.this.finish();
					}})
		.setNegativeButton(getResources().getString(android.R.string.no), 
				new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which) {
			    		DrawActivity.this.finish();
					}})
		.create()
		.show();
		
	}
}
