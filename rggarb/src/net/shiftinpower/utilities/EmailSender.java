package net.shiftinpower.utilities;

import android.os.AsyncTask;

public class EmailSender extends AsyncTask<String, Void, Boolean> {
	String to;
	String from;
	String subject;
	String message;
	String[] attachments;
	Mail mail = new net.shiftinpower.utilities.Mail();
	
	public  EmailSender(String to, String from, String subject, String message, String[] attachments) {
		super();
		this.to = to;
		this.from = from;
		this.subject = subject;
		this.message = message;
		this.attachments = attachments;
	}
	
	@Override
	protected Boolean doInBackground(String... params) {
		
		if (subject != null && subject.length() > 0) {
			mail.setSubject(subject);
		} else {
			mail.setSubject("Subject");
		}

		if (message != null && message.length() > 0) {
			mail.setBody(message);
		} else {
			mail.setBody("Message");
		}
		mail.setTo(to);
		mail.setFrom(from);
		try {
			return mail.send();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
} // End of Class