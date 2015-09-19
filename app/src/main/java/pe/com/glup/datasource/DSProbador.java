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

import pe.com.glup.beans.Catalogo;
import pe.com.glup.beans.Prenda;
import pe.com.glup.bus.BusHolder;
import pe.com.glup.interfaces.OnSuccessPrendas;
import pe.com.glup.session.Session_Manager;
import pe.com.glup.ws.WSGlup;

/**
 * Created by Glup on 24/06/15.
 */
public class DSProbador {

    private Context context;
    private ArrayList<Prenda> prendas = new ArrayList<Prenda>();
    private OnSuccessPrendas onSuccessPrendas;

    public DSProbador(Context context) {
        this.context = context;
        BusHolder.getInstance().register(this);
    }

    public void setOnSuccessPrendas(OnSuccessPrendas onSuccessPrendas) {
        this.onSuccessPrendas = onSuccessPrendas;
    }

    public void getGlobalPrendas(final String filtro_posicion, String pagina, String registros) {
        String URL = WSGlup.ORQUESTADOR_PROBADOR.
                replace(WSGlup.NUMERO_PAGINA, pagina).
                replace(WSGlup.NUMERO_REGISTROS, registros).
                replace(WSGlup.FILTRO_POSICION, filtro_posicion);

        RequestParams params = new RequestParams();
        params.put("tag", "prendaCatalogoPosicion");
        params.put("codigo_usuario", new Session_Manager(context).getCurrentUserCode());

        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.post(context, URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Gson gson = new Gson();

                Catalogo catalogo = gson.fromJson(response.toString(), Catalogo.class);
                prendas = catalogo.getPrendas();
                for (int i=0;i<prendas.size();i++){
                    prendas.get(i).setFiltroPosicion(filtro_posicion);
                }
                ResponseProbador responseProbador = new ResponseProbador();
                responseProbador.message = catalogo.getTag();
                responseProbador.prendas = prendas;
                responseProbador.tipo = filtro_posicion;
                onSuccessPrendas.succesPrendas(responseProbador);//bug retroceso probador-catalog-probador
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);

                ResponseProbador responseProbador = new ResponseProbador();
                responseProbador.message = responseString;
                responseProbador.prendas = null;
                responseProbador.tipo = filtro_posicion;
                onSuccessPrendas.succesPrendas(responseProbador);
            }
        });
    }
    public void setIndProbador(String codPrenda){

        String URL=WSGlup.ORQUESTADOR;

        RequestParams params = new RequestParams();
        params.put("tag","enviarProbador");
        params.put("codigo_usuario", new Session_Manager(context).getCurrentUserCode());
        params.put("codigo_prenda",codPrenda);

        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.post(context,URL,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response){
                super.onSuccess(statusCode,headers,response);
                try {
                    String indProb= response.getString("indProb");
                    Log.e("dsProbador", indProb);
                    BusHolder.getInstance().post(indProb);
                }catch (JSONException e){
                    Log.e("dsProbador",e.toString());
                }
            }
            @Override
            public void onFailure(int statusCode,Header[] headers,String responseString,Throwable throwable){
                super.onFailure(statusCode, headers, responseString, throwable);
                String indProb=responseString;
                BusHolder.getInstance().post(indProb);
            }

        });

    }


    public class ResponseProbador
    {
        public String tipo;
        public String message;
        public ArrayList<Prenda> prendas;
    }
}
