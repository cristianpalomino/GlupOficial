package pe.com.glup.glup;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;


import com.squareup.otto.Subscribe;

import org.rajawali3d.surface.IRajawaliSurface;
import org.rajawali3d.surface.RajawaliSurfaceView;

import pe.com.glup.R;
import pe.com.glup.managers.bus.BusHolder;
import pe.com.glup.network.DSCatalogo;
import pe.com.glup.views.Render3D.Renderer;

/**
 * Created by Glup on 18/01/16.
 */
public class Vista3d extends AppCompatActivity {
    FrameLayout frame3d;
    TextView cargaTextura,cargaObj;
    //private RajawaliSurfaceView surface;
    //private Renderer renderer;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_3d);
        context=this;
        //cargaTextura=(TextView)findViewById(R.id.cargaTextura);
        //cargaObj=(TextView)findViewById(R.id.cargaObj);
        //cargaTextura.setText("Descargo Textura en: 0 msec");
        //cargaObj.setText("Descargo Obj en: 0 msec");

        BusHolder.getInstance().register(this);
        DSCatalogo dsCatalogo=new DSCatalogo(this);
        dsCatalogo.obtenerFilesObj(getIntent().getStringExtra("codigoPrenda"));
        //surface = new RajawaliSurfaceView(this);

        /*surface.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.e("down", "action");
                        //renderer.getCurrentScene().clearAnimations();
                        renderer.onPause();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        ArcballCamera arcball = new ArcballCamera(context, findViewById(R.id.frame3d));
                        arcball.setPosition(0, 0, 4);
                        renderer.getCurrentScene().replaceAndSwitchCamera(renderer.getCurrentCamera(), arcball);
                        renderer.onResume();
                        Log.e("Move", "action");
                        break;
                    case MotionEvent.ACTION_UP:

                        renderer.onResume();
                        //ActiveAnimation activeAnimation=new ActiveAnimation();
                        //BusHolder.getInstance().post(activeAnimation);
                        //renderer.removeScene(renderer.getCurrentScene());
                        //renderer.initScene();
                        //renderer.onResume();
                        //renderer.initScene();
                        Log.e("Up", "action");

                        break;
                }
                return true;
            }
        });*/


    }
    @Subscribe
    public void pintar(DSCatalogo.InputRenderer inputRenderer){
        //frame3d= (FrameLayout) findViewById(R.id.frame3d);
        final RajawaliSurfaceView surface=new RajawaliSurfaceView(this);
        surface.setFrameRate(60.0);
        surface.setRenderMode(IRajawaliSurface.RENDERMODE_WHEN_DIRTY);
        Renderer renderer = new Renderer(this,inputRenderer.filenameTexture,inputRenderer.pathTexture,inputRenderer.pathObj,inputRenderer.filenameObj);
        surface.setSurfaceRenderer(renderer);
        addContentView(surface, new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT));

        Log.e("renderTime", surface.getDrawingTime() + "");
        //frame3d.addView(surface);
        //cargaTextura.setText("Descargo textura tiempo:" + inputRenderer.timeTextura + " msec");
        //cargaObj.setText("Descargo obj tiempo:" + inputRenderer.timeObj + " msec");
    }
    public class ActiveAnimation{}


}
