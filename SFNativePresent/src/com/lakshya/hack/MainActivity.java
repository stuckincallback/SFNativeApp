/*
 * Copyright (c) 2012-2016, salesforce.com, inc.
 * All rights reserved.
 * Redistribution and use of this software in source and binary forms, with or
 * without modification, are permitted provided that the following conditions
 * are met:
 * - Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 * - Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * - Neither the name of salesforce.com, inc. nor the names of its contributors
 * may be used to endorse or promote products derived from this software without
 * specific prior written permission of salesforce.com, inc.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package com.lakshya.hack;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Toast;


import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.salesforce.androidsdk.app.SalesforceSDKManager;
import com.salesforce.androidsdk.rest.RestClient;
import com.salesforce.androidsdk.ui.SalesforceActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import im.delight.android.webview.AdvancedWebView;

/**
 * Main activity
 */
public class MainActivity extends SalesforceActivity implements
        ConnectionCallbacks, OnConnectionFailedListener, ResultCallback<Status> {

    private RestClient client;
    private ArrayAdapter<String> listAdapter;
	private AdvancedWebView myWebView;
    public static GoogleApiClient mGoogleApiClient;
    public static Context mainAcContext;
    private Location mLastLocation;
    private Geofence mHospitalGeoFence=null;
    private PendingIntent mGeofencePendingIntent;
    private PushImplClass pushImpl;
    public MainActivity activity;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Setup view
		//setContentView(R.layout.main);
		//test web view
		setContentView(R.layout.webviewlayout);
        buildGoogleApiClient();

	}
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getBaseContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }
    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
	public void onResume() {
		// Hide everything until we are logged in
		//findViewById(R.id.root).setVisibility(View.INVISIBLE);
        BusProvider.getInstance().register(this);
		findViewById(R.id.webViewLayout).setVisibility(View.INVISIBLE);
		myWebView = (AdvancedWebView)findViewById(R.id.webView);
        WebSettings settings = myWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setDatabaseEnabled(true);
        settings.setDomStorageEnabled(true);
        myWebView.loadUrl("https://patient123465789-developer-edition.ap4.force.com/HMS/PatientHome");

		// Create list adapter
		//listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new ArrayList<String>());
		//((ListView) findViewById(R.id.contacts_list)).setAdapter(listAdapter);
		
		super.onResume();
	}		
	
	@Override
	public void onResume(RestClient client) {

        // Keeping reference to rest client
        this.client = client; 

		// Show everything
		//findViewById(R.id.root).setVisibility(View.VISIBLE);
		findViewById(R.id.webViewLayout).setVisibility(View.VISIBLE);
	}
	@Override
	public void onBackPressed() {
		if (myWebView.canGoBack()) {
			myWebView.goBack();
		} else {
			super.onBackPressed();
		}
	}
	/**
	 * Called when "Logout" button is clicked. 
	 * 
	 * @param v
	 */
	public void onLogoutClick(View v) {
		SalesforceSDKManager.getInstance().logout(this);
	}



    public void testMethodd() {
        Log.d("Dataaaaaaaaaaaaaa","It is working"+String.valueOf(mGoogleApiClient.isConnected()));
        createGeoFence();
        //Add geoFence in Application
        addGeofence();
    }
    public void createGeoFence(){
        mHospitalGeoFence = new Geofence.Builder()
                .setRequestId("Persistent Hospital")
                .setCircularRegion(21.1213735,79.0471586,Constants.GEOFENCE_RADIUS_IN_METERS)
                .setExpirationDuration(Constants.GEOFENCE_EXPIRATION_IN_MILLISECONDS)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                        Geofence.GEOFENCE_TRANSITION_EXIT)
                .build();
    }


    @Override
    public void onConnected(Bundle bundle) {
        Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();

        //Was trying to check If location works without turning on location
       /*try {
           mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
           Toast.makeText(this, "Connected"+ mLastLocation.getLongitude()+" "+mLastLocation.getLatitude(), Toast.LENGTH_SHORT).show();
       }catch(SecurityException e){}*/
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed( ConnectionResult connectionResult) {

    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();

        // The INITIAL_TRIGGER_ENTER flag indicates that geofencing service should trigger a
        // GEOFENCE_TRANSITION_ENTER notification when the geofence is added and if the device
        // is already inside that geofence.
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofence(mHospitalGeoFence);
        // Add the geofences to be monitored by geofencing service.
      //  builder.addGeofences(mGeofenceList);

        // Return a GeofencingRequest.
        return builder.build();
    }
    public void addGeofence() {
        if (!mGoogleApiClient.isConnected()) {
            Toast.makeText(this, getString(R.string.not_connected), Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            LocationServices.GeofencingApi.addGeofences(
                    mGoogleApiClient,
                    // The GeofenceRequest object.
                    getGeofencingRequest(),
                    // A pending intent that that is reused when calling removeGeofences(). This
                    // pending intent is used to generate an intent when a matched geofence
                    // transition is observed.
                    getGeofencePendingIntent()
            ).setResultCallback(this); // Result processed in onResult().
        } catch (SecurityException securityException) {
            // Catch exception generated if the app does not use ACCESS_FINE_LOCATION permission.

        }
    }
    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(getBaseContext(), GeofenceTransitionsIntentService.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when calling
        // addGeofences() and removeGeofences().
        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public void onResult(Status status) {

      }




}
