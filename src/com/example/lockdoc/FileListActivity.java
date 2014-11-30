package com.example.lockdoc;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class FileListActivity extends Activity implements OnItemClickListener {

	ArrayAdapter<Document> fileAdapter;
	// holds our file data
	ArrayList<Document> fileArray = new ArrayList<Document>();
	ListView fileView;
	MenuItem menuItem;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		setTitle("Your Files");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_file_list);
		
		ActionBar actionBar = getActionBar();
		// populate list from database
		DocSave db = new DocSave(this);
		db.open();
		fileArray = db.getDocumentList();
		db.close();

		createList();

	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.file_list,  menu);
		MenuItem searchItem = menu.findItem(R.id.action_search);
		SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
	    return super.onCreateOptionsMenu(menu);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
		case R.id.action_search:
			Toast.makeText(this, "Search Selected", Toast.LENGTH_LONG).show();
			//TODO Create Search
			break;
		case R.id.action_settings:
			Toast.makeText(this, "Settings Selected", Toast.LENGTH_LONG).show();
			//TODO Bring up Settings Section
			break;
		default:
			break;	
		}
		return true;
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
		Document doc = fileArray.get(position);
		Intent docViewer = new Intent(this, DocViewerActivity.class);
		docViewer.putExtra("path", doc.getPath());
		docViewer.putExtra("name", doc.getFilename());
		startActivity(docViewer);
	}

	public void editDialog(AdapterView<?> parent, View v, int position, long id) {
		// method to create an edit dialog box
		String[] options = { "Edit", "Delete", "Share" };
		final int item = position;
		final Document doc = fileArray.get(item);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(fileArray.get(position).getFilename()).setItems(
				options, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							/*
							 * Create intent and pass document to docPreview
							 * check if coming from new creation or update
							 * docPreview loads doc to location update method
							 */
							editFromList(doc);

							// EditDoc
							break;
						case 1:
							// delete doc
							deleteFromList(item);
							break;
						case 2:
							// Share Doc
							shareFromList(item);
							break;
						}

						// The 'which' argument contains the index position
						// of the selected item
					}
				});
		builder.create().show();
	}

	public void editFromList(Document doc) {
		Intent editDoc = new Intent(this, DocPreviewActivity.class);
		editDoc.putExtra("ID", doc.getID());
		startActivity(editDoc);
	}

	public void deleteFromList(int position) {
		DocSave db = new DocSave(this);
		db.open();
		Document doc = fileArray.get(position);
		// deletes from db
		db.deleteEntry(doc.getID());
		// deletes from list
		fileArray.remove(position);
		db.close();
		// updates list
		createList();
	}

	public void shareFromList(int position) {
		Document doc = fileArray.get(position);

		if (doc.getPrivacy().equals("Shareable")) {
			Intent share = new Intent(Intent.ACTION_SEND);
			share.setType("image/jpeg");
			share.putExtra(Intent.EXTRA_STREAM, Uri.parse(doc.getPath()));
			startActivity(Intent.createChooser(share, "Share Image"));
		}
		else{
			Toast.makeText(getApplicationContext(), "Locked up items are not shareable", Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent actionOptions = new Intent(this, ActionOptionsActivity.class);
		startActivity(actionOptions);
	}

}
