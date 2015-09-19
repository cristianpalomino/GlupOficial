package pe.com.glup.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;

import pe.com.glup.R;
import pe.com.glup.beans.DatoUser;
import pe.com.glup.beans.DetalleUser;
import pe.com.glup.datasource.DSUsuario;
import pe.com.glup.dialog.ConfirmationPassDialog;
import pe.com.glup.dialog.NewPassDialog;
import pe.com.glup.interfaces.OnSuccessDetalleUsuario;
import pe.com.glup.interfaces.OnSuccessUpdatePass;
import pe.com.glup.interfaces.OnSuccessUpdateUser;
import pe.com.glup.utils.DatePickerFragment;


public class FClosetProfile extends Fragment implements OnSuccessDetalleUsuario,
        OnSuccessUpdateUser,
        OnSuccessUpdatePass,View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private FCloset context;
    private DSUsuario dsUsuario;

    private LinearLayout perfil;
    private EditText nombres,apellidos,correo,telefono;
    private TextView cumpleanos;
    private Button updateProfile,changePass;
    private String indVerPass;
    private FragmentManager fragmentManager;
    private String changeNombres,changeApellidos,changeCumpleanos,changeCorreo,changeTelefono;
    private String nuevaPassword="";
    private boolean changeToNewPass=false;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FClosetProfile.
     */
    // TODO: Rename and change types and number of parameters
    public static FClosetProfile newInstance(String param1, String param2) {
        FClosetProfile fragment = new FClosetProfile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public  FClosetProfile(FCloset context){
        this.context=context;
    }
    public FClosetProfile() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        perfil = (LinearLayout) getView().findViewById(R.id.profile);

        nombres = (EditText) getView().findViewById(R.id.nombres);
        apellidos = (EditText) getView().findViewById(R.id.apellidos);
        cumpleanos = (TextView) getView().findViewById(R.id.cumpleanos);
        correo = (EditText) getView().findViewById(R.id.correo);
        telefono = (EditText) getView().findViewById(R.id.telefono);
        changePass = (Button) getView().findViewById(R.id.changePass);
        updateProfile = (Button)getView().findViewById(R.id.updateProfile);

        cumpleanos.setOnClickListener(this);
        changePass.setOnClickListener(this);
        updateProfile.setOnClickListener(this);

        dsUsuario = new DSUsuario(getActivity());
        try{
            dsUsuario.setOnSuccessDetalleUsuario(FClosetProfile.this);
        }catch (ClassCastException e){
            Log.e("error",e.toString());
        }

        dsUsuario.loadUsuario();

        dsUsuario = new DSUsuario(getActivity());
        try{
            dsUsuario.setOnSuccessDetalleUsuario(FClosetProfile.this);
        }catch (ClassCastException e){
            Log.e("error",e.toString());
        }

        dsUsuario.loadUsuario();


    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }



    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cumpleanos:
                showDatePicker();
                break;
            case R.id.changePass:
                Log.e("clic", "cambiar password");
                indVerPass = "false";
                fragmentManager = context.getActivity().getSupportFragmentManager();
                new NewPassDialog(FClosetProfile.this).show(fragmentManager,"NewPassDialog");

                break;
            case R.id.updateProfile:
                Log.e("clic","guardar cambios perfil");
                fragmentManager = context.getActivity().getSupportFragmentManager();
                ////dsUsuario = new DSUsuario(getActivity());
                ////dsUsuario.setOnSuccessUpdateUser(FCloset.this);
                //Log.e("antes del indVerPass", indVerPass);
                //if (!changeToNewPass){
                indVerPass = detectedIndVerPass();
                //}

                Log.e("antes indVerPass", indVerPass);
                if (indVerPass.equals("true")){
                    new ConfirmationPassDialog(indVerPass,nombres.getText().toString(),
                            apellidos.getText().toString(),
                            cumpleanos.getText().toString(),
                            correo.getText().toString(),
                            telefono.getText().toString(),FClosetProfile.this).show(fragmentManager, "ConfirmationPassDialog");
                }else{
                    try{
                        dsUsuario.setOnSuccessUpdateUser(FClosetProfile.this);
                    }catch (ClassCastException e){
                        Log.e("error",e.toString());
                    }

                    dsUsuario.updateUsuario(indVerPass, nuevaPassword, nombres.getText().toString(),
                            apellidos.getText().toString(), cumpleanos.getText().toString(),
                            correo.getText().toString(), telefono.getText().toString());
                }
                break;
        }
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
        date.show(context.getActivity().getSupportFragmentManager(), "Date Picker");
    }
    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            cumpleanos.setText(String.valueOf(dayOfMonth) + "-" + String.valueOf(monthOfYear+1)
                    + "-" + String.valueOf(year));
        }
    };
    private String detectedIndVerPass() {
        Log.e("newPassIndVerPass", nuevaPassword);
        if (!(changeNombres.equals(nombres.getText().toString()) && changeApellidos.equals(apellidos.getText().toString())
                && changeCorreo.equals(correo.getText().toString()) && changeTelefono.equals(telefono.getText().toString())
        ) )
            return "true";
        else
            return "false";
    }

    @Override
    public void onSuccess(String success_msg, ArrayList<DetalleUser> detalleuser, ArrayList<DatoUser> datouser) {
        if (success_msg.equals("1")){
            Log.e("coneccion","hecha closet profile");
            try{
                // adapterUser= new DetalleUserAdapter(datouser,detalleuser,getActivity());
                DatoUser dato = datouser.get(0);
                DetalleUser detalle = detalleuser.get(0);


                nombres.setText(detalle.getNomUser());
                apellidos.setText(detalle.getApeUser());
                cumpleanos.setText(detalle.getFecNac());
                correo.setText(detalle.getCorreoUser());
                telefono.setText(detalle.getNumTelef());

                setChangeProfileElements(nombres.getText().toString(),
                        apellidos.getText().toString(),
                        cumpleanos.getText().toString(),
                        correo.getText().toString(),
                        telefono.getText().toString());

            }catch (Exception e){
                Log.e(null,"se hizo la conexion, error en cargar la data");
            }

        }else {
            Log.e("coneccion","no hecha");
        }
    }

    private void setChangeProfileElements(String s, String s1, String s2, String s3, String s4) {
        changeNombres = s;
        changeApellidos=s1;
        changeCumpleanos=s2;
        changeCorreo=s3;
        changeTelefono=s4;

    }

    @Override
    public void onFailed(String error_msg) {
        Log.e("detalleUsuario","wbs fallo");
    }

    @Override
    public void onSuccesUpdatePass(boolean status, int indOp, String msg, String newPass) {
        Log.e("mensajeSuccessPass", msg);
        if (indOp==1){
            Log.e("mensaje","termino update pass con new pass "+newPass+ " sigue update detalle usuario");
            changeToNewPass=true;
            nuevaPassword=newPass;
            try{
                dsUsuario.setOnSuccessUpdateUser(FClosetProfile.this);
            }catch (ClassCastException e){
                Log.e("error",e.toString());
            }

            Log.e("nueva pass", nuevaPassword);
            Log.e("indVerPass", indVerPass);
            Log.e("nombres", nombres.getText().toString());
            dsUsuario.updateUsuario("true", newPass, nombres.getText().toString(),
                    apellidos.getText().toString(), cumpleanos.getText().toString(),
                    correo.getText().toString(), telefono.getText().toString());


            Log.e("Se Guarda","todo el perfil");
        }
    }

    @Override
    public void onSuccesUpdateUser(boolean status, int indOp, String msg) {
        Log.e("mensajeSuccess", msg+ " indicador "+ indOp);
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
