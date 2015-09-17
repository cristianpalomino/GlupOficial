package pe.com.glup.datasource;

import android.content.Context;

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
import pe.com.glup.session.Session_Manager;
import pe.com.glup.ws.WSGlup;

/**
 * Created by Glup on 24/06/15.
 */
public class DSProbador {

    private Context context;
    private ArrayList<Prenda> prendas = new ArrayList<Prenda>();

    public DSProbador(Context context) {
        this.context = context;
        BusHolder.getInstance().register(this);
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

                ResponseProbador responseProbador = new ResponseProbador();
                responseProbador.message = catalogo.getTag();
                responseProbador.prendas = prendas;
                responseProbador.tipo = filtro_posicion;
                BusHolder.getInstance().post(responseProbador);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);

                ResponseProbador responseProbador = new ResponseProbador();
                responseProbador.message = responseString;
                responseProbador.prendas = null;
                responseProbador.tipo = filtro_posicion;
                BusHolder.getInstance().post(responseProbador);
            }
        });
    }
    public void enviarRetirarPrenda(String codPrenda){
        /*
        tag: enviarProbador
        codigo_usuario: aquí se deberá enviar el código de usuario.
        codigo_prenda: aquí se deberá enviar el código de la prenda.
        Respuesta:
        indProb: ‘1’ se envió al probador, ‘0’ se retiró del probador.
         */
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
                }catch (JSONException e){

                }
            }
            @Override
            public void onFailure(int statusCode,Header[] headers,String responseString,Throwable throwable){
                super.onFailure(statusCode, headers, responseString, throwable);

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
