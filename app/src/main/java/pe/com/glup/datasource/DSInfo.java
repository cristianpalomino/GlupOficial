package pe.com.glup.datasource;

import android.content.Context;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;

import pe.com.glup.beans.Catalogo;
import pe.com.glup.beans.Prenda;
import pe.com.glup.beans.Usuario;
import pe.com.glup.interfaces.OnSuccesUpdate;
import pe.com.glup.interfaces.OnSuccessCatalogo;
import pe.com.glup.interfaces.OnSuccessInfo;
import pe.com.glup.interfaces.OnSuccessPrenda;
import pe.com.glup.session.Session_Manager;
import pe.com.glup.ws.WSGlup;

/**
 * Created by Glup on 24/06/15.
 */
public class DSInfo {

    private Context context;
    private OnSuccessInfo onSuccessInfo;
    private OnSuccessPrenda onSuccessPrenda;
    private ArrayList<Prenda> prendas = new ArrayList<Prenda>();

    public DSInfo(Context context) {
        this.context = context;
    }

    public void setOnSuccessInfo(OnSuccessInfo onSuccessInfo) {
        this.onSuccessInfo = onSuccessInfo;
    }

    public void setOnSuccesPrenda(OnSuccessPrenda onSuccesPrenda) {
        this.onSuccessPrenda= onSuccesPrenda;
    }

    public  void getInfoPrenda(String codigo){
        String URL = WSGlup.ORQUESTADOR;

        RequestParams params = new RequestParams();
        params.put("tag", "detallePrenda");
        params.put("codigo_usuario",new Session_Manager(context).getCurrentUserCode());
        params.put("codigo_prenda", codigo);

        AsyncHttpClient httpClient = new AsyncHttpClient();

        httpClient.post(context, URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Gson gson = new Gson();
                Catalogo catalogo = gson.fromJson(response.toString(), Catalogo.class);
                prendas=catalogo.getPrendas();
                // ** En caso de error de este metodo **
                try{
                    onSuccessInfo.onSuccess(prendas.get(0));
                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                onSuccessInfo.onFailed(responseString);
            }
        });
            }


    public  void getReservaPrenda(String codigo_prenda,String codigo_tienda){
        String URL = WSGlup.ORQUESTADOR;

        RequestParams params = new RequestParams();
        params.put("tag", "enviarReserva");
        params.put("codigo_usuario",new Session_Manager(context).getCurrentUserCode());
        params.put("codigo_prenda", codigo_prenda);
        params.put("codigo_prenda", codigo_tienda);


        AsyncHttpClient httpClient = new AsyncHttpClient();

        httpClient.post(context, URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    onSuccessPrenda.onSuccessPrenda(true, response.getInt("indProb"));
                } catch (Exception e) {
                    onSuccessPrenda.onSuccessPrenda(false, -1);
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                onSuccessPrenda.onFailed(responseString);
            }
        });
    }




    }



