package pe.com.glup.datasource;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;

import pe.com.glup.beans.Catalogo;
import pe.com.glup.beans.Prenda;
import pe.com.glup.interfaces.OnSuccessUpdate;
import pe.com.glup.interfaces.OnSuccessCatalogo;
import pe.com.glup.session.Session_Manager;
import pe.com.glup.ws.WSGlup;

/**
 * Created by Glup on 24/06/15.
 */
public class DSCatalogo {

    private Context context;
    private OnSuccessCatalogo onSuccessCatalogo;
    private OnSuccessUpdate onSuccessUpdate;
    private ArrayList<Prenda> prendas = new ArrayList<Prenda>();

    public void setOnSuccessCatalogo(OnSuccessCatalogo onSuccessCatalogo) {
        this.onSuccessCatalogo = onSuccessCatalogo;
    }

    public void setOnSuccessUpdate(OnSuccessUpdate onSuccessUpdate) {
        this.onSuccessUpdate = onSuccessUpdate;
    }

    public DSCatalogo(Context context) {
        this.context = context;
    }

    public void getGlobalPrendas(String buscar, String pagina, String registros) {
        String URL = WSGlup.ORQUESTADOR_NUEVO_CATALOGO.
                replace(WSGlup.NUMERO_PAGINA, pagina).
                replace(WSGlup.NUMERO_REGISTROS, registros).
                replace(WSGlup.BUSCAR, buscar);
        Log.e("Pagina",pagina);
        RequestParams params = new RequestParams();
        params.put("tag", "prendaCatalogo");
        params.put("codigo_usuario", new Session_Manager(context).getCurrentUserCode());

        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.post(context, URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Gson gson = new Gson();

                Catalogo catalogo = gson.fromJson(response.toString(), Catalogo.class);
                prendas = catalogo.getPrendas();
                //for (Prenda p:prendas){
                    //Log.e(null,p.toString());
                //}
                onSuccessCatalogo.onSuccess(catalogo.getTag(), prendas);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                onSuccessCatalogo.onFailed(responseString);
            }
        });
    }

    public void updateProbador(String codigo_prenda) {
        String URL = WSGlup.ORQUESTADOR_NUEVO;

        RequestParams params = new RequestParams();
        params.put("tag", "enviarProbador");
        params.put("codigo_usuario", new Session_Manager(context).getCurrentUserCode());
        params.put("codigo_prenda", codigo_prenda);

        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.post(context, URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    onSuccessUpdate.onSuccesUpdate(true, response.getInt("indProb"));
                } catch (Exception e) {
                    onSuccessUpdate.onSuccesUpdate(false, -1);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                onSuccessUpdate.onSuccesUpdate(false, -1);
            }
        });
    }


}
