package net.shiftinpower.utilities;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import net.shiftinpower.core.C;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONException;
import org.json.JSONObject;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.util.Log;

public class JSONParser {

	InputStream inputStream = null;
	JSONObject jsonObject = null;
	String json = "";

	public JSONParser() {

	}

	public JSONObject getJSONFromUrl(final String url) {

		// Making HTTP request
		try {
			// Construct the client and the HTTP request.
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);

			// Execute the POST request and store the response locally.
			HttpResponse httpResponse = httpClient.execute(httpPost);
			// Extract data from the response.
			HttpEntity httpEntity = httpResponse.getEntity();
			// Open an inputStream with the data content.
			inputStream = httpEntity.getContent();

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			// Create a BufferedReader to parse through the inputStream.
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"), 8);
			// Declare a string builder to help with the parsing.
			StringBuilder sb = new StringBuilder();
			// Declare a string to store the JSON object data in string form.
			String line = null;

			// Build the string until null.
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}

			// Close the input stream.
			inputStream.close();
			reader.close();
			// Convert the string builder data to an actual string.
			json = sb.toString();
		} catch (ClientProtocolException e1) {
			Log.e("Buffer Error", "Client Protocol Exception" + e1.toString());
		} catch (IOException e1) {
			Log.e("Buffer Error", "IOException " + e1.toString());
		} catch (Exception e) {
			Log.e("Buffer Error", "Error converting result " + e.toString());
		}

		// Try to parse the string to a JSON object
		try {
			jsonObject = new JSONObject(json);
		} catch (JSONException e) {
			Log.e("JSON Parser", "Error parsing data " + e.toString());
		}

		// Return the JSON Object.
		return jsonObject;

	} // End of getJSONFromUrl

	// function get json from url
	// by making HTTP POST or GET mehtod
	public JSONObject makeHttpRequest(String url, String method, List<NameValuePair> params) {

		// Making HTTP request
		try {

			// check for request method
			if (method == "POST") {
				// request method is POST
				// defaultHttpClient
				DefaultHttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(url);
				httpPost.setEntity(new UrlEncodedFormEntity(params));

				HttpResponse httpResponse = httpClient.execute(httpPost);
				HttpEntity httpEntity = httpResponse.getEntity();
				inputStream = httpEntity.getContent();

			} else if (method == "GET") {
				// request method is GET
				DefaultHttpClient httpClient = new DefaultHttpClient();
				String paramString = URLEncodedUtils.format(params, "utf-8");
				url += "?" + paramString;
				HttpGet httpGet = new HttpGet(url);

				HttpResponse httpResponse = httpClient.execute(httpGet);
				HttpEntity httpEntity = httpResponse.getEntity();
				inputStream = httpEntity.getContent();
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			inputStream.close();
			reader.close();
			json = sb.toString();
		} catch (ClientProtocolException e1) {
			Log.e("Buffer Error", "Client Protocol Exception" + e1.toString());
		} catch (IOException e1) {
			Log.e("Buffer Error", "IOException " + e1.toString());
		} catch (Exception e) {
			Log.e("Buffer Error", "Error converting result " + e.toString());
		}

		// try parse the string to a JSON object
		try {
			jsonObject = new JSONObject(json);
		} catch (JSONException e) {
			Log.e("JSON Parser", "Error parsing data " + e.toString());
		}

		// return JSON String
		return jsonObject;

	} // End of makeHttpRequest

	public JSONObject uploadImageToServer(String imagePath, String imageFilename, boolean userImage) throws Exception {

		HttpClient httpClient;
		HttpResponse response;
		MultipartEntityBuilder multipartEntity;
		HttpPost postRequest;
		HttpContext localContext;
		Bitmap bitmap;

		// Dont forget to set the folder write permissions on that folder. also check the .ini file on the server for the
		// maximum file size allowed.
		String folderOnServer;
		if (userImage) {
			folderOnServer = C.API.INCOMING_USER_IMAGES_HANDLER;
		} else {
			folderOnServer = C.API.INCOMING_ITEM_IMAGES_HANDLER;
		}
		try {

			// Set the http handlers
			httpClient = new DefaultHttpClient();
			localContext = new BasicHttpContext(); // TODO why do I need this?

			postRequest = new HttpPost(C.API.WEB_ADDRESS + folderOnServer);

			// Deal with the file
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

			bitmap = BitmapFactory.decodeFile(imagePath);
			bitmap.compress(CompressFormat.JPEG, C.ImageHandling.IMAGE_SENT_TO_SERVER_QUALITY_0_T0_100, byteArrayOutputStream);
			bitmap.recycle();

			// Second parameter is the image quality in per cent. The bitmap converts into the third parameter that we later
			// use.
			byte[] byteData = byteArrayOutputStream.toByteArray();
			ByteArrayBody byteArrayBody = new ByteArrayBody(byteData, imageFilename); // second parameter is the name of the
																						// image

			// Send the package
			multipartEntity = MultipartEntityBuilder.create();
			multipartEntity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			multipartEntity.addPart("photo", byteArrayBody);

			postRequest.setEntity(multipartEntity.build());

			// Get the response. we will deal with it in onPostExecute.
			response = httpClient.execute(postRequest, localContext);

			HttpEntity httpEntity = response.getEntity();
			inputStream = httpEntity.getContent();

			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"), 8);
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
				json = sb.toString();
				inputStream.close();
				reader.close();
			} catch (ClientProtocolException e1) {
				Log.e("Buffer Error", "Client Protocol Exception" + e1.toString());
			} catch (IOException e1) {
				Log.e("Buffer Error", "IOException " + e1.toString());
			} catch (Exception e) {
				Log.e("Buffer Error", "Error converting result " + e.toString());
			}

			httpClient.getConnectionManager().shutdown();

			// Try parsing the string to a JSON object
			try {
				jsonObject = new JSONObject(json);
			} catch (JSONException e) {
				Log.e("JSON Parser", "Error parsing data " + e.toString());
			}

			// Return JSON String
			return jsonObject;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	} // End of uploadImageToServer

	public JSONObject getJSONResponse(HttpResponse httpResponse) {

		HttpEntity httpEntity;
		InputStream inputStream = null;

		httpEntity = httpResponse.getEntity();
		try {
			inputStream = httpEntity.getContent();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"), 8);
			StringBuilder stringBuilder = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				stringBuilder.append(line + "\n");
			}
			inputStream.close();
			reader.close();
			json = stringBuilder.toString();
		} catch (ClientProtocolException e1) {
			Log.e("Buffer Error", "Client Protocol Exception" + e1.toString());
		} catch (IOException e1) {
			Log.e("Buffer Error", "IOException " + e1.toString());
		} catch (Exception e) {
			Log.e("Buffer Error", "Error converting result " + e.toString());
		}

		// try parse the string to a JSON object
		try {
			jsonObject = new JSONObject(json);
		} catch (JSONException e) {
			Log.e("JSON Parser", "Error parsing data " + e.toString());
		}

		// return JSON String
		return jsonObject;

	} // End of getJSONResponse

} // End of Class 