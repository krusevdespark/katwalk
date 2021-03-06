package net.shiftinpower.adapters;

// This will not be an enum in the future, because enums consume lots of memory and are a no-no in android
public enum FeedEventType {
	ITEM_AVAILABLE_AT_PLACE,
	ITEM_HAS_BEEN_COMMENTED_ON,
	ITEM_IS_BEING_FOLLOWED,
	ITEM_IS_BEING_SOLD_BY,
	ITEM_IS_NOW_AMONG_THE_ITEMS_OF,
	PLACE_NOW_OPEN,
	USER_COMMENTED_ON_ITEM,
	USER_FOLLOWS_ITEM,
	USER_GAINED_STATUS,
	USER_HAS_RATED_ITEM,
	USER_JUST_GOT_ITEM,
	USER_JUST_SIGNED_UP,
	USER_NO_LONGER_HAS,
	USER_SELLS_ITEM
}
