package pe.com.glup.fragments;


import android.app.Activity;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import pe.com.glup.R;
import pe.com.glup.adapters.DetalleUserAdapter;
import pe.com.glup.adapters.PrendaAdapter;
import pe.com.glup.beans.DatoUser;
import pe.com.glup.beans.DetalleUser;
import pe.com.glup.datasource.DSCloset;
import pe.com.glup.datasource.DSUsuario;
import pe.com.glup.dialog.GlupDialog;
import pe.com.glup.glup.Glup;
import pe.com.glup.interfaces.OnSuccessDetalleUsuario;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FCloset#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FCloset extends Fragment implements View.OnClickListener,OnSuccessDetalleUsuario {

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

    private EditText nombres,apellidos,cumpleanos,correo,telefono,contrasena;
    private Button updateProfile,changePass;
    private String indVerPass;
    private FragmentManager fragmentManager;
    private String changeNombres,changeApellidos,changeCumpleanos,changeCorreo,changeTelefono;
    private String nuevaPassword="";
    private boolean changeToNewPass=false;
    private FClosetGrilla fClosetGrilla;
    private FClosetProfile fClosetProfile;


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

        fClosetGrilla = new FClosetGrilla(FCloset.this);
        fragmentManager= getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.grilla_o_perfil,fClosetGrilla);
        fragmentTransaction.commit();

        ////
        fotoPerfil = (CircleImageView) getView().findViewById(R.id.photo);
        username = (TextView)getView().findViewById(R.id.username);
        cantPrendas = (TextView) getView().findViewById(R.id.cantPrendas);


        //
        /*
        grilla.setOnItemLongClickListener(null);
        grilla.setOnItemSelectedListener(null);
        grilla.setOnScrollListener(null);
        */

        fotoPerfil.setOnClickListener(this);

        dsUsuario = new DSUsuario(getActivity());
        try{
            dsUsuario.setOnSuccessDetalleUsuario(FCloset.this);
        }catch (ClassCastException e){
            Log.e("error",e.toString());
        }

        dsUsuario.loadUsuario();

        /*
        CALL API REST
         */
        //dec1

        /*
        SHOW LOAD DIALOG
         */
        //dec1


        if (getArguments().getString("numm")!=null)
            Log.e("numero 1", getArguments().getString("num"));
        //
    }
    @Override
    public void onDetach() {
        mCallback = null;
        super.onDetach();
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
                        .into(fotoPerfil);
                username.setText(dato.getNomUser() + " " + dato.getApeUser());
                cantPrendas.setText(dato.getNumPrend());



            }catch (Exception e){
                Log.e(null,"se hizo la conexion, error en cargar la data");
            }

        }else {
            Log.e("coneccion","no hecha");
        }
    }

    @Override
    public void onFailed(String error_msg) {
        Log.e("error",error_msg+" fallo carga cabecera de perfill");
    }


    private void setChangeProfileElements(String s, String s1, String s2, String s3, String s4) {
        changeNombres = s;
        changeApellidos=s1;
        changeCumpleanos=s2;
        changeCorreo=s3;
        changeTelefono=s4;

    }










    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.photo:

                //fragment replace closet grilla -> profile 
                //FragmentViewController fragmentViewController= new FragmentViewController();
                //fragmentViewController.setSecondView();
                if (fClosetGrilla!=null){
                    fClosetProfile= new FClosetProfile(FCloset.this);
                    fragmentManager= getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.grilla_o_perfil,fClosetProfile);
                    fragmentTransaction.addToBackStack("FClosetGrilla");
                    fragmentTransaction.commit();
                }

                break;

        }

        /*Bundle bundle = new Bundle();
        bundle.putString("datos", "datos que necesito");
        mCallback.onFragmentIteration(bundle);*/

    }





    class FragmentViewController{
        private boolean firstView = true;

        public boolean isFirstView() { return firstView; }
        public void setFirstView() { firstView = true; }
        public void setSecondView() { firstView = false; }
    }





}



