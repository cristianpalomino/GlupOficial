package pe.com.glup.views;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

public class SquareLayout extends FrameLayout {

    private final double mScale = 1.0;

    private boolean reobtainSize = true;
    private int mHeight = -1;
    private int mWidth = -1;

    private int width,height;
    private  int ancho,altura;
    public SquareLayout(Context context) {
        super(context);
    }
    public SquareLayout(Context context,int ancho,int alto){
        super(context);
        this.ancho=ancho;
        this.altura=alto;
    }

    public SquareLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void init() {
        getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    public void onGlobalLayout() {
                        if (!SquareLayout.this.isShown() ||
                                (mHeight != -1 && mWidth != -1) ||
                                !reobtainSize)
                            return;

                        reobtainSize = false;
                        mHeight = SquareLayout.this.getMeasuredHeight();
                        mWidth = SquareLayout.this.getMeasuredWidth();
                        //Log.e("ANCHOR2",mWidth+"");
                        //Log.e("LARGOR2",mHeight+"");
                    }
                });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        width = MeasureSpec.getSize(widthMeasureSpec);
        //Log.e("ANCHOR",width+"");
        height = MeasureSpec.getSize(heightMeasureSpec);
        //Log.e("LARGOOR", height+"");
        if (width > (int) ((mScale * height) + 0.5)) {
            //Log.e(null,"se modifico ancho");
            width = (int) ((mScale * height) + 0.5);
        } else {
            //Log.e(null,"se modifico alto");
            height = (int) ((width / mScale) + 0.5);
        }

        super.onMeasure(
                MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
        );
    }
    @Override
    public void onDraw(Canvas canvas){
        /*Paint p = new Paint();
        p.setColor(Color.RED);
        p.setStyle(Paint.Style.STROKE);
        width = SquareLayout.this.getMeasuredWidth();
        height = SquareLayout.this.getMeasuredHeight();
        //Log.e("anchoAlto",width+" "+height);
        canvas.drawRect((int) (20),
                (int) (height * 0.16),
                (int) (width* 0.95),
                (int)(height* 0.96),
                    p);*/

        super.onDraw(canvas);
    }

    public void setVisibility(int value) {
        reobtainSize = true;
        super.setVisibility(value);
    }

    public int getmHeight() {
        return mHeight;
    }

    public int getmWidth() {
        return mWidth;
    }

}