package pe.com.glup.views.Render3D;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.MotionEvent;

import com.squareup.otto.Subscribe;

import org.rajawali3d.Object3D;
import org.rajawali3d.animation.Animation;
import org.rajawali3d.animation.Animation3D;
import org.rajawali3d.animation.RotateOnAxisAnimation;
import org.rajawali3d.cameras.ArcballCamera;
import org.rajawali3d.cameras.Camera;
import org.rajawali3d.lights.PointLight;
import org.rajawali3d.loader.LoaderOBJ;
import org.rajawali3d.loader.ParsingException;
import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.textures.ATexture;
import org.rajawali3d.materials.textures.Texture;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.renderer.RajawaliRenderer;

import java.io.File;

import pe.com.glup.R;
import pe.com.glup.managers.bus.BusHolder;

/**
 * Created by Glup on 6/01/16.
 */
public class Renderer extends RajawaliRenderer{
    private PointLight mLight;
    private Object3D mObjectGroup;
    private Animation3D mCameraAnim, mLightAnim;
    private Camera mCamera;
    private float mRadius = -4.2f;
    private float mDegrees = 0;
    private float xpos = 0,xStartPos,xd;
    private String nomTextura,pathTexture,path3ds,nom3ds;


    public Renderer(Context context) {
        super(context);
        //BusHolder.getInstance().register(this);
    }
    public Renderer(Context context, String nomTextura, String pathTexture, String path3ds, String nom3ds) {
        super(context);
        this.nomTextura=nomTextura;
        this.pathTexture=pathTexture;
        this.path3ds=path3ds;
        this.nom3ds=nom3ds;
        BusHolder.getInstance().post(this);
    }

