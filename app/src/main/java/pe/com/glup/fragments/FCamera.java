package pe.com.glup.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import java.io.File;

import pe.com.glup.R;
import pe.com.glup.bus.BusHolder;
import pe.com.glup.datasource.DSCamera;
import pe.com.glup.events.Flash;
import pe.com.glup.events.TakePhoto;
import pe.com.glup.glup.Principal;
import pe.com.glup.utils.CameraUtils;
import pe.com.glup.views.CameraSurface;
import pe.com.glup.views.SquareLayout;

/**
 * Created by Glup on 19/10/15.
 */
public class FCamera extends Fragment implements View.OnClickListener {

	private Flash isFlash = new Flash();
	public CameraSurface surface;
	private FrameLayout frame;
	private SquareLayout middle;

	private TextView title;

	private ToggleButton take;
	private ImageButton atrasCamara;
	private ToggleButton refresh;
	private ImageButton next;

	private ImageView imagea;
	private ImageView imageb;
	private ImageView flashAutomatic;
	private Context context;
	private String codPrenda;
	private String oldImageNameA,oldImageNameB;
	private String filtro;

	private ToggleButton flash,grilla;
	private ImageView superior,medio,iconPreview;

	private int contador = 1;
	private boolean gridActivate;
	
	public static FCamera newInstance() {
		FCamera fragment = new FCamera();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}
	@Override
	public void onCreate(Bundle savedInstance){super.onCreate(savedInstance);}
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstance){
		return inflater.inflate(R.layout.fragment_camera,container,false);
	}
	@Override
	public void onActivityCreated(Bundle savedInstance){
		super.onActivityCreated(savedInstance);

		BusHolder.getInstance().register(this);
		context=getActivity();

		codPrenda="";
		filtro="superior";
		gridActivate=false;
		initUI();
	}

	private void initUI() {
		/**
		 * Imagenes
		 */
		imagea = (ImageView) getView().findViewById(R.id.imagea);
		imageb = (ImageView) getView().findViewById(R.id.imageb);
		next = (ImageButton) getView().findViewById(R.id.next);
		take = (ToggleButton) getView().findViewById(R.id.take);
		flashAutomatic = (ImageView) getView().findViewById(R.id.flash_automatico);
		flash= (ToggleButton) getView().findViewById(R.id.flash);
		grilla = (ToggleButton) getView().findViewById(R.id.grid);
		title = (TextView) getView().findViewById(R.id.title_lado);
		superior = (ImageView) getView().findViewById(R.id.superior);
		medio = (ImageView) getView().findViewById(R.id.medio);
		iconPreview = (ImageView) getView().findViewById(R.id.icon_preview);
		atrasCamara = (ImageButton) getView().findViewById(R.id.atras_camara);
		atrasCamara.setOnClickListener(this);

		Log.e("superior",getRelativeTop(superior)+"dp en pixeles "+(int)convertDpToPixel(getRelativeTop(superior),context));
		surface = new CameraSurface(context,(int)convertDpToPixel(getRelativeTop(superior),context));
		isFlash = new Flash();

		flashAutomatic.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				flashAutomatic.setVisibility(View.GONE);
				flash.setVisibility(View.VISIBLE);
				if (!CameraUtils.isFlash(context)) {
					//Utils.showMessage(CameraGlup.this, "Este dispositivo no tiene Flash");
					return;
				} else {
					BusHolder.getInstance().post(isFlash);
				}
			}
		});
		Log.e(null,"initUI "+CameraUtils.isFlash(context));

		if (!CameraUtils.isFlash(context)) {
			//Utils.showMessage(CameraGlup.this, "Este dispositivo no tiene Flash");
			flashAutomatic.setVisibility(View.GONE);
			flash.setVisibility(View.VISIBLE);
			return;
		} else {
			if (surface!=null){
				DSCamera dsCamera = new DSCamera(context);
				dsCamera.flashAutomatico(isFlash);
			}
		}
		superior.setOnClickListener(this);
		medio.setOnClickListener(this);
		iconPreview.setEnabled(false);
		imagea.setOnClickListener(this);
		imageb.setOnClickListener(this);
		next.setEnabled(false);
		take.setEnabled(true);



		next.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//Utils.showMessage(context,"Generar idPrenda y subir imagenes");
				DSCamera dsCamera = new DSCamera(context);
				dsCamera.generarCodePrenda();
				//renombar imagenes generadas

				/*String uri = imageb.getTag().toString();
				Intent intent = new Intent(context, Canny.class);
				intent.putExtra("uri", uri);
				startActivity(intent);*/
			}
		});
		//tittle = (TextView) findViewById(R.id.contador);
		//tittle.setText("Glup " + contador);

		frame = (FrameLayout) getView().findViewById(R.id.preview);
		frame.addView(surface);

		middle = (SquareLayout) getView().findViewById(R.id.middle);
		middle.init();


		refresh = (ToggleButton) getView().findViewById(R.id.refresh);
		refresh.setEnabled(false);
		refresh.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				contador = 1;
				//  tittle.setText("Glup " + contador);
				imagea.setVisibility(View.GONE);
				imageb.setVisibility(View.GONE);
				next.setEnabled(false);
				take.setEnabled(true);
				take.setChecked(false);
				refresh.setEnabled(false);
				iconPreview.setVisibility(View.VISIBLE);
				title.setText("Lado A");
			}
		});

		take.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				BusHolder.getInstance().post(new TakePhoto());
			}
		});


		flash.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!CameraUtils.isFlash(context)) {
					//Utils.showMessage(CameraGlup.this, "Este dispositivo no tiene Flash");
					return;
				} else {
					BusHolder.getInstance().post(isFlash);
				}
			}
		});
		grilla.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(surface!=null){
					gridActivate=!gridActivate;
					Log.e("gridActivate",gridActivate+"");
					surface.setGridActivate(gridActivate);
					surface.invalidate();
				}

			}
		});


	}
	@Override
	public void onResume() {
		super.onResume();
		BusHolder.getInstance().register(this);
		flashAutomatic.setVisibility(View.GONE);
		flash.setVisibility(View.VISIBLE);
		flash.setChecked(false);
		filtro="superior";
		medio.setVisibility(View.GONE);
		superior.setVisibility(View.VISIBLE);
	}

	@Override
	public void onPause() {
		super.onPause();
		BusHolder.getInstance().unregister(this);
		flashAutomatic.setVisibility(View.GONE);
		flash.setVisibility(View.VISIBLE);
		flash.setChecked(false);
		filtro="superior";
		medio.setVisibility(View.GONE);
		superior.setVisibility(View.VISIBLE);
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.imagea:
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_VIEW);
				intent.setDataAndType(Uri.parse("file://" + v.getTag().toString()), "image/*");
				startActivity(intent);
				break;
			case R.id.imageb:
				Intent intent2 = new Intent();
				intent2.setAction(Intent.ACTION_VIEW);
				intent2.setDataAndType(Uri.parse("file://" + v.getTag().toString()), "image/*");
				startActivity(intent2);
				break;
			case R.id.superior:
				filtro="medio";
				superior.setVisibility(View.GONE);
				medio.setVisibility(View.VISIBLE);
				break;
			case R.id.medio:
				filtro="superior";
				medio.setVisibility(View.GONE);
				superior.setVisibility(View.VISIBLE);
				break;
			case R.id.atras_camara:
				((Principal)context).onBackPressed();
				break;
		}

	}

	@Subscribe
	public void onSuccess(CameraSurface.SuccessSavePhoto successSavePhoto) {
		Log.e(null, successSavePhoto.msg);
		//toCanny(result);
		switch (contador) {
			case 1:
				iconPreview.setVisibility(View.GONE);
				contador++;
				refresh.setEnabled(true);
				refresh.setChecked(false);
				title.setText("Lado A");
				Picasso.with(context).load("file:" + successSavePhoto.result).fit().into(imageb);
				Log.e("fotoA", successSavePhoto.result);
				oldImageNameA=successSavePhoto.result;
				imageb.setBackgroundColor(Color.WHITE);
				imageb.setVisibility(View.VISIBLE);
				imageb.setTag(successSavePhoto.result);
				take.setChecked(false);
				break;
			case 2:
				title.setText("Lado B");
				Picasso.with(context).load("file:" + successSavePhoto.result).fit().into(imagea);
				Log.e("fotoB", successSavePhoto.result);
				oldImageNameB=successSavePhoto.result;
				imagea.setBackgroundColor(Color.RED);
				imagea.setVisibility(View.VISIBLE);
				imagea.setTag(successSavePhoto.result);
				next.setEnabled(true);
				take.setEnabled(false);
				take.setChecked(true);
				break;
		}
	}

	@Subscribe
	public void getCodePrenda(DSCamera.ResponseGenerateCodePrenda responseGenerateCodePrenda){
		Log.e("null", "servicio generar codPrenda hecho");
		if (responseGenerateCodePrenda.success==1){
			codPrenda=responseGenerateCodePrenda.codigo_prenda;
			Log.e("null","codigo de prenda generado "+codPrenda);
			if (!codPrenda.equals("")){
				Log.e(null,"renombrar imagenes A y B");
				String userCode="000000000010";
				String newImageNameA=userCode+"_"+codPrenda+"_A.jpg";
				String newImageNameB=userCode+"_"+codPrenda+"_B.jpg";

				String root = Environment.getExternalStorageDirectory().toString();
				File myDir = new File(root + "/GlupFotos");
				myDir.mkdirs();

				File oldImageA= new File(oldImageNameA);
				File newImageA = new File(myDir,newImageNameA);
				oldImageA.renameTo(newImageA);

				File oldImageB = new File(oldImageNameB);
				File newImageB = new File(myDir,newImageNameB);
				oldImageB.renameTo(newImageB);

				DSCamera dsCamera = new DSCamera(context);
				dsCamera.uploadPrenda(filtro, myDir, newImageNameA, newImageNameB, codPrenda);

				contador = 1;
				//  tittle.setText("Glup " + contador);
				imagea.setVisibility(View.GONE);
				imageb.setVisibility(View.GONE);
				next.setEnabled(false);
				take.setEnabled(true);
				take.setChecked(false);
				iconPreview.setVisibility(View.VISIBLE);

			}
		}
	}
	@Subscribe
	public void getResponseUploadPrenda(DSCamera.ResponseUploadPrenda responseUploadPrenda){
		//Utils.showMessage(context,"Listo termino su carga");
		 title.setText("Lado A");
		 refresh.setChecked(false);
		 refresh.setEnabled(false);

	}

	private int getRelativeTop(View myView) {
		if (myView.getParent() == myView.getRootView())
			return myView.getTop();
		else
			return myView.getTop() + getRelativeTop((View) myView.getParent());
	}
	public  float convertDpToPixel(float dp, Context context){
		float px = dp * (context.getResources().getDisplayMetrics().densityDpi/160);
		return px;
	}

}