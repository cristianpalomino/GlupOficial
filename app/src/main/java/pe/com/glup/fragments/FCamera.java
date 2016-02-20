package pe.com.glup.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.net.URI;
import java.util.IllegalFormatCodePointException;

import android.os.Handler;

import pe.com.glup.R;
import pe.com.glup.dialog.SelectPrendaDialog;
import pe.com.glup.glup.Glup;
import pe.com.glup.managers.bus.BusHolder;
import pe.com.glup.network.DSCamera;
import pe.com.glup.models.events.Flash;
import pe.com.glup.models.events.TakePhoto;
import pe.com.glup.glup.Principal;
import pe.com.glup.utils.CameraUtils;
import pe.com.glup.utils.Util_Fonts;
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
	private ImageView atrasCamara,atrasCamara2;
	private ToggleButton refresh;
	private ImageButton next;

	private ImageView imagea,cruzGuia;
	private ImageView imageb,previewPhoto,imgHelpPhoto,imgHelpPhoto2;
	//private ImageView flashAutomatic;
	private Context context;
	private String codPrenda;
	private String oldImageNameA,oldImageNameB;
	private String filtro;

	private ToggleButton flash,grilla;
	private ImageView superior,medio,iconPreview,checkListo,checkCancelar,previewFrontal,previewPosterior;
	private TextView tvSuperior,tvMedio,tvIconPreview,txtHelpPhoto,txtPie;

	private int contador = 1;
	private boolean gridActivate,selectTipoPrenda;
	private RelativeLayout selectPrenda,contentCamera,frameMsj,frameMsjFinal,frameOptions,frameListo,frameCancelar,frameEnviarPrenda;
	private Button enviarPrenda;
	private TextView txtListo;
	private RadioButton optionContinue,optionToCatalogo;
	private int sizeHelpPhoto,posXGuia,posYGuia;


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
		selectTipoPrenda=false;
		context=getActivity();

		codPrenda="";
		//filtro="superior";
		gridActivate=false;
		initUI();
	}

	private void initUI() {

		getView().findViewById(R.id.frameHelpPhoto).bringToFront();
		getView().findViewById(R.id.frame_select_superior).setEnabled(false);
		/**
		 * Imagenes
		 */
		tvSuperior = (TextView) getView().findViewById(R.id.text_superior);
		tvMedio = (TextView) getView().findViewById(R.id.text_medio);
		tvIconPreview = (TextView) getView().findViewById(R.id.text_icon_preview);

		txtHelpPhoto=(TextView)getView().findViewById(R.id.txtHelpPhoto);
		imgHelpPhoto=(ImageView)getView().findViewById(R.id.imgHelpPhoto);
		imgHelpPhoto.setImageResource(R.drawable.superior_help_a);
		imgHelpPhoto2=(ImageView)getView().findViewById(R.id.imgHelpPhoto2);
        cruzGuia = (ImageView)getView().findViewById(R.id.imgGuia);



		imagea = (ImageView) getView().findViewById(R.id.imagea);
		imageb = (ImageView) getView().findViewById(R.id.imageb);
		frameListo= (RelativeLayout)getView().findViewById(R.id.frameOk);
		frameCancelar=(RelativeLayout)getView().findViewById(R.id.frameCancel);
		previewPhoto=(ImageView)getView().findViewById(R.id.previewPhotoImage);
		txtPie=(TextView)getView().findViewById(R.id.txtPie);
		frameEnviarPrenda=(RelativeLayout)getView().findViewById(R.id.frameEnviarPrenda);
		previewFrontal=(ImageView)getView().findViewById(R.id.previewFrontal);
		previewPosterior=(ImageView)getView().findViewById(R.id.previewPosterior);
		frameMsjFinal=(RelativeLayout)getView().findViewById(R.id.frameMsjFinal);

		next = (ImageButton) getView().findViewById(R.id.next);
		take = (ToggleButton) getView().findViewById(R.id.take);
		//flashAutomatic = (ImageView) getView().findViewById(R.id.flash_automatico);
		flash= (ToggleButton) getView().findViewById(R.id.flash);
		grilla = (ToggleButton) getView().findViewById(R.id.grid);
		title = (TextView) getView().findViewById(R.id.title_lado);
		superior = (ImageView) getView().findViewById(R.id.superior);
		medio = (ImageView) getView().findViewById(R.id.medio);
		iconPreview = (ImageView) getView().findViewById(R.id.icon_preview);
		atrasCamara = (ImageView) getView().findViewById(R.id.atras_camara);
		atrasCamara2 = (ImageView)getView().findViewById(R.id.atras_camara2);

		/*
		atrasCamara.setEnabled(false);
		next.setEnabled(false);
		flash.setEnabled(false);
		superior.setEnabled(false);
		medio.setEnabled(false);
		take.setEnabled(false);
*/

		atrasCamara.setOnClickListener(this);
		atrasCamara2.setOnClickListener(this);

		selectPrenda=(RelativeLayout)getView().findViewById(R.id.select_prenda);
		TextView textTitulo,textSuperior,textMedio;
		textTitulo=(TextView)getView().findViewById(R.id.titulo_select_prenda);
		textSuperior=(TextView)getView().findViewById(R.id.texto_select_superior);
		textMedio=(TextView)getView().findViewById(R.id.texto_select_medio);
		textTitulo.setTypeface(Util_Fonts.setLatoRegular(getActivity()));
		textSuperior.setTypeface(Util_Fonts.setLatoRegular(getActivity()));
		textMedio.setTypeface(Util_Fonts.setLatoRegular(getActivity()));

		contentCamera=(RelativeLayout)getView().findViewById(R.id.content_camara);
		selectPrenda.bringToFront();
		changeEnable(contentCamera, false);

		getView().findViewById(R.id.frame_select_superior).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				selectPrenda.setVisibility(View.GONE);
				contentCamera.bringToFront();
				changeEnable(contentCamera, true);
				iconPreview.setEnabled(false);
				next.setEnabled(false);
				refresh.setEnabled(false);
				superior.setVisibility(View.VISIBLE);
				//tvSuperior.setVisibility(View.VISIBLE);
				medio.setVisibility(View.GONE);
				//tvMedio.setVisibility(View.GONE);
				filtro = "superior";
				selectTipoPrenda=true;
                //txtHelpPhoto.setText(R.string.text_help_toma_foto);


                Bitmap iconHelp=BitmapFactory.decodeResource(getResources(),R.drawable.superior_frontal);
				Bitmap resizeIconHelp=Bitmap.createScaledBitmap(iconHelp, sizeHelpPhoto, sizeHelpPhoto, true);
				imgHelpPhoto2.setImageBitmap(resizeIconHelp);

                final RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)cruzGuia.getLayoutParams();
                params.setMargins(posXGuia, posYGuia, 0, 0);
                cruzGuia.setLayoutParams(params);

				Handler handler3 = new Handler(Looper.getMainLooper());
				handler3.postDelayed(new Runnable() {
					@Override
					public void run() {
                        imgHelpPhoto2.animate().alpha(0.0f).setDuration(2000);
						//imgHelpPhoto2.setVisibility(View.GONE);

                        cruzGuia.setVisibility(View.VISIBLE);
						//((RelativeLayout)getView().findViewById(R.id.afterHeader)).addView(cruzGuia);
						//surface.setCruzActivate(true);
						Log.e("surfacet2", surface.willNotDraw() + " tamaño:"+params.width+" "+params.height+" pos:"+posXGuia+" "+posYGuia);
					}
				}, 3000);

			}
		});
		getView().findViewById(R.id.frame_select_medio).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				selectPrenda.setVisibility(View.GONE);
				contentCamera.bringToFront();
				changeEnable(contentCamera, true);
				iconPreview.setEnabled(false);
				next.setEnabled(false);
				refresh.setEnabled(false);
				medio.setVisibility(View.VISIBLE);
				//tvMedio.setVisibility(View.VISIBLE);
				superior.setVisibility(View.GONE);
				//tvSuperior.setVisibility(View.GONE);
				filtro = "medio";
				selectTipoPrenda=true;
                /*txtHelpPhoto.setText("Encaja la prenda en el marco");
                final RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams)txtHelpPhoto.getLayoutParams();
                params2.setMargins(posXGuia, 2*posYGuia, 0, 0);
                txtHelpPhoto.setLayoutParams(params2);*/


                Bitmap iconHelp=BitmapFactory.decodeResource(getResources(),R.drawable.medio_frontal);
				Bitmap resizeIconHelp=Bitmap.createScaledBitmap(iconHelp, sizeHelpPhoto, sizeHelpPhoto, true);
				imgHelpPhoto2.setImageBitmap(resizeIconHelp);

                final RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)cruzGuia.getLayoutParams();
                params.setMargins(posXGuia, posYGuia, 0, 0);
                cruzGuia.setLayoutParams(params);

				Handler handler4 = new Handler(Looper.getMainLooper());
				handler4.postDelayed(new Runnable() {
					@Override
					public void run() {
                        imgHelpPhoto2.animate().alpha(0.0f).setDuration(2000);
						//imgHelpPhoto2.setVisibility(View.GONE);

                        cruzGuia.setVisibility(View.VISIBLE);
                        //surface.setCruzActivate(true);
						Log.e("surfacet2", surface.willNotDraw() + "");
					}
				}, 3000);
			}
		});

		frameMsj=(RelativeLayout)getView().findViewById(R.id.frameMsj);
		//txtListo=(TextView)getView().findViewById(R.id.txtListo);
		//frameOptions=(RelativeLayout)getView().findViewById(R.id.frameOptions);

		optionContinue=(RadioButton)getView().findViewById(R.id.opcionContinuar);
		optionToCatalogo=(RadioButton)getView().findViewById(R.id.opcionPasarCatalogo);

		//frameMsj.setVisibility(View.GONE);
		enviarPrenda=(Button)getView().findViewById(R.id.btnEnviarPrenda);
		enviarPrenda.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				DSCamera dsCamera = new DSCamera(context);
				dsCamera.generarCodePrenda();
				//txtListo.setText("Estamos procesando tu prenda,deseas cargar una nueva?");

				//frameOptions.setVisibility(View.VISIBLE);
				//enviarPrenda.setVisibility(View.GONE);
				frameEnviarPrenda.setVisibility(View.GONE);
				frameMsjFinal.setVisibility(View.VISIBLE);
				frameMsjFinal.bringToFront();
				//refresh.performClick();

				contador = 1;
				//  tittle.setText("Glup " + contador);
				imagea.setVisibility(View.GONE);
				imageb.setVisibility(View.GONE);
				next.setEnabled(false);
				take.setEnabled(true);
				take.setChecked(false);
				iconPreview.setVisibility(View.VISIBLE);
				tvIconPreview.setVisibility(View.VISIBLE);
				title.setText("TOMA FRONTAL");
				refresh.setChecked(false);
				refresh.setEnabled(false);
				Log.e("fotoA", iconPreview.getVisibility() + "");

				final Handler handler = new Handler(Looper.getMainLooper());
				handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						frameMsjFinal.setVisibility(View.GONE);
                        cruzGuia.setVisibility(View.VISIBLE);
					}
				}, 3000);
			}
		});
		optionContinue.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final Handler handler = new Handler(Looper.getMainLooper());
				handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						frameMsj.setVisibility(View.GONE);
						optionContinue.setChecked(false);
					}
				}, 1500);

			}
		});
		optionToCatalogo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final Handler handler = new Handler(Looper.getMainLooper());
				handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						frameMsj.setVisibility(View.GONE);
						optionToCatalogo.setChecked(false);
						((Principal) context).onBackPressed();
					}
				}, 1500);

			}
		});

		Log.e("superior", getRelativeTop(superior) + "dp en pixeles " + (int) convertDpToPixel(getRelativeTop(superior), context));
		surface = new CameraSurface(context,(int)convertDpToPixel(getRelativeTop(superior),context));

		isFlash = new Flash();


		Log.e("surfacet1", surface.willNotDraw() + " " + surface.getSizeHelpImages());
		/*if (!surface.willNotDraw()){
			Bitmap iconHelp=BitmapFactory.decodeResource(getResources(),R.drawable.superior_frontal);
			Bitmap resizeIconHelp=Bitmap.createScaledBitmap(iconHelp, surface.getSizeHelpImages(), surface.getSizeHelpImages(), true);
			imgHelpPhoto2.setImageBitmap(resizeIconHelp);
		}*/





		/*flashAutomatic.setOnClickListener(new View.OnClickListener() {
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
		Log.e(null, "initUI " + CameraUtils.isFlash(context));

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
		}*/
		superior.setOnClickListener(this);
		medio.setOnClickListener(this);
		//iconPreview.setEnabled(false);
		imagea.setOnClickListener(this);
		imageb.setOnClickListener(this);
		//next.setEnabled(false);
		//take.setEnabled(true);



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
		//refresh.setEnabled(false);
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
				Handler handler = new Handler(Looper.getMainLooper());
				handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refresh.setChecked(false);
                        //refresh.invalidate();
                    }
                }, 1200);

				iconPreview.setVisibility(View.VISIBLE);
				tvIconPreview.setVisibility(View.VISIBLE);
				title.setText("TOMA FRONTAL");
				imgHelpPhoto.setImageResource(R.drawable.superior_help_a);
				//txtHelpPhoto.setText(R.string.text_help_toma_foto);

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
					DSCamera dsCamera = new DSCamera(context);
					dsCamera.flashAutomatico(isFlash);
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

		frameListo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				frameMsj.setVisibility(View.GONE);
                cruzGuia.setVisibility(View.GONE);
				if(contador==1){
					frameEnviarPrenda.setVisibility(View.VISIBLE);
					frameEnviarPrenda.bringToFront();
				}else{
					Bitmap iconHelpB;
					if (filtro.equals("superior")){
						iconHelpB=BitmapFactory.decodeResource(getResources(),R.drawable.superior_posterior);
					}else {
						iconHelpB=BitmapFactory.decodeResource(getResources(),R.drawable.medio_posterior);
					}
					Bitmap resizeIconHelp=Bitmap.createScaledBitmap(iconHelpB, sizeHelpPhoto, sizeHelpPhoto, true);
					imgHelpPhoto2.setImageBitmap(resizeIconHelp);
                    imgHelpPhoto2.animate().alpha(1.0f);
					//imgHelpPhoto2.setVisibility(View.VISIBLE);
					Handler handler5 = new Handler(Looper.getMainLooper());
					handler5.postDelayed(new Runnable() {
						@Override
						public void run() {
                            imgHelpPhoto2.animate().alpha(0.0f).setDuration(2000);
                            //imgHelpPhoto2.setVisibility(View.GONE);
                            cruzGuia.setVisibility(View.VISIBLE);
                            //surface.setCruzActivate(true);
						}
					}, 3000);
				}
			}
		});
		frameCancelar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (contador==2){
					imageb.setVisibility(View.GONE);
					contador=1;
					iconPreview.setVisibility(View.VISIBLE);
					tvIconPreview.setVisibility(View.VISIBLE);
					imgHelpPhoto.setImageResource(R.drawable.superior_help_a);
					//txtHelpPhoto.setText(R.string.text_help_toma_foto);
                    title.setText("TOMA FRONTAL");
				} else if (contador==1){
					imagea.setVisibility(View.GONE);
					iconPreview.setVisibility(View.GONE);
					tvIconPreview.setVisibility(View.GONE);
					imgHelpPhoto.setImageResource(R.drawable.superior_help_b);
					//txtHelpPhoto.setText(R.string.text_help_toma_foto);
					take.setEnabled(true);
					take.setChecked(false);
					contador=2;
                    title.setText("TOMA POSTERIOR");
				}

				frameMsj.setVisibility(View.GONE);
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		BusHolder.getInstance().register(this);
		//flashAutomatic.setVisibility(View.GONE);
		flash.setVisibility(View.VISIBLE);
		flash.setChecked(false);
		filtro="superior";
		medio.setVisibility(View.GONE);
		//tvMedio.setVisibility(View.GONE);
		superior.setVisibility(View.VISIBLE);
		//tvSuperior.setVisibility(View.VISIBLE);
	}

	@Override
	public void onPause() {
		super.onPause();
		BusHolder.getInstance().register(this);
		if (!CameraUtils.isFlash(context)) {
			//Utils.showMessage(CameraGlup.this, "Este dispositivo no tiene Flash");
			//flashAutomatic.setVisibility(View.GONE);
			flash.setVisibility(View.VISIBLE);
			flash.setChecked(false);
			return;
		} else {
			if (surface!=null){
				Log.e("Resumen","entro ");
				DSCamera dsCamera = new DSCamera(context);
				dsCamera.flashAutomatico(isFlash);
			}
		}
		//flashAutomatic.setVisibility(View.GONE);
		flash.setVisibility(View.VISIBLE);
		flash.setChecked(false);
		filtro="superior";
		medio.setVisibility(View.GONE);
		//tvMedio.setVisibility(View.GONE);
		superior.setVisibility(View.VISIBLE);
		//tvSuperior.setVisibility(View.VISIBLE);
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
				/*filtro="medio";
				superior.setVisibility(View.GONE);
				tvSuperior.setVisibility(View.GONE);
				medio.setVisibility(View.VISIBLE);
				tvMedio.setVisibility(View.VISIBLE);*/
				break;
			case R.id.medio:
				/*filtro="superior";
				medio.setVisibility(View.GONE);
				tvMedio.setVisibility(View.GONE);
				superior.setVisibility(View.VISIBLE);
				tvSuperior.setVisibility(View.VISIBLE);*/
				break;
			case R.id.atras_camara:
				contador=1;
				((Principal)context).onBackPressed();
				break;
			case R.id.atras_camara2:
				contador=1;
				((Principal)context).onBackPressed();
				break;
		}

	}




	@Subscribe
	public void onSuccess(CameraSurface.SuccessSavePhoto successSavePhoto) {
		Log.e(null, successSavePhoto.msg);
		//toCanny(result);
		final Handler handler = new Handler(Looper.getMainLooper());
		switch (contador) {
			case 1:
				//frameOptions.setVisibility(View.GONE);
				txtPie.setText("FRONTAL");
				frameMsj.setVisibility(View.VISIBLE);
				//txtListo.setText("Listo");
				frameMsj.bringToFront();

				iconPreview.setVisibility(View.GONE);
				tvIconPreview.setVisibility(View.GONE);
				Log.e("ver", iconPreview.getVisibility() + "");
				contador++;
				refresh.setEnabled(true);
				refresh.setChecked(false);
				Picasso.with(context).load("file:" + successSavePhoto.result).fit().into(imageb);
				Log.e("fotoA", successSavePhoto.result + " iconPreviewvisible:" + iconPreview.getVisibility());
				oldImageNameA=successSavePhoto.result;
				iconPreview.setVisibility(View.GONE);
				tvIconPreview.setVisibility(View.GONE);
				imageb.setBackgroundColor(Color.WHITE);
				imageb.setVisibility(View.VISIBLE);
				imageb.setTag(successSavePhoto.result);
				take.setChecked(false);
				title.setText("TOMA POSTERIOR");
				/*handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						frameMsj.setVisibility(View.GONE);
					}
				}, 2000);*/

				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inPreferredConfig = Bitmap.Config.ARGB_8888;
				Bitmap bitmap = BitmapFactory.decodeFile(imageb.getTag().toString(), options);
				previewPhoto.setImageBitmap(bitmap);
				previewFrontal.setImageBitmap(bitmap);

				imgHelpPhoto.setImageResource(R.drawable.superior_help_b);
				//txtHelpPhoto.setText(R.string.text_help_toma_foto);
				break;
			case 2:
				txtPie.setText("POSTERIOR");
				title.setText("TOMA POSTERIOR");
				Picasso.with(context).load("file:" + successSavePhoto.result).fit().into(imagea);
				Log.e("fotoB", successSavePhoto.result);
				contador--;
				oldImageNameB=successSavePhoto.result;
				imagea.setBackgroundColor(Color.RED);
				imagea.setVisibility(View.VISIBLE);
				imagea.setTag(successSavePhoto.result);
				next.setEnabled(true);
				take.setEnabled(false);
				take.setChecked(true);
				//
				frameMsj.setVisibility(View.VISIBLE);
				//txtListo.setText("Listo");
				//enviarPrenda.setVisibility(View.VISIBLE);
				frameMsj.bringToFront();

				BitmapFactory.Options options1 = new BitmapFactory.Options();
				options1.inPreferredConfig = Bitmap.Config.ARGB_8888;
				Bitmap bitmap1 = BitmapFactory.decodeFile(imagea.getTag().toString(), options1);
				previewPhoto.setImageBitmap(bitmap1);
				previewPosterior.setImageBitmap(bitmap1);


				imgHelpPhoto.setImageResource(R.drawable.superior_help_a);
				//txtHelpPhoto.setText(R.string.text_help_toma_foto);
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


			}
		}
	}
	@Subscribe
	public void getResponseUploadPrenda(DSCamera.ResponseUploadPrenda responseUploadPrenda){
		//Utils.showMessage(context,"Listo termino su carga");



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

	public float convertPixelsToDp(float px, Context context){
		float dp = px / (context.getResources().getDisplayMetrics().densityDpi / 160f);
		return dp;
	}

	private static void changeEnable(ViewGroup layout,boolean enable) {
		layout.setEnabled(enable);
		for (int i = 0; i < layout.getChildCount(); i++) {
			View child = layout.getChildAt(i);
			if (child instanceof ViewGroup) {
				changeEnable((ViewGroup) child, enable);
			} else {
				child.setEnabled(enable);
			}
		}
	}

	@Subscribe
	public void getSizePhotosHelp(CameraSurface.SizesHelp sizesHelp){
		Log.e("surfacet3",sizesHelp.sizeHelpImg+"");
		sizeHelpPhoto=sizesHelp.sizeHelpImg;
		posXGuia=sizesHelp.cruzX;
		posYGuia=sizesHelp.cruzY;
		getView().findViewById(R.id.frame_select_superior).setEnabled(true);


	}
}
