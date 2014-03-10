package net.shiftinpower.objects;

public class Rating {

	private int itemId;
	private int itemRating;
	private int userThatRatedTheItem;
	private String timeOfRating;
	public int getItemId() {
		return itemId;
	}
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
	public int getItemRating() {
		return itemRating;
	}
	public void setItemRating(int itemRating) {
		this.itemRating = itemRating;
	}
	public int getUserThatRatedTheItem() {
		return userThatRatedTheItem;
	}
	public void setUserThatRatedTheItem(int userThatRatedTheItem) {
		this.userThatRatedTheItem = userThatRatedTheItem;
	}
	public String getTimeOfRating() {
		return timeOfRating;
	}
	public void setTimeOfRating(String timeOfRating) {
		this.timeOfRating = timeOfRating;
	}
	
}
