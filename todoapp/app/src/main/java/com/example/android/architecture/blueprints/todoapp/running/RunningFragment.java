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

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.android.architecture.blueprints.todoapp.R;

import java.util.Formatter;
import java.util.Locale;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Main UI for the statistics screen.
 */
public class RunningFragment extends Fragment implements RunningContract.View {

    CheckBox chkUseMetricUntis;
    TextView txtCurrentSpeed;

    CheckBox chkSimulacao;
    TextView textSpeed;


    private RunningContract.Presenter mPresenter;

    public static RunningFragment newInstance() {
        return new RunningFragment();
    }

    @Override
    public void setPresenter(@NonNull RunningContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.running_interface_frag, container, false);
        chkUseMetricUntis = (CheckBox) root.findViewById(R.id.chkMetricUnits);
        txtCurrentSpeed = (TextView) root.findViewById(R.id.txtCurrentSpeed);
        chkUseMetricUntis.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                updateSpeed(null);
            }
        });


        textSpeed = (TextView) root.findViewById(R.id.textSpeed);
        chkSimulacao = (CheckBox) root.findViewById(R.id.chkSimulacao);
        textSpeed.setText("0");

        return root;
    }

    public void updateSpeed(String speed) {
        // TODO Auto-generated method stub
//        float nCurrentSpeed = 0;
//
//        if(location != null)
//        {
//            location.setUseMetricunits(this.useMetricUnits());
//            nCurrentSpeed = location.getSpeed();
//        }
//
//        Formatter fmt = new Formatter(new StringBuilder());
//        fmt.format(Locale.US, "%5.1f", nCurrentSpeed);
//        String strCurrentSpeed = fmt.toString();
//        strCurrentSpeed = strCurrentSpeed.replace(' ', '0');

//        String strUnits = "miles/hour";
//        if(this.useMetricUnits())
//        {
//            strUnits = "meters/second";
//        }
//        txtCurrentSpeed.setText(strCurrentSpeed + " " + strUnits);


        textSpeed.setText(speed);
    }


    public boolean useMetricUnits() {
        // TODO Auto-generated method stub
        return this.chkUseMetricUntis.isChecked();
    }


    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setProgressIndicator(boolean active) {

    }

    @Override
    public void showStatistics(int numberOfIncompleteTasks, int numberOfCompletedTasks) {

    }

    @Override
    public void showLoadingStatisticsError() {
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }
}
