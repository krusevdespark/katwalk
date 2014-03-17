package net.shiftinpower.interfaces;

public interface OnChangeUserAvatarListener {
	void onChangeUserAvatarSuccess(String avatarPath);
	void onChangeUserAvatarFailure(String reason);
}
