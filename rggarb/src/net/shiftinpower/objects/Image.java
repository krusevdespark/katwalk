package net.shiftinpower.objects;

import java.util.ArrayList;

// This class is in development stage and possibly not well thought of
public class Image {
	
	private int imageId;
	private String imageUrl;
	private String imageDescription;
	private String imageTimeAdded;
	private ArrayList<String> imageComments;
	private int imageAddedByUser;
	
	public int getImageAddedByUser() {
		return imageAddedByUser;
	}
	public void setImageAddedByUser(int imageAddedByUser) {
		this.imageAddedByUser = imageAddedByUser;
	}
	public int getImageId() {
		return imageId;
	}
	public void setImageId(int imageId) {
		this.imageId = imageId;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getImageDescription() {
		return imageDescription;
	}
	public void setImageDescription(String imageDescription) {
		this.imageDescription = imageDescription;
	}
	public String getImageTimeAdded() {
		return imageTimeAdded;
	}
	public void setImageTimeAdded(String imageTimeAdded) {
		this.imageTimeAdded = imageTimeAdded;
	}
	public ArrayList<String> getImageComments() {
		return imageComments;
	}
	public void setImageComments(ArrayList<String> imageComments) {
		this.imageComments = imageComments;
	}
	public ArrayList<Integer> getImageLikes() {
		return imageLikes;
	}
	public void setImageLikes(ArrayList<Integer> imageLikes) {
		this.imageLikes = imageLikes;
	}
	private ArrayList<Integer> imageLikes;
	
}
