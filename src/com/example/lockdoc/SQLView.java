package com.example.lockdoc;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class SQLView extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sqlview);
		TextView tv = (TextView)findViewById(R.id.tvSQLinfo);
		DocSave doc = new DocSave(this);
		doc.open();
		String data = doc.getData();
		doc.close();
		tv.setText(data);
	}


}
