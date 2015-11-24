package pe.com.glup.network;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import pe.com.glup.models.Usuario;
import pe.com.glup.managers.bus.BusHolder;
import pe.com.glup.models.interfaces.OnSuccessLogin;
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
        BusHolder.getInstance().register(this);
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
                        Log.e("loginsExo", usuario.getSexoUser());
                        onSuccessLogin.onSuccessLogin(true, usuario, "Bienvenido");
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
    public void sendCodeForgetPass(String correo){
        String URL = WSGlup.ORQUESTADOR_NUEVO;

        RequestParams params = new RequestParams();
        params.put("tag","enviarCorreoOlvidePass");
        params.put("correo_usuario",correo);

        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.post(context,URL,params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {
                    int success = response.getInt("success");
                    Log.e(null,"send code a email "+success);
                    Log.e(null,response.toString());
                    Gson gson = new Gson();
                    ResponseOlvidePass codeForgetPass = gson.fromJson(
                            response.toString(), ResponseOlvidePass.class);
                    BusHolder.getInstance().post(codeForgetPass);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });

    }

    public void changeForgetPass(String codigoUser,String newPass){
        String URL = WSGlup.ORQUESTADOR_NUEVO;

        RequestParams params = new RequestParams();
        params.put("tag","guardarNuevoPassUsuario");
        params.put("codigo_usuario",codigoUser);
        params.put("newpass_usuario",newPass);

        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.post(context,URL,params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    int success = response.getInt("success");
                    Log.e(null,"send change pass "+success);
                    Log.e(null,response.toString());
                    Gson gson = new Gson();
                    ResponseChangePass changeForgetPass = gson.fromJson(
                            response.toString(), ResponseChangePass.class);
                    BusHolder.getInstance().post(changeForgetPass);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });

    }

    public class ResponseOlvidePass{
        public String tag,indEnvio,cod_confirmacion,cod_usuario;
        public int success,error;
    }
    public class ResponseChangePass{
        public String tag;
        public int success,error;
    }

}
