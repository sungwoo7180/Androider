package com.example.bottomnavi;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.ViewGroup;

import net.daum.mf.map.api.CameraUpdateFactory;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPointBounds;
import net.daum.mf.map.api.MapPolyline;
import net.daum.mf.map.api.MapView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
public class MyLocationListener implements LocationListener {
    private MapView mapView;
    private MapPOIItem customMarker;

    public MyLocationListener(MapView mapView, MapPOIItem customMarker) {
        this.mapView = mapView;
        this.customMarker = customMarker;
    }

    @Override
    public void onLocationChanged(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(latitude, longitude);
        customMarker.setMapPoint(mapPoint);

        mapView.setMapCenterPoint(mapPoint, true);
        mapView.removeAllPOIItems();
        mapView.addPOIItem(customMarker);
    }

    // 아래의 메서드들은 필요에 따라 구현해주세요.
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onProviderDisabled(String provider) {}
}

