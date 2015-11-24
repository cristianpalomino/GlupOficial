package pe.com.glup.dialog;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import pe.com.glup.R;
import pe.com.glup.managers.bus.BusHolder;
import pe.com.glup.network.DSProbador;


public class DetailActivity extends AppCompatActivity {
    private DSProbador dsProbador;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        BusHolder.getInstance().register(this);
        if (savedInstanceState == null) {
            crearFullScreenDialog();
        }
        String codigoPrenda=getIntent().getExtras().getString("codigoPrenda");
        Log.e("dentroActvFull", codigoPrenda);
        dsProbador = new DSProbador(this);
        dsProbador.getPrendaDetalle(codigoPrenda);
        dsProbador.getTiendasDisponibles(codigoPrenda);



    }

    private void crearFullScreenDialog() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FullScreenDialog newFragment = new FullScreenDialog();

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.add(android.R.id.content, newFragment, "FullScreenFragment")
                .commit();

    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }

    private class ClaseAsync extends AsyncTask<Void,Void,String>{

        @Override
        protected String doInBackground(Void... params) {
            String codigoPrenda=getIntent().getExtras().getString("codigoPrenda");
            dsProbador.getPrendaDetalle(codigoPrenda);
            return null;
        }
        protected  void onPostExecute(String file_url) {
            String codigoPrenda=getIntent().getExtras().getString("codigoPrenda");
            dsProbador.getTiendasDisponibles(codigoPrenda);
        }
    }



}
