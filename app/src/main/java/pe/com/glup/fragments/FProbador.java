package pe.com.glup.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import pe.com.glup.R;
import pe.com.glup.adapters.PagerBottomAdapter;
import pe.com.glup.adapters.PagerTopAdapter;
import pe.com.glup.beans.Prenda;
import pe.com.glup.bus.BusHolder;
import pe.com.glup.datasource.DSProbador;
import pe.com.glup.glup.Glup;
import pe.com.glup.views.ScrollPager;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FProbador.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FProbador#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FProbador extends Fragment {

    private ViewPager pagerTop;
    private ViewPager pagerBotton;
    private ArrayList<Prenda> prendasTop;
    private ArrayList<Prenda> prendasBottom;

    public static FProbador newInstance() {
        FProbador fragment = new FProbador();
        return fragment;
    }

    public FProbador() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BusHolder.getInstance().register(this);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_probador, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /*
        SlidingMenu menuright = new SlidingMenu(getActivity());
        menuright.setMode(SlidingMenu.LEFT_RIGHT);
        menuright.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
//        menu.setShadowWidthRes(R.dimen.shadow_width);
//        menu.setShadowDrawable(R.drawable.shadow);
        menuright.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        menuright.setFadeDegree(0.35f);
        menuright.attachToActivity(getActivity(), SlidingMenu.SLIDING_CONTENT);

        menuright.setMenu(R.layout.menu_left);
        menuright.setSecondaryMenu(R.layout.menu_right);
*/
        pagerTop = (ViewPager) getView().findViewById(R.id.scroll_top);
        pagerBotton = (ViewPager) getView().findViewById(R.id.scroll_bottom);

        pagerTop.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(prendasTop.get(position).getTipo().toUpperCase().equals("VESTIDO"))
                {
                    pagerBotton.setVisibility(View.GONE);
                }else{
                    pagerBotton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        DSProbador dsProbadorA = new DSProbador(getActivity());
        dsProbadorA.getGlobalPrendas("A", "1", "20");

        DSProbador dsProbadorB = new DSProbador(getActivity());
        dsProbadorB.getGlobalPrendas("B", "1", "20");
    }

    @Subscribe
    public void succesPrendas(DSProbador.ResponseProbador responseProbador) {
        if (responseProbador.tipo.equals("A"))
        {
            this.prendasTop = responseProbador.prendas;
            PagerTopAdapter pagerTopAdapter = new PagerTopAdapter(getActivity(), this.prendasTop);
            pagerTop.setAdapter(pagerTopAdapter);

        } else if (responseProbador.tipo.equals("B"))
        {
            this.prendasBottom = responseProbador.prendas;
            PagerBottomAdapter pagerBottomAdapter = new PagerBottomAdapter(getActivity(), this.prendasBottom);
            pagerBotton.setAdapter(pagerBottomAdapter);
        }
    }
}
