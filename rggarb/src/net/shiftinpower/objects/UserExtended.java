package net.shiftinpower.objects;

public class UserExtended {
	private String userName;
	private String userEmail;
	private String userSex;
	private String userPassword;
	private String userLastSeen;
	private String userAvatarPath;
	private String userDateRegistered;
	private String userQuote;
	private int userPoints;
	private boolean userShowsMoney;
	private boolean userShowsStats;
	private String userAcceptsMessages;
	private String userInteractsWithActivities;
	private int userItemsCount;
	private int userCommentsCount;
	private int userFollowingItemsCount;
	private int userFriendsCount;
	private int userGalleryPhotosCount;
	private int userActivityCount;
	private double userMoneySpentOnItems;
	private boolean userIsFriendsWithCurrentUser;
	private boolean userHasFriendRequestFromUs;
	private boolean userHasSentUsFriendRequest;

	public UserExtended() {

	}
	
	public boolean isUserHasFriendRequestFromUs() {
		return userHasFriendRequestFromUs;
	}

	public void setUserHasFriendRequestFromUs(boolean userHasFriendRequestFromUs) {
		this.userHasFriendRequestFromUs = userHasFriendRequestFromUs;
	}

	public boolean isUserHasSentUsFriendRequest() {
		return userHasSentUsFriendRequest;
	}

	public void setUserHasSentUsFriendRequest(boolean userHasSentUsFriendRequest) {
		this.userHasSentUsFriendRequest = userHasSentUsFriendRequest;
	}

	public boolean isUserIsFriendsWithCurrentUser() {
		return userIsFriendsWithCurrentUser;
	}

	public void setUserIsFriendsWithCurrentUser(boolean userIsFriendsWithCurrentUser) {
		this.userIsFriendsWithCurrentUser = userIsFriendsWithCurrentUser;
	}

	public boolean isUserShowsMoney() {
		return userShowsMoney;
	}

	public void setUserShowsMoney(boolean userShowsMoney) {
		this.userShowsMoney = userShowsMoney;
	}

	public boolean isUserShowsStats() {
		return userShowsStats;
	}

	public void setUserShowsStats(boolean userShowsStats) {
		this.userShowsStats = userShowsStats;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public void setUserSex(String userSex) {
		this.userSex = userSex;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public void setUserLastSeen(String userLastSeen) {
		this.userLastSeen = userLastSeen;
	}

	public void setUserAvatarPath(String userAvatarPath) {
		this.userAvatarPath = userAvatarPath;
	}

	public void setUserDateRegistered(String userDateRegistered) {
		this.userDateRegistered = userDateRegistered;
	}

	public void setUserQuote(String userQuote) {
		this.userQuote = userQuote;
	}

	public void setUserPoints(int userPoints) {
		this.userPoints = userPoints;
	}

	public void setUserAcceptsMessages(String userAcceptsMessages) {
		this.userAcceptsMessages = userAcceptsMessages;
	}

	public void setUserInteractsWithActivities(String userInteractsWithActivities) {
		this.userInteractsWithActivities = userInteractsWithActivities;
	}

	public void setUserItemsCount(int userItemsCount) {
		this.userItemsCount = userItemsCount;
	}

	public void setUserCommentsCount(int userCommentsCount) {
		this.userCommentsCount = userCommentsCount;
	}

	public void setUserFollowingItemsCount(int userFollowingItemsCount) {
		this.userFollowingItemsCount = userFollowingItemsCount;
	}

	public void setUserFriendsCount(int userFriendsCount) {
		this.userFriendsCount = userFriendsCount;
	}

	public void setUserGalleryPhotosCount(int userGalleryPhotosCount) {
		this.userGalleryPhotosCount = userGalleryPhotosCount;
	}

	public void setUserActivityCount(int userActivityCount) {
		this.userActivityCount = userActivityCount;
	}

	public void setUserMoneySpentOnItems(double userMoneySpentOnItems) {
		this.userMoneySpentOnItems = userMoneySpentOnItems;
	}

	public String getUserName() {
		return userName;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public String getUserSex() {
		return userSex;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public String getUserLastSeen() {
		return userLastSeen;
	}

	public String getUserAvatarPath() {
		return userAvatarPath;
	}

	public String getUserQuote() {
		return userQuote;
	}

	public int getUserPoints() {
		return userPoints;
	}

	public String getUserDateRegistered() {
		return userDateRegistered;
	}

	public boolean doesUserShowMoney() {
		return userShowsMoney;
	}

	public int getUserItemsCount() {
		return userItemsCount;
	}

	public int getUserCommentsCount() {
		return userCommentsCount;
	}

	public int getUserFollowingItemsCount() {
		return userFollowingItemsCount;
	}

	public int getUserFriendsCount() {
		return userFriendsCount;
	}

	public int getUserGalleryPhotosCount() {
		return userGalleryPhotosCount;
	}

	public int getUserActivityCount() {
		return userActivityCount;
	}

	public boolean doesUserShowStats() {
		return userShowsStats;
	}

	public String getUserAcceptsMessages() {
		return userAcceptsMessages;
	}

	public String getUserInteractsWithActivities() {
		return userInteractsWithActivities;
	}

	public double getUserMoneySpentOnItems() {
		return userMoneySpentOnItems;
	}

} // End of Class
