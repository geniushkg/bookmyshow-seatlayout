package com.example.customseatlayout.zoomlayout;

import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.view.View;

public class HelperUtils {

    public static Bitmap getBitmapFromView(View view,int left,
                                           int top,int right,int bottom) {
        //Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        int defaultleft = 0;
        int defaulttop = 0;
        int viewWidth = view.getWidth();
        int viewHeight = view.getHeight();
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable =view.getBackground();
        if (bgDrawable!=null) {
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        }   else{
            //does not have background drawable, then draw white background on the canvas
           // canvas.drawColor(Color.WHITE);
        }
        // draw rect on canvas
        Paint myPaint = new Paint();
        myPaint.setColor(Color.rgb(0, 0, 0));
        myPaint.setStrokeWidth(10);
        myPaint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(0, 0, viewWidth, viewHeight, myPaint);


        // draw the view on the canvas
        view.draw(canvas);
        //return the bitmap
        return returnedBitmap;
    }

    public static Rect locateViewWithZoom(View view, float realZoom) {
        Rect loc = new Rect();
        int[] location = new int[2];
        if (view == null) {
            return loc;
        }
        view.getLocationOnScreen(location);

        loc.left = location[0];
        loc.top = location[1];
        float widthofView = view.getWidth()*realZoom;
        float heightofView = view.getHeight()*realZoom;
        loc.right = loc.left + Math.round(widthofView);
        loc.bottom = loc.top + Math.round(heightofView);
        return loc;
    }


    public static Rect locateView(View view) {
        Rect loc = new Rect();
        int[] location = new int[2];
        if (view == null) {
            return loc;
        }
        view.getLocationOnScreen(location);

        loc.left = location[0];
        loc.top = location[1];
        loc.right = loc.left + view.getWidth();
        loc.bottom = loc.top + view.getHeight();
        return loc;
    }

    /* to check if view is visible just helper code only for refrence not used
    public static boolean isVisible(final View view) {
        if (view == null) {
            return false;
        }
        if (!view.isShown()) {
            return false;
        }
        final Rect actualPosition = new Rect();
        view.getGlobalVisibleRect(actualPosition);
        final Rect screen = new Rect(0, 0, getScreenWidth(), getScreenHeight());
        return actualPosition.intersect(screen);
    }
    */
}
