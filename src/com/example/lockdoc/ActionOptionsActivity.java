package com.example.lockdoc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ActionOptionsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_action_options);
	}

	
	public void fileList(View v){
		
		Intent fileList = new Intent(this, FileListActivity.class);
		startActivity(fileList);
	}
	
	public void capture(View v){
		Intent captureDoc = new Intent(this, CaptureActivity.class);
		startActivity(captureDoc);
	}
}
