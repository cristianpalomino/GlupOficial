package pe.com.glup.fragments;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import pe.com.glup.R;
import pe.com.glup.beans.Prenda;
import pe.com.glup.glup.Glup;
import pe.com.glup.glup.Principal;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FDetalle#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FDetalle extends Fragment {

    private static final String DATA = "data";
    private Prenda prenda;

    private Glup glup;

    private ImageView btninfo;
    private ImageView imgprenda;
    private TextView precio;
    private TextView marca;

    private View header_detail;

    public static FDetalle newInstance(Prenda data) {
        FDetalle fragment = new FDetalle();
        Bundle args = new Bundle();
        args.putSerializable(DATA, data);
        fragment.setArguments(args);
        return fragment;
    }

    public FDetalle() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        glup = (Glup) getActivity();
        Principal principal = (Principal) glup;
        header_detail = principal.getHeader().showOptionsDetail();

        if (getArguments() != null) {
            prenda = getArguments().getParcelable(DATA);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detalle, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        btninfo = (ImageView) getView().findViewById(R.id.btn_info);
        imgprenda = (ImageView) getView().findViewById(R.id.imagen_prenda);
        precio = (TextView) getView().findViewById(R.id.precio_prenda);
        marca = (TextView) header_detail.findViewById(R.id.marca_prenda);

        Drawable placeholder = getResources().getDrawable(R.drawable.ic_panorama_white_48dp);
        DrawableCompat.setTint(DrawableCompat.wrap(placeholder), android.R.color.darker_gray);

        Picasso.with(glup).load(prenda.getImagen()).fit().placeholder(placeholder).into(imgprenda);


        //---- Las variables ya se setean en Detalle.java (Metodo reload) ----------
        //precio.setText("S/. " + prenda.getPrecio() + ".00");
        //marca.setText(prenda.getMarca());


    }
}
