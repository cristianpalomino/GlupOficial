package pe.com.glup.glup;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import pe.com.glup.R;
import pe.com.glup.utils.Util_Fonts;


public class Registro extends Glup {

    private Button facebook;
    private Button twitter;
    private Button instagram;

    private EditText edtnombres;
    private EditText edtusuario;
    private EditText edtpassword;

    private Button btnregistro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro);
        setupUI(findViewById(R.id.frame_registro));

        facebook = (Button) findViewById(R.id.btnfacebook);
        twitter = (Button) findViewById(R.id.btntwitter);
        instagram = (Button) findViewById(R.id.btninstagram);
        btnregistro = (Button) findViewById(R.id.btnregistro);

        edtusuario = (EditText) findViewById(R.id.edtusuario);
        edtnombres = (EditText) findViewById(R.id.edtnombres);
        edtpassword = (EditText) findViewById(R.id.edtpassword);

        TextView title = (TextView) findViewById(R.id.registro_title);
        TextView o = (TextView) findViewById(R.id.entrar_o);

        title.setTypeface(Util_Fonts.setRegular(this));
        o.setTypeface(Util_Fonts.setRegular(this));

        facebook.setTypeface(Util_Fonts.setBold(this));
        twitter.setTypeface(Util_Fonts.setBold(this));
        instagram.setTypeface(Util_Fonts.setBold(this));
        btnregistro.setTypeface(Util_Fonts.setBold(this));
        edtnombres.setTypeface(Util_Fonts.setLight(this));
        edtusuario.setTypeface(Util_Fonts.setLight(this));
        edtpassword.setTypeface(Util_Fonts.setLight(this));
    }
}
