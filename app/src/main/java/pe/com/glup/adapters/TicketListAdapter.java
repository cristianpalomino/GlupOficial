package pe.com.glup.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import pe.com.glup.R;
import pe.com.glup.dialog.ConfirmationDelete;
import pe.com.glup.glup.Glup;
import pe.com.glup.models.TicketList;

/**
 * Created by Glup on 2/10/15.
 */
public class TicketListAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private ArrayList<TicketList> tickets;
    private Context context;

    public TicketListAdapter(ArrayList<TicketList> tickets, Context context, String tag) {
        this.tickets = tickets;
        this.context = context;
        this.inflater=LayoutInflater.from(context);
        this.tag = tag;
    }

    private String tag;


    @Override
    public int getCount() {
        return tickets.size();
    }

    @Override
    public Object getItem(int position) {
        return tickets.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        TicketList ticketItem = tickets.get(position);
        if (convertView == null){
            convertView = inflater.inflate(R.layout.item_ticket,parent,false);

            holder = new Holder();
            holder.code = (TextView) convertView.findViewById(R.id.code_ticket);
            holder.precio = (TextView) convertView.findViewById(R.id.total_ticket);
            holder.separador = (View) convertView.findViewById(R.id.separador_item_ticket);
            holder.tacho = (ImageView)convertView.findViewById(R.id.ticket_eliminar);

            if (position==getCount()-1){
                Log.e("elimino","ultimo separador ticket");
                holder.separador.setVisibility(View.GONE);
            }
            convertView.setTag(holder);
        }else {
            holder = (Holder) convertView.getTag();
        }

        holder.code.setText(tickets.get(position).getCodVenta());
        holder.precio.setText("S/." + tickets.get(position).getSumPrecio());

        holder.tacho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String codTicket= tickets.get(position).getCodVenta();
                ConfirmationDelete confirmationDeleteReserva=
                        new ConfirmationDelete(context,codTicket,tag);
                confirmationDeleteReserva.show(((Glup) context).getSupportFragmentManager(), ConfirmationDelete.class.getSimpleName());
                Log.e("eliminar", position + "");

                Log.e("infoTag",tag);

            }
        });

        return convertView;
    }

    class Holder {
        TextView code;
        TextView precio;
        View separador;
        ImageView tacho;
    }
}
