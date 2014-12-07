package com.example.lockdoc;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

@SuppressLint("NewApi")
public class ActionOptionsActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_action_options);
		setTitle("LockDoc");
	}

	public void capture(View v) {
		Intent captureDoc = new Intent(this, UploadActivity.class);
		startActivity(captureDoc);
	}

}
