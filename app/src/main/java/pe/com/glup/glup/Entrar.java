package pe.com.glup.glup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.viewpagerindicator.CirclePageIndicator;

import pe.com.glup.R;
import pe.com.glup.adapters.Adapter_Incio;
import pe.com.glup.beans.Usuario;
import pe.com.glup.datasource.DSCatalogo;
import pe.com.glup.datasource.DSLogin;
import pe.com.glup.dialog.GlupDialog;
import pe.com.glup.interfaces.OnSuccessLogin;
import pe.com.glup.session.Session_Manager;
import pe.com.glup.utils.Util_Fonts;
import pe.com.glup.views.GifView;


public class Entrar extends Glup implements OnSuccessLogin, View.OnClickListener {

    private Button facebook;
    private Button twitter;
    private Button instagram;

    private EditText edtusuario;
    private EditText edtpassword;

    private Button btnentrar,btnIniciar,btnRegistrar;
    private DSLogin dsLogin;

    private GlupDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entrar_new);



        Session_Manager manager = new Session_Manager(Entrar.this);
        if (manager.isLogin()) {
            Intent intent = new Intent(Entrar.this, Principal.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else {
            setupUI(findViewById(R.id.frame_entrar));


        /*facebook = (Button) findViewById(R.id.btnfacebook);
        twitter = (Button) findViewById(R.id.btntwitter);
        instagram = (Button) findViewById(R.id.btninstagram);*/
        btnentrar = (Button) findViewById(R.id.btnentrar);

        btnIniciar = (Button) findViewById(R.id.btnIniciar);
        btnRegistrar = (Button) findViewById(R.id.btnRegistrar);

        btnIniciar.setOnClickListener(this);
        btnRegistrar.setOnClickListener(this);

        edtusuario = (EditText) findViewById(R.id.edtusuario);
        edtpassword = (EditText) findViewById(R.id.edtpassword);

        /*
        TextView title = (TextView) findViewById(R.id.entrar_title);
        TextView o = (TextView) findViewById(R.id.entrar_o);

        title.setTypeface(Util_Fonts.setRegular(this));
        o.setTypeface(Util_Fonts.setRegular(this));
        */

        /*
        TextView title = (TextView) findViewById(R.id.title_entrar);
        title.setTypeface(Util_Fonts.setBold(this));*/

        /*facebook.setTypeface(Util_Fonts.setBold(this));
        twitter.setTypeface(Util_Fonts.setBold(this));
        instagram.setTypeface(Util_Fonts.setBold(this));*/
        btnentrar.setTypeface(Util_Fonts.setBold(this));
        edtusuario.setTypeface(Util_Fonts.setRegular(this));
        edtpassword.setTypeface(Util_Fonts.setRegular(this));

        btnentrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new GlupDialog(Entrar.this);
                dialog.show();

                //String user = edtusuario.getText().toString();
                //String pass = edtpassword.getText().toString();

                String user = "User_Desa";
                String pass = "desa123";

                dsLogin = new DSLogin(Entrar.this);
                dsLogin.loginUsuario(user, pass);
                dsLogin.setOnSuccessLogin(Entrar.this);
            }
        });

        }
    }

    @Override
    public void onSuccessLogin(boolean status, Usuario usuario, String message) {
        dialog.dismiss();
        if (status) {
            openSession(usuario);
        }
        Toast.makeText(Entrar.this, message, Toast.LENGTH_SHORT).show();
    }

    private void openSession(Usuario usuario) {
        Session_Manager manager = new Session_Manager(Entrar.this);
        manager.openSession(usuario);

        Intent intent = new Intent(Entrar.this, Principal.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        Entrar.this.finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnIniciar:
                btnIniciar.setBackgroundDrawable(getResources().getDrawable(R.drawable.glup_square_button_negro));
                btnRegistrar.setBackgroundDrawable(getResources().getDrawable(R.drawable.glup_square_button_celeste));
                break;
            case  R.id.btnRegistrar:
                Intent intent=null;
                intent = new Intent(Entrar.this, Registro.class);
                btnRegistrar.setBackgroundDrawable(getResources().getDrawable(R.drawable.glup_square_button_negro));
                btnIniciar.setBackgroundDrawable(getResources().getDrawable(R.drawable.glup_square_button_celeste));
                break;
        }
    }
}
