package pe.com.glup.views;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.squareup.otto.Subscribe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import pe.com.glup.R;
import pe.com.glup.managers.bus.BusHolder;
import pe.com.glup.models.events.Enfocar;
import pe.com.glup.models.events.Flash;
import pe.com.glup.models.events.TakePhoto;
import pe.com.glup.glup.Principal;
import pe.com.glup.utils.CameraUtils;
import pe.com.glup.utils.ImageUtils;



/**
 * Created by usuario on 23/04/15.
 */
public class CameraSurface extends SurfaceView implements SurfaceHolder.Callback{

    private static final double ASPECT_RATIO = 3.0 / 4.0;

    private Matrix mCameraToPreviewMatrix = new Matrix();
    private Matrix mPreviewToCameraMatrix = new Matrix();

    private boolean mHasFocusArea;
    private boolean gridActivate;



    private int mFocusScreenX;
    private int mFocusScreenY;

    private boolean isTakingPhoto;
    private boolean isControlMode;

    private Camera camera;
    private Context context;
    private SurfaceHolder mHolder;

    private Camera.Size mSurfaceSize;
    private Camera.Size mPictureSize;

    private String codPrenda;
    private int ancho,alto;
    private int margenSuperior;


    public CameraSurface(Context context, int i) {
        super(context);
        this.context = context;
        this.margenSuperior=i;
        initHolder();
        BusHolder.getInstance().register(this);
        codPrenda="";
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        /*if (width > height * ASPECT_RATIO) {
            width = (int) (height * ASPECT_RATIO + 0.5);
        }
        else {
            height = (int) (width / ASPECT_RATIO + 0.5);
        }*/
        setMeasuredDimension(width, height);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
        try {
            camera.setPreviewDisplay(mHolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        setWillNotDraw(false);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        //Bitmap bitmap1;
        //bitmap1 = Bitmap.createScaledBitmap(this.getDrawingCache(), this.getWidth(), this.getHeight(), true);
        Log.e("null", "ancho " + this.getWidth() + " alto " + this.getHeight());
        Log.e("static", "ancho " + width + " alto " + height);
        //Log.e("medidad"," bitmap "+bitmap1.getWidth()+"  "+getHeight());
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (mHolder.getSurface() == null || this.camera == null) {
            return;
        }
        try {
            this.camera.stopPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }
        startCameraPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }

    public synchronized void startCameraPreview() {
        determineDisplayOrientation();
        setupCamera();
        camera.startPreview();
    }

    private void initHolder() {
        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setKeepScreenOn(true);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void setupCamera() {
        setCameraPreviewSize();
        setPictureSize();
    }

    private void setCameraPreviewSize() {
        Camera.Parameters params = camera.getParameters();
        List<Camera.Size> previewSizes = params.getSupportedPreviewSizes();
        if (previewSizes.isEmpty()) {
            return;
        }

        Camera.Size bestSize = previewSizes.get(0);
        for (Camera.Size size : previewSizes) {
            if (size.width * size.height > bestSize.width * bestSize.height) {
                bestSize = size;
            }
        }
        mSurfaceSize = bestSize;
        params.setPreviewSize(mSurfaceSize.width, mSurfaceSize.height);
        camera.setParameters(params);
        adjustViewSize(mSurfaceSize);

    }

    private void setPictureSize() {
        Camera.Parameters params = camera.getParameters();
        params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);

        List<Camera.Size> sizes = params.getSupportedPictureSizes();
        Camera.Size currentSize = null;
        for (Camera.Size size : sizes) {
            if (currentSize == null || size.width > currentSize.width
                    || (size.width == currentSize.width && size.height > currentSize.height)) {
                currentSize = size;
            }
        }
        if (currentSize != null) {
            mPictureSize = currentSize;
            Log.e("picture",mPictureSize.width+" "+mPictureSize.height);
            params.setPictureSize(mPictureSize.width, mPictureSize.height);
            camera.setParameters(params);
        }
    }

    public void determineDisplayOrientation() {
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        Camera.getCameraInfo(Camera.CameraInfo.CAMERA_FACING_BACK, cameraInfo);
        int rotation = ((Principal) context).getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;

            case Surface.ROTATION_90:
                degrees = 90;
                break;

            case Surface.ROTATION_180:
                degrees = 180;
                break;

            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int displayOrientation;
        displayOrientation = (cameraInfo.orientation - degrees + 360) % 360;
        try {
            camera.setDisplayOrientation(displayOrientation);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint p = new Paint();
        if (camera != null) {
            int size = 100;
            p.setColor(Color.BLUE);
            p.setStrokeWidth(3);
            p.setStyle(Paint.Style.STROKE);
            /*if (mHasFocusArea) {
                canvas.drawRect(mFocusScreenX - size,
                        mFocusScreenY - size,
                        mFocusScreenX + size,
                        mFocusScreenY + size,
                        p);
            }*/
            int maxSize = 2048;
            int srcSize = Math.max(mPictureSize.width, mPictureSize.height);
            Log.e("null", "camara ancho " + mPictureSize.width + " alto " + mPictureSize.height);
            float inSampleSize = maxSize < srcSize ? 2 : 1;
            float width= (mPictureSize.width/inSampleSize);
            float height = (mPictureSize.height/inSampleSize);
            Log.e("null", "camara preview ancho " + mSurfaceSize.width + " alto " + mSurfaceSize.height);
            Log.e("null", "camara ancho " + width + " alto " + height);
            int tamano =  Math.min((int)width, (int)height);
            float previewRadio = (float) mSurfaceSize.height / (float) mSurfaceSize.width;
            float cameraRadio = height /  width;
            Log.e("null","size "+tamano+" previewRatio "+previewRadio+" cameraRatio "+cameraRadio);

            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            int length = (int) (tamano * (previewRadio / cameraRadio));
            Log.e("lenght",length+"");
            int ridArea = tamano - length;
            Log.e("ridArea",ridArea+"");
            p.setColor(getResources().getColor(R.color.celeste_glup));
            canvas.drawRect(5,
                    5,
                    length-5,
                    length-((int)(ridArea*0.25)),
                    p);
            if (gridActivate){
                p.setColor(getResources().getColor(R.color.celeste_glup));
                p.setStrokeWidth(4);
                p.setStyle(Paint.Style.STROKE);
                int posy=length-(int)(ridArea*0.25);
                int sizeX=(int)((length-10)*0.3333);
                int sizeY=(int) ((posy-5)*0.33333);

                for (int j=5;j+sizeY<=posy;j=j+sizeY){
                    for (int i=5;i+sizeX<=length-5;i=i+sizeX){
                        canvas.drawRect(i,
                                j,
                                i + sizeX,
                                j + sizeY,
                                p);
                    }
                }
            }
        }
        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getPointerCount() != 1) {
            return true;
        }

        if (event.getAction() != MotionEvent.ACTION_UP) {
            return true;
        }

        if (isTakingPhoto || isControlMode) {
            return true;
        }

        camera.startPreview();
        camera.cancelAutoFocus();

        focusCamera(event);
        new CameraUtils().enfocar(camera);

        return true;
    }

    private ArrayList<Camera.Area> getAreas(float x, float y) {

        float[] coords = {x, y};

        mCameraToPreviewMatrix.reset();
        mCameraToPreviewMatrix.postRotate(90);
        mCameraToPreviewMatrix.postScale(getWidth() / 2000f, getHeight() / 2000f);
        mCameraToPreviewMatrix.postTranslate(getWidth() / 2f, getHeight() / 2f);

        if (!mCameraToPreviewMatrix.invert(mPreviewToCameraMatrix)) {

        }

        mPreviewToCameraMatrix.mapPoints(coords);

        x = coords[0];
        y = coords[1];

        int focusSize = 50;
        Rect rect = new Rect();
        rect.left = (int) x - focusSize;
        rect.right = (int) x + focusSize;
        rect.top = (int) y - focusSize;
        rect.bottom = (int) y + focusSize;

        if (rect.left < -1000) {
            rect.left = -1000;
            rect.right = rect.left + 2 * focusSize;
        } else if (rect.right > 1000) {
            rect.right = 1000;
            rect.left = rect.right - 2 * focusSize;
        }

        if (rect.top < -1000) {
            rect.top = -1000;
            rect.bottom = rect.top + 2 * focusSize;
        } else if (rect.bottom > 1000) {
            rect.bottom = 1000;
            rect.top = rect.bottom - 2 * focusSize;
        }

        ArrayList<Camera.Area> areas = new ArrayList<Camera.Area>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            areas.add(new Camera.Area(rect, 1000));
        }
        return areas;
    }

    private void focusCamera(MotionEvent event) {
        Camera.Parameters parameters = camera.getParameters();
        String focusMode = parameters.getFocusMode();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            if (parameters.getMaxNumFocusAreas() != 0 && focusMode != null &&
                    (focusMode.equals(Camera.Parameters.FOCUS_MODE_AUTO)
                            || focusMode.equals(Camera.Parameters.FOCUS_MODE_MACRO)
                            || focusMode.equals(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE))) {
                float x = event.getX();
                float y = event.getY();

                mHasFocusArea = true;
                mFocusScreenX = (int) x;
                mFocusScreenY = (int) y;
                invalidate();

                ArrayList<Camera.Area> areas = getAreas(event.getX(), event.getY());
                parameters.setFocusAreas(areas);

                if (parameters.getMaxNumMeteringAreas() != 0) { // also set metering areas
                    parameters.setMeteringAreas(areas);
                }
                camera.setParameters(parameters);
            }
        }
    }


