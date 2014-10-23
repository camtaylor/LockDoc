package com.example.lockdoc;

import java.io.File;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class CaptureActivity extends Activity{

	private static int TAKE_PICTURE = 1;
	private Uri imageUri;
	private static String logtag = "CaptureActivity";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_capture);
	
		Button cameraButton = (Button)findViewById(R.id.button_camera);
		cameraButton.setOnClickListener(cameraListener);
	}
	
	private OnClickListener cameraListener = new OnClickListener() {
		public void onClick(View v){
			takePhoto(v);
		}
	};
	
	private void takePhoto(View v){
		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
		File photo = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "picture.jpg");
		imageUri = Uri.fromFile(photo);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		startActivityForResult(intent, TAKE_PICTURE);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent){
		super.onActivityResult(requestCode, resultCode, intent);
		
		if(resultCode == Activity.RESULT_OK){
			Uri selectedImage = imageUri;
			getContentResolver().notifyChange(selectedImage, null);
			ImageView imageView = (ImageView)findViewById(R.id.image_camera);
			ContentResolver cr = getContentResolver();
			Bitmap bitmap;
			
			try{
				bitmap = MediaStore.Images.Media.getBitmap(cr, selectedImage);
				imageView.setImageBitmap(bitmap);
				Toast.makeText(CaptureActivity.this, selectedImage.toString(), Toast.LENGTH_LONG).show();
			} catch(Exception e){
				Log.e(logtag, e.toString());
			}
		}
	}

}
