package pe.com.glup.fragments;

import android.annotation.TargetApi;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import pe.com.glup.R;
import pe.com.glup.adapters.PrendaAdapter;
import pe.com.glup.dialog.GlupDialog;
import pe.com.glup.dialog.GlupDialogNew;
import pe.com.glup.glup.Glup;
import pe.com.glup.glup.Principal;
import pe.com.glup.managers.bus.BusHolder;
import pe.com.glup.managers.session.Session_Manager;
import pe.com.glup.models.PerfilUsuario;
import pe.com.glup.models.Prenda;
import pe.com.glup.models.interfaces.OnSearchListener;
import pe.com.glup.models.interfaces.OnSuccessCatalogo;
import pe.com.glup.models.interfaces.OnSuccessUpdate;
import pe.com.glup.network.DSCloset;
import pe.com.glup.network.DSUsuario;
import pe.com.glup.network.DSUsuarioNew;
import pe.com.glup.utils.Util_Fonts;

/**
 * Created by Glup on 25/11/15.
 */
public class FClosetNew extends Fragment implements View.OnClickListener,OnSuccessCatalogo,
        OnSearchListener,
        AbsListView.OnScrollListener,
        AdapterView.OnItemClickListener{
    private CircleImageView foto;
    private TextView username;
    private DSUsuario dsUsuario;
    private Button updateProfile;
    private FragmentManager fragmentManager;
    private FrameLayout frameBack,frameUpdate,frameGrilla;

    protected GlupDialogNew gd;
    private static final int EMPTY = 0;
    private static final int FULL = 1;
    private GridView grilla;
    private PrendaAdapter adapter;
    private DSCloset dsCloset;
    private Glup glup;
    private static int PAGE = 1;
    private static String TAG = "todos";
    private boolean isLoading = false;
    private RelativeLayout emptyViewCloset;
    private ImageView imageView;

    public static FClosetNew newInstance(){
        FClosetNew fragment=new FClosetNew();
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstance){super.onCreate(savedInstance);}
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstance){
        return inflater.inflate(R.layout.fragment_closet_grid,container,false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstance){
        super.onActivityCreated(savedInstance);
        BusHolder.getInstance().register(this);
        emptyViewCloset = (RelativeLayout)getView().findViewById(R.id.empty_view_closet);
        imageView=(ImageView)getView().findViewById(R.id.image_empty);

        frameGrilla = (FrameLayout) getView().findViewById(R.id.grilla_closet);
        frameBack = (FrameLayout)getView().findViewById(R.id.frame_back);
        frameUpdate = (FrameLayout)getView().findViewById(R.id.frame_update);
        frameBack.setVisibility(View.GONE);
        frameUpdate.setVisibility(View.GONE);
        foto = (CircleImageView) getView().findViewById(R.id.photo);
        username = (TextView)getView().findViewById(R.id.username);
        foto.setOnClickListener(this);
        DSUsuarioNew dsUsuarioNew=new DSUsuarioNew(getActivity());
        dsUsuarioNew.loadUsuario();

        PAGE = 1;
        TAG = "todos";
        isLoading = false;

        if (new Session_Manager(getActivity()).getCurrentUserSexo().equals("H")){
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.bg_closet_men));
        }else {
            Log.e("closet","mujer");
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.bg_closet_woman));
        }

        Principal principal = ((Principal) getActivity());
        try{
            principal.getHeader().setOnSearchListener(FClosetNew.this);
        }catch (ClassCastException e){
            Log.e("error",e.toString());
        }

        getView().findViewById(R.id.empty_view_grilla_).setVisibility(View.GONE);

        grilla = (GridView) getView().findViewById(R.id.grilla_prendas);
        grilla.setOnItemClickListener(this);
        grilla.setOnScrollListener(this);

        dsCloset = new DSCloset(getActivity());
        dsCloset.getUsuarioPrendas(TAG, String.valueOf(PAGE), "10");
        try{
            dsCloset.setOnSuccessCatalogo(FClosetNew.this);//context FCloset
        }catch (ClassCastException e){
            Log.e("error",e.toString());
        }

        /*
        SHOW LOAD DIALOG
         */
        //dec1
        fragmentManager= getActivity().getSupportFragmentManager();
        gd = new GlupDialogNew();
        gd.setCancelable(false);
        gd.show(fragmentManager,GlupDialog.class.getSimpleName());

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.photo:
                OpenProfile openProfile = new OpenProfile();
                BusHolder.getInstance().post(openProfile);
                break;
        }
        //if (v.getId()==R.id.frame_back || v.getId()==R.id.back){getActivity().onBackPressed();}

    }
    @Subscribe
    public void SuccesLoadProfile(PerfilUsuario perfilUsuario){
        gd.dismiss();
        Log.e("LoadUser", perfilUsuario.getSuccess()+"");
        if (perfilUsuario.getSuccess()==1){
            try {
                Picasso.with(getActivity().getApplicationContext())
                .load(perfilUsuario.getDatouser().get(0).getRutaFoto())
                .fit().centerInside().noFade()
                .into(foto);
                username.setText(perfilUsuario.getDatouser().get(0).getNomUser());
            }catch (Exception e){
                //Log.e("LoadUserError",e.getMessage());
            }
        }else{

        }
        if (perfilUsuario.getDatouser().get(0).getNumPrend().toString().equals("0")){
            frameGrilla.setVisibility(View.GONE);
            emptyViewCloset.setVisibility(View.VISIBLE);
        }else{
            frameGrilla.setVisibility(View.VISIBLE);
            emptyViewCloset.setVisibility(View.GONE);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Prenda prenda = (Prenda) parent.getItemAtPosition(position);
        dsCloset = new DSCloset(getActivity());
        dsCloset.updateProbador(glup.getPrendas().get(position).getCod_prenda());
        dsCloset.setOnSuccessUpdate(new OnSuccessUpdate() {
            @Override
            public void onSuccesUpdate(boolean status, int indProb) {
                if (status) {
                    if (indProb == 0) {
                        //((CheckBox) view.findViewById(R.id.check)).setChecked(false);
                    } else if (indProb == 1) {
                        //((CheckBox) view.findViewById(R.id.check)).setChecked(true);
                    }
                } else {

                }
            }
        });
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
                try{
                    dsCloset.setOnSuccessCatalogo(FClosetNew.this);//context FCloset
                }catch (ClassCastException e){
                    Log.e("error",e.toString());
                }

            }
        }
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
        try{
            dsCloset.setOnSuccessCatalogo(FClosetNew.this);//context FCloset
        }catch (ClassCastException e){
            Log.e("error",e.toString());
        }
    }

    @Override
    public void onSuccess(String success_msg, ArrayList<Prenda> prendas) {

        gd.dismiss();
        try {
            if (PAGE == 1) {
                if (prendas != null) {
                    displayMessage(FULL);
                    adapter = new PrendaAdapter(FClosetNew.this.getActivity(), prendas);
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
        gd.dismiss();

    }
    private void displayMessage(int type) {
        if (type == EMPTY) {
            frameGrilla.setVisibility(View.GONE);
            grilla.setVisibility(View.GONE);
            emptyViewCloset.setVisibility(View.VISIBLE);
            //emptyView.setVisibility(View.VISIBLE);
        } else if (type == FULL) {
            frameGrilla.setVisibility(View.VISIBLE);
            grilla.setVisibility(View.VISIBLE);
            emptyViewCloset.setVisibility(View.GONE);
            //emptyView.setVisibility(View.GONE);
        }
    }
    public class OpenProfile{}
}
