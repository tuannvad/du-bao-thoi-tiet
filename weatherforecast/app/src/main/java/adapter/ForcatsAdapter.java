package adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import location.Forcast5Day;
import location.MyDefaultLocation;

/**
 * Created by admin on 9/15/2016.
 */
public class ForcatsAdapter extends FragmentStatePagerAdapter {


    private int numberFragment=2;

    public ForcatsAdapter(FragmentManager fm) {
        super(fm);
    }
    @Override
    public Fragment getItem(int position) {
        Fragment frag=null;
        switch (position){
            case 0:
                frag=new MyDefaultLocation();
                break;
            case 1:
                frag=new Forcast5Day();
                break;
        }
        return frag;    }

    @Override
    public int getCount() {
        return numberFragment;
    }
}
