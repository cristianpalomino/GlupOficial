package pe.com.glup.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import pe.com.glup.R;
import pe.com.glup.models.Usuario;
import pe.com.glup.network.DSRegistro;
import pe.com.glup.dialog.GlupDialog;
import pe.com.glup.glup.Principal;
import pe.com.glup.models.interfaces.OnSuccessRegistro;
import pe.com.glup.managers.session.Session_Manager;

/**
 * Created by Glup on 9/10/15.
 */
public class FragmentRegistrate extends Fragment implements RadioButton.OnCheckedChangeListener,
	View.OnClickListener,OnSuccessRegistro,TextWatcher{
	private RadioButton hombre,mujer;
	private EditText edtCorreo,edtPassword;
	private Button btnregistro;
	private DSRegistro dsRegistro;
	private GlupDialog dialog;
	private Context context;
	private String sexo="";
	private int filtroUpper,filtroLower,filtroNumber,filtroRange;
	private ImageView passSuccess;
	private TextView txtPassSuccess,txtChoiceSexo;
	private boolean upper=false,lower=false,number=false,range=false;
	private ImageView iconValid;

	@Override
	public void onCreate(Bundle savedInstance){
		super.onCreate(savedInstance);
	}
	@Override
	public View onCreateView(LayoutInflater inflater,
							 ViewGroup container,Bundle savedInstance){
		return inflater.inflate(R.layout.fragment_registrate,container,false);
	}
	@Override
	public void onActivityCreated(Bundle savedInstance){
		super.onActivityCreated(savedInstance);
		context = getActivity();
		((ProgressBar)getView().findViewById(R.id.progressbar)).setVisibility(View.GONE);

		passSuccess = (ImageView) getView().findViewById(R.id.pass_success);
		txtPassSuccess = (TextView) getView().findViewById(R.id.txt_pass_success);
		btnregistro = (Button) getView().findViewById(R.id.btnregistro);
		edtCorreo = (EditText) getView().findViewById(R.id.edt_correo);
		edtPassword = (EditText) getView().findViewById(R.id.edt_password);
		hombre= (RadioButton) getView().findViewById(R.id.radio_hombre);
		mujer = (RadioButton) getView().findViewById(R.id.radio_mujer);
		txtChoiceSexo = (TextView) getView().findViewById(R.id.txt_select_sexo);

		hombre.setOnClickListener(this);
		mujer.setOnClickListener(this);
		btnregistro.setOnClickListener(this);
		edtPassword.addTextChangedListener(this);
		filtroLower=0;
		filtroUpper=0;
		filtroNumber=0;
		filtroRange=0;
		sexo="";
		if (edtPassword.getText().toString().length()==0){
			txtPassSuccess.setText(getResources().getString(R.string.validacion_pass_registro));
			passSuccess.setVisibility(View.GONE);
		}

	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()){
			case R.id.radio_hombre:
				mujer.setChecked(false);
				hombre.setChecked(true);
				break;
			case R.id.radio_mujer:
				hombre.setChecked(false);
				mujer.setChecked(true);
				break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.radio_hombre:
				mujer.setChecked(false);
				hombre.setChecked(true);
				txtChoiceSexo.setVisibility(View.GONE);
				break;
			case R.id.radio_mujer:
				hombre.setChecked(false);
				mujer.setChecked(true);
				txtChoiceSexo.setVisibility(View.GONE);
				break;
			case R.id.btnregistro:
				if (mujer.isChecked()){
					sexo="M";
				}
				if (hombre.isChecked()){
					sexo="H";
				}

				if (upper && lower && number && range ){
					if (sexo==""){
						txtChoiceSexo.setVisibility(View.VISIBLE);
					}else{
						txtChoiceSexo.setVisibility(View.GONE);
						dialog = new GlupDialog(context);
						dialog.show();
						dsRegistro = new DSRegistro(context);
						try {
							dsRegistro.registrarUsuario("","",sexo,edtCorreo.getText().toString(),
									edtPassword.getText().toString());
							dsRegistro.setOnSuccessRegistro(FragmentRegistrate.this);
						}catch (ClassCastException c){
							c.printStackTrace();
						}
					}
				}else {
					//MessageUtil.showToastValidation(getActivity(),getResources().getString(R.string.validacion_contrasena_registro));
				}
				break;
		}

	}

	@Override
	public void onSuccessRegistro(boolean status, Usuario usuario, String message) {
		dialog.dismiss();
		if (usuario != null) {
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
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		upper=false;lower=false;number=false;range=false;
		int size=s.toString().length();
		if (size>0){
			passSuccess.setVisibility(View.VISIBLE);
		}else {
			passSuccess.setVisibility(View.GONE);
			txtPassSuccess.setText(getResources().getString(R.string.validacion_pass_registro));
		}
		if (size>=6 && size<=12){
			range=true;
		}
		for (int i=0;i<size;i++){
			if (upper==false && Character.isUpperCase(s.toString().charAt(i))){
				upper=true;
			}
			if (lower==false && Character.isLowerCase(s.toString().charAt(i))){
				lower=true;
			}
			if (number==false && Character.isDigit(s.toString().charAt(i))){
				number=true;
			}
		}
		if (upper && lower && number && range){
			passSuccess.setImageDrawable(getResources().getDrawable(R.drawable.icono_check));
			txtPassSuccess.setText("Contraseña segura");
		}else{
			passSuccess.setImageDrawable(getResources().getDrawable(R.drawable.icono_x));
			txtPassSuccess.setText("Contraseña insegura");
		}

	}

	@Override
	public void afterTextChanged(Editable s) {


	}
}