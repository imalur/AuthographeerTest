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
     * Обработка кнопок главного экрана
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
     *   Доступна ли камера на устройстве 
     */
    private boolean isCameraAvaliable(){
    	return getPackageManager()
        		.hasSystemFeature(
        				PackageManager.FEATURE_CAMERA);
    }
    
    /**
     * Запуск активности для рисования
     */
    private void launchDrawing(String photoPath) {
    	Intent intent = new Intent(this, DrawActivity.class);    	
    	intent.putExtra(EXTRA_PHOTOPATH, photoPath);
		startActivity(intent);		
	}
    
    /**
     * Запуск приложения для работы с камерой 
     * с возможностью сохранения снимка
     */
    private void launchCamera() {
    	Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		photoFileUri = PhotoFileHelper.prepareRawPhotoToSave();
		intent.putExtra(MediaStore.EXTRA_OUTPUT, photoFileUri);
		startActivityForResult(intent, IMAGE_FROM_CAMERA_ACTION);		
	}
    
    /**
     *  Выбор фото из галереи
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
	 *  Обработка откликов от камеры и галереи
	 */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	// Отклик от камеры - фото добавляется в галерею
   	    // и запускается активность для рисования
    	if (requestCode == IMAGE_FROM_CAMERA_ACTION && resultCode == RESULT_OK){    		
    		PhotoFileHelper.addPhotoToGallery(this, photoFileUri);
    		String photoPath = photoFileUri.getEncodedPath();
    		launchDrawing(photoPath);
    	}
    	// отклик от галереи
    	if (requestCode == IMAGE_FROM_GALLERY_ACTION && resultCode == RESULT_OK){
    		// получаем путь к файлу из Uri от gallery
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
