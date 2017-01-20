package self.edu.elderly.method.fcm;

import android.content.Intent;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import self.edu.elderly.method.location.CustomerLocationService;

public class CustomerFirebaseMessagingService extends FirebaseMessagingService {

    public final String TAG = "Message";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO(developer): Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());

        String action = remoteMessage.getNotification().getBody();

        if (action.equals("GPSOn")) {
            turnGPSOn();

        } else if (action.equals("GPSOff")) {
            turnGPSOff();

        }
    }

    private void turnGPSOn() {
        Intent intent = new Intent(this, CustomerLocationService.class);
        startService(intent);
    }

    private void turnGPSOff() {
        Intent intent = new Intent(this, CustomerLocationService.class);
        stopService(intent);
    }

}
