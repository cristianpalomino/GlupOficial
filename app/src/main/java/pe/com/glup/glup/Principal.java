package pe.com.glup.glup;

import android.os.Bundle;

import pe.com.glup.R;
import pe.com.glup.datasource.DSCatalogo;
import pe.com.glup.fragments.Fragment_Home;
import pe.com.glup.views.GlupTab;
import pe.com.glup.views.Header;

public class Principal extends Glup implements GlupTab.OnChangeTab {

    private final String[] MESSAGES = {"HOME", "CLOSET", "PROBADOR", "CAMERA"};
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

        DSCatalogo dsCatalogo = new DSCatalogo(this);
        dsCatalogo.sendRequest("todos","1","10");
    }

    @Override
    public void onChangeTab(int position) {
        onChangeTab.onChangeTab(position);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_principal, Fragment_Home.newInstance("", MESSAGES[position])).commit();
    }

    @Override
    public void currentTab(int current) {
        onChangeTab.currentTab(current);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_principal, Fragment_Home.newInstance("", MESSAGES[current])).commit();
    }

    public interface OnChangeTab {
        void onChangeTab(int position);
        void currentTab(int current);
    }
}
