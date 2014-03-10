package net.shiftinpower.interfaces;

/**
 * Listener/Observer pattern.
 * 
 * @author Kaloyan Roussev
 * 
 */
import net.shiftinpower.objects.UserExtended;

public interface OnDownloadUserInfoFromServerListener {
	void onDownloadUserInfoFromServerSuccess(UserExtended userDetailsAndStats);

	void onDownloadUserInfoFromServerFailure(String reason);
}
