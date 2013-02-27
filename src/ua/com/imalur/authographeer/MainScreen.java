package ua.com.imalur.authographeer;


import ua.com.imalur.authographeer.utils.PhotoFileHelper;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

public class MainScreen extends Activity {

	public static final int IMAGE_FROM_CAMERA_ACTION = 1010;
	public static final int IMAGE_FROM_GALLERY_ACTION = 1011;
	public static final String EXTRA_PHOTOPATH = "photoPath";
	
	private Uri photoFileUri;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);
        
        if (!isCameraAvaliable())
        	findViewById(R.id.btnCamera).setEnabled(false);
    }
    
    /**
     * ��������� ������ �������� ������
     */
    public void onButtonClick(View v){
    	switch(v.getId()){
    	case R.id.btnCamera:
    		launchCamera();
    		break;
    	case R.id.btnGallery:
    		launchGallery();
    		break;
    	case R.id.btnBlank:
    		launchDrawing(null);
    		break;    	
    	}
    }

	/**
     *   �������� �� ������ �� ���������� 
     */
    private boolean isCameraAvaliable(){
    	return getPackageManager()
        		.hasSystemFeature(
        				PackageManager.FEATURE_CAMERA);
    }
    
    /**
     * ������ ���������� ��� ���������
     */
    private void launchDrawing(String photoPath) {
    	Intent intent = new Intent(this, DrawActivity.class);    	
    	intent.putExtra(EXTRA_PHOTOPATH, photoPath);
		startActivity(intent);		
	}
    
    /**
     * ������ ���������� ��� ������ � ������� 
     * � ������������ ���������� ������
     */
    private void launchCamera() {
    	Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		photoFileUri = PhotoFileHelper.prepareRawPhotoToSave();
		intent.putExtra(MediaStore.EXTRA_OUTPUT, photoFileUri);
		startActivityForResult(intent, IMAGE_FROM_CAMERA_ACTION);		
	}
    
    /**
     *  ����� ���� �� �������
     */
    private void launchGallery() {
		Intent intent = new Intent();
	    intent.setType("image/*");
	    intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
        		Intent.createChooser(intent, "Select Picture"),
        		IMAGE_FROM_GALLERY_ACTION);                          		
	}
    
	/**
	 *  ��������� �������� �� ������ � �������
	 */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	// ������ �� ������ - ���� ����������� � �������
   	    // � ����������� ���������� ��� ���������
    	if (requestCode == IMAGE_FROM_CAMERA_ACTION && resultCode == RESULT_OK){    		
    		PhotoFileHelper.addPhotoToGallery(this, photoFileUri);
    		String photoPath = photoFileUri.getEncodedPath();
    		launchDrawing(photoPath);
    	}
    	// ������ �� �������
    	if (requestCode == IMAGE_FROM_GALLERY_ACTION && resultCode == RESULT_OK){
    		// �������� ���� � ����� �� Uri �� gallery
    		Uri uri = data.getData();    		    		
    		Cursor cursor = getContentResolver()
    	               .query(uri, null, null, null, null); 
    	    cursor.moveToFirst(); 
    	    int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
    	    String photoPath = cursor.getString(idx);    		
    		launchDrawing(photoPath);
    	}
    }
    
}
