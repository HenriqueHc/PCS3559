package com.example.android.architecture.blueprints.todoapp.running;

import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

/**
 * Created by mac on 12/11/18.
 */

public interface IBaseGpsListener extends LocationListener, GpsStatus.Listener {

    void onLocationChanged(Location location);

    void onProviderDisabled(String provider);

    void onProviderEnabled(String provider);

    void onStatusChanged(String provider, int status, Bundle extras);

    void onGpsStatusChanged(int event);

}