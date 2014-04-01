package net.shiftinpower.adapters;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Locale;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import net.shiftinpower.activities.item.ItemProfilePrivate;
import net.shiftinpower.activities.item.ItemProfilePublic;
import net.shiftinpower.activities.person.MyProfile;
import net.shiftinpower.activities.person.AnotherUsersProfile;
import net.shiftinpower.core.C;
import net.shiftinpower.customviews.SquareImageView;
import net.shiftinpower.koldrain.R;
import net.shiftinpower.objects.ItemBasic;
import net.shiftinpower.objects.UserBasic;
import net.shiftinpower.utilities.Transporter;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class PersonProfileFriendsAdapter extends BaseAdapter {

	private ArrayList<String> userNames = new ArrayList<String>();
	private ArrayList<String> userAvatarPaths = new ArrayList<String>();
	private ArrayList<Integer> userIds = new ArrayList<Integer>();
	private ArrayList<String> userSexes = new ArrayList<String>();
	private Context context;
	private LayoutInflater layoutInflater;
	private LinkedHashSet<UserBasic> userFriends;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	private CharSequence autocompleteInput;
	private int userId;
	private int currentlyLoggedInUser;

	static class ViewHolder {
		SquareImageView iMyFriendsFriendAvatarImage;
		TextView tvMyFriendsListFriendName;
		Button bMyFriendsMessage;
		Button bMyFriendsUnfriend;
	}

	
	// Default constructor, showing all friends
	public PersonProfileFriendsAdapter(Context context, ImageLoader imageLoader, DisplayImageOptions options, int currentlyLoggedInUser,
			LinkedHashSet<UserBasic> userFriends) {

		this.context = context;
		this.imageLoader = imageLoader;
		this.options = options;
		this.userFriends = userFriends;
		this.currentlyLoggedInUser = currentlyLoggedInUser;

		imageLoader = ImageLoader.getInstance();
		layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		prepareFriendsList(userFriends);
	}
	
	// Constructor used from the search edittext, passing a part of a string, so the adapter only shows friends with that string in their names
	public PersonProfileFriendsAdapter(Context context, ImageLoader imageLoader, DisplayImageOptions options, int currentlyLoggedInUser,
			LinkedHashSet<UserBasic> userFriends, CharSequence autocompleteInput) {

		this.context = context;
		this.imageLoader = imageLoader;
		this.options = options;
		this.userFriends = userFriends;
		this.autocompleteInput = autocompleteInput;
		this.currentlyLoggedInUser = currentlyLoggedInUser;

		imageLoader = ImageLoader.getInstance();
		layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		prepareFriendsList(userFriends, autocompleteInput);
	}

	@Override
	public int getCount() {
		return userIds.size();
	}

	@Override
	public Object getItem(int arg0) {
		return userIds.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		return userIds.get(position);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.item_adapterable_my_profile_friend, parent, false);
		}

		ViewHolder holder = new ViewHolder();

		holder.iMyFriendsFriendAvatarImage = (SquareImageView) convertView.findViewById(R.id.iMyFriendsFriendAvatarImage);
		holder.tvMyFriendsListFriendName = (TextView) convertView.findViewById(R.id.tvMyFriendsListFriendName);
		holder.bMyFriendsMessage = (Button) convertView.findViewById(R.id.bMyFriendsMessage);
		holder.bMyFriendsUnfriend = (Button) convertView.findViewById(R.id.bMyFriendsUnfriend);
		holder.tvMyFriendsListFriendName.setText(userNames.get(position));

		imageLoader.displayImage(C.API.WEB_ADDRESS + C.API.IMAGES_USERS_FOLDER_THUMBNAIL + userAvatarPaths.get(position), holder.iMyFriendsFriendAvatarImage,
				options);

		convertView.setTag(holder);

		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				userId = (int) getItemId(position);

				UserBasic selectedUser = null;
				Iterator<UserBasic> itererator = userFriends.iterator();
				while ((selectedUser == null) && (itererator.hasNext())) {
					UserBasic local = itererator.next();
					if (local.getUserId() == userId) {
						selectedUser = local;
					}
				}

				Transporter.instance().userBasic = selectedUser;

				Intent goToUserProfile;

				if (selectedUser.getUserId() != currentlyLoggedInUser) {
					goToUserProfile = new Intent(context, AnotherUsersProfile.class);
					goToUserProfile.putExtra("personId", selectedUser.getUserId());
					goToUserProfile.putExtra("currentUser", false);
				} else {
					goToUserProfile = new Intent(context, AnotherUsersProfile.class);
					goToUserProfile.putExtra("currentUser", true);
				}

				context.startActivity(goToUserProfile);
			}
		});

		return convertView;

	} // End of getView

	private void prepareFriendsList(LinkedHashSet<UserBasic> userFriends) {
		for (UserBasic userBasic : userFriends) {
			userNames.add(userBasic.getUserName());
			userIds.add(userBasic.getUserId());
			userAvatarPaths.add(userBasic.getUserAvatar());
			userSexes.add(userBasic.getUserSex());
		}
	}
	
	private void prepareFriendsList(LinkedHashSet<UserBasic> userFriends,CharSequence autocompleteInput) {
		for (UserBasic userBasic : userFriends) {
			if(userBasic.getUserName().toLowerCase(Locale.getDefault()).contains((autocompleteInput.toString()).toLowerCase())){
				userNames.add(userBasic.getUserName());
				userIds.add(userBasic.getUserId());
				userAvatarPaths.add(userBasic.getUserAvatar());
				userSexes.add(userBasic.getUserSex());
			}
		}
	}

} // End of Class