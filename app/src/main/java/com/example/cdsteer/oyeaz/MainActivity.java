package com.example.cdsteer.oyeaz;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;


public class MainActivity extends ActionBarActivity {
    HttpClient client = new DefaultHttpClient();
    public TextView longi;
    public TextView lat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent i = new Intent(this.getApplicationContext(), LocationCheckin.class);
        this.startService(i);

        Button btn = (Button)findViewById(R.id.btmPushLocation);
        longi = (TextView)findViewById(R.id.tvLong);
        lat = (TextView)findViewById(R.id.tvLat);

        btn.setOnClickListener(displayLocation());
    }

    private View.OnClickListener displayLocation() {
        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        double longitude = location.getLongitude();
        double latitude = location.getLatitude();

        longi.setText(String.valueOf(longitude));
        lat.setText(String.valueOf(latitude));

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

        return null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
