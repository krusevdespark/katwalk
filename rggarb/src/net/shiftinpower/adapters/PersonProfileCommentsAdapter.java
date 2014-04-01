package net.shiftinpower.adapters;

import java.util.ArrayList;

import net.shiftinpower.koldrain.R;
import android.content.Context;
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
public class PersonProfileCommentsAdapter extends BaseAdapter {

	private ArrayList<String> itemNames =  new ArrayList<String>();
	private ArrayList<Double> itemRatings = new ArrayList<Double>();
	private ArrayList<String> itemComments  = new ArrayList<String>();
	private ArrayList<String> itemCommentDates   = new ArrayList<String>();
	private ArrayList<Integer> itemIds = new ArrayList<Integer>();
	private Context context;
	private LayoutInflater layoutInflater;
	
	private ImageButton iMyCommentsCommentedProductImage;
	private TextView tvMyCommentsCommentedItemName;
	private TextView tvMyCommentsCommentedItemDate;
	private TextView tvMyCommentsCommentedItemContent;
	private TextView tvMyCommentsCommentedItemRating;
	private Button bMyCommentsEditComment;
	private Button bMyCommentsDeleteComment;
	
	public PersonProfileCommentsAdapter(Context context, ArrayList<String> itemNames, ArrayList<Double> itemRatings, ArrayList<String> itemComments, ArrayList<String> itemCommentDates, ArrayList<Integer> itemIds){
		
		this.context = context;
		this.itemNames = itemNames;
		this.itemComments = itemComments;
		this.itemCommentDates = itemCommentDates;
		this.itemRatings = itemRatings;
		this.itemIds = itemIds;
		
		layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		return itemIds.size();
	}

	@Override
	public Object getItem(int arg0) {
		return itemIds.get(arg0)	;
	}

	@Override
	public long getItemId(int position) {
		return itemIds.get(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null) {
			convertView = layoutInflater.inflate(R.layout.item_adapterable_my_profile_comment, parent, false);
		}
		// TODO Employ the ViewHolder pattern here

		
		iMyCommentsCommentedProductImage = (ImageButton) convertView.findViewById(R.id.iMyCommentsCommentedProductImage);
		tvMyCommentsCommentedItemName = (TextView) convertView.findViewById(R.id.tvMyCommentsCommentedItemName);
		tvMyCommentsCommentedItemDate = (TextView) convertView.findViewById(R.id.tvMyCommentsCommentedItemDate);
		tvMyCommentsCommentedItemContent = (TextView) convertView.findViewById(R.id.tvMyCommentsCommentedItemContent);
		tvMyCommentsCommentedItemRating = (TextView) convertView.findViewById(R.id.tvMyCommentsCommentedItemRating);
		bMyCommentsEditComment = (Button) convertView.findViewById(R.id.bMyCommentsEditComment);
		bMyCommentsDeleteComment = (Button) convertView.findViewById(R.id.bMyCommentsDeleteComment);	
		
		tvMyCommentsCommentedItemName.setText(itemNames.get(position));
		tvMyCommentsCommentedItemDate.setText(itemCommentDates.get(position));
		tvMyCommentsCommentedItemRating.setText(String.valueOf(itemRatings.get(position)));
		tvMyCommentsCommentedItemContent.setText(itemComments.get(position));
				
		
		return convertView;
		
	} // End of getView

} // End of Class
