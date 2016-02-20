package pe.com.glup.network;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import pe.com.glup.managers.bus.BusHolder;
import pe.com.glup.models.Catalogo;
import pe.com.glup.models.ObjTextura;
import pe.com.glup.models.Prenda;
import pe.com.glup.models.interfaces.OnSuccessUpdate;
import pe.com.glup.models.interfaces.OnSuccessCatalogo;
import pe.com.glup.managers.session.Session_Manager;
import pe.com.glup.utils.ImageManager;
import pe.com.glup.ws.WSGlup;

/**
 * Created by Glup on 24/06/15.
 */
public class DSCatalogo {

    private Context context;
    private OnSuccessCatalogo onSuccessCatalogo;
    private OnSuccessUpdate onSuccessUpdate;
    private ArrayList<Prenda> prendas = new ArrayList<Prenda>();

    public void setOnSuccessCatalogo(OnSuccessCatalogo onSuccessCatalogo) {
        this.onSuccessCatalogo = onSuccessCatalogo;
    }

    public void setOnSuccessUpdate(OnSuccessUpdate onSuccessUpdate) {
        this.onSuccessUpdate = onSuccessUpdate;
    }

    public DSCatalogo(Context context) {
        this.context = context;
        BusHolder.getInstance().register(this);
    }

    public void getGlobalPrendas(String buscar, String pagina, String registros) {
        AsyncHttpClient httpClient = new AsyncHttpClient();

        String URL = WSGlup.ORQUESTADOR_NUEVO_CATALOGO.
                replace(WSGlup.NUMERO_PAGINA, pagina).
                replace(WSGlup.NUMERO_REGISTROS, registros).
                replace(WSGlup.BUSCAR, buscar);
        Log.e("Pagina", pagina);
        RequestParams params = new RequestParams();
        params.put("tag", "prendaCatalogo");
        params.put("codigo_usuario", new Session_Manager(context).getCurrentUserCode());


        httpClient.post(context, URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Gson gson = new Gson();

                Catalogo catalogo = gson.fromJson(response.toString(), Catalogo.class);
                prendas = catalogo.getPrendas();
                //Log.e("url",prendas.get(0).getImagen());
                //Log.e("indExhibicion",prendas.get(0).getInd_exhibicion()+"");
                //for (Prenda p:prendas){
                    //Log.e(null,p.toString());
                //}
                //Log.e("totalPrendas",prendas.get(0).getNumPrend()+"");
                onSuccessCatalogo.onSuccess(catalogo.getTag(), prendas);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                onSuccessCatalogo.onFailed(responseString);
            }
        });
    }

    public void updateProbador(String codigo_prenda) {
        String URL = WSGlup.ORQUESTADOR_NUEVO;

        RequestParams params = new RequestParams();
        params.put("tag", "enviarProbador");
        params.put("codigo_usuario", new Session_Manager(context).getCurrentUserCode());
        params.put("codigo_prenda", codigo_prenda);

        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.post(context, URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    onSuccessUpdate.onSuccesUpdate(true, response.getInt("indProb"));
                } catch (Exception e) {
                    onSuccessUpdate.onSuccesUpdate(false, -1);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                onSuccessUpdate.onSuccesUpdate(false, -1);
            }
        });
    }

    public void obtenerFilesObj(String codPrenda){
        String URL="http://108.179.236.242/~glupserver/API/orquestadorServiciosApp.php";
        AsyncHttpClient httpClient= new AsyncHttpClient();
        RequestParams params=new RequestParams();
        params.put("tag","obtenerOBJTextura");
        params.put("codigo_usuario","000000000010");
        params.put("codigo_prenda",codPrenda);
        httpClient.post(context, URL, params,new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Gson gson = new Gson();
                Log.e("Coneccion",response.toString());
                /*Catalogo catalogo = gson.fromJson(response.toString(), Catalogo.class);
                prendas = catalogo.getPrendas();*/
                ObjTextura model= gson.fromJson(response.toString(), ObjTextura.class);

                InputRenderer inputRenderer=new InputRenderer();

                String url=model.detalleOBJTextura.get(0).getImagen();
                String urlObj = model.detalleOBJTextura.get(0).getNom_obj();

                inputRenderer.filenameTexture=url.substring(url.lastIndexOf("/") + 1, url.length()-4);

                //Log.e("URL", url);
                long startTimeTextura = System.currentTimeMillis();
                Download miDownload=new Download(url,true);
                try {
                    String miPathTexture=miDownload.execute().get();
                    inputRenderer.timeTextura=miPathTexture.substring(miPathTexture.lastIndexOf(":")+1,miPathTexture.length());
                    inputRenderer.pathTexture=miPathTexture.substring(0,miPathTexture.lastIndexOf(":"));

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }


                long startTimeObj = System.currentTimeMillis();

                Download secondDownload= new Download(urlObj,false);
                try {
                    String pathObjfull= secondDownload.execute().get();
                    inputRenderer.timeObj=pathObjfull.substring(pathObjfull.lastIndexOf(":")+1,pathObjfull.length());
                    inputRenderer.pathObj=pathObjfull.substring(0,pathObjfull.lastIndexOf("/"));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                inputRenderer.filenameObj=urlObj.substring(urlObj.lastIndexOf("/") + 1, urlObj.length()-4);

                Log.e("nomTextura", inputRenderer.filenameTexture + " pathTexture:" + inputRenderer.pathTexture + " path3ds:" + inputRenderer.pathObj + " nom3ds:" + inputRenderer.filenameObj);

                BusHolder.getInstance().post(inputRenderer);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.e("Error", responseString);

            }
        });
    }
    private class Download extends AsyncTask<Void,Void,String> {
        private String url;
        private boolean isTextura;
        public Download(String url, boolean texture){
            this.url=url;
            this.isTextura=texture;
        }
        @Override
        protected String doInBackground(Void... params) {
            ImageManager imageManager= new ImageManager(context);
            String filename;
            if (isTextura){
                filename=url.substring(url.lastIndexOf("/") + 1, url.length());
            }else {
                filename=url.substring(url.lastIndexOf("/") + 1, url.length()-4);
            }
            //Log.e("nom",filename);
            return imageManager.DownloadFromUrl(url, filename, isTextura);
        }

    }
    public class InputRenderer{
        public String filenameTexture,pathTexture,filenameObj,pathObj,timeTextura,timeObj;
    }

}
