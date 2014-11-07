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
	private String filename;
	private String docType;
	private String uploadDate;

	public Document(String filename, String type) {
		Date docDate = new Date();
		this.uploadDate = formatUploadDate(docDate);
		this.filename = filename;
		this.docType = type;
	}

	public Document(String filename, String type, String uploadDate) {
		this.uploadDate = uploadDate;
		this.filename = filename;
		this.docType = type;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
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

}
