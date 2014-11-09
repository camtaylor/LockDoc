package com.example.lockdoc;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
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

		// populate list from database
		DocSave db = new DocSave(this);
		db.open();
		fileArray = db.getDocumentList();
		db.close();

		createList();

	}

	public void createList() {
		// creates the list that holds the files.
		fileAdapter = new FileAdapter(this, fileArray);
		fileView = (ListView) findViewById(R.id.file_view);
		fileView.setAdapter(fileAdapter);
		fileView.setOnItemClickListener(this);
		fileView.setLongClickable(true);
		fileView.setOnItemLongClickListener(new OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> parent, View v,
					int position, long id) {
				// if the user long clicks an item go to edit dialog
				editDialog(parent, v, position, id);
				return true;
			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Open selected file for viewing

	}

	public void editDialog(AdapterView<?> parent, View v, int position, long id) {
		// method to create an edit dialog box
		String[] options = {"Edit", "Delete", "Share"};
		final int item = position;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setTitle(fileArray.get(position).getFilename())
	           .setItems(options, new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int which) {
	            	switch(which){
	            		case 0:
	            			//EditDoc
	            		   break;
	            		case 1:
	            			//delete doc
	            			deleteFromList(item);
	            		   break;
	            		case 2:
	            			//Share Doc
	            			break;
	            	}
	            		   
	               // The 'which' argument contains the index position
	               // of the selected item
	           }
	    });
		builder.create().show();
	}
	
	public void deleteFromList(int position){
		DocSave db = new DocSave(this);
		db.open();
		Document doc = fileArray.get(position);
		//deletes from db
		db.deleteEntry(doc.getID());
		//deletes from list
		fileArray.remove(position);
		db.close();
		//updates list
		createList();
	}

}
