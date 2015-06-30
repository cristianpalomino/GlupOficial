package pe.com.glup.fragments;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;

import java.util.ArrayList;

import pe.com.glup.R;
import pe.com.glup.adapters.PrendaAdapter;
import pe.com.glup.beans.Prenda;
import pe.com.glup.datasource.DSCatalogo;
import pe.com.glup.decorations.MarginDecoration;
import pe.com.glup.glup.Glup;
import pe.com.glup.glup.Principal;
import pe.com.glup.interfaces.OnSearchListener;
import pe.com.glup.interfaces.OnSuccessCatalogo;
import pe.com.glup.utils.Util_Fonts;
import pe.com.glup.views.Header;
import pe.com.glup.views.HidingScrollListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FCatalogo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FCatalogo extends Fragment implements OnSuccessCatalogo, OnSearchListener, UltimateRecyclerView.OnLoadMoreListener, PrendaAdapter.OnItemClickListener {

    private static final int EMPTY = 0;
    private static final int FULL = 1;

    private Glup glup;
    private static int PAGE = 1;
    private static String TAG = "todos";

    private UltimateRecyclerView recyclerView;
    private GridLayoutManager layoutManager;
    private DSCatalogo dsCatalogo;
    private PrendaAdapter adapter;
    private TextView emptyView;

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
        principal.setupUI(getView().findViewById(R.id.ultimate_recycler_view));

        recyclerView = (UltimateRecyclerView) getView().findViewById(R.id.ultimate_recycler_view);
        emptyView = (TextView) getView().findViewById(R.id.empty_view);

        recyclerView.addItemDecoration(new MarginDecoration(getActivity()));
        layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.enableLoadmore();
        recyclerView.setOnLoadMoreListener(FCatalogo.this);
        emptyView.setTypeface(Util_Fonts.setRegular(getActivity()));

        dsCatalogo = new DSCatalogo(getActivity());
        dsCatalogo.sendRequest(TAG, String.valueOf(PAGE), "10");
        dsCatalogo.setOnSuccessCatalogo(FCatalogo.this);
    }

    @Override
    public void onSuccess(String success_msg, ArrayList<Prenda> prendas) {
        if (PAGE == 1) {
            if (prendas != null) {
                displayMessage(FULL);
                adapter = new PrendaAdapter(prendas, getActivity());
                recyclerView.setAdapter(adapter);
                glup.setPrendas(adapter.getPrendas());
                adapter.setOnItemClickListener(FCatalogo.this);
            } else {
                displayMessage(EMPTY);
            }
        } else if (PAGE != 1) {
            if (prendas != null) {
                displayMessage(FULL);
                if (!prendas.isEmpty()) {
                    for (int i = 0; i < prendas.size(); i++) {
                        adapter.addPrenda(prendas.get(i));
                    }
                }
                glup.setPrendas(adapter.getPrendas());
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

        dsCatalogo.sendRequest(TAG, String.valueOf(PAGE), "10");
        dsCatalogo.setOnSuccessCatalogo(FCatalogo.this);
        recyclerView.enableLoadmore();
        recyclerView.setOnLoadMoreListener(FCatalogo.this);
    }

    @Override
    public void loadMore(int i, int i1) {
        PAGE++;
        dsCatalogo.sendRequest(TAG, String.valueOf(PAGE), "10");
        dsCatalogo.setOnSuccessCatalogo(FCatalogo.this);
    }

    private void displayMessage(int type) {
        if (type == EMPTY) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else if (type == FULL) {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        glup.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_principal,FDetalle.newInstance(glup.getPrendas().get(position)))
                .commit();
    }
}
