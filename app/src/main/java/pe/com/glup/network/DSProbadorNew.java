package pe.com.glup.network;

import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import pe.com.glup.dialog.GlupDialog;
import pe.com.glup.dialog.GlupDialogNew;
import pe.com.glup.managers.bus.BusHolder;
import pe.com.glup.managers.session.Session_Manager;
import pe.com.glup.models.Catalogo;
import pe.com.glup.models.Prenda;
import pe.com.glup.models.TallaDisponible;
import pe.com.glup.models.Tienda;
import pe.com.glup.views.MessageV2;
import pe.com.glup.ws.WSGlup;


/**
 * Created by Glup on 10/12/15.
 */
public class DSProbadorNew {
    private Context context;
    private ArrayList<Prenda> prendas = new ArrayList<Prenda>();
    ArrayList<Tienda> tiendas = new ArrayList<Tienda>();
    ArrayList<TallaDisponible> tallas= new ArrayList<TallaDisponible>();
    GlupDialogNew gd;

    public DSProbadorNew(Context context) {
        this.context = context;
        BusHolder.getInstance().register(this);
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
                for (int i = 0; i < prendas.size(); i++) {
                    prendas.get(i).setFiltroPosicion(filtro_posicion);
                }
                ResponseCatalogo responseCatalogo = new ResponseCatalogo();
                responseCatalogo.message = catalogo.getTag();
                responseCatalogo.prendas = prendas;
                responseCatalogo.tipo = filtro_posicion;
                BusHolder.getInstance().post(responseCatalogo);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                ResponseCatalogo responseCatalogo = new ResponseCatalogo();
                responseCatalogo.message = responseString;
                responseCatalogo.prendas = null;
                responseCatalogo.tipo = filtro_posicion;
                BusHolder.getInstance().post(responseCatalogo);
            }
        });
    }
    public class ResponseCatalogo {
        public int success;
        public String tipo;
        public String message;
        public ArrayList<Prenda> prendas;
    }

    public void getGlobalPrendasProbador(final String filtro_posicion,final int pagina, String registros) {
        String URL = WSGlup.ORQUESTADOR_NUEVO_PROBADOR.
                replace(WSGlup.NUMERO_PAGINA, String.valueOf(pagina)).
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
                Log.e("enProbadorResponse", response.toString());
                Catalogo catalogo = gson.fromJson(response.toString(), Catalogo.class);
                prendas = catalogo.getPrendas();
                if (!prendas.isEmpty()){
                    for (int i = 0; i < prendas.size(); i++) {
                        prendas.get(i).setFiltroPosicion(filtro_posicion);
                    }
                }
                //separo para tener un mejor orden en FProbadorNew
                if (filtro_posicion.equals("A")){
                    ResponseProbadorTop responseProbadorTop = new ResponseProbadorTop();
                    responseProbadorTop.success=catalogo.getSuccess();
                    responseProbadorTop.message = catalogo.getTag();
                    responseProbadorTop.prendas = prendas;
                    responseProbadorTop.tipo = filtro_posicion;
                    responseProbadorTop.numPagTop=pagina;
                    BusHolder.getInstance().post(responseProbadorTop);
                }else{
                    ResponseProbadorBottom responseProbadorBottom= new ResponseProbadorBottom();
                    responseProbadorBottom.success=catalogo.getSuccess();
                    responseProbadorBottom.message=catalogo.getTag();
                    responseProbadorBottom.prendas=prendas;
                    responseProbadorBottom.tipo=filtro_posicion;
                    responseProbadorBottom.numPagBottom=pagina;
                    BusHolder.getInstance().post(responseProbadorBottom);
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.e("enProbadorError", responseString);
                if (filtro_posicion.equals("A")){
                    ResponseProbadorTop responseProbadorTop = new ResponseProbadorTop();
                    responseProbadorTop.success=0;
                    responseProbadorTop.message = responseString;
                    responseProbadorTop.prendas = null;
                    responseProbadorTop.tipo = filtro_posicion;
                    responseProbadorTop.numPagTop=pagina;
                    BusHolder.getInstance().post(responseProbadorTop);
                }else{
                    ResponseProbadorBottom responseProbadorBottom = new ResponseProbadorBottom();
                    responseProbadorBottom.success=0;
                    responseProbadorBottom.message = responseString;
                    responseProbadorBottom.prendas = null;
                    responseProbadorBottom.tipo = filtro_posicion;
                    responseProbadorBottom.numPagBottom=pagina;
                    BusHolder.getInstance().post(responseProbadorBottom);
                }
            }
        });
    }
    public class ResponseProbadorTop {
        public int success;
        public String tipo;
        public String message;
        public ArrayList<Prenda> prendas=null;
        public int numPagTop;
    }
    public class ResponseProbadorBottom {
        public int success;
        public String tipo;
        public String message;
        public ArrayList<Prenda> prendas=null;
        public int numPagBottom;
    }

    public void setIndProbador(String codPrenda,int action){
        final String msj1,msj2;
        if (action==1){
            msj1="Se esta enviando a Probador...";
            msj2="Se agrego al Probador";
        }else {
            msj1="Se esta eliminando del Probador...";
            msj2="Se elimino del Probador";
        }
        android.support.v4.app.FragmentManager fragmentManager= ((AppCompatActivity)context).getSupportFragmentManager();
        gd = new GlupDialogNew(msj1,context);
        gd.setCancelable(false);
        gd.show(fragmentManager,GlupDialog.class.getSimpleName());

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
                gd.dismiss();
                final MessageV2 msg= new MessageV2(msj2);
                msg.setCancelable(false);
                msg.show(((AppCompatActivity) context).getSupportFragmentManager(),
                        MessageV2.class.getSimpleName());

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        msg.dismiss();
                    }
                }, 2000);
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
                gd.dismiss();
                final Toast toast= Toast.makeText(context, responseString, Toast.LENGTH_SHORT);
                toast.show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        toast.cancel();
                    }
                }, 1000);
                String indProb=responseString;
                BusHolder.getInstance().post(indProb);
            }
        });
    }
    public class ResponseSetIndProbador{
        public String tag,indProb;
        public int success;
    }

    public void getPrendaDetalle(final String codigoPrenda){
        String URL=WSGlup.ORQUESTADOR_NUEVO;
        Log.e("Detalle",codigoPrenda);
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
                Log.e("Detalle:",response.toString());
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
    public class ResponseDetallePrenda {
        private String tag;
        private int success;
        public String indReserGen;
        private ArrayList<Prenda> prendas;

        public String getTag() {
            return tag;
        }

        public int getSuccess() {
            return success;
        }

        public ArrayList<Prenda> getPrendas() {
            return prendas;
        }
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
    public class ResponseTiendasDisponibles{
        private String tag;
        private int success;
        private ArrayList<Tienda> tiendas;
        public String getTag() {return tag;}
        public int getSuccess() {return success;}
        public ArrayList<Tienda> getTiendas() {return tiendas;}
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
                        int r=0;
                        for (TallaDisponible t:tallas){
                            Log.e("null","talla "+r+":"+t.getTalla());
                            r++;
                        }
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
    public class ResponseTallasDisponibles{
        private String tag;
        private int success;
        private ArrayList<TallaDisponible> tallas;

        public String getTag() {return tag;}
        public int getSuccess() {return success;}
        public ArrayList<TallaDisponible> getTallas() {return tallas;}
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
