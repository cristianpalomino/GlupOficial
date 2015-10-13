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
import android.widget.ToggleButton;

import pe.com.glup.R;
import pe.com.glup.fragments.FragmentEntrarDefault;
import pe.com.glup.fragments.FragmentIniciar;
import pe.com.glup.fragments.FragmentRegistrate;
import pe.com.glup.session.Session_Manager;

/**
 * Created by Glup on 12/10/15.
 */
public class RegistroNew extends Glup implements  View.OnClickListener {
	private static final String TAG[]={
			FragmentEntrarDefault.class.getSimpleName(),
			FragmentIniciar.class.getSimpleName(),
			FragmentRegistrate.class.getSimpleName()
	};
	private ProgressBar progressBar;

	private ToggleButton btnIniciar,btnRegistrar;
	private FragmentManager fragmentManager;
	private FragmentTransaction fragmentTransaction;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registro_new_activity);

		Session_Manager manager = new Session_Manager(RegistroNew.this);
		if (manager.isLogin()) {
			Intent intent = new Intent(RegistroNew.this, Principal.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
			startActivity(intent);
		} else {
            /*facebook = (Button) findViewById(R.id.btnfacebook);
            twitter = (Button) findViewById(R.id.btntwitter);
            instagram = (Button) findViewById(R.id.btninstagram);*/

			btnIniciar = (ToggleButton) findViewById(R.id.btnIniciar);
			btnRegistrar = (ToggleButton) findViewById(R.id.btnRegistrar);

			btnIniciar.setOnClickListener(this);
			btnRegistrar.setOnClickListener(this);

		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.btnIniciar:
				Log.e(null, "clic iniciar");

				break;
			case  R.id.btnRegistrar:
				Log.e(null, "clic registrar");
				startActivity(new Intent(this,Entrar.class));
				break;
		}
	}
}