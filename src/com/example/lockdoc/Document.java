package com.example.lockdoc;

import android.annotation.SuppressLint;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Document {

	/*
	 * Document Object for LocDoc Data Model for Database
	 */
	private long id;
	private String name;
	private String docType;
	private String uploadDate;
	private String privacy;
	private String description;
	private String filename;

	public Document(String name, String type){
		this.name = name;
		this.docType = type;
	}
	public Document(long id, String name, String type, String uploadDate, 
			String description, String privacy, String filename){
		this.id = id;
		this.name = name;
		this.docType = type;
		this.uploadDate = uploadDate;
		this.setDescription(description);
		this.setPrivacy(privacy);
		this.setFilename(filename);
	}

	public String getname() {
		return name;
	}

	public void setname(String name) {
		this.name = name;
	}

	public String getDocType() {
		return this.docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}

	@SuppressLint("SimpleDateFormat")
	public String formatUploadDate(Date docDate) {
		// returns a formatted string rather than a Date object
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		return dateFormat.format(docDate);
	}

	public String getUploadDate() {
		return this.uploadDate;
	}

	public long getID() {
		return this.id;
	}

	public void setID(long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPrivacy() {
		return privacy;
	}

	public void setPrivacy(String privacy) {
		//only 3 options for privacy
		if(privacy.equals("High"))
			this.privacy = privacy;
		else if(privacy.equals("Medium"))
			this.privacy = privacy;
		else
			this.privacy = "Low";
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

}
