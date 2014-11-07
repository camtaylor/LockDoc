package com.example.lockdoc;

import java.io.File;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DocPreviewActivity extends ActionBarActivity {

	private static int TAKE_PICTURE = 1;
	private Uri imageUri;
	private static String logtag = "DocPreviewActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_doc_preview);
		takePhoto();

	}

	private void takePhoto() {
		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
		File photo = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				"picture.jpg");
		imageUri = Uri.fromFile(photo);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		startActivityForResult(intent, TAKE_PICTURE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);

		if (resultCode == Activity.RESULT_OK) {

			Uri selectedImage = imageUri;
			getContentResolver().notifyChange(selectedImage, null);
			ImageView imageView = (ImageView) findViewById(R.id.image_camera);
			ContentResolver cr = getContentResolver();
			Bitmap bitmap;

			try {
				bitmap = MediaStore.Images.Media.getBitmap(cr, selectedImage);
				imageView.setImageBitmap(bitmap);
			} catch (Exception e) {
				Log.e(logtag, e.toString());
			}
		}
	}

	public void upload(View v) {
		// go back to upload option
		super.onBackPressed();
	}

	public void save(View v) {
		EditText docName = (EditText) findViewById(R.id.doc_name);
		String name = docName.getText().toString();
		EditText docType = (EditText) findViewById(R.id.doc_type);
		String type = docType.getText().toString();
		// TODO delete
		Document doc = new Document(name, type);
		String date = doc.getUploadDate();

		Toast.makeText(getApplicationContext(), name, Toast.LENGTH_LONG).show();
		// TODO save in db and start new activity for classification
		boolean didItWork = true;
		try {
			DocSave entry = new DocSave(this);
			entry.open();
			entry.createEntry(name, type, date);
			entry.close();
		} catch (Exception e) {
			didItWork = false;
		} finally {
			if (didItWork) {
				Dialog d = new Dialog(this);
				d.setTitle("Heck Yea!");
				TextView tv = new TextView(this);
				tv.setText("Success");
				d.setContentView(tv);
				d.show();
			}
		}

		Intent view = new Intent(this, SQLView.class);
		startActivity(view);
	}

}
