package self.edu.elderly.method.location;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

import self.edu.elderly.R;
import self.edu.elderly.connect.server.AsyncResponsePost;
import self.edu.elderly.connect.server.HttpLink;
import self.edu.elderly.connect.server.MyAsyncTaskPost;
import self.edu.elderly.method.sharepreference.CustomerSharePreference;

import static self.edu.elderly.method.sharepreference.CustomerSharePreference.mContext;

public class CustomerLocationService extends Service implements LocationListener, AsyncResponsePost {

    private final String TAG = "GPS";

    public static Context mContext;

    public static LocationManager locationManager;

    public Location location; // location

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 1 * 1; // 1 minute

    public CustomerLocationService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        checkGPSStatus();
        Log.d(TAG, "onCreate() executed");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand() executed");

        if (!checkGPSStatus() && !checkNetworkStatus()) {
            // no network provider is enabled

        } else {
            // First get location from Network Provider
            if (checkNetworkStatus()) {
                locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

            }
            // if GPS Enabled get lat/long using GPS Services
            if (checkGPSStatus()) {
                if (location == null) {
                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                }
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public static boolean checkNetworkStatus() {
        boolean network_status;

        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            network_status = true;
        } else {
            network_status = false;
        }

        return network_status;
    }

    public static boolean checkGPSStatus() {
        boolean gps_status;

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            gps_status = true;
        } else {
            gps_status = false;
        }

        return gps_status;
    }

    public static void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        // Setting Dialog Title
        alertDialog.setTitle(mContext.getResources().getString(R.string.dialog_title));

        // Setting Dialog Message
        alertDialog.setMessage(mContext.getResources().getString(R.string.dialog_body));

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    @Override
    public void onLocationChanged(Location location) {
        final String latitude = String.valueOf(location.getLatitude());
        final String longitude = String.valueOf(location.getLongitude());

        Thread thread = new Thread() {
            public void run() {
                uploadElderlyLocation(latitude, longitude);
            }
        };

        thread.start();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void processServerFinish(String result) {

        Log.d(TAG, result);

    }

    public void uploadElderlyLocation(String latitude, String longitude) {
        String eid = CustomerSharePreference.getInstance().getStringPreference(CustomerSharePreference.EID);

        MyAsyncTaskPost.nameValuePairs = new ArrayList<NameValuePair>(3);
        MyAsyncTaskPost.nameValuePairs.add(new BasicNameValuePair("eid", eid));
        MyAsyncTaskPost.nameValuePairs.add(new BasicNameValuePair("latitude", latitude));
        MyAsyncTaskPost.nameValuePairs.add(new BasicNameValuePair("longitude", longitude));

        MyAsyncTaskPost.getInstance(this).executeHttpPost(
                HttpLink.URLLink + "elderly_update_location.php"
        );
    }
}