    @Override
    protected void initScene() {
        mLight = new PointLight();
        mLight.setPosition(0, 0, 4);
        mLight.setPower(0);
        getCurrentScene().setBackgroundColor(1, 1, 1, 0.8f);
        //getCurrentScene().addLight(mLight);
        mCamera=getCurrentCamera();
        getCurrentCamera().setZ(4);


        //Texture texture = new Texture("textura_prueba", BitmapFactory.decodeFile("mnt/sdcard/GlupFiles/textura_prueba.jpg"));
        //mTextureManager.addTexture(texture);
        //LoaderOBJ objParser = new LoaderOBJ(mContext.getResources(),mTextureManager, R.raw.obj_short_obj);
        //LoaderOBJ objParser = new LoaderOBJ(this,new File("mnt/sdcard/GlupFiles","obj_short_obj"));
        //LoaderOBJ objParser = new LoaderOBJ(this,new File("mnt/sdcard/GlupFiles","short_sin_suavizado"));
        //LoaderAWD objParser = new LoaderAWD(this,new File("mnt/sdcard/GlupFiles","awd_arrows.awd"));
        //Loader3DSMax objParser = new Loader3DSMax(this,new File("mnt/sdcard/GlupFiles","short.3ds"));
        Texture texture = new Texture("miTextura", BitmapFactory.decodeFile(pathTexture));

        //Texture texture= new Texture("000000000400001",BitmapFactory.decodeFile("/mnt/sdcard/GlupFiles/textures/000000000400001.jpg"));
        LoaderOBJ objParser = new LoaderOBJ(this,new File(path3ds,nom3ds));
        //Loader3DSMax objParser = new Loader3DSMax(this,new File("mnt/sdcard/GlupFiles","short.3ds"));
        try {

            objParser.parse();

            mObjectGroup = objParser.getParsedObject();

            //mObjectGroup.getMaterial().setColorInfluence(0);
            //mObjectGroup.getMaterial().getTextureList().clear();
            mObjectGroup.setScale(1);
            mObjectGroup.setY(-1);
            //mObjectGroup.setRotation(Vector3.Axis.X,90);


            //Log.e("NumHijos", mObjectGroup.getNumChildren() + " Color "+mObjectGroup.getMaterial().getColor());

            Material material = new Material();
            material.getTextureList().clear();
            material.setColorInfluence(0);
            material.addTexture(texture);
            /*
            Bitmap bitmap= BitmapFactory.decodeFile("mnt/sdcard/GlupFiles/textura_prueba.jpg");
            SquareTerrain.Parameters parameters= SquareTerrain.createParameters(bitmap);
            parameters.setDivisions(128);
            parameters.setTextureMult(16);

            parameters.setColorMapBitmap(bitmap);
            SquareTerrain mTerrain = TerrainGenerator.createSquareTerrainFromBitmap(parameters,true);
            mTerrain.setMaterial(material);
            */
            if (mObjectGroup.getNumChildren()==0){
                mObjectGroup.setMaterial(material);
                mObjectGroup.setDoubleSided(true);
            }else{
                mObjectGroup.getChildAt(0).getMaterial().getTextureList().clear();
                mObjectGroup.getChildAt(0).setMaterial(material);
                mObjectGroup.getChildAt(0).setDoubleSided(true);
            }
            getCurrentScene().addChild(mObjectGroup);
            //getCurrentScene().addChild(mTerrain);

            mCameraAnim = new RotateOnAxisAnimation(Vector3.Axis.Y, -360);
            mCameraAnim.setDurationMilliseconds(16000);
            mCameraAnim.setRepeatMode(Animation.RepeatMode.INFINITE);
            mCameraAnim.setTransformable3D(mObjectGroup);

        } catch (ParsingException e) {
            e.printStackTrace();
        } catch (ATexture.TextureException e) {
            e.printStackTrace();
        }

        /*mLightAnim = new EllipticalOrbitAnimation3D(new Vector3(),
                new Vector3(0, 10, 0), Vector3.getAxisVector(Vector3.Axis.Z), 0,
                360, EllipticalOrbitAnimation3D.OrbitDirection.CLOCKWISE);

        mLightAnim.setDurationMilliseconds(3000);
        mLightAnim.setRepeatMode(Animation.RepeatMode.INFINITE);
        mLightAnim.setTransformable3D(mLight);*/

        getCurrentScene().registerAnimation(mCameraAnim);
        //getCurrentScene().registerAnimation(mLightAnim);

        mCameraAnim.play();

        //mLightAnim.play();
        ArcballCamera arcball = new ArcballCamera(mContext, ((Activity)mContext).findViewById(R.id.frame3d));
        arcball.setPosition(0, 0, 4);
        getCurrentScene().replaceAndSwitchCamera(getCurrentCamera(), arcball);




    }
    @Override
    protected void onRender(long ellapsedRealtime, double deltaTime) {
        super.onRender(ellapsedRealtime, deltaTime);

    }


    @Override
    public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {}

    @Override
    public void onTouchEvent(MotionEvent me) {
        if (me.getAction() == MotionEvent.ACTION_DOWN) {
           xStartPos = me.getX();// Capture the start position to determine the delta
            Log.e("down", xStartPos + "");
            xpos = xStartPos;
        }
        if (me.getAction() == MotionEvent.ACTION_MOVE) {
            xd = xpos - me.getX();
            Log.e("move", xd + "");
            rotCamera(xd);// while the finger is on the screen rotate the camera according to X touch position
            xpos = me.getX();

        }

        try {
            Thread.sleep(15);
        } catch (Exception e) {
        }
    }

    public void rotCamera(float touchOffset)       {
        mDegrees += touchOffset/36;

        float x = (float) (mRadius* Math.cos(Math.toRadians(mDegrees)));
        float y = (float) (mRadius* Math.sin(Math.toRadians(mDegrees)));

        mCamera.setPosition(x, y, 0);
    }

    /*@Subscribe
    public void reanimateCamera(SecondActivity.ActiveAnimation activeAnimation){
        Log.e("ranimate", "entro");
        mCameraAnim = new RotateOnAxisAnimation(Vector3.Axis.Y, -360);
        mCameraAnim.setDurationMilliseconds(8000);
        mCameraAnim.setRepeatMode(Animation.RepeatMode.INFINITE);
        mCameraAnim.setTransformable3D(mObjectGroup);
        getCurrentScene().registerAnimation(mCameraAnim);
        //getCurrentScene().registerAnimation(mLightAnim);
        mCameraAnim.play();


    }*/
}
