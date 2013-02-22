package ua.com.imalur.authographeer;

import java.io.File;
import java.io.FileOutputStream;

import ua.com.imalur.authographeer.utils.PhotoFileHelper;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class DrawActivity extends Activity {

	private AuthographView view;	
	private String photoPath;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        view = (AuthographView) findViewById(R.id.drawing_area);
        photoPath = getIntent().getStringExtra(MainScreen.EXTRA_PHOTOPATH);
        view.setBackgroundPath(photoPath);
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
    	case R.id.menu_save:
    		PhotoFileHelper.saveDrawingToExternalCard(this, view);
    		break;
    	}
    	return true;
    }
	

}
