package pe.com.glup.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.os.Handler;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;

import pe.com.glup.R;
import pe.com.glup.beans.DatoUser;
import pe.com.glup.beans.DetalleUser;
import pe.com.glup.bus.BusHolder;
import pe.com.glup.datasource.DSUsuario;
import pe.com.glup.dialog.ConfirmationPassDialog;
import pe.com.glup.dialog.NewPassDialog;
import pe.com.glup.glup.Entrar;
import pe.com.glup.glup.Glup;
import pe.com.glup.interfaces.OnSuccessDetalleUsuario;
import pe.com.glup.interfaces.OnSuccessUpdatePass;
import pe.com.glup.interfaces.OnSuccessUpdateUser;
import pe.com.glup.session.Session_Manager;
import pe.com.glup.utils.DatePickerFragment;
import pe.com.glup.utils.MessageUtil;
import pe.com.glup.views.Message;
import pe.com.glup.views.MessageV2;


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
    private Button updateProfile,changePass,closeSession;
    private String indVerPass;
    private FragmentManager fragmentManager;
    private String changeNombres,changeApellidos,changeCumpleanos,changeCorreo,changeTelefono;
    private String nuevaPassword="";
    private boolean changeToNewPass=false;
    private RelativeLayout frameChangePass,frameCloseSession;
    private boolean detectSomeChange;
    private boolean flagDirect;
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
        BusHolder.getInstance().register(this);

        perfil = (LinearLayout) getView().findViewById(R.id.profile);

        nombres = (EditText) getView().findViewById(R.id.nombres);
        apellidos = (EditText) getView().findViewById(R.id.apellidos);
        cumpleanos = (TextView) getView().findViewById(R.id.cumpleanos);
        correo = (EditText) getView().findViewById(R.id.correo);
        telefono = (EditText) getView().findViewById(R.id.telefono);
        changePass = (Button) getView().findViewById(R.id.changePass);
        updateProfile = (Button)getView().findViewById(R.id.updateProfile);
        closeSession = (Button) getView().findViewById(R.id.cerrar_sesion);
        frameChangePass = (RelativeLayout) getView().findViewById(R.id.frame_changePass);
        frameCloseSession = (RelativeLayout) getView().findViewById(R.id.frame_cerrarSesion);

        cumpleanos.setOnClickListener(this);
        changePass.setOnClickListener(this);
        updateProfile.setOnClickListener(this);
        closeSession.setOnClickListener(this);
        frameChangePass.setOnClickListener(this);
        frameCloseSession.setOnClickListener(this);

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
                flagDirect=false;
                Log.e("clic", "cambiar password");
                indVerPass = "false";
                fragmentManager = context.getActivity().getSupportFragmentManager();
                new NewPassDialog(FClosetProfile.this).show(fragmentManager,"NewPassDialog");

                break;
            case R.id.cerrar_sesion:
                Log.e("clic", "cerrar sesion");
                Context context=getActivity();
                Session_Manager manager = new Session_Manager(context);
                manager.closeSession();
                Intent intent = new Intent(context, Entrar.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);
                ((Glup)context).finish();
                break;
            case R.id.frame_changePass:
                flagDirect=false;
                Log.e("clic", "cambiar password");
                indVerPass = "false";
                fragmentManager = this.context.getActivity().getSupportFragmentManager();
                new NewPassDialog(FClosetProfile.this).show(fragmentManager,"NewPassDialog");
                break;
            case R.id.frame_cerrarSesion:
                Log.e("clic", "cerrar sesion");
                Context context1=getActivity();
                Session_Manager manager1 = new Session_Manager(context1);
                manager1.closeSession();
                Intent intent1 = new Intent(context1, Entrar.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context1.startActivity(intent1);
                ((Glup)context1).finish();
                break;
        }
    }
    private void showDatePicker() {



        DatePickerFragment date = new DatePickerFragment();
        //date.setStyle(DatePickerFragment.STYLE_NORMAL,R.style.DatePickerDialogTheme);

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
  /*
        new DatePickerDialog(context.getActivity(), R.style.DatePickerDialogTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                //DO SOMETHING
                cumpleanos.setText(String.valueOf(dayOfMonth) + "-" + String.valueOf(monthOfYear+1)
                        + "-" + String.valueOf(year));
            }
        }, args.getInt("year"),args.getInt("month"), args.getInt("day")).show();
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
        /*
        if (!(changeNombres.equals(nombres.getText().toString()) && changeApellidos.equals(apellidos.getText().toString())
                && changeCorreo.equals(correo.getText().toString()) && changeTelefono.equals(telefono.getText().toString())
        ) )
        * */
        if (!(changeCorreo.equals(correo.getText().toString()) && changeTelefono.equals(telefono.getText().toString())))
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

                if (detalle.getNomUser().equals("")){
                    nombres.setHint("Nombres");
                }else {
                    nombres.setText(detalle.getNomUser());
                }
                if (detalle.getApeUser().equals("")){
                    apellidos.setHint("Apellidos");
                }else{
                    apellidos.setText(detalle.getApeUser());
                }
                if (detalle.getFecNac().equals("")){
                    cumpleanos.setHint("Cumpleaños");
                }else{
                    cumpleanos.setText(detalle.getFecNac());
                }
                if (detalle.getCorreoUser().equals("")){
                    correo.setHint("Correo");
                }else {
                    correo.setText(detalle.getCorreoUser());
                }
                if (detalle.getNumTelef().equals("")){
                    telefono.setHint("Teléfono");
                }else {
                    telefono.setText(detalle.getNumTelef());
                }




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
            nombres.setHint("Nombres");
            apellidos.setHint("Apellidos");
            cumpleanos.setHint("Cumpleaños");
            correo.setHint("Correo");
            telefono.setHint("Teléfono");
        }
    }

    private void setChangeProfileElements(String s, String s1, String s2, String s3, String s4) {
        changeNombres = s;
        changeApellidos=s1;
        changeCumpleanos=s2;
        changeCorreo=s3;
        changeTelefono=s4;
    }

    private void inverseSetChangeProfileElements() {
        nombres.setText(changeNombres);
        apellidos.setText(changeApellidos);
        cumpleanos.setText(changeCumpleanos);
        correo.setText(changeCorreo);
        telefono.setText(changeTelefono);
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
        }else {
            //final Message toast = new Message(getActivity(), msg, Toast.LENGTH_SHORT);
            //toast.show();
            final MessageV2 toast=new MessageV2(msg);
            toast.setCancelable(false);
            toast.show(context.getActivity().getSupportFragmentManager(),MessageV2.class.getSimpleName());
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    toast.dismiss();
                }
            }, 2000);
        }
    }

    @Override
    public void onSuccesUpdateUser(boolean status, int indOp, String msg) {
        Log.e("mensajeSuccess", msg + " indicador " + indOp);
        //final Message toast = new Message(getActivity(), msg, Toast.LENGTH_SHORT);
        //toast.show();
        /*final MessageV2 toast=new MessageV2(msg);
        toast.setCancelable(false);
        toast.show(context.getActivity().getSupportFragmentManager(),MessageV2.class.getSimpleName());
        *//*
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                toast.cancel();
            }
        }, 2000);*/
        if (indOp==1){
            setChangeProfileElements(nombres.getText().toString(),
                    apellidos.getText().toString(),
                    cumpleanos.getText().toString(),
                    correo.getText().toString(),
                    telefono.getText().toString());
        }else {
            inverseSetChangeProfileElements();
        }
        if (flagDirect){
            flagDirect=false;
            final MessageV2 message=new MessageV2(msg);
            message.setCancelable(false);
            message.show(context.getActivity().getSupportFragmentManager(),MessageV2.class.getSimpleName());
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    message.dismiss();
                }
            }, 2500);
        }

    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    public class PassToFragmentParent{
        public String changeNombres,changeApellidos,changeCumpleanos,changeCorreo,changeTelefono;
        public String indVerPass,nuevaPassword,nombres,apellidos,cumpleanos,correo,telefono;
    }

    @Subscribe
    public void saveProfile(FCloset.SignalSaveProfile signalSaveProfile){
        Log.e("clic","guardar cambios perfil y contador"+signalSaveProfile.cont);
        fragmentManager = context.getActivity().getSupportFragmentManager();
        int count =fragmentManager.getBackStackEntryCount();
        if (count>0){
            Log.e("ultimoFrag",fragmentManager.getBackStackEntryAt(count-1).getName());
        }

        ////dsUsuario = new DSUsuario(getActivity());
        ////dsUsuario.setOnSuccessUpdateUser(FCloset.this);
        //Log.e("antes del indVerPass", indVerPass);
        //if (!changeToNewPass){
        detectSomeChange= detectChanges();
        if (detectSomeChange){
            indVerPass = detectedIndVerPass();
            //}
            Log.e("antes indVerPass", indVerPass);
            if (indVerPass.equals("true")){
                flagDirect=false;
                new ConfirmationPassDialog(indVerPass,nombres.getText().toString(),
                        apellidos.getText().toString(),
                        cumpleanos.getText().toString(),
                        correo.getText().toString(),
                        telefono.getText().toString(),FClosetProfile.this).show(fragmentManager, "ConfirmationPassDialog");
            }else{
                flagDirect=true;
                try{
                    dsUsuario.setOnSuccessUpdateUser(FClosetProfile.this);
                }catch (ClassCastException e){
                    Log.e("error",e.toString());
                }
                Log.e("UpdateNom",nombres.getText().toString());
                dsUsuario.updateUsuario(indVerPass, nuevaPassword, nombres.getText().toString(),
                        apellidos.getText().toString(), cumpleanos.getText().toString(),
                        correo.getText().toString(), telefono.getText().toString());
            }
        }else {
            final MessageV2 message=new MessageV2("No realizo ningun cambio");
            message.setCancelable(false);
            message.show(context.getActivity().getSupportFragmentManager(),MessageV2.class.getSimpleName());
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    message.dismiss();
                }
            }, 2000);
        }

    }

    private boolean detectChanges() {
        if (changeNombres.equals(nombres.getText().toString()) && changeApellidos.equals(apellidos.getText().toString())
                && changeCorreo.equals(correo.getText().toString()) && changeTelefono.equals(telefono.getText().toString())
        ){
            return false;
        }else {
            return true;
        }
    }


    @Subscribe
    public void setUpdateUsername(DSUsuario.SignalChangeUsername signalChangeUsername){
        Log.e("nullindverpass2",signalChangeUsername.indVerPass);
        SignalChangeUsername2 signalChangeUsername2=new SignalChangeUsername2();
        signalChangeUsername2.username=nombres.getText().toString();
        BusHolder.getInstance().post(signalChangeUsername2);
    }

    public class SignalChangeUsername2{public String username;}
}
