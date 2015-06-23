package pe.com.glup.glup;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.viewpagerindicator.CirclePageIndicator;

import pe.com.glup.R;
import pe.com.glup.adapters.Adapter_Incio;
import pe.com.glup.utils.Util_Fonts;
import pe.com.glup.views.GifView;


public class Entrar extends Glup {

    private Button facebook;
    private Button twitter;
    private Button instagram;

    private EditText edtusuario;
    private EditText edtpassword;

    private Button btnentrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entrar);
        setupUI(findViewById(R.id.frame_entrar));

        facebook = (Button) findViewById(R.id.btnfacebook);
        twitter = (Button) findViewById(R.id.btntwitter);
        instagram = (Button) findViewById(R.id.btninstagram);
        btnentrar = (Button) findViewById(R.id.btnentrar);

        edtusuario = (EditText) findViewById(R.id.edtusuario);
        edtpassword = (EditText) findViewById(R.id.edtpassword);

        TextView title = (TextView) findViewById(R.id.entrar_title);
        TextView o = (TextView) findViewById(R.id.entrar_o);

        title.setTypeface(Util_Fonts.setRegular(this));
        o.setTypeface(Util_Fonts.setRegular(this));

        facebook.setTypeface(Util_Fonts.setBold(this));
        twitter.setTypeface(Util_Fonts.setBold(this));
        instagram.setTypeface(Util_Fonts.setBold(this));
        btnentrar.setTypeface(Util_Fonts.setBold(this));
        edtusuario.setTypeface(Util_Fonts.setLight(this));
        edtpassword.setTypeface(Util_Fonts.setLight(this));

    }
}
