package com.example.lockdoc;

import android.annotation.SuppressLint;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Document {

	private String filename;
	private String docType;
	private Date uploadDate;

	public Document(String filename, String type) {
		this.uploadDate = new Date();
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
		return docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}

	@SuppressLint("SimpleDateFormat")
	public String getUploadDate() {
		//returns a formatted string rather than a Date object
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		return dateFormat.format(this.uploadDate);
	}

}
