package de.treebug.taintviolationtester;

import android.os.AsyncTask;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by christoph on 10.09.17.
 */

public class SendDataTask extends AsyncTask<String, Void, String>{
    private static final String TAG = SendDataTask.class.getSimpleName();

    private Exception ex = null;

    @Override
    protected String doInBackground(String... params) {
        String data = "";

        HttpURLConnection httpURLConnection = null;
        Log.d(TAG, "Taint: Starting async exec");

        try {
            httpURLConnection = (HttpURLConnection) new URL(params[0]).openConnection();
            httpURLConnection.setRequestMethod("POST");

            httpURLConnection.setDoOutput(true);

            DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
            wr.write(params[1].getBytes());
            wr.flush();
            wr.close();

            InputStreamReader inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());
            int inData = inputStreamReader.read();

            while (inData != -1) {
                char current = (char) inData;
                data += current;

                inData = inputStreamReader.read();
            }


        } catch (Exception e) {
            ex = e;
            Log.e(TAG, "Taint: "+e.getMessage());
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }

        return data;
    }
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        if (ex != null) {
            Log.e(TAG, "Taint: " + ex.getMessage());
        }

        Log.d(TAG, result); // this is expecting a response code to be sent from your server upon receiving the POST data
    }

}
