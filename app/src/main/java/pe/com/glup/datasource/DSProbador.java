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
import pe.com.glup.beans.TallaDisponible;
import pe.com.glup.beans.Tienda;
import pe.com.glup.bus.BusHolder;
import pe.com.glup.interfaces.OnSuccessPrendas;
import pe.com.glup.interfaces.OnSuccessProbador;
import pe.com.glup.session.Session_Manager;
import pe.com.glup.ws.WSGlup;

/**
 * Created by Glup on 24/06/15.
 */
public class DSProbador {

    private Context context;
    private ArrayList<Prenda> prendas = new ArrayList<Prenda>();
    ArrayList<Tienda> tiendas = new ArrayList<Tienda>();
    ArrayList<TallaDisponible> tallas= new ArrayList<TallaDisponible>();
    private OnSuccessPrendas onSuccessPrendas;
    private OnSuccessProbador onSuccessProbador;


    public DSProbador(Context context) {
        this.context = context;
        BusHolder.getInstance().register(this);
    }

    public void setOnSuccessPrendas(OnSuccessPrendas onSuccessPrendas) {
        this.onSuccessPrendas = onSuccessPrendas;
    }

    public void setOnSuccessProbador(OnSuccessProbador onSuccessProbador) {
        this.onSuccessProbador = onSuccessProbador;
    }

    public void getGlobalPrendasCatalogo(final String filtro_posicion, String pagina, String registros) {
            String URL = WSGlup.ORQUESTADOR_NUEVO_PROBADOR.
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
                ResponseCatalogo responseCatalogo = new ResponseCatalogo();
                responseCatalogo.message = catalogo.getTag();
                responseCatalogo.prendas = prendas;
                responseCatalogo.tipo = filtro_posicion;
                onSuccessPrendas.succesPrendas(responseCatalogo);//bug retroceso probador-catalog-probador
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);

