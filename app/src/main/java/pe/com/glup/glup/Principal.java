package pe.com.glup.glup;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import pe.com.glup.R;
import pe.com.glup.datasource.DSCatalogo;
import pe.com.glup.fragments.FCatalogo;
import pe.com.glup.fragments.Fragment_Home;
import pe.com.glup.views.GlupTab;
import pe.com.glup.views.Header;

public class Principal extends Glup implements GlupTab.OnChangeTab {

    private final String[] MESSAGES = {"HOME", "CLOSET", "PROBADOR", "CAMERA"};
    private final Fragment[] FRAGMENTS = {
                                          FCatalogo.newInstance(),
                                          Fragment_Home.newInstance(MESSAGES[1],MESSAGES[1]),
                                          Fragment_Home.newInstance(MESSAGES[2],MESSAGES[2]),
                                          Fragment_Home.newInstance(MESSAGES[3],MESSAGES[3])
                                          };


    private GlupTab glupTab;
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

        glupTab = (GlupTab) findViewById(R.id.glutab);
        header = (Header) findViewById(R.id.header);
        header.initView(Principal.this);

        glupTab.setOnChangeTab(this);
        glupTab.initView();
    }

    @Override
    public void onChangeTab(int position) {
        onChangeTab.onChangeTab(position);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_principal, FRAGMENTS[position])
                .commit();    }

    @Override
    public void currentTab(int current) {
        onChangeTab.currentTab(current);
        getSupportFragmentManager().beginTransaction()
                                   .replace(R.id.frame_principal,FRAGMENTS[current])
                                   .commit();
    }

    public interface OnChangeTab {
        void onChangeTab(int position);
        void currentTab(int current);
    }

    public Header getHeader() {
        return header;
    }
}
