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
import android.widget.ListView;

public class FileListActivity extends Activity implements OnItemClickListener {

	ArrayAdapter<Document> fileAdapter;
	// holds our file data
	ArrayList<Document> fileArray = new ArrayList<Document>();
	ListView fileView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		setTitle("Your Files");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_file_list);

	}

	public void createList() {
		// creates the list that holds the files.
		fileAdapter = new FileAdapter(this, fileArray);
		fileView = (ListView) findViewById(R.id.file_view);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// Open selected file for viewing

	}

}
