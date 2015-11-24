package pe.com.glup.glup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import pe.com.glup.R;
import pe.com.glup.models.Usuario;
import pe.com.glup.network.DSRegistro;
import pe.com.glup.dialog.GlupDialog;
import pe.com.glup.models.interfaces.OnSuccessRegistro;
import pe.com.glup.managers.session.Session_Manager;
import pe.com.glup.utils.Util_Fonts;


public class Registro extends Glup implements OnSuccessRegistro {

    private Button facebook;
    private Button twitter;
    private Button instagram;

    private EditText edtnombres;
    private EditText edtusuario;
    private EditText edtpassword;

    private Button btnregistro;

    private DSRegistro dsRegistro;
    private GlupDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_new);
        setupUI(findViewById(R.id.frame_registro));
/*
        facebook = (Button) findViewById(R.id.btnfacebook);
        twitter = (Button) findViewById(R.id.btntwitter);
        instagram = (Button) findViewById(R.id.btninstagram);*/
        btnregistro = (Button) findViewById(R.id.btnregistro);

        edtusuario = (EditText) findViewById(R.id.edtusuario);
        edtnombres = (EditText) findViewById(R.id.edtnombres);
        edtpassword = (EditText) findViewById(R.id.edtpassword);

        /*
        TextView title = (TextView) findViewById(R.id.registro_title);
        TextView o = (TextView) findViewById(R.id.entrar_o);

        title.setTypeface(Util_Fonts.setRegular(this));
        o.setTypeface(Util_Fonts.setRegular(this));
        */
/*
        TextView title = (TextView) findViewById(R.id.title_registro);
        title.setTypeface(Util_Fonts.setBold(this));*/
/*
        facebook.setTypeface(Util_Fonts.setBold(this));
        twitter.setTypeface(Util_Fonts.setBold(this));
        instagram.setTypeface(Util_Fonts.setBold(this));*/
        btnregistro.setTypeface(Util_Fonts.setBold(this));
        edtnombres.setTypeface(Util_Fonts.setRegular(this));
        edtusuario.setTypeface(Util_Fonts.setRegular(this));
        edtpassword.setTypeface(Util_Fonts.setRegular(this));

        btnregistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new GlupDialog(Registro.this);
                dialog.show();
/*
                dsRegistro = new DSRegistro(Registro.this);
                dsRegistro.registrarUsuario(edtnombres.getText().toString(),
                        edtusuario.getText().toString(),
                        edtpassword.getText().toString());
                dsRegistro.setOnSuccessRegistro(Registro.this);*/
            }
        });
    }

    @Override
    public void onSuccessRegistro(boolean status, Usuario usuario, String message) {
        dialog.dismiss();
        if (usuario != null) {
            openSession(usuario);
        }
        Toast.makeText(Registro.this, message, Toast.LENGTH_SHORT).show();
    }

    private void openSession(Usuario usuario) {
        Session_Manager manager = new Session_Manager(Registro.this);
        manager.openSession(usuario);

        Intent intent = new Intent(Registro.this, Principal.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        Registro.this.finish();
    }
}
