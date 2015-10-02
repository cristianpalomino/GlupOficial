package pe.com.glup.datasource;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import pe.com.glup.beans.ReservaList;
import pe.com.glup.beans.TicketList;
import pe.com.glup.bus.BusHolder;
import pe.com.glup.glup.Glup;
import pe.com.glup.session.Session_Manager;
import pe.com.glup.ws.WSGlup;

/**
 * Created by Glup on 30/09/15.
 */
public class DSReserva {
    private Context context;

    public DSReserva(Context context) {
        this.context = context;
        BusHolder.getInstance().register(this);
    }

    public void listarReserva(){
        String URL = WSGlup.ORQUESTADOR_NUEVO;
        RequestParams params = new RequestParams();
        params.put("codigo_usuario",new Session_Manager(context).getCurrentUserCode());
        params.put("tag","listarReserva");

        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.post(context, URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                Gson gson = new Gson();
                try {
                    Log.e("dsReserva", response.getInt("success") + " Listando ...");
                    ResponseReserva responseReserva = gson.fromJson(response.toString(),
                            ResponseReserva.class);
                    if (response.getInt("success") == 1) {

                        for (ReservaList list : responseReserva.listaReserva) {
                            Log.e(null, list.getLocal() + " tiene " +
                                    list.getDatos().size() + " tiendas");
                        }

                    }
                    BusHolder.getInstance().post(responseReserva);
                } catch (JSONException e) {
                    Log.e("dsReserva", e.toString());
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }
        });
    }
    public void eliminarDeReserva(final String codPrenda){
        String URL=WSGlup.ORQUESTADOR_NUEVO;

        RequestParams params = new RequestParams();
        params.put("codigo_prenda",codPrenda);
        params.put("codigo_usuario",new Session_Manager(context).getCurrentUserCode());
        params.put("tag", "eliminarPrendaReserva");

        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.post(context,URL,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                Gson gson = new Gson();

                try {
                    Log.e("dsReserva",response.getInt("success") + " Se elimino prenda "+codPrenda);
                        if (response.getInt("success")==1){
                            ResponseReload responseReload= new ResponseReload();
                            responseReload.fragment=((Glup)context).getSupportFragmentManager().findFragmentByTag("FReservaInfo");
                            Log.e("fragment", responseReload.fragment.toString());
                            BusHolder.getInstance().post(responseReload);
                        }
                } catch (JSONException e) {
                    Log.e("dsReserva", e.toString());
                }
            }
        });
    }

    public void sendEmail(){
        String URL=WSGlup.ORQUESTADOR_NUEVO;

        RequestParams params = new RequestParams();
        params.put("codigo_usuario",new Session_Manager(context).getCurrentUserCode());
        params.put("tag","enviarCorreoVerifica");

        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.post(context,URL,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                Gson gson = new Gson();
                try {
                    Log.e("dsReserva", response.getInt("success") + " Se envio a correo "+response.getString("indEnvio"));
                    ResponseSendEmail responseSendEmail = gson.fromJson(response.toString(),
                            ResponseSendEmail.class);
                    Log.e(null,"codigo generado "+responseSendEmail.cod_confirmacion);
                    BusHolder.getInstance().post(responseSendEmail);
                } catch (JSONException e) {
                    Log.e("dsReserva", e.toString());
                }

            }

        });
    }
    public void confirmReseva() {
        String URL=WSGlup.ORQUESTADOR_NUEVO;

        RequestParams params = new RequestParams();
        params.put("codigo_usuario", new Session_Manager(context).getCurrentUserCode());
        params.put("tag","confirmarReserva");

        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.post(context,URL,params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                Gson gson = new Gson();
                try {
                    Log.e("dsReserva", response.getInt("success") + " Se genero codigo de confirmacion "+response.getString("cod_confirmacion"));
                    ResponseConfirmReserva responseConfirmReserva = gson.fromJson(response.toString(),
                            ResponseConfirmReserva.class);
                    Log.e(null,"codigo generado "+responseConfirmReserva.cod_confirmacion);
                    BusHolder.getInstance().post(responseConfirmReserva);
                } catch (JSONException e) {
                    Log.e("dsReserva", e.toString());
                }

            }
        });
    }
    public void listarTicket(){
        String URL=WSGlup.ORQUESTADOR_NUEVO;

        RequestParams params = new RequestParams();
        params.put("codigo_usuario",new Session_Manager(context).getCurrentUserCode());
        params.put("tag","listarTicketReserva");

        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.post(context,URL,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                Gson gson = new Gson();
                try {
                    Log.e("dsReserva ", response.getInt("success") + " listando tickets ... ");
                    ResponseTicket responseTicket = gson.fromJson(response.toString(),
                            ResponseTicket.class);
                    if (response.getInt("success")==1){
                        Log.e(null,"cant. de tickets "+responseTicket.listaTicket.size());
                    }
                    BusHolder.getInstance().post(responseTicket);
                } catch (JSONException e) {
                    Log.e("dsReserva", e.toString());
                }

            }
        });
    }

    

    public class ResponseReserva
    {   public String tag;
        public int success;
        public int error;
        public ArrayList<ReservaList> listaReserva;
    }
    public class ResponseReload{
        public Fragment fragment;
    }
    public class ResponseSendEmail{
        public int success;
        public String indEnvio;
        public String cod_confirmacion;
    }
    public class ResponseConfirmReserva{
        public int success;
        public String cod_confirmacion;
    }
    public class ResponseTicket{
        public  int success;
        public int error;
        public ArrayList<TicketList> listaTicket;

    }

}