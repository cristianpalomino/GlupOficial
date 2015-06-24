package pe.com.glup.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import pe.com.glup.fragments.Fragment_Tutorial;

/**
 * Created by Glup on 19/06/15.
 */
public class Adapter_Incio extends FragmentPagerAdapter {

    protected static final String[] CONTENT = new String[]{"Esto", "Es", "Glup", "!!!!",};
    private int mCount = CONTENT.length;

    public Adapter_Incio(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return Fragment_Tutorial.newInstance(CONTENT[position]);
    }

    @Override
    public int getCount() {
        return mCount;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return CONTENT[position];
    }

    public void setCount(int count) {
        if (count > 0 && count <= 10) {
            mCount = count;
            notifyDataSetChanged();
        }
    }
}
