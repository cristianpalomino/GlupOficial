package pe.com.glup.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Glup on 30/07/15.
 */
public class Adapter_tallas extends FragmentPagerAdapter {

    List<Fragment> fragmentos;
    public Adapter_tallas(FragmentManager fm) {
        super(fm);
        fragmentos = new ArrayList();
    }

    public void agregarFragmentos(Fragment xfragmento){
        fragmentos.add(xfragmento);
    }


    @Override
    public Fragment getItem(int position) {
        return fragmentos.get(position);
    }

    @Override
    public int getCount() {
        return fragmentos.size();
    }
}
