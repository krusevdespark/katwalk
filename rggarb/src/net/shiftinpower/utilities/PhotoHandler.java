package net.shiftinpower.utilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import net.shiftinpower.core.C;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

public class PhotoHandler {

	private Context context;
	private String folderName = C.Preferences.RGGARB_DIRECTORY_ON_USER_STORAGE;
	private String filenamePrefix = C.ImageHandling.TEMP_IMAGE_FILENAME_PREFIX;
	private String filenameSuffix = C.ImageHandling.IMAGES_FILE_EXTENSION;
	private Bitmap imageBitmap;

	private String imagePath;

	public PhotoHandler(Context context) {
		this.context = context;
	}

	public void setImageBitmap(Bitmap imageBitmap) {
		this.imageBitmap = imageBitmap;
	}

	public Bitmap getImageBitmap() {
		return imageBitmap;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getImagePath() {
		return imagePath;
	}

	public File createImageFile() throws IOException {

		// Get current date and time
		String dateFormat = C.Miscellaneous.DATE_FORMAT;
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.US);
		String formattedDate = simpleDateFormat.format(calendar.getTime());

		String imageFileName = filenamePrefix + formattedDate + "_";
		File albumFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + folderName);
		File imageFile = File.createTempFile(imageFileName, filenameSuffix, albumFile);
		setImagePath(imageFile.toString());

