package net.shiftinpower.interfaces;

import java.util.LinkedHashSet;
import net.shiftinpower.objects.UserBasic;

public interface OnGetUserFriendsListener {
	void onGetUserFriendsSuccess(LinkedHashSet<UserBasic> userFriends);
	void onGetUserFriendsFailure();
}
