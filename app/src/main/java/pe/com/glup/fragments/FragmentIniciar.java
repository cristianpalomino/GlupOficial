package pe.com.glup.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import pe.com.glup.R;
import pe.com.glup.beans.Usuario;
import pe.com.glup.bus.BusHolder;
import pe.com.glup.datasource.DSLogin;
import pe.com.glup.dialog.GlupDialog;
import pe.com.glup.dialog.GlupDialogNew;
import pe.com.glup.glup.ForgetPassCode;
import pe.com.glup.glup.Principal;
import pe.com.glup.interfaces.OnSuccessLogin;
import pe.com.glup.session.Session_Manager;
import pe.com.glup.utils.Util_Fonts;

/**
 * Created by Glup on 9/10/15.
 */
public class FragmentIniciar extends Fragment implements OnSuccessLogin,View.OnClickListener{
	private EditText edtusuario,edtpassword;
	private Button btnRecoveryPass,btnentrar;
	private DSLogin dsLogin;
	private GlupDialogNew dialog;
	private Context context;
	private List<String> pruebaLog;
	@Override
	public void onCreate(Bundle savedInstance){
		super.onCreate(savedInstance);
	}
	@Override
	public View onCreateView(LayoutInflater inflater,
							 ViewGroup container,Bundle savedInstance){
		BusHolder.getInstance().register(this);
		return inflater.inflate(R.layout.fragment_iniciar,container,false);
	}
	@Override
	public void onActivityCreated(Bundle savedInstance){
		super.onActivityCreated(savedInstance);
		context=getActivity();
		((ProgressBar)getView().findViewById(R.id.progressbar)).setVisibility(View.GONE);

		//pruebaLog.add("Esto me va dar un error");

		btnRecoveryPass = (Button) getView().findViewById(R.id.change_pass);
		btnentrar = (Button) getView().findViewById(R.id.btnentrar);
		edtusuario = (EditText) getView().findViewById(R.id.edtusuario);
		edtpassword = (EditText) getView().findViewById(R.id.edtpassword);
		edtusuario.setTypeface(Util_Fonts.setRegular(getActivity()));
		edtpassword.setTypeface(Util_Fonts.setRegular(getActivity()));

		btnentrar.setTypeface(Util_Fonts.setBold(getActivity()));
		btnRecoveryPass.setText(getResources().getString(R.string.btn_recovery_pass));
		btnRecoveryPass.setOnClickListener(this);
		btnentrar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			dialog = new GlupDialogNew();
			android.support.v4.app.FragmentManager fragmentManager= getActivity().getSupportFragmentManager();
			dialog.show(fragmentManager,GlupDialogNew.class.getSimpleName());

			String user = edtusuario.getText().toString();
			String pass = edtpassword.getText().toString();

			//String user = "User_Desa";
			//String pass = "desa123";

			dsLogin = new DSLogin(context);
			try {
				dsLogin.loginUsuario(user, pass);
				dsLogin.setOnSuccessLogin(FragmentIniciar.this);
			} catch (ClassCastException c) {
				c.printStackTrace();
			}

			}
		});
	}
	@Override
	public void onSuccessLogin(boolean status, Usuario usuario, String message) {
		dialog.dismiss();
		if (status) {
			openSession(usuario);
		}
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}
	private void openSession(Usuario usuario) {
		Session_Manager manager = new Session_Manager(context);
		manager.openSession(usuario);

		Intent intent = new Intent(context, Principal.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivity(intent);
		getActivity().finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.change_pass:
				btnRecoveryPass.setText(getResources().getString(R.string.btn_recovery_pass_pressed));
				startActivity(new Intent(context, ForgetPassCode.class));
				break;
		}
	}
}