    @Subscribe
    public void onTakePhoto(TakePhoto takePhoto) {
        if (isTakingPhoto) {
            return;
        }

        Camera.ShutterCallback shutterCallback = new Camera.ShutterCallback() {
            @Override
            public void onShutter() {

            }
        };

        Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                int maxSize = 2048;
                camera.stopPreview();

                BitmapFactory.Options options = new BitmapFactory.Options();

                options.inJustDecodeBounds = true;
                Log.e("Antes","camara ancho "+options.outWidth+" alto "+options.outHeight);
                BitmapFactory.decodeByteArray(data, 0, data.length, options);
                Log.e("Despues", "camara ancho " + options.outWidth + " alto " + options.outHeight);
                int width = options.outWidth;
                int height = options.outHeight;
                ancho=width; alto=height;

                int srcSize = Math.max(width, height);
                options.inSampleSize = maxSize < srcSize ? (srcSize / maxSize) : 1;
                options.inJustDecodeBounds = false;
                Bitmap tmp = BitmapFactory.decodeByteArray(data, 0, data.length, options);

                Log.e("null","camara ancho "+options.outWidth+" alto "+options.outHeight);
                int size = Math.min(options.outWidth, options.outHeight);
                float previewRatio = (float) mSurfaceSize.height / (float) mSurfaceSize.width;
                Log.e("null", "camara preview ancho " + mSurfaceSize.width + " alto " + mSurfaceSize.height);
                Log.e("null","camara ancho "+options.outWidth+" alto "+options.outHeight);
                float cameraRatio = (float) options.outHeight / (float) options.outWidth;

                Matrix matrix = new Matrix();
                matrix.postRotate(90);
                Log.e("null","size "+size+" previewRatio "+previewRatio+" cameraRatio "+cameraRatio);
                int length = (int) (size * (previewRatio / cameraRatio));
                Log.e("null", "lenght " + length);
                int rid = size - length;

                EnvioBitmap envioBitmap=new EnvioBitmap();
                envioBitmap.bitmap=tmp;
                BusHolder.getInstance().post(envioBitmap);


                Bitmap source = Bitmap.createBitmap(tmp,
                        0,
                        (int) (rid * 0.5),
                        length,
                        length,
                        matrix, true);

                /*
                        Bitmap source = Bitmap.createBitmap(tmp,
                        (int)(tmp.getWidth()*0.1),
                        (int)(tmp.getHeight()*0.1075),
                        (int)(tmp.getWidth()*0.75),
                        (int)(tmp.getHeight()*0.75),
                        matrix, true);

                        Bitmap source = Bitmap.createBitmap(tmp,
                        (int)(width*0.1),
                        (int)(height*0.1),
                        (int)(width*0.75),
                        (int)(height*0.75),
                        matrix, true);
                 */
                tmp.recycle();
                isTakingPhoto = false;
                savePhoto(source);

                /**
                 * CUSTOM
                 * OPTION = 2592*1944
                 * SURFACE = 960*1280
                 *
                 * NATIVE
                 * OPTION = 2592*1944
                 * SURFACE =  960*1280
                 */
            }
        };

        try {
            camera.takePicture(shutterCallback,null,pictureCallback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Subscribe
    public void turnFlash(Flash flash) {
        if (camera!=null){
            if (flash.isTurn()) {
                Camera.Parameters p = camera.getParameters();
                p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                camera.setParameters(p);
                camera.startPreview();
                flash.setTurn(false);
            } else {
                Log.e(null, "camara " + camera.getParameters());
                Camera.Parameters p = camera.getParameters();
                p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                camera.setParameters(p);
                camera.startPreview();
                flash.setTurn(true);
            }
            BusHolder.getInstance().post(new SuccessEnableFlash());
        }


    }

    @Subscribe
    public void autoFocus(Enfocar enfocar) {
        mHasFocusArea = false;
        invalidate();
    }

    private void adjustViewSize(Camera.Size size) {
        int width = getWidth();
        ViewGroup.LayoutParams layoutParams = this.getLayoutParams();
        layoutParams.width = width;
        float coefficient = (float) size.height / width;
        layoutParams.height = (int) (size.width * coefficient);
        this.setLayoutParams(layoutParams);
    }

    private void savePhoto(final Bitmap bitmap) {
        new AsyncTask<Void, Void, String>() {
            ProgressDialog dialog;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog = ProgressDialog.show(context, null, "Espere...", true, false);
            }

            @Override
            protected String doInBackground(Void... params) {
                try {
                    return ImageUtils.SaveImage(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String aVoid) {
                super.onPostExecute(aVoid);
                dialog.dismiss();
                camera.startPreview();
                SuccessSavePhoto successSavePhoto = new SuccessSavePhoto();
                successSavePhoto.success=1;
                successSavePhoto.msg="Se genero y guardo foto";
                successSavePhoto.result=aVoid;
                Log.e("TomaFoto:",aVoid);
                BusHolder.getInstance().post(successSavePhoto);
            }
        }.execute();
    }

    public void setGridActivate(boolean gridActivate) {
        this.gridActivate = gridActivate;
    }

    public class SuccessSavePhoto{
        public int success=0;
        public String msg="No hay mensaje";
        public String result;
    }

    public class EnvioBitmap{
        public Bitmap bitmap;
    }

    public class SuccessEnableFlash{}

}
