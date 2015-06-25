package pe.com.glup.datasource;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import pe.com.glup.beans.Catalogo;
import pe.com.glup.beans.Prenda;
import pe.com.glup.interfaces.OnSuccessCatalogo;
import pe.com.glup.ws.WSGlup;

/**
 * Created by Glup on 24/06/15.
 */
public class DSCatalogo {

    private Context context;
    private OnSuccessCatalogo onSuccessCatalogo;

    public void setOnSuccessCatalogo(OnSuccessCatalogo onSuccessCatalogo) {
        this.onSuccessCatalogo = onSuccessCatalogo;
    }

    public DSCatalogo(Context context) {
        this.context = context;
    }

    public void sendRequest(String buscar, String pagina, String registros) {
        String URL = WSGlup.ORQUESTADOR_CATALOGO.
                replace(WSGlup.NUMERO_PAGINA, pagina).
                replace(WSGlup.NUMERO_REGISTROS, registros).
                replace(WSGlup.BUSCAR, buscar);

        RequestParams params = new RequestParams();
        params.put("tag", "prendaCatalogo");
        params.put("codigo_usuario", "000000000010");

        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.post(context, URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Gson gson = new Gson();
                Catalogo catalogo = gson.fromJson(response.toString(), Catalogo.class);
                Log.e("CATALOGO",catalogo.toString());


                onSuccessCatalogo.onSuccess(catalogo.getTag(), catalogo.getPrendas());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                onSuccessCatalogo.onFailed(responseString);
            }
        });
    }

}
