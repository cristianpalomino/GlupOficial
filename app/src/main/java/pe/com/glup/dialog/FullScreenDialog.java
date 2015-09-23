package pe.com.glup.dialog;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;


import com.squareup.otto.Subscribe;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import pe.com.glup.R;
import pe.com.glup.beans.Prenda;
import pe.com.glup.beans.Tienda;
import pe.com.glup.bus.BusHolder;
import pe.com.glup.datasource.DSProbador;


public class FullScreenDialog extends DialogFragment {

    private TextView marca,tipo,descripcion,componente,precio;
    private Spinner tiendaSpinner;
    private Button addReserva;
    private ArrayList<Prenda> prendaDetalle= new ArrayList<Prenda>();
    private ArrayList<Tienda> tiendas = new ArrayList<Tienda>();
    private DSProbador dsProbador;
    public FullScreenDialog() {
        // Required empty public constructor

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        // Obtener instancia de la action bar
        ActionBar actionBar = ((AppCompatActivity) getActivity())
                .getSupportActionBar();

        if (actionBar != null) {
            // Habilitar el Up Button
            actionBar.setDisplayHomeAsUpEnabled(true);
            // Cambiar icono del Up Button
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_close);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fullscreen_dialog, container, false);
        return view;
    }
    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        BusHolder.getInstance().register(this);
        marca=(TextView)getView().findViewById(R.id.marca_reserva);
        tipo = (TextView)getView().findViewById(R.id.tipo_prenda_reserva);
        descripcion=(TextView)getView().findViewById(R.id.descripcion_reserva);
        componente=(TextView)getView().findViewById(R.id.componente1);
        precio=(TextView)getView().findViewById(R.id.precio_reserva);
        tiendaSpinner = (Spinner) getView().findViewById(R.id.elegir_tienda_reserva);
        dsProbador = new DSProbador(getActivity());
        String codigoPrenda=getActivity().getIntent().getExtras().getString("codigoPrenda");
        Log.e("dentroFull",codigoPrenda);
        dsProbador.getPrendaDetalle(codigoPrenda);
        dsProbador.getTiendasDisponibles(codigoPrenda);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fullscreen_dialog, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                // procesarDescartar()
                break;
            case R.id.action_save:
                // procesarGuardar()
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Subscribe
    public void setPrendaDetalle(DSProbador.ResponseDetallePrenda responseDetallePrenda){
        Log.e("entro","detalle");
        prendaDetalle = responseDetallePrenda.getPrendas();
        marca.setText(prendaDetalle.get(0).getMarca());
        tipo.setText(prendaDetalle.get(0).getTipo());
        descripcion.setText(prendaDetalle.get(0).getDescripcion());
        componente.setText(prendaDetalle.get(0).getComposicion());
        precio.setText(prendaDetalle.get(0).getPrecio());
    }

    @Subscribe
    public void setTiendasDisponibles(DSProbador.ResponseTiendasDisponibles responseTiendasDisponibles){
        Log.e("entro", "tiendas");
        tiendas = responseTiendasDisponibles.getTiendas();
        ArrayList<String> items= new ArrayList<String>();
        for (int i=0;i<tiendas.size();i++){
            items.add(tiendas.get(i).getLocal());
        }
        items.add("Elegir tienda");
        ArrayAdapter<String> arrayAdapter= new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,items){
            @Override
            public int getCount() {
                return super.getCount()-1; // you doent display last item. It is used as hint.
            }
        };
        tiendaSpinner.setAdapter(arrayAdapter);
    }


}
