package pe.com.glup.fragments;


import android.content.Intent;
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
import android.widget.Toast;

import java.util.ArrayList;

import javax.security.auth.login.LoginException;

import pe.com.glup.R;
import pe.com.glup.adapters.PrendaAdapter;
import pe.com.glup.beans.Prenda;
import pe.com.glup.datasource.DSCloset;
import pe.com.glup.glup.Detalle;
import pe.com.glup.glup.Glup;
import pe.com.glup.glup.Principal;
import pe.com.glup.interfaces.OnSearchListener;
import pe.com.glup.interfaces.OnSuccesUpdate;
import pe.com.glup.interfaces.OnSuccessCatalogo;
import pe.com.glup.utils.Util_Fonts;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FCloset#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FCloset extends Fragment implements OnSuccessCatalogo,
        OnSearchListener,
        AbsListView.OnScrollListener,
        AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener {

    private static final int EMPTY = 0;
    private static final int FULL = 1;

    private Glup glup;
    private static int PAGE = 1;
    private static String TAG = "todos";

    private DSCloset dsCloset;
    private PrendaAdapter adapter;

    private TextView emptyView;
    private GridView grilla;

    private boolean isLoading = false;

    public static FCloset newInstance() {
        FCloset fragment = new FCloset();
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
        isLoading = false;

        Principal principal = ((Principal) getActivity());
        principal.getHeader().setOnSearchListener(FCloset.this);
        principal.setupUI(getView().findViewById(R.id.frame_grilla));

        emptyView = (TextView) getView().findViewById(R.id.empty_view);
        emptyView.setTypeface(Util_Fonts.setRegular(getActivity()));

        grilla = (GridView) getView().findViewById(R.id.grilla_prendas);

        /*
        grilla.setOnItemLongClickListener(null);
        grilla.setOnItemSelectedListener(null);
        grilla.setOnScrollListener(null);
        */

        grilla.setOnItemLongClickListener(this);
        grilla.setOnItemClickListener(this);
        grilla.setOnScrollListener(this);

        dsCloset = new DSCloset(getActivity());
        dsCloset.getUsuarioPrendas(TAG, String.valueOf(PAGE), "10");
        dsCloset.setOnSuccessCatalogo(FCloset.this);
    }

    @Override
    public void onSuccess(String success_msg, ArrayList<Prenda> prendas) {
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

        dsCloset.getUsuarioPrendas(TAG, String.valueOf(PAGE), "10");
        dsCloset.setOnSuccessCatalogo(FCloset.this);
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
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0) {
            if (!isLoading) {
                isLoading = true;

                PAGE++;
                dsCloset.getUsuarioPrendas(TAG, String.valueOf(PAGE), "10");
                dsCloset.setOnSuccessCatalogo(FCloset.this);
            }
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        ArrayList<Prenda> prendas = glup.getPrendas();
        Log.e("prendas",prendas.size()+"");
        Intent intent = new Intent(glup, Detalle.class);
        intent.putExtra("prendas", prendas);
        intent.putExtra("current", position);
        startActivity(intent);
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
        Prenda prenda = (Prenda) parent.getItemAtPosition(position);
        dsCloset = new DSCloset(getActivity());
        dsCloset.updateProbador(glup.getPrendas().get(position).getCod_prenda());
        dsCloset.setOnSuccesUpdate(new OnSuccesUpdate() {
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
}
