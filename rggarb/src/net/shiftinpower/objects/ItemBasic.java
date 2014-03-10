package net.shiftinpower.objects;

public class ItemBasic {
	
	private int itemId;
	private int itemOwnerId;
	private String itemName;
	private String itemImage;
	private String itemTimeAdded;
	private String itemDescription;
	private String itemBrand;
	private double itemPriceAquired;
	private int itemRating;
	private boolean itemBoughtFromPlace;
	private String itemBoughtFromPlaceName;
	private boolean itemBoughtInConditionNew;
	private boolean itemBeingSold;
	private double itemPriceBeingSold;
	private boolean itemWasAGift;
	private int itemCategory;
	private int itemSubcategory;
	
	public int getItemRating() {
		return itemRating;
	}

	public void setItemRating(int itemRating) {
		this.itemRating = itemRating;
	}

	public String getItemBoughtFromPlaceName() {
		return itemBoughtFromPlaceName;
	}

	public void setItemBoughtFromPlaceName(String itemBoughtFromPlaceName) {
		this.itemBoughtFromPlaceName = itemBoughtFromPlaceName;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public int getItemId() {
		return itemId;
	}
	
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
	
	public int getItemOwnerId() {
		return itemOwnerId;
	}
	
	public void setItemOwnerId(int itemOwnerId) {
		this.itemOwnerId = itemOwnerId;
	}
	
	public String getItemTimeAdded() {
		return itemTimeAdded;
	}
	
	public void setItemTimeAdded(String itemTimeAdded) {
		this.itemTimeAdded = itemTimeAdded;
	}
	
	public String getItemDescription() {
		return itemDescription;
	}
	
	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}
	
	public String getItemBrand() {
		return itemBrand;
	}
	
	public void setItemBrand(String itemBrand) {
		this.itemBrand = itemBrand;
	}
	
	public double getItemPriceAquired() {
		return itemPriceAquired;
	}
	
	public void setItemPriceAquired(double itemPriceAquired) {
		this.itemPriceAquired = itemPriceAquired;
	}
	
	public boolean isItemBoughtFromPlace() {
		return itemBoughtFromPlace;
	}
	
	public void setItemBoughtFromPlace(boolean itemBoughtFromPlace) {
		this.itemBoughtFromPlace = itemBoughtFromPlace;
	}
	
	public boolean isItemBoughtInConditionNew() {
		return itemBoughtInConditionNew;
	}
	
	public void setItemBoughtInConditionNew(boolean itemBoughtInConditionNew) {
		this.itemBoughtInConditionNew = itemBoughtInConditionNew;
	}
	
	public boolean isItemBeingSold() {
		return itemBeingSold;
	}
	
	public void setItemBeingSold(boolean itemBeingSold) {
		this.itemBeingSold = itemBeingSold;
	}
	
	public double getItemPriceBeingSold() {
		return itemPriceBeingSold;
	}
	
	public void setItemPriceBeingSold(double itemPriceBeingSold) {
		this.itemPriceBeingSold = itemPriceBeingSold;
	}
	
	public boolean isItemWasAGift() {
		return itemWasAGift;
	}
	
	public void setItemWasAGift(boolean itemWasAGift) {
		this.itemWasAGift = itemWasAGift;
	}
	
	public int getItemCategory() {
		return itemCategory;
	}
	
	public void setItemCategory(int itemCategory) {
		this.itemCategory = itemCategory;
	}
	
	public int getItemSubcategory() {
		return itemSubcategory;
	}
	
	public void setItemSubcategory(int itemSubcategory) {
		this.itemSubcategory = itemSubcategory;
	}

	public String getItemImage(){
		return itemImage;
	}
	
	public void setItemImage(String itemImage) {
		this.itemImage = itemImage;
	}

} // End of Class
