package net.shiftinpower.adapters;

import java.util.ArrayList;

import net.shiftinpower.koldrain.R;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

public class MessagesAdapter extends BaseAdapter {

	private ArrayList<String> userNames = new ArrayList<String>();
	private ArrayList<String> userAvatarPaths = new ArrayList<String>();
	private ArrayList<String> datesOfMessages = new ArrayList<String>();
	private ArrayList<String> messageContents = new ArrayList<String>();
	private ArrayList<Integer> userIds = new ArrayList<Integer>();
	private Context context;
	private LayoutInflater layoutInflater;
	
	private boolean lastMessageOutgoing = false;
	
	private ImageButton ivMessageFromUserImage;
	private ImageButton ivMessageFromUserSentOrReceivedIndicator;
	private TextView tvMessageFromUserName;
	private TextView tvMessageFromUserLastMessageDate;
	private TextView tvMessageFromUserContent;
	
	public MessagesAdapter(Context context, ArrayList<String> userNames, ArrayList<String> userAvatarPaths, ArrayList<String> datesOfMessages, ArrayList<String> messageContents, ArrayList<Integer> userIds){
		
		this.context = context;
		this.userNames = userNames;
		this.userAvatarPaths = userAvatarPaths;
		this.datesOfMessages = datesOfMessages;
		this.messageContents = messageContents;
		this.userIds = userIds;
		
		layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		return userIds.size();
	}

	@Override
	public Object getItem(int arg0) {
		return userIds.get(arg0)	;
	}

	@Override
	public long getItemId(int position) {
		return userIds.get(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null) {
			convertView = layoutInflater.inflate(R.layout.item_adapterable_inbox_message, parent, false);
		}
		
		ivMessageFromUserImage = (ImageButton) convertView.findViewById(R.id.ivMessageFromUserImage);
		ivMessageFromUserSentOrReceivedIndicator = (ImageButton) convertView.findViewById(R.id.ivMessageFromUserSentOrReceivedIndicator);
	    tvMessageFromUserName= (TextView) convertView.findViewById(R.id.tvMessageFromUserName);
		tvMessageFromUserLastMessageDate= (TextView) convertView.findViewById(R.id.tvMessageFromUserLastMessageDate);
		tvMessageFromUserContent= (TextView) convertView.findViewById(R.id.tvMessageFromUserContent);
		
		tvMessageFromUserName.setText(userNames.get(position));
		tvMessageFromUserLastMessageDate.setText(datesOfMessages.get(position));
		tvMessageFromUserContent.setText(messageContents.get(position));
		
		ivMessageFromUserImage.setImageBitmap(BitmapFactory.decodeFile(userAvatarPaths.get(position)));
		if(lastMessageOutgoing) {
			ivMessageFromUserSentOrReceivedIndicator.setImageResource(R.drawable.images_arrow_purple_up);
		}
		
		
		return convertView;
	} // End of getView

} // End of Class
