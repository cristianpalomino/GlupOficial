package pe.com.glup.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import pe.com.glup.R;
import pe.com.glup.utils.Util_Fonts;


/**
 * A placeholder fragment containing a simple view.
 */
public class Fragment_Tutorial extends Fragment {

    protected TextView title;
    protected String msg;


    public static final Fragment_Tutorial newInstance(String msg) {
        Fragment_Tutorial frag = new Fragment_Tutorial();
        Bundle bundle = new Bundle();
        bundle.putString("msg", msg);
        frag.setArguments(bundle);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        msg = getArguments().getString("msg");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.catalogo, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        title = (TextView) getView().findViewById(R.id.txtmensaje);
        title.setTypeface(Util_Fonts.setBold(getActivity()));
        title.setText(msg);
    }
}
