package pe.com.glup.glup;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import pe.com.glup.R;
import pe.com.glup.beans.Prenda;
import pe.com.glup.dialog.DetalleDialog;
import pe.com.glup.utils.Util_Fonts;

/**
 * Created by Glup on 2/07/15.
 */
public class Detalle extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<Prenda> prendas;
    private int current_position;

    private ImageView btninfo;
    private ImageView imgprenda;
    private TextView precio;
    private TextView marca;

    private ImageView next;
    private ImageView prev;

    private RadioGroup tallaGroup;

    private Button btnprobar;
    private Button btnreservar;



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

        precio.setTypeface(Util_Fonts.setRegular(this));
        marca.setTypeface(Util_Fonts.setBold(this));
        btnprobar.setTypeface(Util_Fonts.setBold(this));
        btnreservar.setTypeface(Util_Fonts.setBold(this));

        tallaGroup = (RadioGroup) findViewById(R.id.talla_group);

        prev.setOnClickListener(this);
        next.setOnClickListener(this);
        btninfo.setOnClickListener(this);

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
        } else if(v.equals(btninfo)){
            DetalleDialog dialog = new DetalleDialog(Detalle.this);
            View root =getLayoutInflater().inflate(
                    (R.layout.fragment_detalle),null);

            dialog.show();
        }
    }

    private void reload(Prenda prenda) {
        Picasso.with(this).load(prenda.getImagen()).fit().placeholder(R.drawable.progress_animator).noFade().into(imgprenda);
        if(prenda.getPrecio()==null){
        precio.setVisibility(View.GONE);
        }
        precio.setText("S/. " + prenda.getPrecio() + ".00");
        marca.setText(prenda.getMarca() + " - " + current_position + " - " + prendas.size());
        tallaGroup.removeAllViews();

        /*
        ArrayList<String> tallas = prenda.getTalla();
        for (int i = 0; i < tallas.size(); i++) {
            RadioButton view_talla = new RadioButton(this);
            view_talla.setText(tallas.get(i).toString());
            view_talla.setTypeface(Util_Fonts.setRegular(Detalle.this));
            tallaGroup.addView(view_talla);
        }
        */
    }
}
