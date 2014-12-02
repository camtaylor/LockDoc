package com.example.lockdoc;

// Delete me later
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Random;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
	private String internalPath;
	private Bitmap bitmap;
	private ImageView image;
	private static final int PICK_IMAGE = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_doc_preview);
		setTitle("Preview");
		Bundle extras = getIntent().getExtras();
		if (extras == null){
			takePhoto();
		}
		else if (extras.getBoolean("Select") == true) {
			selectImage();
		} else {
			id = extras.getLong("ID");
			editPreview(id);
			newDoc = false;
		}
	}

	public void selectImage() {
		Intent intent = new Intent("android.media.action.ACTION_GET_CONTENT");
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(Intent.createChooser(intent, "Select File"),
				PICK_IMAGE);
		
	}

	private void takePhoto() {
		// sends intent to built in Android camera
		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");

		File photo = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				"lockdoctemp.jpg");

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
		RadioGroup tg = (RadioGroup) findViewById(R.id.radioType);
		EditText description = (EditText) findViewById(R.id.doc_description);
		description.setText(doc.getDescription());
		RadioGroup rg = (RadioGroup) findViewById(R.id.radioPrivacy);

		try {
			ImageView iv = (ImageView) findViewById(R.id.image_camera);
			FileInputStream is;
			is = new FileInputStream(doc.getPath());
			Bitmap b = BitmapFactory.decodeStream(is);
			iv.setImageBitmap(b);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		if (doc.getPrivacy().equals("Locked Up"))
			rg.check(R.id.high_button);
		else
			rg.check(R.id.low_button);
		if (doc.getDocType().equals("Business"))
			tg.check(R.id.business_button);
		else
			tg.check(R.id.personal_button);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);	
		String filePath = null;
//		if (requestCode == PICK_IMAGE && resultCode == RESULT_OK
//				&& null != intent) {
//			Uri selectedImage = intent.getData();
//			String picPath = getPath(selectedImage);
//			if(picPath !=null){
//				filePath = picPath;
//				Bitmap myImage = BitmapFactory.decodeFile(picPath);
//				image.setImageBitmap(myImage);
//			}
//			else
//				Toast.makeText(getApplicationContext(), "Couldn't Upload Image NULL Error", Toast.LENGTH_LONG).show();
//			
//			
			
//		}
	if (resultCode == Activity.RESULT_OK) {

			Uri selectedImage = imageUri;
			getContentResolver().notifyChange(selectedImage, null);
			ImageView imageView = (ImageView) findViewById(R.id.image_camera);
			ContentResolver cr = getContentResolver();
			// Bitmap bitmap;

			ContextWrapper cw = new ContextWrapper(getApplicationContext());
			File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);

			Random randomInt = new Random();
			int tempInt = randomInt.nextInt(10000);
			File mypath = new File(directory, Integer.toString(tempInt)
					+ ".jpeg");
			internalPath = mypath.getAbsolutePath();
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(mypath);
				// sets image view to image
				bitmap = MediaStore.Images.Media.getBitmap(cr, selectedImage);
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
				fos.close();
				// load bitmap from internal storage
				Bitmap b = BitmapFactory.decodeStream(new FileInputStream(
						mypath));
				imageView.setImageBitmap(b);
			} catch (Exception e) {
				Log.e(logtag, e.toString());
			}
		}

		File toDelete = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				"lockdoctemp.jpg");
		toDelete.delete();
	
	}
	
	// Get Path from URI
		public String getPath(Uri uri){
			String[] filePath = { MediaStore.Images.Media.DATA };
			Cursor cursor = getContentResolver().query(uri, filePath,
						null, null, null);
			if(cursor!=null){
				int columnInd = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				cursor.moveToFirst();
				return cursor.getString(columnInd);
			}
			else
				return null;
			
		}

	public void decodeFile(String path) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, opt);

		final int REQUIRED_SIZE = 1024; // Scaled Size

		int width = opt.outWidth;
		int height = opt.outHeight;
		int scale = 1;
		while (true) {
			if (width < REQUIRED_SIZE && height < REQUIRED_SIZE)
				break;
			width /= 2;
			height /= 2;
			scale *= 2;
		}

		BitmapFactory.Options opt2 = new BitmapFactory.Options();
		opt2.inSampleSize = scale;
		bitmap = BitmapFactory.decodeFile(path, opt2);

		//image.setImageBitmap(bitmap);
	}

	public void upload(View v) {
		// go back to upload option
		super.onBackPressed();
	}

	public void save(View v) {
		EditText docName = (EditText) findViewById(R.id.doc_name);
		String name = docName.getText().toString();
		RadioGroup tg = (RadioGroup) findViewById(R.id.radioType);
		RadioButton typeButton = (RadioButton) findViewById(tg
				.getCheckedRadioButtonId());
		String type = typeButton.getText().toString();
		EditText docDescription = (EditText) findViewById(R.id.doc_description);
		String description = docDescription.getText().toString();
		Document doc = new Document(name, type);
		String date = doc.getUploadDate();
		// save in db and start new activity for classification
		RadioGroup rg = (RadioGroup) findViewById(R.id.radioPrivacy);
		RadioButton privacyButton = (RadioButton) findViewById(rg
				.getCheckedRadioButtonId());
		String privacy = privacyButton.getText().toString();
		
		
		
		boolean didItWork = true;

		// check if it is a new doc or old one
		if (newDoc) {
			try {
				DocSave entry = new DocSave(this);
				entry.open();
				entry.createEntry(name, type, date, description, privacy,
						internalPath);
				entry.close();
			} catch (Exception e) {
				didItWork = false;
			} finally {
				if (didItWork) {
					Toast.makeText(getApplicationContext(), "Image saved",
							Toast.LENGTH_LONG).show();
				}
			}
		} else {
			DocSave entry = new DocSave(this);
			entry.open();
			entry.editEntry(id, name, type, description);
			entry.close();
		}
		// starts file list after saving to database
		Intent view = new Intent(this, FileListActivity.class);
		view.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(view);
	}

}
