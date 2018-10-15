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

package com.example.android.architecture.blueprints.todoapp.gesture;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NavUtils;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.android.architecture.blueprints.todoapp.Injection;
import com.example.android.architecture.blueprints.todoapp.R;
import com.example.android.architecture.blueprints.todoapp.image_sentiment.ImageSentimentActivity;
import com.example.android.architecture.blueprints.todoapp.running.RunningActivity;
import com.example.android.architecture.blueprints.todoapp.statistics.StatisticsActivity;
import com.example.android.architecture.blueprints.todoapp.tasks.TasksActivity;
import com.example.android.architecture.blueprints.todoapp.util.ActivityUtils;
import com.example.android.architecture.blueprints.todoapp.voice.VoiceActivity;

import java.util.List;

/**
 * Show statistics for tasks.
 */
public class GestureActivity extends AppCompatActivity implements SensorEventListener {

    public SensorManager mgr;
    public Sensor sensor;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.gesture_interface_act);

        // Set up the toolbar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setTitle(R.string.gesture_title);
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        // Set up the navigation drawer.
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setStatusBarBackground(R.color.colorPrimaryDark);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        GestureFragment statisticsFragment = (GestureFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);
        if (statisticsFragment == null) {
            statisticsFragment = GestureFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    statisticsFragment, R.id.contentFrame);
        }

        mgr = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        sensor = mgr.getDefaultSensor(Sensor.TYPE_LIGHT);

        new GesturePresenter(
                Injection.provideTasksRepository(getApplicationContext()), statisticsFragment);
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
                                NavUtils.navigateUpFromSameTask(GestureActivity.this);
                                break;
                            case R.id.statistics_navigation_menu_item:
                                // Do nothing, we're already on that screen
                                break;

                            case R.id.gesture_navigation_menu_item:
                                intent =
                                        new Intent(GestureActivity.this, GestureActivity.class);
                                startActivity(intent);
                                break;
                            case R.id.image_navigation_menu_item:
                                intent =
                                        new Intent(GestureActivity.this, ImageSentimentActivity.class);
                                startActivity(intent);
                                break;
                            case R.id.running_navigation_menu_item:
                                intent =
                                        new Intent(GestureActivity.this, RunningActivity.class);
                                startActivity(intent);
                                break;
                            case R.id.voice_navigation_menu_item:
                                intent =
                                        new Intent(GestureActivity.this, VoiceActivity.class);
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

    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mgr.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }
}
