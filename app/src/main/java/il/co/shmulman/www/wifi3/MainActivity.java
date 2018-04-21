package il.co.shmulman.www.wifi3;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Network;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {

    // TAG is used for informational messages
    private final static String TAG = "MainActivity 21.4.2018";

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
                ip_str = "http://www.shmulman.co.il/";
                textView.append("IP:" + ip_str +"\n");

                new MakeNetworkCall().execute("http://www.shmulman.co.il/", "Get");


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

    InputStream ByGetMethod(String ServerURL) {

        InputStream DataInputStream = null;
        try {

            URL url = new URL(ServerURL);
            HttpURLConnection cc = (HttpURLConnection)
                    url.openConnection();
            //set timeout for reading InputStream
            cc.setReadTimeout(5000);
            // set timeout for connection
            cc.setConnectTimeout(5000);
            //set HTTP method to GET
            cc.setRequestMethod("GET");
            //set it to true as we are connecting for input
            cc.setDoInput(true);

            //reading HTTP response code
            int response = cc.getResponseCode();

            //if response code is 200 / OK then read Inputstream
            if (response == HttpURLConnection.HTTP_OK) {
                DataInputStream = cc.getInputStream();
            }

        } catch (Exception e) {
            Log.e(TAG, "Error in GetData", e);
        }
        return DataInputStream;
    }

    String ConvertStreamToString(InputStream stream) {

        InputStreamReader isr = new InputStreamReader(stream);
        BufferedReader reader = new BufferedReader(isr);
        StringBuilder response = new StringBuilder();

        String line = null;
        try {

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

        } catch (IOException e) {
            Log.e(TAG, "Error in ConvertStreamToString", e);
        } catch (Exception e) {
            Log.e(TAG, "Error in ConvertStreamToString", e);
        } finally {

            try {
                stream.close();

            } catch (IOException e) {
                Log.e(TAG, "Error in ConvertStreamToString", e);

            } catch (Exception e) {
                Log.e(TAG, "Error in ConvertStreamToString", e);
            }
        }
        return response.toString();


    }

    public void DisplayMessage(String a) {

        textView = (TextView) findViewById(R.id.capsense_value);
        textView.append(a);
    }

    private class MakeNetworkCall extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            DisplayMessage("Please Wait ...");
        }

        @Override
        protected String doInBackground(String... arg) {

            InputStream is = null;
            String URL = arg[0];
            Log.d(TAG, "URL: " + URL);
            String res = "";


            is = ByGetMethod(URL);

            if (is != null) {
                res = ConvertStreamToString(is);
            } else {
                res = "Something went wrong";
            }
            return res;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            DisplayMessage(result);
            Log.d(TAG, "Result: " + result);
        }
    }

}