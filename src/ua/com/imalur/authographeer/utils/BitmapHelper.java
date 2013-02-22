package ua.com.imalur.authographeer.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

public class BitmapHelper {
	
   public static Bitmap getScaledBitmapFromFile(String photoPath, int screenWidth, int screenHeight) {
    	if (TextUtils.isEmpty(photoPath)){
    		return null;
    	}
		// Get the dimensions of the bitmap
		BitmapFactory.Options bmOptions = new BitmapFactory.Options();
		bmOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(photoPath, bmOptions);
		int photoW = bmOptions.outWidth;
		int photoH = bmOptions.outHeight;
				
		// Determine how much to scale down the image
		int scaleFactor = Math.min(photoW/screenWidth, photoH/screenHeight);
				
		// Decode the image file into a Bitmap sized to fill the View
		bmOptions.inJustDecodeBounds = false;
		bmOptions.inSampleSize = scaleFactor;
		bmOptions.inPurgeable = true;
				
		return BitmapFactory.decodeFile(photoPath, bmOptions);
    }
}
