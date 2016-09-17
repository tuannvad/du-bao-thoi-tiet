package location;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.tuannv007.weatherforecast.MainActivity;
import com.tuannv007.weatherforecast.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import api.MySingleton;
import base.BaseFragment;
import weather.Weather;


/**
 * Created by admin on 8/28/2016.
 */
public class MyDefaultLocation extends BaseFragment implements View.OnClickListener {
    private static final String SAVE_DATA_LAST_UPDATE = "save_date_last_update";
    private static final String CITY_NAME = "city_name";
    private static final String TEMP = "temp";
    private static final String HUMIDITY = "humidity";
    private static final String CLOUDS = "clouds";
    private static final String SPEED_WIND = "speed_wind";
    private TextView txtDate, txtCityName, txtMainWeather, txtTemp, txtSpeedWind, txtHumidity, txtClouds;
    private String TAG = "MyDefaultLocation";
    private static final String keyApi = "706bde0b46846d0958eef7aa860ced53";
    private String linkDefaultLocation = "";
    private SharedPreferences sharedPreferences;
    private Weather weather = new Weather();
    private ProgressDialog dialog;
    private ImageView imvIconWeather;
    private DrawerLayout drawerLayout;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.default_location_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        updateLocation();
        CheckEnableGPS();
        getDateDefault();
        getDataJsonFromServer();
        showDialog();
    }

    private void updateLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 1, locationListener);
    }

    private void CheckEnableGPS() {
        String provider = Settings.Secure.getString(getActivity().getContentResolver(),
                Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if (!provider.equals("")) {
            //GPS Enabled
          /*  getDefaultLocation();*/
            linkDefaultLocation = "http://api.openweathermap.org/data/2.5/weather?lat=" + getLatitude() + "&lon=" + getLongitude() + "&appid=" + keyApi;
        } else {
            Toast.makeText(getActivity(), "not load gps", Toast.LENGTH_LONG).show();
        }

    }

    private void saveDataLastUpdate() {
        //// FIXME: 9/5/2016 save data by shareprefense
        //// TODO: 9/6/2016  fixed
        if (sharedPreferences != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(CITY_NAME, weather.getCityName());
            editor.putString(TEMP, String.format("%.1f", weather.getTemp()) + "");
            editor.putString(HUMIDITY, weather.getHumidity() + "");
            editor.putString(CLOUDS, weather.getClouds() + "");
            editor.putString(SPEED_WIND, weather.getWind() + "");
            editor.apply();
        }

    }

    private void restoreDataLastUpdate() {
        //// TODO: 9/5/2016 restore data
        //// TODO: 9/6/2016 fixed

        if (sharedPreferences != null) {
            String city = sharedPreferences.getString(CITY_NAME, "");
            String temp = sharedPreferences.getString(TEMP, "");
            String humidity = sharedPreferences.getString(HUMIDITY, "");
            String clouds = sharedPreferences.getString(CLOUDS, "");
            String speed_wind = sharedPreferences.getString(SPEED_WIND, "");

            txtSpeedWind.setText(speed_wind);
            txtClouds.setText(clouds);
            txtHumidity.setText(humidity);
            txtCityName.setText(city);
            txtTemp.setText(temp);
        } else {
            Toast.makeText(getActivity(), "not connect internet", Toast.LENGTH_LONG).show();
        }
    }

    // init view
    private void initView(View view) {
        txtDate = (TextView) view.findViewById(R.id.txt_date);
        txtCityName = (TextView) view.findViewById(R.id.txt_name_city_default);
        txtTemp = (TextView) view.findViewById(R.id.txt_temp);
        txtSpeedWind = (TextView) view.findViewById(R.id.txt_speed_wind);
        txtClouds = (TextView) view.findViewById(R.id.txt_clouds);
        txtHumidity = (TextView) view.findViewById(R.id.txt_humidity);
        txtMainWeather = (TextView) view.findViewById(R.id.txt_main_weather);
        imvIconWeather = (ImageView) view.findViewById(R.id.imv_icon_weather);
        dialog = new ProgressDialog(getActivity());
        sharedPreferences = getActivity().getSharedPreferences(SAVE_DATA_LAST_UPDATE, Context.MODE_PRIVATE);
        initToolbar(view);
    }

    private void initToolbar(View view) {
        Toolbar mToolbar = (Toolbar) view.findViewById(R.id.my_toolbar);
        ((MainActivity) getActivity()).setSupportActionBar(mToolbar);
        NavigationView navigationView = (NavigationView) view.findViewById(R.id.navigation_viewer);
        drawerLayout = (DrawerLayout) view.findViewById(R.id.drawer);
        mToolbar.setNavigationIcon(R.drawable.nav_menu);
        mToolbar.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        mToolbar.setCollapsible(true);
        mToolbar.setTitle(getResources().getString(R.string.weather));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();

                switch (id) {
                    case R.id.inbox:
                        Toast.makeText(getActivity(), "inbox1", Toast.LENGTH_LONG).show();
                        drawerLayout.closeDrawer(Gravity.LEFT);
                        break;
                    case R.id.inbox2:
                        Toast.makeText(getActivity(), "inbox1", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.inbox3:
                        Toast.makeText(getActivity(), "inbox1", Toast.LENGTH_LONG).show();

                }
                return true;
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.menu_right, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    // Get default location of user


    // Get date default in device android
    private void getDateDefault() {
        String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        txtDate.setText(date);

    }

    private void showDialog() {
        //// FIXME: 9/7/2016 dialog
        dialog.show();
        dialog.setMessage("Loading");
        dialog.setCancelable(false);
        dialog.setProgressStyle(1);
    }


    // Get data from Server by Volley
    private void getDataJsonFromServer() {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                linkDefaultLocation, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    dialog.dismiss();
                    /*dialog.dismiss();*/
                    String nameLocation = response.getString("name");
                    JSONObject winds = response.getJSONObject("wind");
                    JSONObject main = response.getJSONObject("main");
                    JSONObject clouds = response.getJSONObject("clouds");
                    JSONArray weatherData = response.getJSONArray("weather");
                    JSONObject objectWeatherData = weatherData.getJSONObject(0);
                    String icon = objectWeatherData.getString("icon");
                    String main_weather = objectWeatherData.getString("main");
                    String linkIcon = "http://openweathermap.org/img/w/" + icon + ".png";

                    double iClouds = clouds.getDouble("all");
                    double temp = main.getDouble("temp");
                    double speedWind = winds.getDouble("speed");
                    float c = (float) (temp - 270.15);
                    double humidity = main.getDouble("humidity");

                    Glide.with(getActivity()).load(linkIcon).into(imvIconWeather);
                    weather.setClouds(iClouds);
                    weather.setCityName(nameLocation);
                    weather.setTemp(c);
                    weather.setHumidity(humidity);
                    weather.setWind((int) speedWind);
                    weather.setMain(main_weather);

                    txtHumidity.setText(weather.getHumidity() + "%");
                    txtCityName.setText(weather.getCityName());
                    txtTemp.setText(String.format("%.1f", weather.getTemp())); // C = (K-273.15)
                    txtSpeedWind.setText(weather.getWind() + "m/s");
                    txtClouds.setText(weather.getClouds() + "%");
                    txtMainWeather.setText("Weather Status: " + weather.getMain());

                    Log.e("tag", nameLocation);
                    saveDataLastUpdate();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                restoreDataLastUpdate();
                dialog.dismiss();

            }


        });
        MySingleton.getInstance(getActivity()).addToRequestQueue(jsonObjReq);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

        }
    }

    @Override
    public void onResume() {

        super.onResume();
    }

    @Override
    public void onPause() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLocationManager.removeUpdates(locationListener);
        drawerLayout.closeDrawer(Gravity.LEFT);
        super.onPause();
    }


}
