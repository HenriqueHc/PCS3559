/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.architecture.blueprints.todoapp.running;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.android.architecture.blueprints.todoapp.Injection;
import com.example.android.architecture.blueprints.todoapp.R;
import com.example.android.architecture.blueprints.todoapp.gesture.GestureActivity;
import com.example.android.architecture.blueprints.todoapp.image_sentiment.ImageSentimentActivity;
import com.example.android.architecture.blueprints.todoapp.util.ActivityUtils;
import com.example.android.architecture.blueprints.todoapp.voice.VoiceActivity;

import android.widget.Toast;

/**
 * Show statistics for tasks.
 */
public class RunningActivity extends AppCompatActivity implements IBaseGpsListener {

    private DrawerLayout mDrawerLayout;
    private RunningFragment statisticsFragment;

    private Location lastLocation = null;
    private double calculatedSpeed = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.running_interface_act);

        // Set up the toolbar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setTitle(R.string.running_title);
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        // Set up the navigation drawer.
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setStatusBarBackground(R.color.colorPrimaryDark);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        statisticsFragment = (RunningFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);
        if (statisticsFragment == null) {
            statisticsFragment = RunningFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    statisticsFragment, R.id.contentFrame);
        }

        new RunningPresenter(
                Injection.provideTasksRepository(getApplicationContext()), statisticsFragment);


        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if ( ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED ) {

                        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                        statisticsFragment.updateSpeed(null);
                    }

                    Toast.makeText(this, "OPA1", Toast.LENGTH_LONG).show();


                } else {

                    Toast.makeText(this, "OPA2", Toast.LENGTH_LONG).show();

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }

                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Open the navigation drawer when the home icon is selected from the toolbar.
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        Intent intent;
                        switch (menuItem.getItemId()) {
                            case R.id.list_navigation_menu_item:
                                NavUtils.navigateUpFromSameTask(RunningActivity.this);
                                break;
                            case R.id.gesture_navigation_menu_item:
                                intent =
                                        new Intent(RunningActivity.this, GestureActivity.class);
                                startActivity(intent);
                                break;
                            case R.id.image_navigation_menu_item:
                                intent =
                                        new Intent(RunningActivity.this, ImageSentimentActivity.class);
                                startActivity(intent);
                                break;
                            case R.id.running_navigation_menu_item:
                                intent =
                                        new Intent(RunningActivity.this, RunningActivity.class);
                                startActivity(intent);
                                break;
                            case R.id.voice_navigation_menu_item:
                                intent =
                                        new Intent(RunningActivity.this, VoiceActivity.class);
                                startActivity(intent);
                                break;
                            default:
                                break;
                        }
                        // Close the navigation drawer when an item is selected.
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

    public void finish()
    {
        super.finish();
        System.exit(0);
    }

    @Override
    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub
//        if(location != null)
//        {
//            Toast.makeText(this, "onLocationChanged: location != null", Toast.LENGTH_LONG).show();
//            CLocation myLocation = new CLocation(location, statisticsFragment.useMetricUnits());
//            statisticsFragment.updateSpeed(myLocation);
//        }

        if (lastLocation != null) {
            double elapsedTime = (location.getTime() - lastLocation.getTime()) / 1_000; // Convert milliseconds to seconds
            calculatedSpeed = lastLocation.distanceTo(location) / elapsedTime;
        }
        this.lastLocation = location;

        double speed = location.hasSpeed() ? location.getSpeed() : calculatedSpeed;

//        Toast.makeText(this, String.valueOf(speed * 3.6), Toast.LENGTH_LONG).show();

        statisticsFragment.updateSpeed(String.valueOf(Math.round(speed * 3.6)));
        /* There you have it, a speed value in m/s */


    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onGpsStatusChanged(int event) {
        // TODO Auto-generated method stub

    }
}
