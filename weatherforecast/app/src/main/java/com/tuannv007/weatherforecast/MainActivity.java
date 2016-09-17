package com.tuannv007.weatherforecast;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.viewpagerindicator.CirclePageIndicator;

import adapter.ForcatsAdapter;

public class MainActivity extends AppCompatActivity {

    private ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       /* initFragment();*/
        initPager();
    }

    private void initPager() {
        pager = (ViewPager) findViewById(R.id.view_pager);
        FragmentManager manager = getSupportFragmentManager();
        ForcatsAdapter adapter = new ForcatsAdapter(manager);
        pager.setAdapter(adapter);
        CirclePageIndicator indicator = (CirclePageIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(pager);
        final float density = getResources().getDisplayMetrics().density;
        indicator.setRadius(4 * density);
        indicator.setStrokeColor(Color.WHITE);
        indicator.setStrokeWidth(1 * density);
    }

}

