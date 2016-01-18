package com.jpassion.ws_restclient;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.util.Log;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.DefaultHandler;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import javax.xml.parsers.SAXParser;

public class MainActivity extends Activity {

	final String TAG = "MainActivity";
	String response;
	EditText mEditText;
	TextView mTextView;
	
	RESTClient restClient = new RESTClient();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mEditText = (EditText) findViewById(R.id.edit_text);

        final Button mButton = (Button) findViewById(R.id.button);
        mTextView = (TextView) findViewById(R.id.content);

        mButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                try {

                    String stringUrl = mEditText.getText().toString();

                    Log.v(TAG, mEditText.getText().toString());

                    ConnectivityManager connMgr = (ConnectivityManager)
                            getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                    if (networkInfo != null && networkInfo.isConnected()) {
                        new DownloadWebpageTask().execute(stringUrl);
                    } else {

                        mTextView.setText(getResources().getString(R.string.no_network));
                    }

                } catch (Exception e) {
                    response += "\n" + e.getMessage();
                    mTextView.setText(response);
                    e.printStackTrace();
                }
            }
        });


    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selected
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	
    // Uses AsyncTask, necessary for internet connections
    private class DownloadWebpageTask extends AsyncTask<String, Void, String> {
       @Override
       protected String doInBackground(String... urls) {
             
           // params comes from the execute()
           try {
               return restClient.downloadUrl(urls[0]);
           } catch (IOException e) {
               return "URL may be invalid.";
           }
       }
       // onPostExecute displays the results of the AsyncTask.
       @Override
       protected void onPostExecute(String result) {

           mTextView.setText(result);

       }
   }
}
