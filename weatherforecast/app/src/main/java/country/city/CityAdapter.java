package country.city;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tuannv007.weatherforecast.R;

import java.util.ArrayList;

/**
 * Created by admin on 8/27/2016.
 */
public class CityAdapter extends RecyclerView.Adapter<CityAdapter.CountryHolder> {
    private ArrayList<CityItem> listItemCountry = new ArrayList<>();
    private Context mContext;
    public CityAdapter(ArrayList<CityItem> listItemCountry,Context mContext) {
        this.listItemCountry = listItemCountry;
        this.mContext =mContext;
    }

    @Override
    public CountryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_city_layout, parent, false);
        return new CountryHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CountryHolder holder, int position) {
        holder.txtCountryName.setText(listItemCountry.get(position).getNameCountry());
        Picasso.with(mContext).load(listItemCountry.get(position).getImageCountry()).resize(100,100).centerCrop().into(holder.imvImageCountry);
    }

    @Override
    public int getItemCount() {
        return listItemCountry.size();
    }

    class CountryHolder extends RecyclerView.ViewHolder {
        private TextView txtCountryName;
        private ImageView imvImageCountry;

        public CountryHolder(View itemView) {
            super(itemView);
            txtCountryName = (TextView) itemView.findViewById(R.id.txt_name_city);
            imvImageCountry = (ImageView) itemView.findViewById(R.id.imv_image_city);
        }
    }
}
