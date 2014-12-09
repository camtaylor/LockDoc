package com.example.lockdoc;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import com.rekap.lockdoc.R;

@SuppressLint("NewApi")
public class ActionOptionsActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_action_options);
		setTitle("LockDoc");

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(
				"To get started, tap the button below to Lock up your files")
				.setTitle("Welcome To LockDoc!");
		builder.setPositiveButton("Got it!",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

					}
				});

		builder.create().show();
	}

	public void capture(View v) {
		Intent captureDoc = new Intent(this, UploadActivity.class);
		startActivity(captureDoc);
	}

}
