package com.jpassion.ws_restclient;


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;
import android.util.Xml;

public class RESTClient {

	static final String TAG = "RESTClient";

	// Given a URL, establishes an HttpUrlConnection
	public String downloadUrl(String myurl) throws IOException {
		Log.d(TAG, "downloadUrl: " + myurl);
		InputStream is = null;
	    // Only display the first 500 characters retrieved
	    int len = 5000;
	    String contentAsString = "Fail!";
	    
	    try {
	        URL url = new URL(myurl);
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setReadTimeout(10000 /* milliseconds */);
	        conn.setConnectTimeout(15000 /* milliseconds */);
	        conn.setRequestMethod("GET");
	        conn.setDoInput(true);	
	        // account setup for mashape webservice
	        conn.setRequestProperty("X-Mashape-Authorization", "xxxxxxxxxxxxxxxxxxxxxxxxx");
	        Log.d(TAG, "downloadUrl start: " + myurl);
	        // Starts the query
	        conn.connect();
	        Log.d(TAG, "downloadUrl connect: " + myurl);
	        int response = conn.getResponseCode();
	        
	        Log.d(TAG, "The response is: " + response);
	        is = conn.getInputStream();

	       //Call methoz for take the element we want of the input stream
			contentAsString = returnWantedString(is);

	    } catch (UnsupportedEncodingException e) {
	    	Log.d(TAG, "UnsupportedEncodingException: " + e.getMessage());
	    } catch (IOException e) {
	    	Log.d(TAG, "IOException: " + e.getMessage());
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} finally {
	        if (is != null) {
	            is.close();
	        }	        
	    }
	    return contentAsString;
	}

	/**
	 * Change the inputream to xml and find the value of the label wanted
	 * @param is
	 * @return String
	 */
	public String returnWantedString(InputStream is) throws IOException, XmlPullParserException {
		XmlPullParser parser = Xml.newPullParser();
		String name = "";
		try {
			parser.setInput(is, null);
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}
		int eventType = 0;
		try {
			eventType = parser.getEventType();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}

		boolean done = true;
		int sw =0;
		while (done && eventType != XmlPullParser.END_DOCUMENT){

			if(eventType == XmlPullParser.START_TAG){
				//This time, I only want to see the first value of magnitude
				if (parser.getName().equals("magnitude") ){

					name = parser.getName();
					sw = 1;
				}
			}
			if (eventType == XmlPullParser.TEXT && sw==1){

				name += parser.getText();
				done = false;
				sw=0;
				Log.d("TAG",name);
			}

			eventType = parser.next();
		}
		return name;
	}


}
