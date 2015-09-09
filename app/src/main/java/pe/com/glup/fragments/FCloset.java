package pe.com.glup.fragments;


import android.app.Activity;
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
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.security.auth.login.LoginException;

import de.hdodenhof.circleimageview.CircleImageView;
import pe.com.glup.R;
import pe.com.glup.adapters.PrendaAdapter;
import pe.com.glup.beans.Prenda;
import pe.com.glup.datasource.DSCloset;
import pe.com.glup.dialog.GlupDialog;
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
        AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener, View.OnClickListener {

    private CircleImageView fotoPerfil;
    private FragmentIterationListener mCallback = null;


    public interface FragmentIterationListener{
        public void onFragmentIteration(Bundle parameters);
    }



    private static final int EMPTY = 0;
    private static final int FULL = 1;

    private Glup glup;
    private static int PAGE = 1;
    private static String TAG = "todos";

    private DSCloset dsCloset;
    private PrendaAdapter adapter;
    protected GlupDialog gd;

    private TextView emptyView;
    private GridView grilla;
    private LinearLayout perfil;
    private EditText cumpleanos;

    private boolean isLoading = false;

    public static FCloset newInstance() {
        FCloset fragment = new FCloset();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            mCallback = (FragmentIterationListener) activity;
        }catch(Exception ex){
            Log.e("ExampleFragment", "El Activity debe implementar la interfaz FragmentIterationListener");
        }
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
        return inflater.inflate(R.layout.fragment_closet, container, false);
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
        principal.setupUI(getView().findViewById(R.id.frame_profile));

        fotoPerfil = (CircleImageView) getView().findViewById(R.id.photo);

        emptyView = (TextView) getView().findViewById(R.id.empty_view);
        emptyView.setTypeface(Util_Fonts.setRegular(getActivity()));

        grilla = (GridView) getView().findViewById(R.id.grilla_prendas);
        perfil = (LinearLayout) getView().findViewById(R.id.profile);
        cumpleanos = (EditText) getView().findViewById(R.id.cumpleanos);
        //SimpleDateFormat sdf = new SimpleDateFormat( "yyyy/MM/dd" );
        //cumpleanos.setText(DateFormat.getDateInstance().format(new Date()));
        grilla.setVisibility(View.VISIBLE);
        perfil.setVisibility(View.GONE);

        /*grilla.setVisibility(View.VISIBLE);
        perfil.setVisibility(View.GONE);*/
        /*
        grilla.setOnItemLongClickListener(null);
        grilla.setOnItemSelectedListener(null);
        grilla.setOnScrollListener(null);
        */

        fotoPerfil.setOnClickListener(this);

        grilla.setOnItemLongClickListener(this);
        grilla.setOnItemClickListener(this);
        grilla.setOnScrollListener(this);

        /*
        CALL API REST
         */
        dsCloset = new DSCloset(getActivity());
        dsCloset.getUsuarioPrendas(TAG, String.valueOf(PAGE), "10");
        dsCloset.setOnSuccessCatalogo(FCloset.this);

        /*
        SHOW LOAD DIALOG
         */
        gd = new GlupDialog(getActivity());
        gd.setCancelable(false);
        gd.show();
    }
    public void onDetach() {
        mCallback = null;
        super.onDetach();
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
        Log.e("prendas", prendas.size() + "");
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


    @Override
    public void onClick(View v) {
        grilla.setVisibility(View.GONE);
        perfil.setVisibility(View.VISIBLE);
        /*Bundle bundle = new Bundle();
        bundle.putString("datos", "datos que necesito");
        mCallback.onFragmentIteration(bundle);*/

    }


}
