package net.shiftinpower.objects;

public class UserBasic {

	private int userId;
	private String userName;
	private String userSex;
	private String userAvatar;
	private boolean userIsFriendOfCurrentUser;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserSex() {
		return userSex;
	}

	public boolean isUserFriendOfCurrentUser() {
		return userIsFriendOfCurrentUser;
	}

	public void setUserSex(String userSex) {
		this.userSex = userSex;
	}

	public String getUserAvatar() {
		return userAvatar;
	}

	public void setUserAvatar(String userAvatar) {
		this.userAvatar = userAvatar;
	}

	public void setUserIsFriendOfCurrentUser(boolean userIsFriendOfCurrentUser) {
		this.userIsFriendOfCurrentUser = userIsFriendOfCurrentUser;
	}

}
