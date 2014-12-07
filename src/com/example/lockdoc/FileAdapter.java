package com.example.lockdoc;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FileAdapter extends ArrayAdapter<Document> {

	/***
	 * Saves the adapters context and data (the files)
	 * 
	 * @param context
	 * @param fileList
	 */

	Activity context;
	private List<Document> fileList;

	public FileAdapter(Activity context, List<Document> fileList) {
		super(context, R.layout.document_layout, fileList);	
		
		this.context = context;
		this.fileList = fileList;
	}

	/*
	 * Returns a given Document's position in the list
	 */
	@Override 
	public Document getItem(int position){
		return fileList.get(position);
	}
	
	
	/**
	 * returns a view that has been filled with data for the row
	 */
	
	public View getView(int position, View convertView, ViewGroup parent){
		
		//get the document/file that corresponds to the row given by the position
		Document file = fileList.get(position);
		
		//inflate our row layout
		LayoutInflater inflater = context.getLayoutInflater();
		View docView = inflater.inflate(R.layout.document_layout, null);
		
		ImageView imageView;
		if(file.getDocType().equals("Personal")){
			
			imageView = (ImageView)docView.findViewById(R.id.typeView);
			imageView.setImageResource(R.drawable.personal);
		}
		else {
			imageView = (ImageView)docView.findViewById(R.id.typeView);
			imageView.setImageResource(R.drawable.business);
		}
		
		TextView nameView = (TextView)docView.findViewById(R.id.nameView);
		nameView.setText(file.getFilename());
		
		TextView dateView = (TextView)docView.findViewById((R.id.dateView));
		dateView.setText(file.getUploadDate());
		
		return docView;
	}
}
