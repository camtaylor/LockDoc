package com.example.lockdoc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class UploadActivity extends Activity {
	/*
	 * Handle uploads here, from gallery or from camera capture
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_capture);
		setTitle("Upload File");

		Button cameraButton = (Button) findViewById(R.id.button_camera);
		cameraButton.setOnClickListener(cameraListener);
		Button selectImageButton = (Button) findViewById(R.id.selectImageButton);
		selectImageButton.setOnClickListener(selectListener);
	}

	private OnClickListener cameraListener = new OnClickListener() {
		public void onClick(View v) {
			Intent docPreview = new Intent(UploadActivity.this,
					DocPreviewActivity.class);
			startActivity(docPreview);
		}
	};

	private OnClickListener selectListener = new OnClickListener() {
		public void onClick(View v) {
			chooser();
		}
	};

	void chooser() {
		Intent i = new Intent(this, AbsolutePathActivity.class);
		i.putExtra("Select", true);
		startActivity(i);
	}

}
