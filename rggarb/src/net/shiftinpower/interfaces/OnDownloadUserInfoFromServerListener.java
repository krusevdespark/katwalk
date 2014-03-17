package net.shiftinpower.interfaces;

import net.shiftinpower.objects.UserExtended;

public interface OnDownloadUserInfoFromServerListener {
    void onDownloadUserInfoFromServerSuccess(UserExtended userDetailsAndStats);
    void onDownloadUserInfoFromServerFailure(String reason);
}
