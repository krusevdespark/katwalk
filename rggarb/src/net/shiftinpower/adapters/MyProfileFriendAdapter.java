package net.shiftinpower.adapters;

import java.util.ArrayList;

import net.shiftinpower.koldrain.R;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
/**
 * Just a draft
 * 
 * @author Kaloyan Roussev
 * 
 */
public class MyProfileFriendAdapter extends BaseAdapter {

	private ArrayList<String> userNames =  new ArrayList<String>();
	private ArrayList<String> userAvatarPaths   = new ArrayList<String>();
	private ArrayList<Integer> userIds = new ArrayList<Integer>();
	private Context context;
	private LayoutInflater layoutInflater;
	
	private ImageButton iMyFriendsFriendAvatarImage;
	private TextView tvMyFriendsListFriendName;
	private Button bMyFriendsMessage;
	private Button bMyFriendsUnfriend;
	
	public MyProfileFriendAdapter(Context context, ArrayList<String> userNames, ArrayList<String> userAvatarPaths,  ArrayList<Integer> userIds){
		
		this.context = context;
		this.userNames = userNames;
		this.userAvatarPaths = userAvatarPaths;
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
			convertView = layoutInflater.inflate(R.layout.item_adapterable_my_profile_friend, parent, false);
		}
		
		iMyFriendsFriendAvatarImage = (ImageButton) convertView.findViewById(R.id.iMyFriendsFriendAvatarImage);
		tvMyFriendsListFriendName = (TextView) convertView.findViewById(R.id.tvMyFriendsListFriendName);

		bMyFriendsMessage = (Button) convertView.findViewById(R.id.bMyFriendsMessage);
		bMyFriendsUnfriend = (Button) convertView.findViewById(R.id.bMyFriendsUnfriend);	
		
		tvMyFriendsListFriendName.setText(userNames.get(position));
		
		iMyFriendsFriendAvatarImage.setImageBitmap(BitmapFactory.decodeFile(userAvatarPaths.get(position)));
		
		return convertView;
		
	} // End of getView

} // End of Class