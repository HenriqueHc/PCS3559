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
import android.os.VibrationEffect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.os.Vibrator;
import android.os.Build;
import android.media.MediaPlayer;

import com.example.android.architecture.blueprints.todoapp.R;
import com.google.common.collect.Lists;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Main UI for the statistics screen.
 */
public class GestureFragment extends Fragment implements GestureContract.View {

    //private TextView mStatisticsTV;

    private GestureContract.Presenter mPresenter;

    MediaPlayer mediaPlayer;

    private TextView txtList;

    private int contador = 0;

    private final int[] resID = { R.raw.gaiola, R.raw.atrasadinha, R.raw.youngr,
            R.raw.medicina, R.raw.amor_falso, R.raw.fuleragem, R.raw.taki_taki, R.raw.refem };

    private final List<String> LISTA_MUSICAS = Lists.newArrayList("Música 1", "Música 2", "Música 3", "Música 4", "Música 5", "Música 6", "Música 7", "Música 8", "Música 9");
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;



    public static GestureFragment newInstance() {
        return new GestureFragment();
    }

    @Override
    public void setPresenter(@NonNull GestureContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.gesture_interface_frag, container, false);
        //mStatisticsTV = (TextView) root.findViewById(R.id.statistics);

        txtList = (TextView)  root.findViewById(R.id.text_gesture);


        // ShakeDetector initialization
        mSensorManager = (SensorManager) (getActivity()).getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {

            @Override
            public void onShake(int count) {
                /*
                 * The following method, "handleShakeEvent(count):" is a stub //
                 * method you would use to setup whatever you want done once the
                 * device has been shook.
                 */
                contador++;
                if (contador == resID.length){
                    contador = 0;
                }

                mediaPlayer.reset();
                mediaPlayer = MediaPlayer.create(getActivity().getApplicationContext(), resID[contador]);
                mediaPlayer.start();
                txtList.setText(LISTA_MUSICAS.get(contador));

                Vibrator vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);

                //deprecated in API 26
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                    vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                else vibrator.vibrate(500);

            }
        });

        mediaPlayer = MediaPlayer.create(getActivity().getApplicationContext(), R.raw.gaiola);

        mediaPlayer.start();


        txtList.setText(LISTA_MUSICAS.get(contador));

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
        mSensorManager.registerListener(mShakeDetector, mAccelerometer,	SensorManager.SENSOR_DELAY_UI);
        txtList.setVisibility(View.VISIBLE);
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



}
