package net.shiftinpower.utilities;

import android.os.Environment;

public class StorageStatusChecker {
	public static boolean isExternalStorageAvailable() {  
		  String extStorageState = Environment.getExternalStorageState();  
		  if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {  
		   return true;  
		  }  
		  return false;  
		 } 
} // End of Class
