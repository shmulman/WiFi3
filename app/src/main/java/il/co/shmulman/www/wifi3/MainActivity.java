package il.co.shmulman.www.wifi3;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {

    // TAG is used for informational messages
    private final static String TAG = "MainActivity 18.4.2018";

    // Location parameter
    int ALLOW_LOCATION = 1;

    EditText ip_address;
    TextView textView;
    Button start_button, read_html_button;

    WifiManager wifiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        start_button = findViewById(R.id.start_wifi);
        read_html_button = findViewById(R.id.read_html);
        textView = findViewById(R.id.capsense_value);
        textView.setMovementMethod(new ScrollingMovementMethod());
        ip_address = findViewById(R.id.editText);

        // Location permission
        requestStoragePermission();

        start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Date
                showDate();

                // Wifi
                showWiFi();
            }
        });

        read_html_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ip_str = getString(R.string.ip_string);
                textView.setText("Reading HTML file\n");
                textView.append("IP:" + ip_str +"\n");

            }
        });
    }

    private void requestStoragePermission(){
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            textView.append("The LOCATION permission is already granted\n");
        } else {
            textView.append("The LOCATION permission is NOT granted\n");
            ActivityCompat.requestPermissions(MainActivity.this, new String[]
                    {Manifest.permission.ACCESS_COARSE_LOCATION},ALLOW_LOCATION);
            textView.append("The LOCATION permissions are updated\n");
        }
    }

    private void showDate(){
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+3:00"));
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("HH:mm:ss");
        date.setTimeZone(TimeZone.getTimeZone("GMT+3:00"));
        String localTime = date.format(currentLocalTime);
        textView.append("onClick is initiated: "+ localTime +"\n");
    }

    private void showWiFi(){
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        List<ScanResult> list;
        list = wifiManager.getScanResults();
        for(int i = 0; i < list.size(); i++){
            textView.append("SSID from list:" + list.get(i).SSID + "\n");
        }
        textView.append("wifiManager list size: "+ list.size() +"\n");
    }
}