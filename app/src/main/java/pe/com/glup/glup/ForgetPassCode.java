package pe.com.glup.glup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import pe.com.glup.R;
import pe.com.glup.bus.BusHolder;
import pe.com.glup.datasource.DSLogin;

/**
 * Created by Glup on 12/10/15.
 */
public class ForgetPassCode extends AppCompatActivity implements View.OnClickListener {
	private Button sendCode,next;
	private TextView textSendCode,textCodeVerification;
	private EditText inputCorreo,inputCode;
	private String codigo,codigoUser;
	@Override
	public void onCreate(Bundle savedInstance){
		super.onCreate(savedInstance);
		setContentView(R.layout.iniciar_forget_pass);
		BusHolder.getInstance().register(this);
		inputCorreo = (EditText) findViewById(R.id.input_correo);
		sendCode = (Button) findViewById(R.id.send_code);
		textSendCode = (TextView) findViewById(R.id.text_send_code);
		inputCode = (EditText) findViewById(R.id.input_code);
		textCodeVerification = (TextView) findViewById(R.id.text_code_verification);
		next = (Button) findViewById(R.id.btn_next);
		next.setEnabled(false);
		sendCode.setOnClickListener(this);
		next.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.send_code:
				Log.e(null,"clic send code");
				if (!inputCorreo.getText().toString().equals("")){
					sendCode.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_selector_pressed));
					sendCode.setTextColor(getResources().getColor(R.color.blanco));
					DSLogin dsLogin = new DSLogin(this);
					dsLogin.sendCodeForgetPass(inputCorreo.getText().toString());
				}
				break;
			case R.id.btn_next:
				Log.e(null,"clic next");
				if (!inputCode.getText().toString().equals("")){
					textCodeVerification.setVisibility(View.VISIBLE);
					if (inputCode.getText().toString().equals(codigo)){
						textCodeVerification.setText("Codigos coinciden");
						Intent intent = new Intent(this,ForgetPassChange.class);
						Bundle args = new Bundle();
						args.putString("codigoUsuario",codigoUser);
						intent.putExtras(args);
						startActivity(intent);
					}else {
						textCodeVerification.setText("Codigos no coinciden");
					}
				}
				break;
		}
	}
	@Subscribe
	public void getCodeChangePass(DSLogin.ResponseOlvidePass responseOlvidePass){
		if (responseOlvidePass.error==1){
			textSendCode.setText("Correo no registrado");
			sendCode.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_selector));
			sendCode.setTextColor(getResources().getColor(R.color.celeste_glup));
		}else{
			textSendCode.setText("Se envio el codigo a su correo");
			textSendCode.setVisibility(View.VISIBLE);
			next.setEnabled(true);
			codigo=responseOlvidePass.cod_confirmacion;
			codigoUser=responseOlvidePass.cod_usuario;
		}
	}
}
