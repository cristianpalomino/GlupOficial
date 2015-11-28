package pe.com.glup.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;
import pe.com.glup.R;
import pe.com.glup.dialog.ConfirmationPassDialog;
import pe.com.glup.dialog.NewPassDialog;
import pe.com.glup.glup.Entrar;
import pe.com.glup.glup.Glup;
import pe.com.glup.managers.bus.BusHolder;
import pe.com.glup.managers.session.Session_Manager;
import pe.com.glup.models.DetalleUser;
import pe.com.glup.models.PerfilUsuario;
import pe.com.glup.network.DSUsuarioNew;
import pe.com.glup.utils.DatePickerFragment;
import pe.com.glup.views.MessageV2;

/**
 * Created by Glup on 26/11/15.
 */
public class FClosetProfileNew extends Fragment
    implements View.OnClickListener,TextView.OnEditorActionListener{
    private CircleImageView foto;
    private TextView username;

    private FrameLayout frameBack,frameUpdate;
    private ImageView back;
    private Button update;

    private EditText nombres,apellidos,correo,telefono;
    private TextView cumpleanos;
    private String changeNombres,changeApellidos,changeCumpleanos,changeCorreo,changeTelefono;
    private Button changePass,closeSession;
    private RelativeLayout frameChangePass,frameCloseSession;
    private String indVerPass;
    private boolean flagDirect;
    private String nuevaPassword="";
    private boolean flagLoadProfile=false;


    public static FClosetProfileNew newInstance(){
        FClosetProfileNew fragment=new FClosetProfileNew();
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstance){super.onCreate(savedInstance);}
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstance){
        return  inflater.inflate(R.layout.fragment_closet_profile,container,false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstance){
        super.onActivityCreated(savedInstance);
        BusHolder.getInstance().register(this);


        foto = (CircleImageView) getView().findViewById(R.id.photo);
        username = (TextView)getView().findViewById(R.id.username);
        foto.setOnClickListener(this);

        back = (ImageView) getView().findViewById(R.id.back);
        frameBack = (FrameLayout) getView().findViewById(R.id.frame_back);
        update = (Button) getView().findViewById(R.id.update);
        frameUpdate = (FrameLayout) getView().findViewById(R.id.frame_update);
        back.setOnClickListener(this);
        frameBack.setOnClickListener(this);
        update.setOnClickListener(this);
        frameUpdate.setOnClickListener(this);

        nombres=(EditText) getView().findViewById(R.id.nombres);
        apellidos=(EditText) getView().findViewById(R.id.apellidos);
        cumpleanos =(TextView) getView().findViewById(R.id.cumpleanos);
        correo = (EditText) getView().findViewById(R.id.correo);
        telefono = (EditText) getView().findViewById(R.id.telefono);
        nombres.setOnEditorActionListener(this);
        apellidos.setOnEditorActionListener(this);
        cumpleanos.setOnEditorActionListener(this);
        correo.setOnEditorActionListener(this);
        telefono.setOnEditorActionListener(this);
        cumpleanos.setOnClickListener(this);

        changePass = (Button) getView().findViewById(R.id.changePass);
        frameChangePass = (RelativeLayout) getView().findViewById(R.id.frame_changePass);
        closeSession = (Button) getView().findViewById(R.id.cerrar_sesion);
        frameCloseSession = (RelativeLayout) getView().findViewById(R.id.frame_cerrarSesion);
        changePass.setOnClickListener(this);
        frameChangePass.setOnClickListener(this);
        closeSession.setOnClickListener(this);
        frameCloseSession.setOnClickListener(this);

        flagLoadProfile=true;
        DSUsuarioNew dsUsuario = new DSUsuarioNew(getActivity());
        dsUsuario.loadUsuario();
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.photo){
            DSUsuarioNew dsUsuario = new DSUsuarioNew(getActivity());
            dsUsuario.loadUsuario();
        }
        if (v.getId()==R.id.back || v.getId()==R.id.frame_back){
            flagLoadProfile=false;
            getActivity().onBackPressed();
        }
        if (v.getId()==R.id.update || v.getId()==R.id.frame_update){
            boolean detectSomeChange = detectChanges();
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
                            telefono.getText().toString(),FClosetProfileNew.this).show(getActivity().getSupportFragmentManager(), "ConfirmationPassDialog");
                }else{
                    flagDirect=true;
                    DSUsuarioNew dsUsuarioNew = new DSUsuarioNew(getActivity());
                    Log.e("UpdateNom",nombres.getText().toString());
                    dsUsuarioNew.updateUsuario(indVerPass, nuevaPassword, nombres.getText().toString(),
                            apellidos.getText().toString(), cumpleanos.getText().toString(),
                            correo.getText().toString(), telefono.getText().toString());
                }
            }else {
                final MessageV2 message=new MessageV2("No realizo ningun cambio");
                message.setCancelable(false);
                message.show(getActivity().getSupportFragmentManager(),MessageV2.class.getSimpleName());
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        message.dismiss();
                    }
                }, 2000);
            }
        }
        if (v.getId()==R.id.cumpleanos){
            showDatePicker();
        }
        if (v.getId()==R.id.changePass || v.getId()==R.id.frame_changePass){
            flagDirect=false;
            Log.e("clic", "cambiar password");
            indVerPass = "false";
            new NewPassDialog(FClosetProfileNew.this).show(getActivity().getSupportFragmentManager(),"NewPassDialog");
        }
        if (v.getId()==R.id.cerrar_sesion || v.getId()==R.id.frame_cerrarSesion){
            Log.e("clic", "cerrar sesion");
            Context context=getActivity();
            Session_Manager manager = new Session_Manager(context);
            manager.closeSession();
            Intent intent = new Intent(context, Entrar.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
            ((Glup)context).finish();
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
    private String detectedIndVerPass() {
        Log.e("newPassIndVerPass", nuevaPassword);
        if (!(changeCorreo.equals(correo.getText().toString()) && changeTelefono.equals(telefono.getText().toString())))
            return "true";
        else
            return "false";
    }

    @Subscribe
    public void SuccesLoadProfile(PerfilUsuario perfilUsuario){
        Log.e("LoadUser", perfilUsuario.getSuccess()+"");
        if (flagLoadProfile){
            if (perfilUsuario.getSuccess()==1){
                try {
                    Picasso.with(getActivity().getApplicationContext())
                            .load(perfilUsuario.getDatouser().get(0).getRutaFoto())
                            .fit().centerInside().noFade()
                            .into(foto);
                    username.setText(perfilUsuario.getDatouser().get(0).getNomUser());
                    Log.e("Detalle",perfilUsuario.getDetalleuser().get(0).toString());
                    YesOrNoHint(perfilUsuario.getDetalleuser().get(0));
                    setChangeProfileElements(
                            nombres.getText().toString(),
                            apellidos.getText().toString(),
                            cumpleanos.getText().toString(),
                            correo.getText().toString(),
                            telefono.getText().toString());
                }catch (Exception e){
                    Log.e("LoadUserError",e.getMessage());
                }
            }else{
                Log.e("coneccion","no hecha");
                nombres.setHint("Nombres");
                apellidos.setHint("Apellidos");
                cumpleanos.setHint("Cumpleaños");
                correo.setHint("Correo");
                telefono.setHint("Teléfono");
            }
        }
    }

    private void YesOrNoHint(DetalleUser detalle) {
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

    @Subscribe
    public void SuccessUpdateProfile(DSUsuarioNew.SignalUpdateProfile signalUpdateProfile){
        Log.e("mensajeSuccess", signalUpdateProfile.msg + " indicador " + signalUpdateProfile.success);
        if (signalUpdateProfile.success==1){
            username.setText(nombres.getText().toString());
        }else {
            //inverseSetChangeProfileElements();
        }
        setChangeProfileElements(nombres.getText().toString(),
                apellidos.getText().toString(),
                cumpleanos.getText().toString(),
                correo.getText().toString(),
                telefono.getText().toString());
        if (flagDirect){
            flagDirect=false;
            final MessageV2 message=new MessageV2(signalUpdateProfile.msg);
            message.setCancelable(false);
            message.show(getActivity().getSupportFragmentManager(),MessageV2.class.getSimpleName());
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    message.dismiss();
                }
            }, 2000);
        }
    }

    private void showDatePicker() {
        DatePickerFragment date = new DatePickerFragment();
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        date.setCallBack(ondate);
        date.show(getActivity().getSupportFragmentManager(), "Date Picker");
    }
    private DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            cumpleanos.setText(String.valueOf(dayOfMonth) + "-" + String.valueOf(monthOfYear+1)
                    + "-" + String.valueOf(year));
        }
    };
    @Subscribe
    public void SuccessUpdatePass(DSUsuarioNew.SignalChangeNewPass signalChangeNewPass){
        Log.e("mensajeSuccessPass", signalChangeNewPass.msg);
        if (signalChangeNewPass.success==1){
            Log.e("mensaje","termino update pass con new pass "+signalChangeNewPass.password+ " sigue update profile");
            //changeToNewPass=true;
            nuevaPassword=signalChangeNewPass.password;
            Log.e("nueva pass", nuevaPassword);
            Log.e("indVerPass", indVerPass);
            Log.e("nombres", nombres.getText().toString());
            DSUsuarioNew dsUsuario= new DSUsuarioNew(getActivity());
            dsUsuario.updateUsuario("true", nuevaPassword, nombres.getText().toString(),
                    apellidos.getText().toString(), cumpleanos.getText().toString(),
                    correo.getText().toString(), telefono.getText().toString());


            Log.e("Se Guarda","todo el perfil");
        }else {
            //final Message toast = new Message(getActivity(), msg, Toast.LENGTH_SHORT);
            //toast.show();
            final MessageV2 toast=new MessageV2(signalChangeNewPass.msg);
            toast.setCancelable(false);
            toast.show(getActivity().getSupportFragmentManager(),MessageV2.class.getSimpleName());
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
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_SEND)) {
            Log.e("null", "entro");
            update.performClick();
            //return true;
        }
        return false;
    }
}
