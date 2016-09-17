package base;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;


/**
 * Created by admin on 8/28/2016.
 */
public abstract class BaseFragment extends Fragment {
    public Location lastLocation;
    public double longitude, latitude;
    public final LocationListener locationListener;
    public LocationManager mLocationManager;
    public void getDefaultLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Criteria criteria = new Criteria();
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        lastLocation = mLocationManager.getLastKnownLocation(mLocationManager.getBestProvider(criteria, false));
    }
    public double getLongitude() {
        getDefaultLocation();
        if (lastLocation != null) {
            longitude = lastLocation.getLongitude();

        }
        return longitude;
    }
    public double getLatitude(){
        getDefaultLocation();
        if (lastLocation != null) {
            latitude = lastLocation.getLatitude();

        }
        return latitude;
    }
    {
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                //location.getLatitude(), location.getLongtitude()
                //location coordinates does not change at all everytime onLocationChanged is called even after I already move several meters or kilometers.
                longitude = location.getLongitude();
                latitude = location.getLatitude();
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }

        };
    }
}
