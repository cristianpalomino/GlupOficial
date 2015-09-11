package pe.com.glup.datasource;

import android.content.Context;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;

import pe.com.glup.beans.DatoUser;
import pe.com.glup.beans.DetalleUser;
import pe.com.glup.beans.PerfilUsuario;
import pe.com.glup.interfaces.OnSuccessDetalleUsuario;
import pe.com.glup.session.Session_Manager;
import pe.com.glup.ws.WSGlup;

/**
 * Created by Glup on 10/09/15.
 */
public class DSUsuario {
    private Context context;
    private OnSuccessDetalleUsuario onSuccessDetalleUsuario;
    private ArrayList<DatoUser> datoUser = new ArrayList<DatoUser>();
    private ArrayList<DetalleUser> detalleUser = new ArrayList<DetalleUser>();
    public void setOnSuccessDetalleUsuario(OnSuccessDetalleUsuario onSuccessDetalleUsuario) {
        this.onSuccessDetalleUsuario = onSuccessDetalleUsuario;
    }
    public DSUsuario(Context context) {
        this.context = context;
    }

    public void loadUsuario(){
        String URL = WSGlup.ORQUESTADOR_NUEVO;

        RequestParams params = new RequestParams();
        params.put("tag","detalleUsuario");
        params.put("codigo_usuario",new Session_Manager(context).getCurrentUserCode());

        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.post(context, URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Gson gson = new Gson();
                PerfilUsuario usuario = gson.fromJson(response.toString(), PerfilUsuario.class);
                detalleUser=usuario.getDetalleuser();
                datoUser= usuario.getDatouser();
                onSuccessDetalleUsuario.onSuccess(usuario.getTag(), detalleUser,datoUser);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                onSuccessDetalleUsuario.onFailed(responseString);

            }
        });



    }

}
