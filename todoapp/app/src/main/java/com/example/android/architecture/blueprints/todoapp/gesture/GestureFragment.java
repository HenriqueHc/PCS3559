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
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.architecture.blueprints.todoapp.R;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Main UI for the statistics screen.
 */
public class GestureFragment extends Fragment implements GestureContract.View, SensorEventListener {

    //private TextView mStatisticsTV;

    private GestureContract.Presenter mPresenter;

    private TextView txtList;

    public static GestureFragment newInstance() {
        return new GestureFragment();
    }

    @Override
    public void setPresenter(@NonNull GestureContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.gesture_interface_frag, container, false);
        //mStatisticsTV = (TextView) root.findViewById(R.id.statistics);


        txtList = (TextView)  root.findViewById(R.id.text_gesture);


        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
        List<Sensor> sensorList = ((GestureActivity)getActivity()).mgr.getSensorList(Sensor.TYPE_ALL);
        StringBuilder strBuilder = new StringBuilder();
        for(Sensor s: sensorList){
            strBuilder.append(s.getName()+"\n");
        }
        txtList.setVisibility(View.VISIBLE);
        txtList.setText(strBuilder);
    }

    @Override
    public void setProgressIndicator(boolean active) {
//        if (active) {
//            mStatisticsTV.setText(getString(R.string.loading));
//        } else {
//            mStatisticsTV.setText("");
//        }
    }

    @Override
    public void showStatistics(int numberOfIncompleteTasks, int numberOfCompletedTasks) {
//        if (numberOfCompletedTasks == 0 && numberOfIncompleteTasks == 0) {
//            mStatisticsTV.setText(getResources().getString(R.string.statistics_no_tasks));
//        } else {
//            String displayString = getResources().getString(R.string.statistics_active_tasks) + " "
//                    + numberOfIncompleteTasks + "\n" + getResources().getString(
//                    R.string.statistics_completed_tasks) + " " + numberOfCompletedTasks;
//            mStatisticsTV.setText(displayString);
//        }
    }

    @Override
    public void showLoadingStatisticsError() {
//        mStatisticsTV.setText(getResources().getString(R.string.statistics_error));
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}
