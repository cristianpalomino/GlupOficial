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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import pe.com.glup.beans.DatoUser;
import pe.com.glup.beans.DetalleUser;
import pe.com.glup.beans.PerfilUsuario;
import pe.com.glup.bus.BusHolder;
import pe.com.glup.interfaces.OnSuccessDetalleUsuario;
import pe.com.glup.interfaces.OnSuccessUpdatePass;
import pe.com.glup.interfaces.OnSuccessUpdateUser;
import pe.com.glup.session.Session_Manager;
import pe.com.glup.ws.WSGlup;

/**
 * Created by Glup on 10/09/15.
 */
public class DSUsuario {
    private Context context;
    private OnSuccessDetalleUsuario onSuccessDetalleUsuario;
    private OnSuccessUpdateUser onSuccessUpdateUser;
    private OnSuccessUpdatePass onSuccessUpdatePass;


    private ArrayList<DatoUser> datoUser = new ArrayList<DatoUser>();
    private ArrayList<DetalleUser> detalleUser = new ArrayList<DetalleUser>();



    public void setOnSuccessUpdateUser(OnSuccessUpdateUser onSuccessUpdateUser) {
        this.onSuccessUpdateUser = onSuccessUpdateUser;
    }

    public void setOnSuccessUpdatePass(OnSuccessUpdatePass onSuccessUpdatePass) {
        this.onSuccessUpdatePass = onSuccessUpdatePass;
    }

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
        Log.e("codeUser", new Session_Manager(context).getCurrentUserCode());
        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.post(context, URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Gson gson = new Gson();
                Log.e("null",response.toString());
                PerfilUsuario perfilUsuario = gson.fromJson(response.toString(), PerfilUsuario.class);
                Log.e("Success", String.valueOf(perfilUsuario.getSuccess()));
                String cumple="";
                if (perfilUsuario.getDetalleuser().get(0).getFecNac().equals(null)){
                    cumple="";
                }else {
                    cumple= inverseResetFormatFecha(perfilUsuario.getDetalleuser().get(0).getFecNac().toString());
                }
                perfilUsuario.getDetalleuser().get(0).setFecNac(cumple);
                detalleUser=perfilUsuario.getDetalleuser();
                Log.e("apeUser",perfilUsuario.getDetalleuser().get(0).getCorreoUser().toString());
                datoUser= perfilUsuario.getDatouser();
                Log.e("numPren", perfilUsuario.getDatouser().get(0).getNumPrend().toString());
                onSuccessDetalleUsuario.onSuccess(String.valueOf(perfilUsuario.getSuccess()), detalleUser,datoUser);
                /*
                *
                try{
                    onSuccessDetalleUsuario = (OnSuccessDetalleUsuario) context;
                    onSuccessDetalleUsuario.onSuccess(String.valueOf(perfilUsuario.getSuccess()), detalleUser,datoUser);
                }catch (ClassCastException e){
                    e.printStackTrace();
                }
                */

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                onSuccessDetalleUsuario.onFailed(responseString);

            }
        });
    }
    public void updateUsuario(String indVerPass,String passUser,String nombre, String apellido, String fecNac, String correo, String telef){
        String URL = WSGlup.ORQUESTADOR_NUEVO;

        Log.e("NombreActual",nombre);
        Log.e("updateUsuarioPass",passUser);
        Log.e("indicadorverpass",indVerPass);

        RequestParams params = new RequestParams();
        params.put("tag","modificarDatoUser");
        params.put("codigo_usuario",new Session_Manager(context).getCurrentUserCode());
        params.put("pass_usuario",passUser);
        params.put("ind_verpass",indVerPass);
        params.put("nom_usuario",nombre);
        params.put("ape_usuario",apellido);
        params.put("fecnac_usuario",resetFormatFecha(fecNac));
        params.put("correo_usuario",correo);
        params.put("numtelef_usuario", telef);



        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.post(context, URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    if (response.getInt("success") == 1) {
                        onSuccessUpdateUser.onSuccesUpdateUser(true, response.getInt("success"), response.getString("success_msg"));
                        SignalChangeUsername signalChangeUsername=new SignalChangeUsername();
                        BusHolder.getInstance().post(signalChangeUsername);
                    } else {
                        onSuccessUpdateUser.onSuccesUpdateUser(true, response.getInt("success"), response.getString("error_msg"));
                    }

                    Log.e("json", response.toString());
                } catch (JSONException e) {
                    onSuccessUpdateUser.onSuccesUpdateUser(false, -1, "");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                onSuccessUpdateUser.onSuccesUpdateUser(false, -1, "");
            }
        });

    }

    public class SignalChangeUsername{}


    private String resetFormatFecha(String fecNac) {
        try {
            DateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
            DateFormat outputFormat = new SimpleDateFormat("MM/dd/yyyy");
            //String inputDateStr = "2013-06-24";
            Date date = inputFormat.parse(fecNac);
            String outputDateStr = outputFormat.format(date);
            Log.e("testFecha", outputDateStr);
            return outputDateStr;
        }catch (ParseException e) {
                Log.e("parseError",e.toString());
        }
        return "";
    }
    private String inverseResetFormatFecha(String fecNac) {
        try {
            DateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
            DateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
            //String inputDateStr = "2013-06-24";
            Date date = inputFormat.parse(fecNac);
            String outputDateStr = outputFormat.format(date);
            Log.e("testFecha", outputDateStr);
            return outputDateStr;
        }catch (ParseException e) {
            Log.e("parseError",e.toString());
        }
        return "";
    }

    public void updatePassUsuario(final String newPass, final String oldPass){
        String URL = WSGlup.ORQUESTADOR_NUEVO;

        RequestParams params = new RequestParams();
        params.put("tag","cambiarPassUser");
        params.put("codigo_usuario",new Session_Manager(context).getCurrentUserCode());
        params.put("newpass_usuario",newPass);
        params.put("pass_usuario",oldPass);

        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.post(context,URL,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    String currentPass;
                    if (response.getInt("success")==1){
                        currentPass=newPass;
                        onSuccessUpdatePass.onSuccesUpdatePass(true,response.getInt("success"),response.getString("success_msg"),currentPass);
                    }else{
                        currentPass=oldPass;//inseguro
                        onSuccessUpdatePass.onSuccesUpdatePass(true,response.getInt("success"),response.getString("error_msg"),currentPass);
                    }
                    Log.e("passwordActual",currentPass);

                } catch (JSONException e) {
                    onSuccessUpdatePass.onSuccesUpdatePass(false,-1 ,"","");
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                onSuccessUpdatePass.onSuccesUpdatePass(false,-1,"","");
            }
        });

    }


}
