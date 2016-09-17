package location;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tuannv007.weatherforecast.R;

import java.util.ArrayList;

import country.city.CityAdapter;
import country.city.CityItem;

/**
 * Created by admin on 8/27/2016.
 */
public class CityLocation extends Fragment {
    private ArrayList<CityItem> listCity;
    private CityAdapter mAdapter;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.city_location_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView) view.findViewById(R.id.list_city);
        initCityInCountry();
    }
    private void initCityInCountry() {
        initCity();
        mAdapter = new CityAdapter(listCity, getActivity());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);
    }

    private void initCity() {
        listCity = new ArrayList<>();
        listCity.add(new CityItem(R.drawable.ic_hanoi, "Hà Nội"));
        listCity.add(new CityItem(R.drawable.ic_hanoi, "Bắc Giang"));
        listCity.add(new CityItem(R.drawable.ic_hanoi, "Thái Bình"));
    }
}
