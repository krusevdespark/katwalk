package net.shiftinpower.utilities;

import java.util.ArrayList;
import java.util.List;
import net.shiftinpower.koldrain.R;
import net.shiftinpower.objects.Category;
import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class LoadSpinnerData {
	
	public static void loadSpinnerData(Context context, ArrayList<String> array, Spinner spinner) {
        
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context,
                R.drawable.simple_spinner_item, array);
 
        // Drop down layout style - list view with radio button
        dataAdapter
                .setDropDownViewResource(R.drawable.simple_spinner_dropdown_item);
 
        // Attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
    }
	
	public static <T extends Category> void loadCategoriesIntoSpinner(Context context,  List<T> array, Spinner spinner) {
        
        // Creating adapter for spinner
        ArrayAdapter<T> dataAdapter = new ArrayAdapter<T>(context,
                R.drawable.simple_spinner_item, array);
 
        // Drop down layout style - list view with radio button
        dataAdapter
                .setDropDownViewResource(R.drawable.simple_spinner_dropdown_item);
 
        // Attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
    }
	
} // End of Class
