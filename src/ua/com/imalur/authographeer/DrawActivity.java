package ua.com.imalur.authographeer;

import java.io.File;
import java.io.FileOutputStream;

import ua.com.imalur.authographeer.utils.PhotoFileHelper;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class DrawActivity extends Activity {

	private AuthographView view;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        view = (AuthographView) findViewById(R.id.drawing_area);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
    	switch(item.getItemId()){
    	case R.id.menu_save:
    		saveDrawingToExternalCard();
    		
    		break;
    	
    	}
    	
    	
    	return true;
    }

	private void saveDrawingToExternalCard() {
//		1.	set Drawing Cache Enabled
//		2.	Draw whatever you want
//		3.	Get Bitmap object from view
//		4.	Compress and save the image file		
		view.setDrawingCacheEnabled(true);
		view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
		Bitmap bitmap = view.getDrawingCache();
		Uri filename = PhotoFileHelper.prepareDrawingToSave();
		File file = new File(filename.getEncodedPath());
		FileOutputStream ostream;
		try {
		    file.createNewFile();
		    ostream = new FileOutputStream(file);
		    bitmap.compress(CompressFormat.JPEG, 100, ostream);
		    ostream.flush();
		    ostream.close();
		    Toast.makeText(getApplicationContext(), R.string.toast_image_saved, 5000).show();
		} catch (Exception e) {
		    e.printStackTrace();
		    Toast.makeText(getApplicationContext(), R.string.toast_error_saving_image, 5000).show();
		}
		// add drawing to gallery
		PhotoFileHelper.addPhotoToGallery(this, filename);
		
	}
}
