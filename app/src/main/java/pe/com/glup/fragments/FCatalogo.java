package pe.com.glup.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import pe.com.glup.R;
import pe.com.glup.adapters.PrendaAdapter2;
import pe.com.glup.models.Prenda;
import pe.com.glup.managers.bus.BusHolder;
import pe.com.glup.network.DSCatalogo;
import pe.com.glup.dialog.GlupDialogNew;
import pe.com.glup.glup.Glup;
import pe.com.glup.glup.Principal;
import pe.com.glup.models.interfaces.OnSearchListener;
import pe.com.glup.models.interfaces.OnSuccessCatalogo;
import pe.com.glup.utils.Util_Fonts;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FCatalogo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FCatalogo extends Fragment implements OnSuccessCatalogo,
        OnSearchListener,
        AbsListView.OnScrollListener, AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener{

    private static final int EMPTY = 0;
    private static final int FULL = 1;

    private Glup glup;
    private static int PAGE = 1;
    private static String TAG = "todos";

    private DSCatalogo dsCatalogo;
    private PrendaAdapter2 prendaAdapter;
    protected GlupDialogNew gd;

    private TextView emptyView,emptyViewSubtitle;
    private GridView grilla;

    private boolean isLoading = false;
    private Context context;
    private Button prueba;

    public static FCatalogo newInstance() {
        FCatalogo fragment = new FCatalogo();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public static FCatalogo newInstance(String TAG) {
        FCatalogo fragment = new FCatalogo();
        FCatalogo.TAG=TAG;
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
        BusHolder.getInstance().register(this);
        context=getActivity();
        PAGE = 1;
        //TAG = "todos";
        isLoading = false;

        Principal principal = ((Principal) getActivity());
        principal.getHeader().setOnSearchListener(FCatalogo.this);

        emptyView = (TextView) getView().findViewById(R.id.empty_view);
        emptyViewSubtitle = (TextView) getView().findViewById(R.id.empty_view_subtitle);

        emptyViewSubtitle.setTypeface(Util_Fonts.setRegular(getActivity()));

        grilla = (GridView) getView().findViewById(R.id.grilla_prendas);

        /*
        grilla.setOnItemLongClickListener(null);
        grilla.setOnItemSelectedListener(null);
        grilla.setOnScrollListener(null);
        */

        grilla.setOnItemLongClickListener(this);
        //grilla.setOnItemClickListener(this);
        grilla.setOnScrollListener(this);

        /*
        CALL REST API
         */
        dsCatalogo = new DSCatalogo(getActivity());
        dsCatalogo.getGlobalPrendas(TAG, String.valueOf(PAGE), "10");
        dsCatalogo.setOnSuccessCatalogo(FCatalogo.this);

        /*
        SHOW LOAD DIALOG
         */
        android.support.v4.app.FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
        gd = new GlupDialogNew();
        //gd.setCancelable(false);
        gd.show(fragmentManager,GlupDialogNew.class.getSimpleName());
    }

    @Override
    public void onSuccess(String success_msg, ArrayList<Prenda> prendas) {
        gd.dismiss();
        try {
            if (PAGE == 1) {
                if (prendas != null) {
                    displayMessage(FULL);
                    Log.e("tamaño:","prendas size"+prendas.size());
                    try{
                        prendaAdapter.notifyDataSetChanged();
                    }
                    catch(NullPointerException e)
                    {
                        prendaAdapter = new PrendaAdapter2(context, prendas);
                    }

                    grilla.setAdapter(prendaAdapter);
                    glup.setPrendas(prendaAdapter.getmPrendas());
                    for (Prenda pp:prendaAdapter.getmPrendas()){
                        //Log.e("FCatalogo",pp.toString());
                    }
                    isLoading = false;
                } else {
                    displayMessage(EMPTY);
                }
            } else if (PAGE != 1) {
                if (prendas != null) {
                    displayMessage(FULL);
                    if (!prendas.isEmpty()) {
                        for (int i = 0; i < prendas.size(); i++) {
                            prendaAdapter.add(prendas.get(i));
                        }
                    }
                    glup.setPrendas(prendaAdapter.getmPrendas());
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

    @Override
    public void onSearchListener(String cadena) {
        Log.e("null", "se ejecuta onSearchListner en Catalogo");
        PAGE = 1;
        if (cadena.equals("")) {
            TAG = "todos";
        } else {
            TAG = cadena;
        }

        //dsCatalogo.getGlobalPrendas(TAG, String.valueOf(PAGE), "10");
        //dsCatalogo.setOnSuccessCatalogo(FCatalogo.this);
    }

    private void displayMessage(int type) {
        if (type == EMPTY) {
            grilla.setVisibility(View.GONE);
            emptyViewSubtitle.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.VISIBLE);
        } else if (type == FULL) {
            grilla.setVisibility(View.VISIBLE);
            emptyViewSubtitle.setVisibility(View.GONE);
            emptyView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        Log.e("cargando","onscroll");
        if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0) {
            if (!isLoading) {
                isLoading = true;

                PAGE++;

                dsCatalogo.getGlobalPrendas(TAG, String.valueOf(PAGE), "10");

                dsCatalogo.setOnSuccessCatalogo(FCatalogo.this);
            }
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Log.e("longclick","en grilla");
        Log.e("Prenda:", "posicion " + position + " codigo:" +
                ((Prenda) parent.getItemAtPosition(position)).getCod_prenda() + " Tipo:" +
                ((Prenda) parent.getItemAtPosition(position)).getTipo());


        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
        /*Prenda prenda = (Prenda) parent.getItemAtPosition(position);
        dsCatalogo = new DSCatalogo(getActivity());
        dsCatalogo.updateProbador(glup.getPrendas().get(position).getCod_prenda());
        dsCatalogo.setOnSuccessUpdate(new OnSuccessUpdate() {
            @Override
            public void onSuccesUpdate(boolean status, int indProb) {
                if (status) {
                    if (indProb == 0) {
                        ((CheckBox) view.findViewById(R.id.check)).setChecked(false);
                    } else if (indProb == 1) {
                        ((CheckBox) view.findViewById(R.id.check)).setChecked(true);
                    }
                } else {
                    Log.e(FCatalogo.class.getName(), "Ocurrio un error");
                }
            }
        });*/
    }
    @Subscribe
    public void getIndProbador(String indProb) {
        try{
            Log.e("reload","contador");
            prendaAdapter.notifyDataSetChanged();
        }
        catch(NullPointerException e)
        {
            Log.e(null,"prendaAdapter en catalogo null-recarga de corazones fail");
        }

    }
}
