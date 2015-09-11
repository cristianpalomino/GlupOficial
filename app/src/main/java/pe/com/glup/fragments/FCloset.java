package pe.com.glup.fragments;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.security.auth.login.LoginException;

import de.hdodenhof.circleimageview.CircleImageView;
import pe.com.glup.R;
import pe.com.glup.adapters.DetalleUserAdapter;
import pe.com.glup.adapters.PrendaAdapter;
import pe.com.glup.beans.DatoUser;
import pe.com.glup.beans.DetalleUser;
import pe.com.glup.beans.Prenda;
import pe.com.glup.beans.Usuario;
import pe.com.glup.datasource.DSCloset;
import pe.com.glup.datasource.DSRegistro;
import pe.com.glup.datasource.DSUsuario;
import pe.com.glup.dialog.GlupDialog;
import pe.com.glup.glup.Detalle;
import pe.com.glup.glup.Glup;
import pe.com.glup.glup.Principal;
import pe.com.glup.interfaces.OnSearchListener;
import pe.com.glup.interfaces.OnSuccesUpdate;
import pe.com.glup.interfaces.OnSuccessCatalogo;
import pe.com.glup.interfaces.OnSuccessDetalleUsuario;
import pe.com.glup.utils.DatePickerFragment;
import pe.com.glup.utils.Util_Fonts;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FCloset#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FCloset extends Fragment implements OnSuccessCatalogo,
        OnSuccessDetalleUsuario,
        OnSearchListener,
        AbsListView.OnScrollListener,
        AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener, View.OnClickListener {

    private CircleImageView fotoPerfil;
    private TextView username,cantPrendas;
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
    private DSUsuario dsUsuario;
    private PrendaAdapter adapter;
    private DetalleUserAdapter adapterUser;
    protected GlupDialog gd;

    private TextView emptyView;
    private GridView grilla;
    private LinearLayout perfil;

    private EditText nombreCompleto;
    private EditText cumpleanos;
    private EditText correo;
    private EditText telefono;
    private EditText contrasena;
    private Button updateProfile;


    private boolean isLoading = false;

    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            cumpleanos.setText(String.valueOf(dayOfMonth) + "-" + String.valueOf(monthOfYear+1)
                    + "-" + String.valueOf(year));
        }
    };

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
        username = (TextView)getView().findViewById(R.id.username);
        cantPrendas = (TextView) getView().findViewById(R.id.cantPrendas);

        emptyView = (TextView) getView().findViewById(R.id.empty_view);
        emptyView.setTypeface(Util_Fonts.setRegular(getActivity()));

        grilla = (GridView) getView().findViewById(R.id.grilla_prendas);
        perfil = (LinearLayout) getView().findViewById(R.id.profile);

        nombreCompleto = (EditText) getView().findViewById(R.id.nombreCompleto);
        cumpleanos = (EditText) getView().findViewById(R.id.cumpleanos);
        correo = (EditText) getView().findViewById(R.id.correo);
        telefono = (EditText) getView().findViewById(R.id.telefono);
        contrasena = (EditText)getView().findViewById(R.id.contrasena);
        updateProfile = (Button)getView().findViewById(R.id.updateProfile);



        cumpleanos.setOnClickListener(this);
        updateProfile.setOnClickListener(this);

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

        dsUsuario = new DSUsuario(getActivity());
        dsUsuario.setOnSuccessDetalleUsuario(FCloset.this);
        dsUsuario.loadUsuario();

        /*
        SHOW LOAD DIALOG
         */
        gd = new GlupDialog(getActivity());
        gd.setCancelable(false);
        gd.show();
    }
    @Override
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
    public void onSuccess(String success_msg, ArrayList<DetalleUser> detalleuser, ArrayList<DatoUser> datouser) {
        if (success_msg.equals("1")){
            Log.e("coneccion","hecha");
            try{
               // adapterUser= new DetalleUserAdapter(datouser,detalleuser,getActivity());
                DatoUser dato = datouser.get(0);
                DetalleUser detalle = detalleuser.get(0);

                Picasso.with(getActivity().getApplicationContext()).load(dato.getRutaFoto()).fit()
                        .placeholder(R.drawable.progress_animator)
                        .centerInside()
                        .noFade()
                        .into( fotoPerfil);
                 username.setText(dato.getNomUser() + " " + dato.getApeUser());
                 cantPrendas.setText(dato.getNumPrend());

                 nombreCompleto.setText(detalle.getNomUser() + " " + detalle.getApeUser());
                 cumpleanos.setText(detalle.getFecNac());
                 correo.setText(detalle.getCorreoUser());
                 telefono.setText(detalle.getNumTelef());
            }catch (Exception e){
                e.printStackTrace();
            }

        }else {
            Log.e("coneccion","no hecha");
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
        switch (v.getId()){
            case R.id.photo:
                grilla.setVisibility(View.GONE);
                perfil.setVisibility(View.VISIBLE);
                break;
            case R.id.cumpleanos:
                showDatePicker();
                break;
            case R.id.updateProfile:
                break;
        }

        /*Bundle bundle = new Bundle();
        bundle.putString("datos", "datos que necesito");
        mCallback.onFragmentIteration(bundle);*/

    }
    private void showDatePicker() {
        DatePickerFragment date = new DatePickerFragment();
        /**
         * Set Up Current Date Into dialog
         */
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        /**
         * Set Call back to capture selected date
         */
        date.setCallBack(ondate);
        date.show(getFragmentManager(), "Date Picker");
    }



}
