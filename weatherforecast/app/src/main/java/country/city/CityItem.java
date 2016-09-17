package country.city;

/**
 * Created by admin on 8/27/2016.
 */
public class CityItem {
    private String nameCountry;
    private int imageCountry;

    public int getImageCountry() {
        return imageCountry;
    }

    public String getNameCountry() {
        return nameCountry;
    }

    public CityItem(int imageCountry, String nameCountry) {
        this.imageCountry = imageCountry;
        this.nameCountry = nameCountry;
    }
}
