package pe.com.glup.glup;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import pe.com.glup.R;
import pe.com.glup.beans.Prenda;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_detalle);

        current_position = getIntent().getIntExtra("current", 0);
        prendas = (ArrayList<Prenda>) getIntent().getSerializableExtra("prendas");

        btninfo = (ImageView) findViewById(R.id.btn_info);
        imgprenda = (ImageView) findViewById(R.id.imagen_prenda);

        next = (ImageView) findViewById(R.id.btnnext);
        prev = (ImageView) findViewById(R.id.btnprev);

        precio = (TextView) findViewById(R.id.precio_prenda);
        marca = (TextView) findViewById(R.id.marca_prenda);

        tallaGroup = (RadioGroup) findViewById(R.id.talla_group);

        prev.setOnClickListener(this);
        next.setOnClickListener(this);

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
            if (current_position != prendas.size()-1) {
                current_position++;
                reload(prendas.get(current_position));
            }
        }
    }

    private void reload(Prenda prenda) {
        Picasso.with(this).load(prenda.getImagen()).fit().placeholder(R.drawable.progress_animator).noFade().into(imgprenda);
        precio.setText("S/. " + prenda.getPrecio() + ".00");
        marca.setText(prenda.getMarca() + " - " + current_position + " - " + prendas.size());

        tallaGroup.removeAllViews();
        ArrayList<String> tallas = prenda.getTalla();
        for (int i = 0; i < tallas.size(); i++) {
            RadioButton view_talla = new RadioButton(this);
            view_talla.setText(tallas.get(i).toString());
            tallaGroup.addView(view_talla);
        }
    }
}
