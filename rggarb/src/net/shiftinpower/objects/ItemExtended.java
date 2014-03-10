package net.shiftinpower.objects;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import net.shiftinpower.objects.ItemBasic;
import net.shiftinpower.objects.ItemCategory;
import net.shiftinpower.objects.ItemSubcategory;
import net.shiftinpower.objects.Place;
import net.shiftinpower.objects.UserBasic;

public class ItemExtended {
	
	private ItemBasic itemBasic;
	private Brand brand;
	private LinkedHashSet<Image> itemImages; 
	private LinkedHashSet<ItemCategory> itemCategories;
	private LinkedHashSet<ItemSubcategory> itemSubcategories;
	private LinkedHashSet<UserBasic> itemUsers;   
	private LinkedHashSet<Place> itemPlaces;
	private LinkedHashSet<ItemComment> itemComments;
	private LinkedHashSet<Rating> itemRatings;
	private ArrayList<Integer> itemFollowers;
	
	public ItemExtended (ItemBasic itemBasic, Brand brand, LinkedHashSet<Image> itemImages, LinkedHashSet<ItemCategory> itemCategories,
			LinkedHashSet<ItemSubcategory> itemSubcategories, LinkedHashSet<UserBasic> itemUsers, LinkedHashSet<Place> itemPlaces, 
			LinkedHashSet<ItemComment> itemComments, ArrayList<Integer> itemFollowers, LinkedHashSet<Rating> itemRatings){
		
		this.itemBasic = itemBasic;
		this.itemImages = itemImages;
		this.itemCategories = itemCategories;
		this.itemSubcategories = itemSubcategories;
		this.itemUsers = itemUsers;
		this.itemPlaces = itemPlaces;
		this.brand = brand;
		this.itemComments = itemComments;
		this.itemFollowers = itemFollowers;
		this.itemRatings = itemRatings;
		
	}
	
	public ItemExtended() {
		
	}
	
	public LinkedHashSet<Rating> getItemRatings() {
		return itemRatings;
	}

	public void setItemRatings(LinkedHashSet<Rating> itemRatings) {
		this.itemRatings = itemRatings;
	}

	public ArrayList<Integer> getItemFollowers() {
		return itemFollowers;
	}

	public void setItemFollowers(ArrayList<Integer> itemFollowers) {
		this.itemFollowers = itemFollowers;
	}

	public LinkedHashSet<ItemComment> getItemComments() {
		return itemComments;
	}

	public void setItemComments(LinkedHashSet<ItemComment> itemComments) {
		this.itemComments = itemComments;
	}

	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}

	public ItemBasic getItemBasic() {
		return itemBasic;
	}
	public void setItemBasic(ItemBasic itemBasic) {
		this.itemBasic = itemBasic;
	}
	public LinkedHashSet<Image> getItemImages() {
		return itemImages;
	}
	public void setItemImages(LinkedHashSet<Image> itemImages) {
		this.itemImages = itemImages;
	}
	public LinkedHashSet<ItemCategory> getItemCategories() {
		return itemCategories;
	}
	public void setItemCategories(LinkedHashSet<ItemCategory> itemCategories) {
		this.itemCategories = itemCategories;
	}
	public LinkedHashSet<ItemSubcategory> getItemSubcategories() {
		return itemSubcategories;
	}
	public void setItemSubcategories(LinkedHashSet<ItemSubcategory> itemSubcategories) {
		this.itemSubcategories = itemSubcategories;
	}
	public LinkedHashSet<UserBasic> getItemUsers() {
		return itemUsers;
	}
	public void setItemUsers(LinkedHashSet<UserBasic> itemUsers) {
		this.itemUsers = itemUsers;
	}
	public LinkedHashSet<Place> getItemPlaces() {
		return itemPlaces;
	}
	public void setItemPlaces(LinkedHashSet<Place> itemPlaces) {
		this.itemPlaces = itemPlaces;
	}
	
}