package net.shiftinpower.utilities;

import android.os.Environment;

/**
* Checks whether the storage is locked for usage
*
* @author Kaloyan Roussev
*
*/

public class StorageStatusChecker {
	public static boolean isExternalStorageAvailable() {  
		  String extStorageState = Environment.getExternalStorageState();  
		  if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {  
		   return true;  
		  }  
		  return false;  
		 } 
} // End of Class
