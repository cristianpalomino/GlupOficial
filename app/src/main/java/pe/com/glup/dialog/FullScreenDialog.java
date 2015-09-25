package pe.com.glup.dialog;


import android.content.Context;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;


import com.squareup.otto.Subscribe;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import pe.com.glup.R;
import pe.com.glup.beans.Prenda;
import pe.com.glup.beans.TallaDisponible;
import pe.com.glup.beans.Tienda;
import pe.com.glup.bus.BusHolder;
import pe.com.glup.datasource.DSProbador;


public class FullScreenDialog extends DialogFragment implements View.OnClickListener,
        Spinner.OnItemSelectedListener, RadioGroup.OnCheckedChangeListener ,
        ToggleButton.OnCheckedChangeListener{

    private Context context;
    private String codPrenda;
    private TextView marca,tipo,descripcion,componente,precio;
    private Spinner tiendaSpinner;
    private RadioGroup radioGroup;
    private Button addReserva;
    private Button cerrarReserva;
    private ArrayList<ToggleButton> tallasMax= new ArrayList<ToggleButton>(4);
    private ArrayList<Boolean> estadoTalla = new ArrayList<Boolean>(4);
    private ArrayList<Prenda> prendaDetalle= new ArrayList<Prenda>();
    private ArrayList<Tienda> tiendas = new ArrayList<Tienda>();
    private DSProbador dsProbador;
    public FullScreenDialog() {
        // Required empty public constructor

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
        /*
        // Obtener instancia de la action bar
        ActionBar actionBar = ((AppCompatActivity) getActivity())
                .getSupportActionBar();

        if (actionBar != null) {
            // Habilitar el Up Button
            actionBar.setDisplayHomeAsUpEnabled(true);
            // Cambiar icono del Up Button
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_close);
        } */
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
        context=getActivity();
        marca=(TextView)getView().findViewById(R.id.marca_reserva);
        tipo = (TextView)getView().findViewById(R.id.tipo_prenda_reserva);
        descripcion=(TextView)getView().findViewById(R.id.descripcion_reserva);
        componente=(TextView)getView().findViewById(R.id.componente1);
        precio=(TextView)getView().findViewById(R.id.precio_reserva);
        tiendaSpinner = (Spinner) getView().findViewById(R.id.elegir_tienda_reserva);
        cerrarReserva = (Button) getView().findViewById(R.id.cerrar_add_reserva);
        //radioGroup = (RadioGroup) getView().findViewById(R.id.elegir_talla_reserva);
        tallasMax.add((ToggleButton)getView().findViewById(R.id.talla1));
        tallasMax.add((ToggleButton)getView().findViewById(R.id.talla2));
        tallasMax.add((ToggleButton)getView().findViewById(R.id.talla3));
        tallasMax.add((ToggleButton) getView().findViewById(R.id.talla4));
        int i=0;
        String[] prueba={"S","M","L","XL"};
        for (ToggleButton toggleButton:tallasMax){
            //toggleButton.setTextSize(18);
            toggleButton.setTextOn(prueba[i]);
            toggleButton.setTextOff(prueba[i]);
            i++;
            toggleButton.setTextColor(getResources().getColor(R.color.text_talla_off));
            toggleButton.setChecked(false);
            toggleButton.setOnCheckedChangeListener(this);
            toggleButton.setVisibility(View.INVISIBLE);
        }

        Log.e("cerrarReserv", cerrarReserva.getId() + "");
        cerrarReserva.setOnClickListener(this);


        String codigoPrenda=getActivity().getIntent().getExtras().getString("codigoPrenda");
        Log.e("dentroFull", codigoPrenda);

        dsProbador = new DSProbador(getActivity().getApplicationContext());
        /*dsProbador.getTiendasDisponibles(codigoPrenda);*/

        tiendaSpinner.setOnItemSelectedListener(this);
        //radioGroup.setOnCheckedChangeListener(this);


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
                Log.e(null,"cruce con home");
                break;
            case R.id.action_save:
                // procesarGuardar()
                Log.e(null,"cruce con save");
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Subscribe
    public void setPrendaDetalle(DSProbador.ResponseDetallePrenda responseDetallePrenda){
        if (responseDetallePrenda.getSuccess()==1){
            Log.e("entro", "detalle");
            prendaDetalle = responseDetallePrenda.getPrendas();
            marca.setText(prendaDetalle.get(0).getMarca());
            tipo.setText(prendaDetalle.get(0).getTipo());
            descripcion.setText(prendaDetalle.get(0).getDescripcion());
            componente.setText(prendaDetalle.get(0).getComposicion());
            precio.setText("S/."+prendaDetalle.get(0).getPrecio());
            codPrenda=prendaDetalle.get(0).getCod_prenda();

        }

    }

    @Subscribe
    public void setTiendasDisponibles(DSProbador.ResponseTiendasDisponibles responseTiendasDisponibles){
        if (responseTiendasDisponibles.getSuccess()==1){
            Log.e("entro", "tiendas");
            Log.e("tienda1",responseTiendasDisponibles.getTiendas().get(0).getLocal());
            ArrayList<Tienda> tiendas1 = new ArrayList<Tienda>();
            tiendas1 = responseTiendasDisponibles.getTiendas();
            ArrayList<String> items= new ArrayList<String>();
            for (int i=0;i<tiendas1.size();i++){
                items.add(tiendas1.get(i).getLocal());
            }
            items.add("Elegir tienda");
            Log.e("tiendaitem1", items.get(0).toString());
            ArrayAdapter<String> arrayAdapter= new ArrayAdapter<String>(context,R.layout.item_elegir_tienda,items){
                @Override
                public int getCount() {
                    return super.getCount()-1; // you doent display last item. It is used as hint.
                }
            };
            tiendaSpinner.setAdapter(arrayAdapter);
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cerrar_add_reserva:
                Log.e(null,"clic cerrar add reservar");
                getActivity().onBackPressed();
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String nomLocal = parent.getItemAtPosition(position).toString();
        Log.e("spinSelect", nomLocal);
        dsProbador.getTallasDisponibles(codPrenda, nomLocal);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    @Subscribe
    public void setTallasDisponibles(DSProbador.ResponseTallasDisponibles responseTallasDisponibles){
        Log.e("talla0Full", responseTallasDisponibles.getTallas().get(0).getTalla());
        Log.e("entro","setvisibilidad "+responseTallasDisponibles.getSuccess());
        if (responseTallasDisponibles.getSuccess()==1){
            ArrayList<TallaDisponible> tallas1=responseTallasDisponibles.getTallas();
            //max 4 tallas por no deforma la interfaz
            int size=tallas1.size();
            Log.e("tamaño",size+"");
            for (int i=0;i<size;i++){
                tallasMax.get(i).setTextOff(tallas1.get(i).getTalla());
                tallasMax.get(i).setTextOn(tallas1.get(i).getTalla());
                Log.e("textoChan",String.valueOf(tallasMax.get(i).getTextOff()));
                tallasMax.get(i).setVisibility(View.VISIBLE);
            }

        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case -1:Log.e("NO","select");break;
            case R.id.talla1:Log.e("talla1","select");break;
            case R.id.talla2:Log.e("talla2","select");break;
            case R.id.talla3:break;
            case R.id.talla4:break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.talla1:
                Log.e("talla1",tallasMax.get(0).isChecked()+"");
                if (!tallasMax.get(0).isChecked()){
                    for (ToggleButton toggleButton:tallasMax){
                        toggleButton.setChecked(false);
                        toggleButton.setTextColor(getResources().getColor(R.color.text_talla_off));
                    }
                }else{
                    tallasMax.get(0).setChecked(true);
                    tallasMax.get(0).setTextColor(getResources().getColor(R.color.blanco));
                    tallasMax.get(1).setChecked(false);
                    tallasMax.get(1).setTextColor(getResources().getColor(R.color.text_talla_off));
                    tallasMax.get(2).setChecked(false);
                    tallasMax.get(2).setTextColor(getResources().getColor(R.color.text_talla_off));
                    tallasMax.get(3).setChecked(false);
                    tallasMax.get(3).setTextColor(getResources().getColor(R.color.text_talla_off));
                }

                Log.e("talla1","selectChange");
                break;
            case R.id.talla2:
                Log.e("talla2",tallasMax.get(1).isChecked()+"");
                if (!tallasMax.get(1).isChecked()){
                    for (ToggleButton toggleButton:tallasMax){
                        toggleButton.setChecked(false);
                        toggleButton.setTextColor(getResources().getColor(R.color.text_talla_off));
                    }
                }else{
                    tallasMax.get(0).setChecked(false);
                    tallasMax.get(0).setTextColor(getResources().getColor(R.color.text_talla_off));
                    tallasMax.get(1).setChecked(true);
                    tallasMax.get(1).setTextColor(getResources().getColor(R.color.blanco));
                    tallasMax.get(2).setChecked(false);
                    tallasMax.get(2).setTextColor(getResources().getColor(R.color.text_talla_off));
                    tallasMax.get(3).setChecked(false);
                    tallasMax.get(3).setTextColor(getResources().getColor(R.color.text_talla_off));
                }

                Log.e("talla2","selectChange");
                break;
            case R.id.talla3:
                Log.e("talla3",tallasMax.get(2).isChecked()+"");
                if (!tallasMax.get(2).isChecked()){
                    for (ToggleButton toggleButton:tallasMax){
                        toggleButton.setChecked(false);
                        toggleButton.setTextColor(getResources().getColor(R.color.text_talla_off));
                    }
                }else{
                    tallasMax.get(0).setChecked(false);
                    tallasMax.get(0).setTextColor(getResources().getColor(R.color.text_talla_off));
                    tallasMax.get(1).setChecked(false);
                    tallasMax.get(1).setTextColor(getResources().getColor(R.color.text_talla_off));
                    tallasMax.get(2).setChecked(true);
                    tallasMax.get(2).setTextColor(getResources().getColor(R.color.blanco));
                    tallasMax.get(3).setChecked(false);
                    tallasMax.get(3).setTextColor(getResources().getColor(R.color.text_talla_off));
                }

                Log.e("talla3", "selectChange");
                break;
            case R.id.talla4:
                Log.e("talla4",tallasMax.get(3).isChecked()+"");
                if (!tallasMax.get(3).isChecked()){
                    for (ToggleButton toggleButton:tallasMax){
                        toggleButton.setChecked(false);
                        toggleButton.setTextColor(getResources().getColor(R.color.text_talla_off));
                    }
                }else{
                    tallasMax.get(0).setChecked(false);
                    tallasMax.get(0).setTextColor(getResources().getColor(R.color.text_talla_off));
                    tallasMax.get(1).setChecked(false);
                    tallasMax.get(1).setTextColor(getResources().getColor(R.color.text_talla_off));
                    tallasMax.get(2).setChecked(false);
                    tallasMax.get(2).setTextColor(getResources().getColor(R.color.text_talla_off));
                    tallasMax.get(3).setChecked(true);
                    tallasMax.get(3).setTextColor(getResources().getColor(R.color.blanco));
                }

                Log.e("talla4", "selectChange");
                break;
        }
    }

    public void setVisibleTallas(DSProbador.ResponseTallasDisponibles responseTallasDisponibles){

    }
}
