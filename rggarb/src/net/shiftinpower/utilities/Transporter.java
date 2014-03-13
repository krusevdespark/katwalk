package net.shiftinpower.utilities;

import java.util.LinkedHashSet;

import net.shiftinpower.objects.ItemCategory;
import net.shiftinpower.objects.ItemExtended;
import net.shiftinpower.objects.ItemSubcategory;
import net.shiftinpower.objects.TemporaryImage;
import net.shiftinpower.objects.ItemBasic;
import net.shiftinpower.objects.UserExtended;

/**
 * Android provides a system to pass objects between activities that is not at all flexible and requires a lot of typing We
 * are going to circumvent that by using a simple custom Transporter singleton class
 * 
 * @author Kaloyan Roussev
 */
public class Transporter {
	
	public Transporter() {
		
	}

	static Transporter transporter = null;

	public static Transporter instance() {
		if (transporter == null) {
			transporter = new Transporter();
		}
		return transporter;
	}

	// Add any type of object that might need to be transported to another activity
	public LinkedHashSet<TemporaryImage> itemImages;
	public LinkedHashSet<ItemCategory> itemCategories;
	public LinkedHashSet<ItemSubcategory> itemSubcategories;
	public ItemBasic itemBasic;
	public ItemExtended itemExtended;
	public UserExtended userExtended;
	public ItemBasic userItem;

} // End of Class
