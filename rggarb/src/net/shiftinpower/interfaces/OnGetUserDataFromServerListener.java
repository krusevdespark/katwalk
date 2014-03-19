package net.shiftinpower.interfaces;

import net.shiftinpower.objects.UserExtended;

public interface OnGetUserDataFromServerListener {
    void onGetUserDataFromServerSuccess(UserExtended userDetailsAndStats);
    void onGetUserDataFromServerFailure(String reason);
}
