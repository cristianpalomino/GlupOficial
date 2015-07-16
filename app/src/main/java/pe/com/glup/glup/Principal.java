package pe.com.glup.glup;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import pe.com.glup.R;
import pe.com.glup.fragments.FCatalogo;
import pe.com.glup.fragments.FCloset;
import pe.com.glup.fragments.Fragment_Home;
import pe.com.glup.views.Footer;
import pe.com.glup.views.Header;

public class Principal extends Glup implements Footer.OnChangeTab {

    private final String[] MESSAGES = {"HOME", "CLOSET", "PROBADOR", "CAMERA"};
    private final Fragment[] FRAGMENTS = {
            FCatalogo.newInstance(),
            FCloset.newInstance(),
            Fragment_Home.newInstance(MESSAGES[2], MESSAGES[2]),
            Fragment_Home.newInstance(MESSAGES[3], MESSAGES[3])
    };
    private String CURRENT_FRAGMENT_TAG;


    private Footer footer;
    private Header header;
    private OnChangeTab onChangeTab;

    public void setOnChangeTab(OnChangeTab onChangeTab) {
        this.onChangeTab = onChangeTab;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.principal);
        setupUI(findViewById(R.id.container_principal));

        footer = (Footer) findViewById(R.id.glutab);
        header = (Header) findViewById(R.id.header);
        header.initView(Principal.this);

        footer.setOnChangeTab(this);
        footer.initView();
    }

    @Override
    public void onChangeTab(int position) {
        onChangeTab.onChangeTab(position);
        CURRENT_FRAGMENT_TAG = FRAGMENTS[position].getClass().getName().toString();
        Fragment current = getSupportFragmentManager().findFragmentByTag(CURRENT_FRAGMENT_TAG);
        if (current != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_principal, FRAGMENTS[position], CURRENT_FRAGMENT_TAG)
                    .commit();
        } else {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_principal, FRAGMENTS[position], CURRENT_FRAGMENT_TAG)
                    .addToBackStack(CURRENT_FRAGMENT_TAG)
                    .commit();
        }

        Log.e("FRAGMENTS", getSupportFragmentManager().getBackStackEntryCount() + "");
        Log.e("FRAGMENTS", CURRENT_FRAGMENT_TAG + "");
    }

    @Override
    public void currentTab(int current) {
        onChangeTab.currentTab(current);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_principal, FRAGMENTS[current])
                .commit();
    }

    public interface OnChangeTab {
        void onChangeTab(int position);

        void currentTab(int current);
    }

    public Header getHeader() {
        return header;
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }
}
