package pe.com.glup.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import pe.com.glup.R;
import pe.com.glup.beans.TicketList;

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
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        TicketList ticketItem = tickets.get(position);
        if (convertView == null){
            convertView = inflater.inflate(R.layout.item_ticket,parent,false);

            holder = new Holder();
            holder.code = (TextView) convertView.findViewById(R.id.code_ticket);
            holder.precio = (TextView) convertView.findViewById(R.id.total_ticket);
            holder.separador = (View) convertView.findViewById(R.id.separador_item_ticket);

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


        return convertView;
    }

    class Holder {
        TextView code;
        TextView precio;
        View separador;
    }
}
