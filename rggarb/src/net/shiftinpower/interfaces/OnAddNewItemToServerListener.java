package net.shiftinpower.interfaces;

import android.os.Bundle;

public interface OnAddNewItemToServerListener {
	void onAddNewItemSuccess(Bundle dataRegardingTheItemJustAdded);
	void onAddNewItemFailure(String reason);
}
