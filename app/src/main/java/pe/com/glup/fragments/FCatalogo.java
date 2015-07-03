package pe.com.glup.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import pe.com.glup.datasource.DSCatalogo;
import pe.com.glup.glup.Detalle;
import pe.com.glup.glup.Glup;
import pe.com.glup.glup.Principal;
import pe.com.glup.interfaces.OnSearchListener;
import pe.com.glup.interfaces.OnSuccesUpdate;
import pe.com.glup.interfaces.OnSuccessCatalogo;
import pe.com.glup.utils.Util_Fonts;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FCatalogo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FCatalogo extends Fragment implements OnSuccessCatalogo, OnSearchListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, AbsListView.OnScrollListener {

    private static final int EMPTY = 0;
    private static final int FULL = 1;

    private Glup glup;
    private static int PAGE = 1;
    private static String TAG = "todos";

    private DSCatalogo dsCatalogo;
    private PrendaAdapter adapter;

    private TextView emptyView;
    private GridView grilla;

    private boolean isLoading = false;

    public static FCatalogo newInstance() {
        FCatalogo fragment = new FCatalogo();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        glup = (Glup) getActivity();
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fcatalogo, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        PAGE = 1;
        TAG = "todos";

        Principal principal = ((Principal) getActivity());
        principal.getHeader().setOnSearchListener(FCatalogo.this);
        principal.setupUI(getView().findViewById(R.id.frame_grilla));

        emptyView = (TextView) getView().findViewById(R.id.empty_view);
        grilla = (GridView) getView().findViewById(R.id.grilla_prendas);
        emptyView.setTypeface(Util_Fonts.setRegular(getActivity()));

        grilla.setOnItemClickListener(this);
        grilla.setOnItemLongClickListener(this);
        grilla.setOnScrollListener(this);

        dsCatalogo = new DSCatalogo(getActivity());
        dsCatalogo.getGlobalPrendas(TAG, String.valueOf(PAGE), "10");
        dsCatalogo.setOnSuccessCatalogo(FCatalogo.this);
    }

    @Override
    public void onSuccess(String success_msg, ArrayList<Prenda> prendas) {
        if (PAGE == 1) {
            if (prendas != null) {
                displayMessage(FULL);
                adapter = new PrendaAdapter(getActivity(),prendas);
                grilla.setAdapter(adapter);
                glup.setPrendas(adapter.getmPrendas());
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
    }

    @Override
    public void onFailed(String error_msg) {
        displayMessage(EMPTY);
    }

    @Override
    public void onSearchListener(String cadena) {
        PAGE = 1;
        if (cadena.equals("")) {
            TAG = "todos";
        } else {
            TAG = cadena;
        }

        dsCatalogo.getGlobalPrendas(TAG, String.valueOf(PAGE), "10");
        dsCatalogo.setOnSuccessCatalogo(FCatalogo.this);
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

    @Override
    public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
        dsCatalogo = new DSCatalogo(getActivity());
        dsCatalogo.updateProbador(glup.getPrendas().get(position).getCod_prenda());
        dsCatalogo.setOnSuccesUpdate(new OnSuccesUpdate() {
            @Override
            public void onSuccesUpdate(boolean status, int indProb) {
                if (status) {
                    if (indProb == 0) {
                        ((CheckBox) view.findViewById(R.id.check)).setChecked(false);
                    } else if (indProb == 1) {
                        ((CheckBox) view.findViewById(R.id.check)).setChecked(true);
                    }
                } else {

                }
            }
        });
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        ArrayList<Prenda> prendas = glup.getPrendas();
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
                dsCatalogo.getGlobalPrendas(TAG, String.valueOf(PAGE), "10");
                dsCatalogo.setOnSuccessCatalogo(FCatalogo.this);
            }
        }
    }

    /**
     *
     PAGE++;
     dsCatalogo.getGlobalPrendas(TAG, String.valueOf(PAGE), "10");
     dsCatalogo.setOnSuccessCatalogo(FCatalogo.this);
     */
}
