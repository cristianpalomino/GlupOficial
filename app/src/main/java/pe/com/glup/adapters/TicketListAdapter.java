package pe.com.glup.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import pe.com.glup.R;

/**
 * Created by Glup on 2/10/15.
 */
public class TicketListAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;

        if (convertView == null){
            convertView = inflater.inflate(R.layout.item_ticket,parent,false);
            holder = new Holder();
            holder.code = (TextView) convertView.findViewById(R.id.code_ticket);
            holder.precio = (TextView) convertView.findViewById(R.id.total_ticket);

            //holder.code.setText();
            //holder.precio.setText();
            convertView.setTag(holder);
        }else {
            holder = (Holder) convertView.getTag();
        }

        return convertView;
    }

    class Holder {
        TextView code;
        TextView precio;
    }
}
