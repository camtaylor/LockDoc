package com.example.lockdoc;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

public class DocViewerActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_doc_viewer);
		Bundle extras = getIntent().getExtras();
		setTitle(extras.getString("name"));
		String path = extras.getString("path");
		setImage(path);

	}

	public void setImage(String path) {
		try {
			ImageView iv = (ImageView) findViewById(R.id.image_view);
			FileInputStream is;
			is = new FileInputStream(path);
			Bitmap b = BitmapFactory.decodeStream(is);
			iv.setImageBitmap(b);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}
