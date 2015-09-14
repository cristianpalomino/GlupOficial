package pe.com.glup.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import pe.com.glup.R;
import pe.com.glup.beans.DatoUser;
import pe.com.glup.beans.DetalleUser;

/**
 * Created by Glup on 10/09/15.
 */
public class DetalleUserAdapter extends BaseAdapter {
    private ArrayList<DatoUser> datoUser = new ArrayList<DatoUser>();
    private ArrayList<DetalleUser> detalleUser = new ArrayList<DetalleUser>();
    private Context context;
    private LayoutInflater inflater;

    public DetalleUserAdapter(ArrayList<DatoUser> datoUser, ArrayList<DetalleUser> detalleUser, Context context) {
        this.datoUser = datoUser;
        this.detalleUser = detalleUser;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return detalleUser.size();
    }

    @Override
    public Object getItem(int position) {
        return detalleUser.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        DatoUser dato = datoUser.get(position);
        DetalleUser detalle = detalleUser.get(position);


        holder= new Holder();
        holder.foto = (CircleImageView) convertView.findViewById(R.id.photo);
        holder.username = (TextView) convertView.findViewById(R.id.username);
        holder.cantPrendas = (TextView) convertView.findViewById(R.id.cantPrendas);

        holder.nombreCompleto = (EditText) convertView.findViewById(R.id.nombres);
        holder.cumpleanos = (EditText) convertView.findViewById(R.id.cumpleanos);
        holder.correo = (EditText) convertView.findViewById(R.id.correo);
        holder.telefono = (EditText) convertView.findViewById(R.id.telefono);


        Picasso.with(context).load(dato.getRutaFoto()).fit()
                .placeholder(R.drawable.progress_animator)
                .centerInside()
                .noFade()
                .into(holder.foto);
        holder.username.setText(dato.getNomUser() + " " + dato.getApeUser());
        holder.cantPrendas.setText(dato.getNumPrend());

        holder.nombreCompleto.setText(detalle.getNomUser() + " " + detalle.getApeUser());
        holder.cumpleanos.setText(detalle.getFecNac());
        holder.correo.setText(detalle.getCorreoUser());
        holder.telefono.setText(detalle.getNumTelef());


        return null;
    }

    class Holder {
        CircleImageView foto;
        TextView username;
        TextView cantPrendas;
        EditText nombreCompleto;
        EditText cumpleanos;
        EditText correo;
        EditText telefono;
        EditText contrasena;

    }
    public void updateDetalle(){

    }

    public ArrayList<DatoUser> getDatoUser() {
        return datoUser;
    }

    public ArrayList<DetalleUser> getDetalleUser() {
        return detalleUser;
    }
}
