package pe.com.glup.datasource;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import pe.com.glup.beans.DatoUser;
import pe.com.glup.beans.DetalleUser;
import pe.com.glup.beans.PerfilUsuario;
import pe.com.glup.beans.Usuario;
import pe.com.glup.interfaces.OnSuccessDetalleUsuario;
import pe.com.glup.interfaces.OnSuccessRegistro;
import pe.com.glup.session.Session_Manager;
import pe.com.glup.ws.WSGlup;

/**
 * Created by Glup on 24/06/15.
 */
public class DSRegistro {

    private Context context;
    private OnSuccessRegistro onSuccessRegistro;

    public void setOnSuccessRegistro(OnSuccessRegistro onSuccessRegistro) {
        this.onSuccessRegistro = onSuccessRegistro;
    }


    public DSRegistro(Context context) {
        this.context = context;
    }

    public void registrarUsuario(String nombre,String apellido,String sexo,String correo, String password) {
        String URL = WSGlup.ORQUESTADOR_NUEVO;
        Log.e("sexoUsuario",sexo);

        RequestParams params = new RequestParams();
        params.put("tag", "registroUser");
        params.put("nombre_usuario", nombre);
        params.put("ape_usuario",apellido);
        params.put("correo_usuario", correo);
        params.put("sexo_usuario",sexo);
        params.put("pass_usuario", password);

        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.post(context, URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {
                    int success = response.getInt("success");
                    Log.e(null,"registro success:"+success);
                    if (success == 1) {
                        Gson gson = new Gson();
                        Usuario usuario = gson.fromJson(response.toString(), Usuario.class);
                        Log.e("sexoRegistrado", usuario.getSexoUser());
                        onSuccessRegistro.onSuccessRegistro(true, usuario, "Se registro exitosamente.");
                    } else {
                        onSuccessRegistro.onSuccessRegistro(false, null, response.getString("error_msg"));
                    }
                } catch (JSONException e) {
                    onSuccessRegistro.onSuccessRegistro(false, null, "Ocurrio un error en el Servidor.");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                onSuccessRegistro.onSuccessRegistro(false, null, "Ocurrio un error en el Servidor.");
            }
        });
    }


}
