package pe.com.glup.glup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import pe.com.glup.R;
import pe.com.glup.fragments.FragmentEntrarDefault;
import pe.com.glup.fragments.FragmentIniciar;
import pe.com.glup.fragments.FragmentRegistrate;
import pe.com.glup.managers.session.Session_Manager;


public class Entrar extends AppCompatActivity implements  View.OnClickListener {
    private static final String TAG[]={
            FragmentEntrarDefault.class.getSimpleName(),
            FragmentIniciar.class.getSimpleName(),
            FragmentRegistrate.class.getSimpleName()
    };
    private ProgressBar progressBar;

    private Button btnIniciar,btnRegistrar;
    private FrameLayout frameLayout;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;


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
            /*facebook = (Button) findViewById(R.id.btnfacebook);
            twitter = (Button) findViewById(R.id.btntwitter);
            instagram = (Button) findViewById(R.id.btninstagram);*/
            fragmentManager = this.getSupportFragmentManager();
            /*
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_entrar, new FragmentEntrarDefault(), TAG[0]);
            fragmentTransaction.commit();
            */

            btnIniciar = (Button) findViewById(R.id.btnIniciar);
            btnRegistrar = (Button) findViewById(R.id.btnRegistrar);

            btnIniciar.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_selector_pressed));
            btnIniciar.setTextColor(getResources().getColor(R.color.blanco));
            btnRegistrar.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_selector));
            btnRegistrar.setTextColor(getResources().getColor(R.color.celeste_glup));
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_entrar,new FragmentIniciar(),TAG[1]);
            fragmentTransaction.commit();


            btnIniciar.setOnClickListener(this);
            btnRegistrar.setOnClickListener(this);

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnIniciar:
                Log.e(null, "clic iniciar");
                //startActivity(new Intent());
                btnIniciar.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_selector_pressed));
                btnIniciar.setTextColor(getResources().getColor(R.color.blanco));
                btnRegistrar.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_selector));
                btnRegistrar.setTextColor(getResources().getColor(R.color.celeste_glup));
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_entrar,new FragmentIniciar(),TAG[1]);
                fragmentTransaction.commit();
                break;
            case  R.id.btnRegistrar:
                Log.e(null, "clic registrar");/*
                startActivity(new Intent(this,RegistroNew.class));*/

                btnRegistrar.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_selector_pressed));
                btnRegistrar.setTextColor(getResources().getColor(R.color.blanco));
                btnIniciar.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_selector));
                btnIniciar.setTextColor(getResources().getColor(R.color.celeste_glup));
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_entrar,new FragmentRegistrate(),TAG[2]);
                fragmentTransaction.commit();
                break;
        }
    }
}
