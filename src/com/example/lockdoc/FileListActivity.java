package com.example.lockdoc;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;

public class FileListActivity extends Activity implements OnItemClickListener {

	
	ArrayAdapter<Document> fileAdapter;
	//holds our file data
	ArrayList<Document> fileArray = new ArrayList<Document>();

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		setTitle("Your Files");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_file_list);
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.file_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// Open selected file for viewing
		
	}
	
	public void createList(){
		//creates the list that holds the files.
	}
	

}
