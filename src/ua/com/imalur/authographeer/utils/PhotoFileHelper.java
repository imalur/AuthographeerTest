package ua.com.imalur.authographeer.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

public class PhotoFileHelper {

	public static final String FILENAME_RAW_PREFIX = "RawAuthoPhoto_";
	public static final String FILENAME_PREFIX = "AuthoPhoto_";
	
	public static final String FILENAME_EXTENSION = ".jpg";
	
	/*
	 * ����������� ���� , ���������� � ������ , ��� ����������
	 */
	public static Uri preparePhotoToSave(String prefix) {
	    	// ���� ����� �������� �� ����� ������
	    	// ������� ����-����� ��� ����������� ����� �����
	    	String dateTime =  new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
			String filename = prefix + dateTime + FILENAME_EXTENSION;
						    
	    	File storageFile = new File(
					Environment.getExternalStoragePublicDirectory(
							Environment.DIRECTORY_PICTURES),
					filename);		
			return Uri.fromFile(storageFile);							
		}
	
	public static Uri prepareRawPhotoToSave(){
		return preparePhotoToSave(FILENAME_RAW_PREFIX);
	}
	
	public static Uri prepareDrawingToSave(){
		return preparePhotoToSave(FILENAME_PREFIX);
	}
	
	/*
	 * �������� ���� � �������
	 */
	public static void addPhotoToGallery(Context context, Uri uri) {
		Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		mediaScanIntent.setData(uri);
	    context.sendBroadcast(mediaScanIntent);
	}
}
