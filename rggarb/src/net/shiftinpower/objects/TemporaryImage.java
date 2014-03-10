package net.shiftinpower.objects;

import net.shiftinpower.core.C;

// Used for opeartional purposes, at the moment at the AddItemStepOnePhotos class
public class TemporaryImage {

	private String path = C.ImageHandling.TAG_DEFAULT_AS_SET_IN_DATABASE;
	private String filename;
	private String description = C.ImageHandling.TAG_DEFAULT_AS_SET_IN_DATABASE;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

} // End of Class
