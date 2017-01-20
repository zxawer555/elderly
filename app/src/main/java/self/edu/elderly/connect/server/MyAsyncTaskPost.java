package self.edu.elderly.connect.server;

import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;

public class MyAsyncTaskPost extends AsyncTask<String, Void, String>{
	
	public static List<NameValuePair> nameValuePairs;
	public AsyncResponsePost delegate = null;
	private static MyAsyncTaskPost _Instance;
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}
	
	@Override
	protected String doInBackground(String... url) {
		// TODO Auto-generated method stub
		String result = executeHttpPost(url[0]);
		return result;
	}
	
	@Override
	protected void onPostExecute(String result) {
		delegate.processServerFinish(result);
	}

	public static MyAsyncTaskPost getInstance(AsyncResponsePost asyncResponse) {
		_Instance = new MyAsyncTaskPost(asyncResponse);
		
		return _Instance;
	}

	public  MyAsyncTaskPost(AsyncResponsePost asyncResponse) {
	    
		delegate = asyncResponse;
	   
	}
	
	public String executeHttpPost(String url) {
		String result = "";
		
		// HttpClient acts like a Browser
		HttpClient client = new DefaultHttpClient();
		
		// Create object to represent a POST request
		HttpPost request = new HttpPost(url);
		
		// This will store the response from the server
		HttpResponse response;
		
		try {
			request.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			
			// Actually call the server
			response = client.execute(request);
			
			// Extract text message from server
			result = EntityUtils.toString(response.getEntity());
			
		} catch (Exception e) {
			result = "[ERROR] " + e.toString();
			
		}

		delegate.processServerFinish(result);
		return result;
	}

}
