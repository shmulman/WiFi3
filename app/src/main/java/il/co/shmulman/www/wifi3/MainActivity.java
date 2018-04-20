package il.co.shmulman.www.wifi3;

import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.net.wifi.WifiManager;
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

    EditText ip_address;
    TextView textView;
    Button start_button;

    WifiManager wifiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        start_button = findViewById(R.id.start_wifi);
        textView = findViewById(R.id.capsense_value);
        textView.setMovementMethod(new ScrollingMovementMethod());
        ip_address = findViewById(R.id.editText);

        start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

                Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+3:00"));
                Date currentLocalTime = cal.getTime();
                DateFormat date = new SimpleDateFormat("HH:mm:ss");
                date.setTimeZone(TimeZone.getTimeZone("GMT+3:00"));
                String localTime = date.format(currentLocalTime);
                textView.append("onClick is initiated: "+ localTime +"\n");

                List<android.net.wifi.ScanResult> list;
                list = wifiManager.getScanResults();
                for(int i = 0; i < list.size(); i++){
                    textView.append("SSID from list:" + list.get(i).SSID + "\n");
                }

                textView.append("wifiManager list size: "+ list.size() +"\n");

            }
        });
    }
}