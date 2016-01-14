package edu.msu.liunan.examliunan;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.io.Serializable;
import java.util.Random;

/**
 * TODO: document your custom view class.
 */
public class HaroldSpinView extends View {

    private static class Parameters implements Serializable {
        /**
         * spinCount and spinMoney used in textSpin
         */
        private int spinMoney=0;
        private int spinCount=0;
        /**
         * location of the box, 2D array index randomX and randomY
         */
        private int randomX=-5;
        private int randomY=-5;

        /** Spin rate in boxes per second */
        private double spinRate = START_RATE;
        /**
         * current button state
         * spinning true, start spinning
         * isStop stop spinning
         */
        private boolean spinning = false;
        private boolean isSTop=false;


    }

    /**
     * The image bitmap. None initially.
     */
    private Bitmap imageBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.haroldgrid);;

    /**
     * Image drawing scale
     */
    private float imageScale = 1;

    /**
     * Image left margin in pixels
     */
    private float marginLeft = 0;

    /**
     * Image top margin in pixels
     */
    private float marginTop = 0;


    public static final double START_RATE =2.0;


    private double SPEEDUP =1.01;

    /** Time we last called onDraw */
    private long lastTime = 0;

    /** How long a box has been on the screen */
    private double duration = 0;

    private Random rnd = new Random();



    private Paint boxPaint;


    /** Money grid */
    private int money[][] = {{1, 5, 100, -1},
            {-1, 100, 1, 50},
            {5, 50, -1, 20},
            {10, 10, -1, 1}};

    /**
     * The current parameters
     */
    private Parameters params = new Parameters();

    public HaroldSpinView(Context context) {
        super(context);
        init(null, 0);
    }

    public HaroldSpinView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public HaroldSpinView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        boxPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        boxPaint.setColor(Color.RED);
        boxPaint.setStrokeWidth(10);
        boxPaint.setStyle(Paint.Style.STROKE);
    }

    /**
     * Handle a draw event
     *
     * @param canvas canvas to draw on.
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        /*
         * Determine the margins and scale to draw the image
         * centered and scaled to maximum size on any display
         */
        // Get the canvas size
        float wid = canvas.getWidth();
        float hit = canvas.getHeight();

        // What would be the scale to draw the where it fits both
        // horizontally and vertically?
        float scaleH = wid / imageBitmap.getWidth();
        float scaleV = hit / imageBitmap.getHeight();

        // Use the lesser of the two
        imageScale = scaleH < scaleV ? scaleH : scaleV;

        // What is the scaled image size?
        float iWid = imageScale * imageBitmap.getWidth();
        float iHit = imageScale * imageBitmap.getHeight();

        // Determine the top and left margins to center
        marginLeft = (wid - iWid) / 2;
        marginTop = (hit - iHit) / 2;

        /*
         * Draw the image bitmap
         */
        canvas.save();
        canvas.translate(marginLeft, marginTop);
        canvas.scale(imageScale, imageScale);
        canvas.drawBitmap(imageBitmap, 0, 0, null);
        canvas.restore();

        if(params.spinning) {
            long time = System.currentTimeMillis();
            double delta = (time - lastTime) * 0.001;
            lastTime = time;

            duration += delta;
            if(duration >= 1.0 / params.spinRate ) {
                newCell();
                params.spinRate *= SPEEDUP;
                duration = 0;
            }
            drawBox(canvas);
            postInvalidate();
        }
        if (params.isSTop){
            drawBox(canvas);
        }

    }

    public void setSpinning(boolean spin){
        params.spinning = spin;
    }

    public boolean getSpinning(){
        return params.spinning;
    }

    public void newCell() {
        // generate two random Integer [0-3]
        params.randomX = rnd.nextInt(4);
        params.randomY = rnd.nextInt(4);
    }

    public void drawBox(Canvas canvas){
        // Compute left corner coordinate
        float iWid = imageScale * imageBitmap.getWidth();
        float iHit = imageScale * imageBitmap.getHeight();
        float boxSizeX= iWid/8;
        float boxSizeY=iHit/8;
        float boxCenterX=marginLeft+((2*params.randomX+1)*boxSizeX);
        float boxCenterY=marginTop+((2*params.randomY+1)*boxSizeY);
        //draw a box starting at left corner coordinates.
        canvas.save();
        canvas.translate(boxCenterX, boxCenterY);
        canvas.rotate(0);
        canvas.drawRect(-boxSizeX, -boxSizeY, boxSizeX, boxSizeY, boxPaint);
        canvas.restore();
    }

    public void setIsStop(boolean stop){
        params.isSTop=true;
    }

    public boolean getIsStop(){
        return params.isSTop;
    }

    public void calculateMoney(){
        //Log.i("Money[1][0]",String.valueOf(money[1][0]));
        if (money[params.randomY][params.randomX]==-1){
            //Log.i("money["+String.valueOf(randomX)+String.valueOf(randomY),String.valueOf(money[randomX][randomY]));
            params.spinMoney=0;
        }
        else{
            params.spinMoney+=money[params.randomY][params.randomX];
        }
    }
    public void calculateSpinCount(){
        if (params.spinning){
            params.spinCount++;
        }
    }
    public int getSpinMoney(){
        return params.spinMoney;
    }

    public int getSpinCount(){
        return params.spinCount;
    }
    public void setSpinMoney(int currentMoney){
        params.spinMoney=currentMoney;
    }
    public void setSpinCount(int currentCount){
        params.spinCount=currentCount;
    }

    public void setSpinRate(double rate){
        params.spinRate=rate;
    }

    public void setRandomX(int randomX){
        params.randomX=randomX;
    }

    public void setRandomY(int randomY){
        params.randomY=randomY;
    }
    public void putToBundle(String key, Bundle bundle){
        bundle.putSerializable(key, params);

    }
    public void getFromBundle(String key, Bundle bundle){
        params = (Parameters)bundle.getSerializable(key);

        // Ensure the options are all set
        setSpinMoney(params.spinMoney);
        setSpinCount(params.spinCount);
        setSpinning(params.spinning);
        setIsStop(params.isSTop);
        setSpinRate(params.spinRate);
        setRandomX(params.randomX);
        setRandomY(params.randomY);
    }

    public void startNewGame(){

        params.spinMoney=0;
        params.spinCount=0;
        /**
         * location of the box, 2D array index randomX and randomY
         */
        params.randomX=-5;
        params.randomY=-5;

        /** Spin rate in boxes per second */
        params.spinRate = START_RATE;
        /**
         * current button state
         * spinning true, start spinning
         * isStop stop spinning
         */
        params.spinning = false;
        params.isSTop=false;


    }
}
