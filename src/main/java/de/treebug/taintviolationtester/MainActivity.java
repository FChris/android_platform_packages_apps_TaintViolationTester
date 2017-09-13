package de.treebug.taintviolationtester;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.GenGpioManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private Button button_sendTainted;
    private Button button_sendUntainted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button_sendTainted = (Button) findViewById(R.id.button_send_tainted);
        button_sendUntainted = (Button) findViewById(R.id.button_send_untainted);

        attachListeners();
    }

    private void attachListeners() {
        button_sendTainted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GenGpioManager manager = (GenGpioManager) getSystemService(Context.GENGPIO_SERVICE);
                String gpioData = manager.read(32, 0);
                new SendDataTask().execute("http://posttestserver.com", gpioData);

                Log.v(TAG, "Send tainted");

            }
        });

        button_sendUntainted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String data = "Untainted Data created locally";
                new SendDataTask().execute("http://posttestserver.com", data);

                Log.v(TAG, "Send untainted");
            }
        });
    }
}
