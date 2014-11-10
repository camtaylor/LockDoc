package com.example.lockdoc;

// Delete me later
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

	/*
	 * Previews the file to be uploaded and saves to database
	 */

	private static int TAKE_PICTURE = 1;
	private Uri imageUri;
	private static String logtag = "DocPreviewActivity";
	boolean newDoc = true;
	long id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_doc_preview);
		Bundle extras = getIntent().getExtras();
		if (extras == null)
			takePhoto();
		else {
			id = extras.getLong("ID");
			editPreview(id);
			newDoc = false;
		}
	}

	private void takePhoto() {
		// sends intent to built in Android camera
		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
		File photo = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				"picture.jpg");
		imageUri = Uri.fromFile(photo);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		startActivityForResult(intent, TAKE_PICTURE);
	}

	private void editPreview(long id) {
		DocSave db = new DocSave(this);
		Document doc;

		db.open();
		doc = db.getDocumentAtCursor(id);

		db.close();
		// Query database for obj with ID
		setContentView(R.layout.activity_doc_preview);
		EditText name = (EditText) findViewById(R.id.doc_name);
		name.setText(doc.getFilename());
		EditText type = (EditText) findViewById(R.id.doc_type);
		type.setText(doc.getDocType());
		// EditText description = (EditText) findViewById(R.id.doc_description);
		// type.setText(doc.getDocDescription());
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
				// sets image view to image
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
		// save in db and start new activity for classification
		boolean didItWork = true;
		if(newDoc){
		try {
			DocSave entry = new DocSave(this);
			entry.open();
			entry.createEntry(name, type, date);
			entry.close();
		} catch (Exception e) {
			didItWork = false;
		} finally {
			if (didItWork) {
				Toast.makeText(getApplicationContext(), "Image saved",
						Toast.LENGTH_LONG).show();
			}
		}
		}
		else{
			DocSave entry = new DocSave(this);
			entry.open();
			entry.editEntry(id, name, type);
			entry.close();
		}
		// starts file list after saving to database
		Intent view = new Intent(this, FileListActivity.class);
		startActivity(view);
	}

}