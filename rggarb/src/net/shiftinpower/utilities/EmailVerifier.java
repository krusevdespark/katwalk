package net.shiftinpower.utilities;

/**
 * Checks whether given string is in a valid email address format
 * 
 * @author Kaloyan Roussev
 * 
 */
public class EmailVerifier {
	public final boolean isEmailValid(CharSequence userEmail) {
		if (userEmail == null) {
			return false;
		} else {
			return android.util.Patterns.EMAIL_ADDRESS.matcher(userEmail).matches();
		}
	}
} // End of Class
