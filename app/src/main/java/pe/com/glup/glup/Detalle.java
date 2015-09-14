package pe.com.glup.glup;


import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import pe.com.glup.R;
import pe.com.glup.adapters.Adapter_tallas;
import pe.com.glup.beans.Prenda;
import pe.com.glup.datasource.DSCatalogo;
import pe.com.glup.dialog.DetalleDialog;
import pe.com.glup.fragments.FDetalle;
import pe.com.glup.interfaces.OnSuccessUpdate;
import pe.com.glup.interfaces.OnSuccessPrenda;
import pe.com.glup.utils.Util_Fonts;

/**
 * Created by Glup on 2/07/15.
 */
//public class Detalle extends AppCompatActivity implements View.OnClickListener,OnsuccesReserva{
public class Detalle extends AppCompatActivity implements View.OnClickListener,OnSuccessUpdate,OnSuccessPrenda {

    private ArrayList<Prenda> prendas;
    private int current_position;

    private ImageView btninfo;
    private ImageView imgprenda;
    private TextView precio;
    private TextView marca;
    private TextView prueba;

    private ImageView next;
    private ImageView prev;

    private RadioGroup tallaGroup;

    private Button btnprobar;
    private Button btnreservar;

    private ViewPager paginador;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_detalle);

        btnprobar = (Button) findViewById(R.id.btnprobar);
        btnreservar = (Button) findViewById(R.id.btnreservar);

        current_position = getIntent().getIntExtra("current", 0);
        prendas = (ArrayList<Prenda>) getIntent().getSerializableExtra("prendas");

        btninfo = (ImageView) findViewById(R.id.btn_info);
        imgprenda = (ImageView) findViewById(R.id.imagen_prenda);

        next = (ImageView) findViewById(R.id.btnnext);
        prev = (ImageView) findViewById(R.id.btnprev);

        precio = (TextView) findViewById(R.id.precio_prenda);
        marca = (TextView) findViewById(R.id.marca_prenda);
        paginador=(ViewPager) findViewById(R.id.ptallas);

        precio.setTypeface(Util_Fonts.setRegular(this));
        marca.setTypeface(Util_Fonts.setBold(this));
        btnprobar.setTypeface(Util_Fonts.setBold(this));
        btnreservar.setTypeface(Util_Fonts.setBold(this));

        tallaGroup = (RadioGroup) findViewById(R.id.talla_group);

        prev.setOnClickListener(this);
        next.setOnClickListener(this);
        btninfo.setOnClickListener(this);
        btnprobar.setOnClickListener(this);
        btnreservar.setOnClickListener(this);

        reload(prendas.get(current_position));
    }

    @Override
    public void onClick(View v) {
        if (v.equals(prev)) {
            if (current_position != 0) {
                current_position--;
                reload(prendas.get(current_position));
            }
        } else if (v.equals(next)) {
            if (current_position != prendas.size() - 1) {
                current_position++;
                reload(prendas.get(current_position));
            }
        }else if(v.equals(btninfo)){
            DetalleDialog dialog = new DetalleDialog(Detalle.this);
            dialog.setCodigo_prenda(prendas.get(current_position).getCod_prenda());
            dialog.initDialog();
            dialog.show();
        }else if(v.equals(btnprobar)){
            //Toast.makeText(Detalle.this,"Boton Probar",Toast.LENGTH_LONG).show();
            DSCatalogo dsCatalogo = new DSCatalogo(Detalle.this);
            dsCatalogo.updateProbador(prendas.get(current_position).getCod_prenda());
            dsCatalogo.setOnSuccessUpdate(Detalle.this);
        }
        else if(v.equals(btnreservar)){
            Toast.makeText(Detalle.this,"Boton Reservar Presionado",Toast.LENGTH_LONG).show();
            // *** A Modificar ***
            //DSInfo dsInfo= new DSInfo(Detalle.this);
            //dsInfo.getReservaPrenda(prendas.get(current_position).getCod_prenda(), prendas.get(current_position).getNombre());
            //dsInfo.setOnSuccesPrenda(Detalle.this);
        }
    }

    private void reload(Prenda prenda) {
        Picasso.with(this).load(prenda.getImagen()).fit().placeholder(R.drawable.progress_animator).noFade().into(imgprenda);
        if(prenda.getPrecio()==null){
        precio.setVisibility(View.GONE);
        btnreservar.setVisibility(View.GONE);
        }
        precio.setText("S/. " + prenda.getPrecio() + ".00");
        marca.setText(prenda.getMarca() + " - " + current_position + " - " + prendas.size());
        tallaGroup.removeAllViews();

        try{
            ArrayList<String> tallas = prenda.getTalla();
            for (int i = 0; i < tallas.size(); i++) {
                RadioButton view_talla = new RadioButton(this);
                view_talla.setText(tallas.get(i).toString());
                view_talla.setTypeface(Util_Fonts.setRegular(Detalle.this));
                tallaGroup.addView(view_talla);
            }
        }
        catch (Exception e){
            Log.e(Detalle.class.getName(), "Ocurrio un error");
        }


    }

    public void onSuccesUpdate(boolean status, int indProb) {
        if (status) {
            if (indProb == 0) {
                Toast.makeText(Detalle.this,"Se elimino la prenda del probador",Toast.LENGTH_LONG).show();
            } else if (indProb == 1) {
                Toast.makeText(Detalle.this,"Se agrego la prenda al probador ",Toast.LENGTH_LONG).show();
            }
        } else {
            Log.e(Detalle.class.getName(), "Ocurrio un error");
        }


    }


    @Override
    public void onSuccessPrenda(boolean status, int indProb) {
        if (status) {
            if (indProb == 0) {
                Toast.makeText(Detalle.this,"Se reservo la prenda ",Toast.LENGTH_LONG).show();
            } else if (indProb == 1) {
                Toast.makeText(Detalle.this,"Se quito la reserva",Toast.LENGTH_LONG).show();
            }
        } else {
            Log.e(Detalle.class.getName(), "Ocurrio un error");
        }
    }

    @Override
    public void onFailed(String error_msg) {

    }

    public class Detail extends ActionBarActivity{

        String[] ropa = {
                "s",
                "m",
                "l",
                "xl"
        };

        ViewPager mViewPager;
        Adapter_tallas manejotallas;


        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.fragment_detalle);

            mViewPager = (ViewPager) findViewById(R.id.ptallas);
            manejotallas = new Adapter_tallas(getSupportFragmentManager());
            manejotallas.agregarFragmentos(FDetalle.instantiate(this,ropa[0]));
            manejotallas.agregarFragmentos(FDetalle.instantiate(this,ropa[1]));
            manejotallas.agregarFragmentos(FDetalle.instantiate(this,ropa[2]));
            manejotallas.agregarFragmentos(FDetalle.instantiate(this,ropa[3]));

            mViewPager.setAdapter(manejotallas);

        }
    }
}
