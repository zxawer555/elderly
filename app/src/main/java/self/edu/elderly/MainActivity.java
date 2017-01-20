package self.edu.elderly;

import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import self.edu.elderly.method.fcm.CustomerFirebaseInstanceIdService;
import self.edu.elderly.method.location.CustomerLocationService;
import self.edu.elderly.method.sharepreference.CustomerSharePreference;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MainActivity extends AppCompatActivity {

    private TextView tvIdentifier;
    private Button btnShowIdentifier;

    private final int LOCATION_STATUS_REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CustomerSharePreference.mContext = this;

        tvIdentifier = (TextView) findViewById(R.id.tvIdentifier);
        btnShowIdentifier = (Button) findViewById(R.id.btnShowIdentifier);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION},
                        LOCATION_STATUS_REQUEST_CODE);
            }
        }

        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                while (CustomerSharePreference.getInstance().getBooleanPreference(CustomerSharePreference.Registed) == false) {
                    Log.d("result", "Trying...");

                    CustomerFirebaseInstanceIdService register = new CustomerFirebaseInstanceIdService();
                    register.setRegistrationFCMToken(
                            CustomerSharePreference.getInstance().getStringPreference(CustomerSharePreference.FCMToken));
                }
            }
        }, 10000);

        CustomerLocationService.mContext = this;
        CustomerLocationService.locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (!CustomerLocationService.checkGPSStatus()) {
            CustomerLocationService.showSettingsAlert();
        }

        btnShowIdentifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String identifier = CustomerSharePreference.getInstance().getStringPreference(CustomerSharePreference.Identifier);

                if (!identifier.equals("")) {
                    tvIdentifier.setText(identifier);
                    tvIdentifier.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.identifier_error_message), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        doNext(requestCode, grantResults);
    }

    private void doNext(int requestCode, int[] grantResults) {
        if (requestCode == LOCATION_STATUS_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
            } else {
                Toast.makeText(this, "Please Allow Location Permission!", Toast.LENGTH_LONG).show();
            }
        }
    }
}