		return imageFile;
	}

	public boolean handleBigCameraPhoto() {

		try {
			setImageBitmap(rotateBitmap(imagePath, getBitmapAndResizeIt(imagePath)));
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		try {
			FileOutputStream fileOutputStream = new FileOutputStream(createImageFile());
			getImageBitmap().compress(Bitmap.CompressFormat.JPEG, C.ImageHandling.IMAGE_SENT_TO_SERVER_QUALITY_0_T0_100, fileOutputStream);
			fileOutputStream.flush();
			fileOutputStream.close();
			galleryAddPic();
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

	} // End of handleBigCameraPhoto

	// This class is used when we are downloading a bitmap from the internet and we want to store it in a local file for
	// later reference
	// We createImageFile to store the file into and call galleryAddPic to make the file available
	// ImagePath is set within the createImageFile method
	// If we want to use the ImagePath we can call getImagePath
	public boolean handleIncomingPhoto(Bitmap bitmap) {

		try {
			FileOutputStream fileOutputStream = new FileOutputStream(createImageFile());
			bitmap.compress(Bitmap.CompressFormat.JPEG, C.ImageHandling.IMAGE_SENT_TO_SERVER_QUALITY_0_T0_100, fileOutputStream);
			fileOutputStream.flush();
			fileOutputStream.close();
			galleryAddPic();
			bitmap.recycle(); // NEW
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

	} // End of handleIncomingPhoto

	// This method is used when the user has picked an image from their phone gallery
	// The intent returns in the form of data and we get the Uri from it, then using a cursor and reading through a filtered
	// amount of data
	// The path to the image is obtained and we make it available for ourselves by setImagePath
	public boolean handleGalleryPhoto(Intent data) {

		Uri selectedImage = data.getData();
		String[] filePathColumn = { MediaStore.Images.Media.DATA };
		Cursor cursor = context.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
		cursor.moveToFirst();
		int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
		setImagePath(cursor.getString(columnIndex));

		try {
			Bitmap resizedResampledBitmap = getBitmapAndResizeIt(imagePath); // NEW
			Bitmap rotatedBitmap = rotateBitmap(imagePath, resizedResampledBitmap); // NEW
			resizedResampledBitmap.recycle(); // NEW
			setImageBitmap(rotatedBitmap);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		try {
			FileOutputStream fileOutputStream = new FileOutputStream(createImageFile());
			Bitmap imageBitmap = getImageBitmap(); // NEW
			imageBitmap.compress(Bitmap.CompressFormat.JPEG, C.ImageHandling.IMAGE_SENT_TO_SERVER_QUALITY_0_T0_100, fileOutputStream);
			fileOutputStream.flush();
			fileOutputStream.close();
			imageBitmap.recycle(); // NEW
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

	} // End of handleGalleryPhoto

	// When an image File is created it must be made available and visible in the gallery, so this is what this method does
	private void galleryAddPic() {
		Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
		File imageFile = new File(imagePath);
		Uri contentUri = Uri.fromFile(imageFile);
		mediaScanIntent.setData(contentUri);
		context.sendBroadcast(mediaScanIntent);
	}

	// This is mainly used to check whether user's device has a camera so we know whether they can take a photo or not
	public static boolean isIntentAvailable(Context context, String action) {
		final PackageManager packageManager = context.getPackageManager();
		final Intent intent = new Intent(action);
		List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
		return list.size() > 0;
	}

	public static String generateImageFilename(String filenamePrefix, String filenameSuffix, boolean includeDate) {
		// Get current date and time
		String dateFormat = C.Miscellaneous.DATE_FORMAT;
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.US);
		String formattedDate = simpleDateFormat.format(calendar.getTime());

		// Return a file name
		if (includeDate) {
			return filenamePrefix + formattedDate + filenameSuffix;
		} else {
			return filenamePrefix + filenameSuffix;
		}
	} // End of generateImageFilename

	/*
	 * We dont know what image is the user going to try to upload, so we will resample it in order to avoid outofmemory
	 * exception If the image is particularly huge, we will try resampling it harder Also, some acceptable file size is
	 * desired, so we are dealing with this as well
	 */
	public Bitmap getBitmapAndResizeIt(String pathName) {

		int maxImageFileSize = C.ImageHandling.MAX_IMAGE_FILE_SIZE;
		int maxImageWidth = C.ImageHandling.MAX_IMAGE_WIDTH;
		int maxImageHeight = C.ImageHandling.MAX_IMAGE_HEIGHT;
		int initialResampleFactor = C.ImageHandling.INITIAL_BITMAP_RESAMPLE_SIZE;
		int harderResampleFactor = C.ImageHandling.HARDER_BITMAP_RESAMPLE_SIZE;

		Bitmap bitmapLarge;
		Bitmap bitmapNormalized;
		BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
		bitmapOptions.inDither = false;
		bitmapOptions.inPurgeable = true;
		bitmapOptions.inInputShareable = true;
		bitmapOptions.inTempStorage = new byte[16 * 1024];

		try {
			bitmapLarge = BitmapFactory.decodeFile(pathName, bitmapOptions);

		} catch (OutOfMemoryError ex) {
			ex.printStackTrace();

			try {
				bitmapOptions.inSampleSize = initialResampleFactor;
				bitmapOptions.inJustDecodeBounds = false;
				bitmapLarge = BitmapFactory.decodeFile(pathName, bitmapOptions);

			} catch (OutOfMemoryError ex2) {
				ex2.printStackTrace();
				bitmapOptions.inSampleSize = harderResampleFactor;
				bitmapOptions.inJustDecodeBounds = false;
				bitmapLarge = BitmapFactory.decodeFile(pathName, bitmapOptions);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			bitmapLarge = null;
			return null;

		}

		int originalWidth = bitmapOptions.outWidth; // Example 15
		int originalHeight = bitmapOptions.outHeight; // Example 12

		int newWidth = originalWidth; // Example 15
		int newHeight = originalHeight; // Example 12

		// Desired is Example 12/9.6

		// We want the new image to have a maximum width of 640 or a maximum
		// height of 480 but maintain its aspect ratio as well.
		if ((originalWidth > maxImageWidth)) { // Example 15>12
			newWidth = maxImageWidth; // Example12 = 12
			newHeight = ((newWidth * originalHeight) / originalWidth);
			// Example 9.6 = ((12*12)/15) new height is 9.6
		}

		// Example Desired = new - so far so good
		if (newHeight > maxImageHeight) {
			newHeight = maxImageHeight;
			newWidth = ((newHeight * originalWidth) / originalHeight);
		}

		// This method will resize the image giving it the new dimensions
		bitmapNormalized = Bitmap.createScaledBitmap(bitmapLarge, newWidth, newHeight, true);

		// Check wether the filesize still exceeds the permitted. If it does,
		// reduce the dimensions of the image by 10% until it fits.
		int bitmapFileSize = (bitmapNormalized.getRowBytes() * bitmapNormalized.getHeight());

		while (bitmapFileSize > maxImageFileSize) {
			bitmapNormalized = Bitmap.createScaledBitmap(bitmapLarge, (int) Math.round((newWidth - (newWidth * 0.1))),
					(int) Math.round((newHeight - (newHeight * 0.1))), true);
			bitmapFileSize = (bitmapNormalized.getRowBytes() * bitmapNormalized.getHeight());
		}

		// recycle the bitmaps
		bitmapLarge.recycle();

		return bitmapNormalized;

	} // End of getBitmapAndResizeIt

	public static Bitmap rotateBitmap(String src, Bitmap bitmap) {
		try {
			int orientation = getExifOrientation(src);

			if (orientation == 1) {
				return bitmap;
			}

			Matrix matrix = new Matrix();
			switch (orientation) {
			case 2:
				matrix.setScale(-1, 1);
				break;
			case 3:
				matrix.setRotate(180);
				break;
			case 4:
				matrix.setRotate(180);
				matrix.postScale(-1, 1);
				break;
			case 5:
				matrix.setRotate(90);
				matrix.postScale(-1, 1);
				break;
			case 6:
				matrix.setRotate(90);
				break;
			case 7:
				matrix.setRotate(-90);
				matrix.postScale(-1, 1);
				break;
			case 8:
				matrix.setRotate(-90);
				break;
			default:
				return bitmap;
			}

			try {
				Bitmap oriented = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
				bitmap.recycle();
				return oriented;
			} catch (OutOfMemoryError e) {
				e.printStackTrace();
				return bitmap;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return bitmap;
	} // End of rotateBitmap

	private static int getExifOrientation(String src) throws IOException {
		int orientation = 1;

		try {
			/**
			 * if you are targeting only api level >= 5 ExifInterface exif = new ExifInterface(src); orientation =
			 * exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
			 */
			if (Build.VERSION.SDK_INT >= 5) {
				Class<?> exifClass = Class.forName("android.media.ExifInterface");
				Constructor<?> exifConstructor = exifClass.getConstructor(new Class[] { String.class });
				Object exifInstance = exifConstructor.newInstance(new Object[] { src });
				Method getAttributeInt = exifClass.getMethod("getAttributeInt", new Class[] { String.class, int.class });
				Field tagOrientationField = exifClass.getField("TAG_ORIENTATION");
				String tagOrientation = (String) tagOrientationField.get(null);
				orientation = (Integer) getAttributeInt.invoke(exifInstance, new Object[] { tagOrientation, 1 });
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}

		return orientation;
	} // End of getExifOrientation

} // End of Class