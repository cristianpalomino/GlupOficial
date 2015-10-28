package pe.com.glup.datasource;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;

import pe.com.glup.bus.BusHolder;
import pe.com.glup.events.Flash;
import pe.com.glup.interfaces.INotification;
import pe.com.glup.notifications.CargaFotoAB;
import pe.com.glup.utils.Utils;
import pe.com.glup.ws.WSGlup;

/**
 * Created by Glup on 20/10/15.
 */
public class DSCamera {
	private Context context;
	private ProgressDialog dialog;
	private INotification interface_notification = new CargaFotoAB();

	public DSCamera(Context context) {
		this.context = context;
		BusHolder.getInstance().register(this);
	}
	public void generarCodePrenda(){
		//dialog = ProgressDialog.show(context, null, "Generando codigo de Prenda", true, true);
		String URL= WSGlup.ORQUESTADOR_PROCESOS;
		RequestParams params= new RequestParams();
		params.put("codigo_usuario", "000000000010");
		params.put("tag", "reservarCodPrenda");
		AsyncHttpClient httpClient= new AsyncHttpClient();
		httpClient.post(context, URL, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				/*dialog.dismiss();
				dialog.hide();*/
				Gson gson = new Gson();
				try {
					Log.e("dsCamera", response.getInt("success") + " Generando codigo de Prenda ...");
				} catch (JSONException e) {
					Log.e("dsCodePrenda", e.toString());
				}
				ResponseGenerateCodePrenda responseGenerateCodePrenda = gson.fromJson(response.toString(),
						ResponseGenerateCodePrenda.class);
				BusHolder.getInstance().post(responseGenerateCodePrenda);
				Utils.showMessage(context, "Observe el progreso de la carga de su Prenda a Glup en la barra de notificaciones");
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				Log.e("ERROR", responseString);
				super.onFailure(statusCode, headers, responseString, throwable);
				/*dialog.dismiss();
				dialog.hide();*/
			}
		});
	}
	public void uploadPrenda(String filtro, File dir, String nomFrente, String nomEspalda, String codPrenda){
		try{
		Log.e(null, "entro a uploadPrenda");
		/*dialog = ProgressDialog.show(context, null, "Enviando Prenda ...", true, true);*/
		interface_notification.createNotification(context);
		interface_notification.getBuilder().setContentTitle("Subiendo Imagenes a Glup");
		interface_notification.setProgress(2, 0);
		final AsyncHttpClient httpClient = new AsyncHttpClient();
		httpClient.setTimeout(12000000);

		String URL=WSGlup.ORQUESTADOR_IMAGENES_CAMERA;
		RequestParams params =new RequestParams();
		if (filtro.equals("superior")){
			try {
				params.put("img_a",new File(dir,nomFrente));
				params.put("img_b",new File(dir,nomEspalda));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			params.put("wear_code",codPrenda);
			params.put("user_code", "000000000010");
		}else {
			URL=WSGlup.ORQUESTADOR_IMAGENES_CAMERA_INFERIOR;
			params.put("tag", "procesarImagen");
			try {
				params.put("uploaded_fileA",new File(dir,nomFrente));
				params.put("uploaded_fileB",new File(dir,nomEspalda));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			params.put("codigo_usuario","000000000010");
			params.put("codigo_prenda",codPrenda);

		}
		Log.e("URL",URL);

		httpClient.post(context, URL, params, new TextHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, String response) {

				Log.e("null", "hizo el servicio en uploadPrenda "+response);
				/*
					Log.e("dsCamera"," terminar de subir prenda ...");
					dialog.setMessage("Prenda cargada satisfactoriamente");
					httpClient.setTimeout(2000000);
					dialog.dismiss();
					dialog.hide();*/

				interface_notification.getBuilder().setContentTitle("Listo")
						.setContentText("Termino carga de Prenda a Glup ")
						.setProgress(0, 0, false);
				interface_notification.invalidate();
				ResponseUploadPrenda responseUploadPrenda = new ResponseUploadPrenda();
				BusHolder.getInstance().post(responseUploadPrenda);

			}
			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				/*Log.e("ERROR", responseString);
				dialog.setMessage("Error al subir la prenda");
				httpClient.setTimeout(2000000);
				dialog.dismiss();
				dialog.hide();*/
				interface_notification.getBuilder().setContentTitle("Error")
						.setContentText("Lo sentimos, revise su conexion a Internet")
						.setProgress(0, 0, false);
				interface_notification.invalidate();
			}
			@Override
			public void onProgress(long bytesWritten, long totalSize) {
				super.onProgress(bytesWritten, totalSize);
				/*int progressPercentage = (int)(100 * bytesWritten / totalSize);
				dialog.setMessage("Cargando " + progressPercentage + "%");*/
				int progressPercentage = (int)(100 * bytesWritten / totalSize);
				if (progressPercentage > 95 && progressPercentage < 101) {
					interface_notification.getBuilder().setProgress(0, 0, true);
					interface_notification.getManager().notify(1, interface_notification.getBuilder().build());
				} else {
					interface_notification.setProgress(100, progressPercentage);
				}

			}
		});
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	public void flashAutomatico(final Flash isFlash){
		AsyncHttpClient httpClient = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		httpClient.post(context, "http://google.com", params, new TextHttpResponseHandler() {
			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				Log.e("null", "entro a error flash");
				try {
					BusHolder.getInstance().post(isFlash);
				}catch (Exception e){
					Log.e("error",e.getMessage());
				}

			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, String responseString) {
				Log.e("null","entro a success flash");
				BusHolder.getInstance().post(isFlash);
				try {
					BusHolder.getInstance().post(isFlash);
				}catch (Exception e){
					Log.e("error",e.getMessage());
				}

			}
		});
	}

	public class ResponseGenerateCodePrenda{
		public int success;
		public String  codigo_prenda;
	}
	public class ResponseUploadPrenda {}
}