                ResponseCatalogo responseCatalogo = new ResponseCatalogo();
                responseCatalogo.message = responseString;
                responseCatalogo.prendas = null;
                responseCatalogo.tipo = filtro_posicion;
                onSuccessPrendas.succesPrendas(responseCatalogo);
            }
        });
    }
    public void getGlobalPrendasProbador(final String filtro_posicion, String pagina, String registros) {
        String URL = WSGlup.ORQUESTADOR_NUEVO_PROBADOR.
                replace(WSGlup.NUMERO_PAGINA, pagina).
                replace(WSGlup.NUMERO_REGISTROS, registros).
                replace(WSGlup.FILTRO_POSICION, filtro_posicion);

        RequestParams params = new RequestParams();
        params.put("tag", "prendaProbadorPosicion");
        params.put("codigo_usuario", new Session_Manager(context).getCurrentUserCode());

        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.post(context, URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Gson gson = new Gson();
                Log.e("enProbadorResp",response.toString());
                Catalogo catalogo = gson.fromJson(response.toString(), Catalogo.class);
                prendas = catalogo.getPrendas();
                for (int i = 0; i < prendas.size(); i++) {
                    prendas.get(i).setFiltroPosicion(filtro_posicion);
                }
                ResponseProbador responseProbador = new ResponseProbador();
                responseProbador.success=catalogo.getSuccess();
                responseProbador.message = catalogo.getTag();
                responseProbador.prendas = prendas;
                responseProbador.tipo = filtro_posicion;
                onSuccessProbador.succesPrendas(responseProbador);//bug retroceso probador-catalog-probador
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.e("enProbadorError",responseString);
                ResponseProbador responseProbador = new ResponseProbador();
                responseProbador.message = responseString;
                responseProbador.prendas = null;
                responseProbador.tipo = filtro_posicion;
                onSuccessProbador.succesPrendas(responseProbador);
            }
        });
    }

    public void setIndProbador(String codPrenda){

        String URL=WSGlup.ORQUESTADOR_NUEVO;

        RequestParams params = new RequestParams();
        params.put("tag","enviarProbador");
        params.put("codigo_usuario", new Session_Manager(context).getCurrentUserCode());
        params.put("codigo_prenda",codPrenda);

        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.post(context,URL,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response){
                super.onSuccess(statusCode, headers, response);
                try {
                    String indProb= response.getString("indProb");
                    Log.e("dsProbador", indProb);
                    Gson gson=new Gson();
                    ResponseSetIndProbador responseSetIndProbador = gson.fromJson(response.toString(), ResponseSetIndProbador.class);
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

    public void getPrendaDetalle(final String codigoPrenda){
        String URL=WSGlup.ORQUESTADOR_NUEVO;

        RequestParams params = new RequestParams();
        params.put("tag","detallePrenda");
        params.put("codigo_prenda",codigoPrenda);
        params.put("codigo_usuario", new Session_Manager(context).getCurrentUserCode());

        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.post(context,URL,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response){
                super.onSuccess(statusCode, headers, response);
                Gson gson=new Gson();


                ResponseDetallePrenda responseDetallePrenda = gson.fromJson(response.toString(),ResponseDetallePrenda.class);
                prendas = responseDetallePrenda.getPrendas();
                Log.e("success",responseDetallePrenda.getSuccess()+"");
                Log.e("descripcion", prendas.get(0).getDescripcion());


                BusHolder.getInstance().post(responseDetallePrenda);

            }
            @Override
            public  void onFailure(int statusCode, Header[] headers,String responseString,Throwable throwable){
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }
    public void getTiendasDisponibles(String codigoPrenda){
        String URL=WSGlup.ORQUESTADOR_NUEVO;

        RequestParams params = new RequestParams();
        params.put("tag","listarTiendaPrenda");
        params.put("codigo_prenda",codigoPrenda);
        params.put("codigo_usuario", new Session_Manager(context).getCurrentUserCode());

        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.post(context,URL,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response){
                super.onSuccess(statusCode, headers, response);
                Gson gson=new Gson();


                ResponseTiendasDisponibles responseTiendasDisponibles = gson.fromJson(response.toString(),ResponseTiendasDisponibles.class);
                tiendas = responseTiendasDisponibles.getTiendas();
                Log.e("success",responseTiendasDisponibles.getSuccess() + "");
                Log.e("descripcion", tiendas.get(0).getLocal());



                BusHolder.getInstance().post(responseTiendasDisponibles);

            }
            @Override
            public  void onFailure(int statusCode, Header[] headers,String responseString,Throwable throwable){
                super.onFailure(statusCode,headers,responseString,throwable);
            }
        });
    }

    public void getTallasDisponibles(String codPrenda, String nomLocal){
        String URL=WSGlup.ORQUESTADOR_NUEVO;

        RequestParams params = new RequestParams();
        params.put("codigo_prenda",codPrenda);
        params.put("codigo_usuario",new Session_Manager(context).getCurrentUserCode());
        params.put("nom_local",nomLocal);
        params.put("tag","listarTallaPrenda");

        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.post(context,URL,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode,Header[] headers, JSONObject response){
                super.onSuccess(statusCode, headers, response);
                Gson gson=new Gson();

                try {
                    ResponseTallasDisponibles responseTallasDisponibles=
                            gson.fromJson(response.toString(),ResponseTallasDisponibles.class);
                    if (response.getInt("success")==1){
                        tallas=responseTallasDisponibles.getTallas();
                        Log.e("success",responseTallasDisponibles.getSuccess()+"");
                        Log.e("talla0",tallas.get(0).getTalla());
                    }
                    BusHolder.getInstance().post(responseTallasDisponibles);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public  void onFailure(int statusCode, Header[] headers,String responseString,Throwable throwable){
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }
    public void reservarPrenda(String codPrenda, String idTalla){
        String URL = WSGlup.ORQUESTADOR_NUEVO;

        RequestParams params=new RequestParams();
        params.put("codigo_prenda",codPrenda);
        params.put("codigo_usuario",new Session_Manager(context).getCurrentUserCode());
        params.put("id_talla",idTalla);
        params.put("tag","reservarPrenda");

        AsyncHttpClient httpClient= new AsyncHttpClient();
        httpClient.post(context,URL,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode,Header[] headers,JSONObject response){
                super.onSuccess(statusCode,headers,response);
                Gson gson=new Gson();
                try {
                    Log.e("successR",response.getInt("success")+"");
                    ResponseReservarPrenda responseReservarPrenda=gson.fromJson(
                            response.toString(),ResponseReservarPrenda.class);
                    Log.e("seReservo",responseReservarPrenda.getSuccess()+"");
                    BusHolder.getInstance().post(responseReservarPrenda);
                }catch (JSONException e){
                    e.printStackTrace();
                }


            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.e("ERROR",responseString);
            }
        });

    }
    public class ResponseSetIndProbador{
        public String tag,indProb;
        public int success;
    }

    public class ResponseCatalogo
    {   public int success;
        public String tipo;
        public String message;
        public ArrayList<Prenda> prendas;
    }

    public class ResponseProbador
    {   public int success;
        public String tipo;
        public String message;
        public ArrayList<Prenda> prendas;
    }

    public class ResponseDetallePrenda{
        private String tag;
        private int success;
        private ArrayList<Prenda> prendas;

        public String getTag() {return tag;}
        public int getSuccess() {return success;}
        public ArrayList<Prenda> getPrendas() {return prendas;}
    }
    public class ResponseTiendasDisponibles{
        private String tag;
        private int success;
        private ArrayList<Tienda> tiendas;

        public String getTag() {return tag;}
        public int getSuccess() {return success;}
        public ArrayList<Tienda> getTiendas() {return tiendas;}
    }
    public class ResponseTallasDisponibles{
        private String tag;
        private int success;
        private ArrayList<TallaDisponible> tallas;

        public String getTag() {return tag;}
        public int getSuccess() {return success;}
        public ArrayList<TallaDisponible> getTallas() {return tallas;}
    }
    public class  ResponseReservarPrenda{
        private String tag;
        private int success;
        private int error;
        private String indReserva;

        public String getTag() {return tag;}
        public int getSuccess() {return success;}
        public int getError() {return error;}
        public String getIndReserva() {return indReserva;}
    }
}
