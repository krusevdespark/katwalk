package net.shiftinpower.interfaces;

/**
 * Listener/Observer pattern.
 * 
 * @author Kaloyan Roussev
 * 
 */
public interface OnChangeUserAvatarListener {
	void onChangeUserAvatarSuccess(String avatarPath);

	void onChangeUserAvatarFailure(String reason);
}
