package self.edu.elderly.method.fcm;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import self.edu.elderly.MainActivity;
import self.edu.elderly.connect.server.AsyncResponsePost;
import self.edu.elderly.connect.server.HttpLink;
import self.edu.elderly.connect.server.MyAsyncTaskPost;
import self.edu.elderly.connect.server.json.HandleJsonMethod;
import self.edu.elderly.connect.server.json.HandleJsonVariable;
import self.edu.elderly.method.sharepreference.CustomerSharePreference;

public class CustomerFirebaseInstanceIdService extends FirebaseInstanceIdService implements AsyncResponsePost {

    private final String TAG = "Token";

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        setRegistrationFCMToken(refreshedToken);
    }

    public void setRegistrationFCMToken(String token) {
        CustomerSharePreference.getInstance().setStringPreference(CustomerSharePreference.FCMToken, token);

        MyAsyncTaskPost.nameValuePairs = new ArrayList<NameValuePair>(1);
        MyAsyncTaskPost.nameValuePairs.add(new BasicNameValuePair("FCMToken", token));
        MyAsyncTaskPost.getInstance(this).executeHttpPost(
                HttpLink.URLLink + "elderly_register.php"
        );
    }

    @Override
    public void processServerFinish(String result) {
        String[] varName = { "eid", "identifier" };
        List<HandleJsonVariable> resultList = new ArrayList<HandleJsonVariable>();
        resultList = HandleJsonMethod.getInstance().convertJsonToVariable(
                result, varName);

        CustomerSharePreference.getInstance().setStringPreference(
                CustomerSharePreference.EID, String.valueOf(resultList.get(0).eid));
        CustomerSharePreference.getInstance().setStringPreference(
                CustomerSharePreference.Identifier, String.valueOf(resultList.get(0).identifier));

        CustomerSharePreference.getInstance().setBooleanPreference(CustomerSharePreference.Registed, true);
    }
}
