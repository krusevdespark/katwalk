package net.shiftinpower.asynctasks;

import net.shiftinpower.core.C;
import net.shiftinpower.interfaces.OnContactUsSendEmailListener;
import net.shiftinpower.utilities.EmailSender;
import net.shiftinpower.utilities.ShowLoadingMessage;
import android.content.Context;
import android.os.AsyncTask;

public class ContactUsSendEmail extends AsyncTask<Void, Void, Void> {

	private OnContactUsSendEmailListener listener;
	private String userEmail;
	private String emailContent;
	private String emailLineOfInquiry;
	private Context context;

	public ContactUsSendEmail(Context context, OnContactUsSendEmailListener listener, String userEmail, String emailLineOfInquiry, String emailContent) {
		this.context = context;
		this.userEmail = userEmail;
		this.emailContent = emailContent;
		this.emailLineOfInquiry = emailLineOfInquiry;
		this.listener = listener;
	}

	@Override
	protected void onPreExecute() {
		ShowLoadingMessage.loading(context, C.LoadingMessages.SENDING_MESSAGE);
		super.onPreExecute();
	}

	@Override
	protected Void doInBackground(Void... params) {
		
		if (listener != null) {
			try {
				new EmailSender(C.Emailz.BRAGGR_OFFICIAL_ADDRESS, userEmail, C.Emailz.NEW_CONTACT_US_INQUIRY + emailLineOfInquiry, emailContent, null).execute();
				listener.onContactUsSendEmailSuccess();
			} catch (Exception e) {
				e.printStackTrace();
				listener.onContactUsSendEmailFailure();
			}
		}
		
		return null;

	} // End of doInBackground

	@Override
	protected void onPostExecute(Void result) {

		ShowLoadingMessage.dismissDialog();

		super.onPostExecute(null);

	}

} // End of Class
