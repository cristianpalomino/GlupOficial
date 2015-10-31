package pe.com.glup.dialog;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
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
import android.widget.LinearLayout;
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
import pe.com.glup.utils.MessageUtil;
import pe.com.glup.views.Message;


public class FullScreenDialog extends DialogFragment implements View.OnClickListener,
        Spinner.OnItemSelectedListener, RadioGroup.OnCheckedChangeListener ,
        ToggleButton.OnCheckedChangeListener{

    private Context context;
    private String codPrenda,idTalla;
    private TextView marca,tipo,descripcion,componente,precio;
    private Spinner tiendaSpinner;
    private RadioGroup radioGroup;
    private Button addReserva;
    private ImageView cerrarReserva;
    private ArrayList<String> idTallas;
    private ArrayList<ToggleButton> tallasMax= new ArrayList<ToggleButton>(4);
    private ArrayList<Boolean> estadoTalla = new ArrayList<Boolean>(4);
    private ArrayList<Prenda> prendaDetalle= new ArrayList<Prenda>();
    private ArrayList<Tienda> tiendas = new ArrayList<Tienda>();
    private DSProbador dsProbador;
    private ToggleButton info;
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
        cerrarReserva = (ImageView) getView().findViewById(R.id.cerrar_add_reserva);
        //radioGroup = (RadioGroup) getView().findViewById(R.id.elegir_talla_reserva);
        tallasMax.add((ToggleButton)getView().findViewById(R.id.talla1));
        tallasMax.add((ToggleButton)getView().findViewById(R.id.talla2));
        tallasMax.add((ToggleButton)getView().findViewById(R.id.talla3));
        tallasMax.add((ToggleButton) getView().findViewById(R.id.talla4));
        addReserva=(Button) getView().findViewById(R.id.add_reserva);
        info = (ToggleButton) getView().findViewById(R.id.info_detalle);
        info.setChecked(true);
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
            toggleButton.setVisibility(View.GONE);
        }

        Log.e("cerrarReserv", cerrarReserva.getId() + "");
        cerrarReserva.setOnClickListener(this);
        addReserva.setOnClickListener(this);


        String codigoPrenda=getActivity().getIntent().getExtras().getString("codigoPrenda");
        Log.e("dentroFull", codigoPrenda);

        dsProbador = new DSProbador(getActivity().getApplicationContext());
        /*dsProbador.getTiendasDisponibles(codigoPrenda);*/

        tiendaSpinner.setOnItemSelectedListener(this);
        //radioGroup.setOnCheckedChangeListener(this);


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
            precio.setText("S/." + prendaDetalle.get(0).getPrecio());
            codPrenda=prendaDetalle.get(0).getCod_prenda();
            Log.e("indReser",prendaDetalle.get(0).getIndReser());
            if (prendaDetalle.get(0).getIndReser().equals("1")){
                addReserva.setText("Ya esta reservado");
                addReserva.setTextColor(Color.GRAY);
                addReserva.setEnabled(false);
                addReserva.postInvalidate();
            }else if (prendaDetalle.get(0).getIndReserGen().equals("1")){
                    addReserva.setText("Reservado por otro usuario");
                    addReserva.setTextColor(Color.GRAY);
                    addReserva.setEnabled(false);
                    addReserva.postInvalidate();
            }else{
                    addReserva.setText("Agregar a Reserva");
                    addReserva.setTextColor(context.getResources().getColor(R.color.celeste_glup));
                    addReserva.setEnabled(true);
                    addReserva.postInvalidate();
            }






        }

    }

    @Subscribe
    public void setTiendasDisponibles(DSProbador.ResponseTiendasDisponibles responseTiendasDisponibles){
        if (responseTiendasDisponibles.getSuccess()==1){
            Log.e("entro", "tiendas");
            Log.e("tienda1", responseTiendasDisponibles.getTiendas().get(0).getLocal());
            ArrayList<Tienda> tiendas1 = new ArrayList<Tienda>();
            tiendas1 = responseTiendasDisponibles.getTiendas();
            ArrayList<String> items= new ArrayList<String>();
            items.add("Elegir tienda");
            for (int i=0;i<tiendas1.size();i++){
                items.add(tiendas1.get(i).getLocal());
            }

            Log.e("tiendaitem1", items.get(0).toString());
            ArrayAdapter<String> arrayAdapter= new ArrayAdapter<String>(context,R.layout.item_elegir_tienda,items){
                @Override
                public boolean isEnabled(int position){
                    if(position == 0)
                    {
                        // Disable the first item from Spinner
                        // First item will be use for hint
                        return false;
                    }
                    else
                    {
                        return true;
                    }
                }
                @Override
                public View getDropDownView(int position, View convertView,
                                            ViewGroup parent) {
                    View view = super.getDropDownView(position, convertView, parent);
                    TextView tv = (TextView) view;
                    if(position == 0){
                        tv.setEnabled(false);
                        tv.setTextColor(Color.GRAY);
                    }
                    else {
                        tv.setTextColor(Color.BLACK);
                    }
                    return view;
                }
                @Override
                public int getCount(){
                    return super.getCount();
                }


            };

            arrayAdapter.setDropDownViewResource(R.layout.item_elegir_tienda);
            //Log.e("arraycount", arrayAdapter.getCount() + " " + items.get(arrayAdapter.getCount()).toString());
            tiendaSpinner.setAdapter(arrayAdapter);
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cerrar_add_reserva:
                Log.e(null, "clic cerrar add reservar");
                info.setChecked(false);
                getActivity().onBackPressed();
                break;
            case R.id.add_reserva:
                idTalla="";
                int i=0;
                for (ToggleButton toggleButton:tallasMax){
                    if (toggleButton.isChecked()){
                        Log.e(null,"tengo q aparecer 1 vez "+toggleButton.getTextOn().toString());
                        idTalla=idTallas.get(i);
                    }
                    i++;
                }
                if (idTalla.equals("")){
                    MessageUtil.showToast(context,"Falta seleccionar la talla");
                }else{
                    Log.e(null, "mandar al servicio addReserva " + codPrenda + " id_talla " + idTalla);
                    //dsProbador= new DSProbador(context.getApplicationContext());
                    dsProbador.reservarPrenda(codPrenda, idTalla);

                    addReserva.setText("Procesando reserva");
                    addReserva.setTextColor(Color.GRAY);
                    addReserva.setEnabled(false);
                    addReserva.postInvalidate();

                }

                break;
            case R.id.info_detalle:
                info.setChecked(true);
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

        Log.e("entro","setvisibilidad "+responseTallasDisponibles.getSuccess());
        if (responseTallasDisponibles.getSuccess()==1){
            Log.e("talla0Full", responseTallasDisponibles.getTallas().get(0).getTalla());
            ArrayList<TallaDisponible> tallas1=responseTallasDisponibles.getTallas();
            //max 4 tallas por no deforma la interfaz
            int size=tallas1.size();
            boolean tallaStandar =false;
            if (size==1 && tallas1.get(0).getTalla().equals("Standar")){
                Log.e("null","change dimen size talla "+tallas1.get(0).getTalla());
                tallaStandar=true;
            }
            /*}else{*/
                if (size>4){
                    size=4;
                }
                limpiarToggleButtons(size);
                Log.e("tamaño", size + "");
                idTallas=new ArrayList<String>();
                for (int i=0;i<size;i++){
                    idTallas.add(tallas1.get(i).getId_talla());
                    tallasMax.get(i).setTextOff(tallas1.get(i).getTalla());
                    tallasMax.get(i).setTextOn(tallas1.get(i).getTalla());
                    Log.e("textoChan", String.valueOf(tallasMax.get(i).getTextOff()));
                    if (tallaStandar){
                        tallasMax.get(i).setBackgroundDrawable(getResources().getDrawable(R.drawable.talla_prenda_standar));
                        tallasMax.get(i).setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        tallasMax.get(i).setTextSize(20);
                    }
                    tallasMax.get(i).setVisibility(View.VISIBLE);
                    tallasMax.get(i).setChecked(tallasMax.get(i).isChecked());
                }


        }
    }
    @Subscribe
    public void confirmacionReservarPrenda(DSProbador.ResponseReservarPrenda responseReservarPrenda){
        Log.e(null,"SE RESERVO "+responseReservarPrenda.getSuccess());
        if (responseReservarPrenda.getSuccess()==1) {
            addReserva.setText("Ya esta en reserva");
            addReserva.setTextColor(Color.GRAY);
            addReserva.setEnabled(false);
            addReserva.postInvalidate();
            MessageUtil.showToast(context,"Reserva de Prenda Satisfactoria");
        } else {
            addReserva.setText("Agregar a Reserva");
            addReserva.setTextColor(context.getResources().getColor(R.color.celeste_glup));
            addReserva.setEnabled(true);
            addReserva.postInvalidate();
        }
    }


    private void limpiarToggleButtons(int size) {
        int i=0;
        switch (size){
            case 0:
                for (ToggleButton toggleButton:tallasMax){
                    toggleButton.setVisibility(View.GONE);
                }
                break;
            case 1:
                i=0;
                for (ToggleButton toggleButton:tallasMax){
                     if (i==(size-1)){
                         toggleButton.setVisibility(View.VISIBLE);
                     }else{
                         toggleButton.setVisibility(View.GONE);
                     }
                    i++;
                }
                break;
            case 2:
                i=0;
                for (ToggleButton toggleButton:tallasMax){
                    if (i<=(size-1)){
                        toggleButton.setVisibility(View.VISIBLE);
                    }else{
                        toggleButton.setVisibility(View.GONE);
                    }
                    i++;
                }
                break;
            case 3:
                i=0;
                for (ToggleButton toggleButton:tallasMax){
                    if (i<=(size-1)){
                        toggleButton.setVisibility(View.VISIBLE);
                    }else{
                        toggleButton.setVisibility(View.GONE);
                    }
                    i++;
                }
                break;
            default:break;
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


}
