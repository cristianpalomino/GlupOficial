package pe.com.glup.network;

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

import pe.com.glup.managers.bus.BusHolder;
import pe.com.glup.managers.session.Session_Manager;
import pe.com.glup.models.DatoUser;
import pe.com.glup.models.DetalleUser;
import pe.com.glup.models.ErrorConection;
import pe.com.glup.models.PerfilUsuario;
import pe.com.glup.ws.WSGlup;

/**
 * Created by Glup on 24/11/15.
 */
public class DSUsuarioNew {
    private Context context;
    private ArrayList<DatoUser> datoUser = new ArrayList<DatoUser>();
    private ArrayList<DetalleUser> detalleUser = new ArrayList<DetalleUser>();

    public DSUsuarioNew(Context context) {
        this.context = context;
        BusHolder.getInstance().register(this);
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
                //detalleUser=perfilUsuario.getDetalleuser();
                Log.e("apeUser",perfilUsuario.getDetalleuser().get(0).getCorreoUser().toString());
                //datoUser= perfilUsuario.getDatouser();
                Log.e("numPren", perfilUsuario.getDatouser().get(0).getNumPrend().toString());
                BusHolder.getInstance().post(perfilUsuario);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                ErrorConection errorConection=new ErrorConection();
                errorConection.setMessage(responseString);
                BusHolder.getInstance().post(errorConection);
            }
        });
    }

    public void updateUsuario(final String indVerPass,String passUser,String nombre, String apellido, String fecNac, String correo, String telef){
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
                    SignalUpdateProfile signalUpdateProfile =new SignalUpdateProfile();
                    if (response.getInt("success") == 1) {
                        signalUpdateProfile.success=response.getInt("success");//1
                        signalUpdateProfile.msg=response.getString("success_msg");

                    } else {
                        signalUpdateProfile.success=response.getInt("success");//0
                        signalUpdateProfile.msg=response.getString("error_msg");
                    }
                    signalUpdateProfile.indVerPass=indVerPass;
                    BusHolder.getInstance().post(signalUpdateProfile);
                    Log.e("json", response.toString());
                } catch (JSONException e) {
                    SignalUpdateProfile signalUpdateProfileError =new SignalUpdateProfile();
                    signalUpdateProfileError.success=-1;//-1
                    signalUpdateProfileError.msg="";
                    BusHolder.getInstance().post(signalUpdateProfileError);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                ErrorConection errorConection=new ErrorConection();
                errorConection.setMessage(responseString);
                BusHolder.getInstance().post(errorConection);
            }
        });

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
                    SignalChangeNewPass signalChangeNewPass=new SignalChangeNewPass();
                    if (response.getInt("success")==1){
                        signalChangeNewPass.success=response.getInt("success");
                        signalChangeNewPass.msg=response.getString("success_msg");
                        signalChangeNewPass.password=newPass;
                    }else{
                        signalChangeNewPass.success=response.getInt("success");
                        signalChangeNewPass.msg=response.getString("error_msg");
                        signalChangeNewPass.password=oldPass;//no seguro
                    }
                    Log.e("passwordActual",signalChangeNewPass.password);
                    BusHolder.getInstance().post(signalChangeNewPass);

                } catch (JSONException e) {
                    SignalChangeNewPass signalChangeNewPassError=new SignalChangeNewPass();
                    signalChangeNewPassError.success=-1;
                    signalChangeNewPassError.msg="";
                    signalChangeNewPassError.password=oldPass;
                    BusHolder.getInstance().post(signalChangeNewPassError);
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                ErrorConection errorConection=new ErrorConection();
                errorConection.setMessage(responseString);
                BusHolder.getInstance().post(errorConection);
            }
        });

    }

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

    public class SignalUpdateProfile {public int success;public String msg;public String indVerPass;}
    public class SignalChangeNewPass{public int success;public String msg;public String indVerPass;public String password;}

}
