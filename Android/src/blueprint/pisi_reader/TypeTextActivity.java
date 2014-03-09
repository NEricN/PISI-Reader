package blueprint.pisi_reader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class TypeTextActivity extends Activity {
	
	EditText mEditText;
	Button mBtnSend;
    ProgressDialog dialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_type_text);
		
		mEditText = (EditText) this.findViewById(R.id.txt_input_book);
		mBtnSend = (Button) this.findViewById(R.id.btn_send_text);
		
		mBtnSend.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (mEditText.getText().toString().trim().equals("")) {
					Toast.makeText(TypeTextActivity.this, "Type a sentense of your book.", Toast.LENGTH_LONG).show();
				}
				else {
					AsyncPostText postText = new AsyncPostText();
					postText.execute(mEditText.getText().toString());
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.type_text, menu);
		return true;
	}
	
	//TODO
	private class AsyncPostText extends AsyncTask<String, Void, String> {
		
		@Override
	    protected  void onPreExecute()
	    {
			dialog = ProgressDialog.show(TypeTextActivity.this, "", "Uploading book...", true);
	    }
		
	    protected String doInBackground(String... strings) {
	    	 // url where the data will be posted
	    	 String postReceiverUrl = "http://192.168.6.135:8080/textupload";

	    	 // HttpClient
	    	 HttpClient httpClient = new DefaultHttpClient();

	    	 // post header
	    	 HttpPost httpPost = new HttpPost(postReceiverUrl);

	    	 // add your data
	    	 List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
	    	 nameValuePairs.add(new BasicNameValuePair("uploaded_text", strings[0]));

	    	 try {
				 httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				
				 // execute HTTP post request
		    	 HttpResponse response = httpClient.execute(httpPost);
		    	 HttpEntity resEntity = response.getEntity();

		    	 if (resEntity != null) {
		    	     
		    		InputStream ips  = resEntity.getContent();
	    	        BufferedReader buf = new BufferedReader(new InputStreamReader(ips,"UTF-8"));
	    	        if(response.getStatusLine().getStatusCode()!=HttpStatus.SC_OK)
	    	        {
	    	            throw new Exception(response.getStatusLine().getReasonPhrase());
	    	        }
	    	        StringBuilder sb = new StringBuilder();
	    	        String s;
	    	        while(true )
	    	        {
	    	            s = buf.readLine();
	    	            if(s==null || s.length()==0)
	    	                break;
	    	            sb.append(s);

	    	        }
	    	        buf.close();
	    	        ips.close();
	    	        return sb.toString();
		    	 }
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
	     }

	     protected void onPostExecute(String result) {
	    	 dialog.dismiss(); 
	    	 if (result != null) {
	    		 Intent intent = new Intent();
	    		 intent.putExtra(MainActivity.BOOK_LEVEL, result);
	    		 intent.putExtra(MainActivity.BOOK_TEXT, mEditText.getText().toString());
	 		     setResult(Activity.RESULT_OK, intent);
	 		     TypeTextActivity.this.finish();
	    	 }
	    	 else {
	    		 Toast.makeText(TypeTextActivity.this, "Cannot send text data to the server", Toast.LENGTH_LONG).show();
	    	 }
	     }
	 }

}
