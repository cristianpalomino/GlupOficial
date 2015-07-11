package pe.com.glup.glup;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;

import com.viewpagerindicator.CirclePageIndicator;

import pe.com.glup.R;
import pe.com.glup.adapters.Adapter_Incio;
import pe.com.glup.utils.Util_Fonts;
import pe.com.glup.views.GifView;


public class Inicio extends Glup implements View.OnClickListener {

    private Button btnentrar;
    private Button btnregistro;

    private GifView gift;
    private Adapter_Incio adapter;
    private ViewPager pager;
    private CirclePageIndicator indicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inicio);
        setupUI(findViewById(R.id.frame_inicio));

        adapter = new Adapter_Incio(getSupportFragmentManager());
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        indicator = (CirclePageIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(pager);

        //gift = (GifView) findViewById(R.id.gift);
        //gift.setMovieResource(R.drawable.video);

        btnentrar = (Button) findViewById(R.id.btnentrar);
        btnregistro = (Button) findViewById(R.id.btnregistro);

        btnentrar.setTypeface(Util_Fonts.setBold(this));
        btnregistro.setTypeface(Util_Fonts.setBold(this));

        btnregistro.setOnClickListener(this);
        btnentrar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        if (v.equals(btnentrar)) {
            intent = new Intent(Inicio.this, Entrar.class);
        } else if (v.equals(btnregistro)) {
            intent = new Intent(Inicio.this, Registro.class);
        }
        startActivity(intent);
    }
}
