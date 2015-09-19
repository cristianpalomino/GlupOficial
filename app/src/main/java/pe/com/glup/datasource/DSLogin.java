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

import pe.com.glup.beans.Usuario;
import pe.com.glup.interfaces.OnSuccessLogin;
import pe.com.glup.interfaces.OnSuccessRegistro;
import pe.com.glup.ws.WSGlup;

/**
 * Created by Glup on 24/06/15.
 */
public class DSLogin {

    private Context context;
    private OnSuccessLogin onSuccessLogin;

    public void setOnSuccessLogin(OnSuccessLogin onSuccessLogin) {
        this.onSuccessLogin = onSuccessLogin;
    }

    public DSLogin(Context context) {
        this.context = context;
    }

    public void loginUsuario(String usuario, String password) {
        String URL = WSGlup.ORQUESTADOR_NUEVO;

        RequestParams params = new RequestParams();
        params.put("tag", "login");
        params.put("user", usuario);
        params.put("pass", password);

        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.post(context, URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {
                    int success = response.getInt("success");
                    if (success == 1) {
                        Gson gson = new Gson();
                        Usuario usuario = gson.fromJson(response.toString(), Usuario.class);
                        Log.e("loginsExo",usuario.getSexoUser());
                        onSuccessLogin.onSuccessLogin(true, usuario,"Bienvenido");
                    } else {
                        onSuccessLogin.onSuccessLogin(false, null, response.getString("error_msg"));
                    }
                } catch (JSONException e) {
                    onSuccessLogin.onSuccessLogin(false, null, "Ocurrio un error en el Servidor.");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                onSuccessLogin.onSuccessLogin(false, null, "Ocurrio un error en el Servidor.");
            }
        });
    }

}
