package net.shiftinpower.interfaces;

/**
 * Listener/Observer pattern.
 * 
 * @author Kaloyan Roussev
 * 
 */
public interface OnChangeUserQuoteListener {
	void onChangeUserQuoteSuccess();

	void onChangeUserQuoteFailure(String reason);
}
