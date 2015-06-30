package com.example.cdsteer.oyeaz;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;

import java.io.IOException;

public class LocationCheckin extends Service {
    android.os.Handler mHandler;
    StringBuilder builder = new StringBuilder();
    HttpClient client = new DefaultHttpClient();

    @Override
    public void onCreate() {
        mHandler = new android.os.Handler();
        ping();
        Toast toast = Toast.makeText(getApplicationContext(), "LocationCheckin Started", Toast.LENGTH_LONG);
        toast.show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void ping() {
        try {
            // sendLocation();
            new LongOperation().execute("");
        } catch (Exception e) {
            Log.e("Error", "In onStartCommand");
            e.printStackTrace();
        }
        scheduleNext();
    }

    private void scheduleNext() {
        mHandler.postDelayed(new Runnable() {
            public void run() {
                ping();
            }
        }, 30000);
    }


    private class LongOperation extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                sendLocation();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }

        public void sendLocation() throws IOException, JSONException {
            // add choice best location provider later
            LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
            Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            String url = "https://oyeaz-app.herokuapp.com/users/1/locations/new?latitude="+latitude+"&longitude="+longitude;

            try {
                HttpGet httpget = new HttpGet(url);
                httpget.setHeader("Accept", "application/json");
                httpget.setHeader("Content-type", "application/json");
                HttpResponse response = client.execute(httpget);
                Log.v("Response", response.toString());
            } catch (Exception e) {
                Log.e("log_tag", "Error in http connection "+e.toString());
            }
        }
    }

}
