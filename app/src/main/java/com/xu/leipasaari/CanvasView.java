package com.xu.leipasaari;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.util.Pair;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Custom View used in MainActivity Layout
 * https://examples.javacodegeeks.com/android/core/graphics/canvas-graphics/android-canvas-example/
 */
public class CanvasView extends ImageView  {

    public int width;
    public int height;
    public Bitmap mBitmap;
    private Canvas mCanvas;
    private Path mPath;
    Context context;

    private float mX, mY;
    private static final float TOLERANCE =5;
    //tOLERANCE EXISTS TO MAKE SURE WE ARE MOVING FINGER ENOUGH TO ACTUALLY REGISTER AS MOVEMENT
    List<Pair<Path,Integer>> path_color_list = new ArrayList<Pair<Path,Integer>>();
    //set initial color to blue, this is changed by setBrushColor
    private int color = R.color.Blue;



    //////Inner Class Stroke to handle each new color/////////
    private class Stroke{
        //stroke is used to track multiple paths, initialiized for each color swtich
        Path path;
        Paint paint;

        //Path traces where we draw
        //Create new Paint object with the attributes we want
            Stroke(int color){
                path = new Path();
                paint = new Paint();
                paint.setAntiAlias(true);
                paint.setStrokeWidth(20);
                paint.setColor(color);
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeJoin(Paint.Join.ROUND);
                paint.setStrokeCap(Paint.Cap.ROUND);

        }

        //OLD VERSION, NEED TO INTEGRATE THE PATH INTO THIS
        //Following methods are all called by movement, and action Up/Down
        //Action_Down below, move the path to trace accordingly
        private void startTouch(float x, float y){
            mPath.moveTo(x,y);
            mX= x;
            mY=y;
        }

        //Action_Move

        private void moveTouch(float x, float y) {
            float dx = Math.abs(x - mX);
            float dy = Math.abs(y-mY);
            //tolerance check to make sure we are moving for sure
            if(dx>= TOLERANCE || dy>=TOLERANCE){
                mPath.quadTo(mX,mY,(x+mX)/2,(y+mY)/2);
                //why use quadto?
                mX=x;
                mY=y;
            }
        }

        //Action_UP stop touch

        private void upTouch(){
            mPath.lineTo(mX, mY);
        }

    }

    private List<Stroke> strokeList = new ArrayList<Stroke>();

    public CanvasView (Context c, AttributeSet attrs){
        super(c, attrs);
        this.setDrawingCacheEnabled(true);
        init();
    }

    public CanvasView(Context c){
        super(c);
        this.setDrawingCacheEnabled(true);
        init();
    }

    private void init(){
        strokeList.add(new Stroke(color));
    }

    //Override onSizeChanged, CALLED WHEN SIZE NEEDS TO BE CALCULATED, background stuff
    //calls th set up for the canvas
    @Override
    protected void onSizeChanged(int w,  int h, int oldw, int oldh){
        super.onSizeChanged(w, h, oldw, oldh);

        //binding Canvas onto Bitmap

        mBitmap = Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }

    //Override onDraw to allow the drawing to take place
    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);

        //draw the mPath with mPaint
        //add a bit of extra code to prevent color change being retroactive on all previous drawn.
        // need to create a  list of strokes holding each path and color
        for (Stroke stroke:strokeList){
            canvas.drawPath(stroke.path,stroke.paint);
        }
    }


    //override the onTouchEvent, call the corresponding method to draw
    @Override
    public boolean onTouchEvent(MotionEvent event){
        float x =event.getX();
        float y = event.getY();

        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                //everytime we draw, we add a new stroke object
                strokeList.add(new Stroke(color));
                strokeList.get(strokeList.size()-1).path.moveTo(x,y);
                //get latestcolor
                return true;
                //invalidate forces view to redraw

            case MotionEvent.ACTION_MOVE:
                strokeList.get(strokeList.size()-1).path.lineTo(x,y);

                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                return false;

        }
        invalidate();
        return true;
    }

    //long press handler in the  main file
   // final GestureDetector gestureDetector = new GestureDetector(getContext(),new GestureDetector.SimpleOnGestureListener() {
        //public void onLongPress(MotionEvent e) {
            //launch the dialog required to send image or clear canvas
        //}
   // });

    //clean the canvas
    public void clearCanvas(){
        for (Stroke stroke:strokeList){
            stroke.path.reset();
        }
        this.destroyDrawingCache();
        invalidate();
    }

    public void setBrushColor(int color){
        this.color= color;
    }


    public Bitmap getBitmap(){
        //required in order to access bitmap from canvas
        //canvases have no natural way of giving out your bitmap
        return this.getDrawingCache();

    }

}