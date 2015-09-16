package pe.com.glup.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;

import pe.com.glup.R;
import pe.com.glup.adapters.PrendaAdapter;
import pe.com.glup.beans.Prenda;
import pe.com.glup.datasource.DSCloset;
import pe.com.glup.dialog.GlupDialog;
import pe.com.glup.glup.Detalle;
import pe.com.glup.glup.Glup;
import pe.com.glup.glup.Principal;
import pe.com.glup.interfaces.OnSearchListener;
import pe.com.glup.interfaces.OnSuccessCatalogo;
import pe.com.glup.interfaces.OnSuccessUpdate;
import pe.com.glup.utils.Util_Fonts;


public class FClosetGrilla extends Fragment implements OnSuccessCatalogo,
        OnSearchListener,
        AbsListView.OnScrollListener,
        AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int EMPTY = 0;
    private static final int FULL = 1;
    private FCloset context;
    protected GlupDialog gd;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView emptyView;
    private GridView grilla;
    private PrendaAdapter adapter;
    private DSCloset dsCloset;

    private Glup glup;

    private static int PAGE = 1;
    private static String TAG = "todos";
    private boolean isLoading = false;

    private OnFragmentInteractionListener mListener;

    // TODO: Rename and change types and number of parameters
    public static FClosetGrilla newInstance(String param1, String param2) {
        FClosetGrilla fragment = new FClosetGrilla();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public FClosetGrilla(FCloset context){
        this.context=context;
    }
    public FClosetGrilla() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        glup = (Glup) context.getActivity();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fcatalogo, container, false);
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        PAGE = 1;
        TAG = "todos";
        isLoading = false;

        Principal principal = ((Principal) getActivity());
        try{
            principal.getHeader().setOnSearchListener(FClosetGrilla.this);
        }catch (ClassCastException e){
            Log.e("error",e.toString());
        }

        emptyView = (TextView) getView().findViewById(R.id.empty_view);
        emptyView.setTypeface(Util_Fonts.setRegular(getActivity()));

        grilla = (GridView) getView().findViewById(R.id.grilla_prendas);
        grilla.setOnItemLongClickListener(this);
        grilla.setOnItemClickListener(this);
        grilla.setOnScrollListener(this);

        dsCloset = new DSCloset(getActivity());
        dsCloset.getUsuarioPrendas(TAG, String.valueOf(PAGE), "10");
        try{
            dsCloset.setOnSuccessCatalogo(FClosetGrilla.this);//context FCloset
        }catch (ClassCastException e){
            Log.e("error",e.toString());
        }


        /*
        SHOW LOAD DIALOG
         */
        //dec1
        gd = new GlupDialog(getActivity());
        gd.setCancelable(false);
        gd.show();


    }




    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
        Prenda prenda = (Prenda) parent.getItemAtPosition(position);
        dsCloset = new DSCloset(getActivity());
        dsCloset.updateProbador(glup.getPrendas().get(position).getCod_prenda());
        dsCloset.setOnSuccessUpdate(new OnSuccessUpdate() {
            @Override
            public void onSuccesUpdate(boolean status, int indProb) {
                if (status) {
                    if (indProb == 0) {
                        ((CheckBox) view.findViewById(R.id.check)).setChecked(false);
                    } else if (indProb == 1) {
                        ((CheckBox) view.findViewById(R.id.check)).setChecked(true);
                    }
                } else {
                    Log.e(FCloset.class.getName(), "Ocurrio un error");
                }
            }
        });
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        ArrayList<Prenda> prendas = glup.getPrendas();
        Log.e("prendas", prendas.size() + "");
        Intent intent = new Intent(glup, Detalle.class);
        intent.putExtra("prendas", prendas);
        intent.putExtra("current", position);
        startActivity(intent);
        return false;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0) {
            if (!isLoading) {
                isLoading = true;

                PAGE++;
                dsCloset.getUsuarioPrendas(TAG, String.valueOf(PAGE), "10");
                try{
                    dsCloset.setOnSuccessCatalogo(FClosetGrilla.this);//context FCloset
                }catch (ClassCastException e){
                    Log.e("error",e.toString());
                }

            }
        }
    }

    @Override
    public void onSearchListener(String cadena) {
        PAGE = 1;
        if (cadena.equals("")) {
            TAG = "todos";
        } else {
            TAG = cadena;
        }

        dsCloset.getUsuarioPrendas(TAG, String.valueOf(PAGE), "10");
        try{
            dsCloset.setOnSuccessCatalogo(FClosetGrilla.this);//context FCloset
        }catch (ClassCastException e){
            Log.e("error",e.toString());
        }

    }

    @Override
    public void onSuccess(String success_msg, ArrayList<Prenda> prendas) {
        gd.dismiss();
        try {
            if (PAGE == 1) {
                if (prendas != null) {
                    displayMessage(FULL);
                    adapter = new PrendaAdapter(getActivity(), prendas);
                    grilla.setAdapter(adapter);
                    glup.setPrendas(adapter.getmPrendas());
                    isLoading = false;
                } else {
                    displayMessage(EMPTY);
                }
            } else if (PAGE != 1) {
                if (prendas != null) {
                    displayMessage(FULL);
                    if (!prendas.isEmpty()) {
                        for (int i = 0; i < prendas.size(); i++) {
                            adapter.add(prendas.get(i));
                        }
                    }
                    glup.setPrendas(adapter.getmPrendas());
                    isLoading = false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailed(String error_msg) {
        gd.dismiss();
        displayMessage(EMPTY);
    }

    private void displayMessage(int type) {
        if (type == EMPTY) {
            grilla.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else if (type == FULL) {
            grilla.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }



    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
