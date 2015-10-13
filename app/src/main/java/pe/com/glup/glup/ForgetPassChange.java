package pe.com.glup.glup;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import pe.com.glup.R;
import pe.com.glup.bus.BusHolder;
import pe.com.glup.datasource.DSLogin;

/**
 * Created by Glup on 12/10/15.
 */
public class ForgetPassChange extends AppCompatActivity implements View.OnClickListener{
	private EditText inputNewPass,inputRepeatPass;
	private TextView textConfirmPass,textRepeatPass;
	private Button btnNextLogin;
	private boolean upper=false,lower=false,number=false,range=false;
	private Context context;
	@Override
	public void onCreate(Bundle savedInstance){
		super.onCreate(savedInstance);
		setContentView(R.layout.iniciar_forget_pass_change);
		BusHolder.getInstance().register(this);
		context = this;
		inputNewPass=(EditText)findViewById(R.id.new_pass);
		inputRepeatPass = (EditText) findViewById(R.id.repeat_new_pass);
		textConfirmPass = (TextView) findViewById(R.id.txt_pass_info_success);
		textRepeatPass = (TextView) findViewById(R.id.txt_pass_success);
		btnNextLogin = (Button) findViewById(R.id.btn_next_login);
		btnNextLogin.setEnabled(false);
		inputRepeatPass.setEnabled(false);
		textRepeatPass.setVisibility(View.INVISIBLE);
		btnNextLogin.setOnClickListener(this);
		inputNewPass.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				upper = false;
				lower = false;
				number = false;
				range = false;
				int size = s.toString().length();
				if (size == 0) {
					textConfirmPass.setText(getResources().getString(R.string.validacion_pass_registro));
				}
				if (size >= 6 && size <= 12) {
					range = true;
				}
				for (int i = 0; i < size; i++) {
					if (upper == false && Character.isUpperCase(s.toString().charAt(i))) {
						upper = true;
					}
					if (lower == false && Character.isLowerCase(s.toString().charAt(i))) {
						lower = true;
					}
					if (number == false && Character.isDigit(s.toString().charAt(i))) {
						number = true;
					}
				}
				if (upper && lower && number && range) {
					//passSuccess.setImageDrawable(getResources().getDrawable(R.drawable.icono_check));
					textConfirmPass.setText("Contraseña segura");
					inputRepeatPass.setEnabled(true);
				} else {
					//passSuccess.setImageDrawable(getResources().getDrawable(R.drawable.icono_x));
					textConfirmPass.setText("Contraseña insegura");
					inputRepeatPass.setEnabled(false);
				}

			}

			@Override
			public void afterTextChanged(Editable s) {
				int size = s.toString().length();
				if (size == 0) {
					textConfirmPass.setText(getResources().getString(R.string.validacion_pass_registro));
				}
			}
		});
		inputRepeatPass.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				int size = s.toString().length();
				if (size > 0) {
					textRepeatPass.setVisibility(View.VISIBLE);
					if (s.toString().equals(inputNewPass.getText().toString())) {
						textRepeatPass.setText("Contraseñas coinciden");
						btnNextLogin.setEnabled(true);
					} else {
						textRepeatPass.setText("Contraseñas no coinciden");
						btnNextLogin.setEnabled(false);
					}

				} else {
					textRepeatPass.setVisibility(View.INVISIBLE);
				}

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.btn_next_login:
				DSLogin dsLogin = new DSLogin(context);
				dsLogin.changeForgetPass(getIntent().getStringExtra("codigoUsuario"),inputRepeatPass.getText().toString());
				startActivity(new Intent(this, Entrar.class));
				break;
		}
	}

}
