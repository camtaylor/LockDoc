package com.example.lockdoc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
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
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Toast;

//import android.support.v7.widget.SearchView;

@SuppressLint("NewApi")
public class FileListActivity extends Activity implements OnItemClickListener,
		OnQueryTextListener {

	ArrayAdapter<Document> fileAdapter;
	// holds our file data
	ArrayList<Document> searchArray = new ArrayList<Document>();
	ArrayList<Document> fileArray = new ArrayList<Document>();
	ListView fileView;
	MenuItem menuItem;
	String searchInput = "";

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
		createList(fileArray);
		
		if(fileArray.size() == 0){
			Intent intent = new Intent(this, ActionOptionsActivity.class);
			startActivity(intent);
		}	

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.file_list, menu);

		// SearchView
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
				.getActionView();
		searchView.setOnQueryTextListener(this);
		return super.onCreateOptionsMenu(menu);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_search:
			Toast.makeText(this, "Search Selected", Toast.LENGTH_LONG).show();
			// TODO Create Search
			break;
		case R.id.action_logout:
			Toast.makeText(this, "Logging Out",  Toast.LENGTH_LONG).show();
			Intent logout = new Intent(this, LoginActivity.class);
			logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(logout);
			break;
		case R.id.action_capture:
			Intent add = new Intent(this, UploadActivity.class);
			startActivity(add);
		default:
			break;
		}
		return true;
	}

	public void createList(ArrayList<Document> fileList) {
		// creates the list that holds the files.
		fileAdapter = new FileAdapter(this, fileList);
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
		Document doc;
		if (searchInput.length() == 0)
			doc = fileArray.get(position);
		else
			doc = searchArray.get(position);

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
							try {
								shareFromList(item);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
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
		final int index = position;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Are you sure you want to delete this file?").setTitle("Delete");
		builder.setPositiveButton("Delete", new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int id){
				DocSave db = new DocSave(getApplicationContext());
				db.open();
				Document doc = fileArray.get(index);
				// deletes from db
				db.deleteEntry(doc.getID());
				// deletes from list
				fileArray.remove(index);
				db.close();
				// updates list
				createList(fileArray);
			}
		});
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	        	   
	           }
	       });
		builder.create().show();
		
		
	}

	public void shareFromList(int position) throws IOException {
		Document doc = fileArray.get(position);

		if (doc.getPrivacy().equals("Shareable")) {
			File inFile = new File(doc.getPath());
			File outFile = new File(
					Environment
							.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
					"lockdocshare.jpg");
			copyFile(inFile, outFile);
			Uri uri = Uri.fromFile(outFile);

			Intent share = new Intent(Intent.ACTION_SEND);
			share.setType("image/jpeg");
			share.putExtra(Intent.EXTRA_STREAM, uri);
			startActivity(Intent.createChooser(share, "Share Image"));
		} else {
			Toast.makeText(getApplicationContext(),
					"Locked up items are not shareable", Toast.LENGTH_LONG)
					.show();
		}
	}

	public void copyFile(File src, File dst) throws IOException {
		FileChannel inChannel = new FileInputStream(src).getChannel();
		FileChannel outChannel = new FileOutputStream(dst).getChannel();
		try {
			inChannel.transferTo(0, inChannel.size(), outChannel);
		} finally {
			if (inChannel != null)
				inChannel.close();
			if (outChannel != null)
				outChannel.close();
		}
	}


	@Override
	public boolean onQueryTextSubmit(String query) {
		Toast.makeText(this, query, Toast.LENGTH_SHORT).show();
		return true;
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		search(newText);
		return true;
	}

	public void search(String input) {
		searchArray.clear();
		searchInput = input;
		for (Document doc : fileArray) {
			String name = doc.getFilename();
			if (name.toLowerCase().contains(input.toLowerCase())) {
				searchArray.add(doc);
			}
		}
		createList(searchArray);
	}

}
