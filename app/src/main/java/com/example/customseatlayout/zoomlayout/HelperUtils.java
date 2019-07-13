package com.example.customseatlayout.zoomlayout;

import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.view.View;

public class HelperUtils {

    /**
     * This method will return bitmap image of seatlayout with rectangle drawn to preview
     * @param view input view whose image needs to be previewed
     * @param leftDiffrence rectangle left margin
     * @param topDifference rectangle top margin
     * @param rightDifference rectangel right margin
     * @param bottomDifference rectange bottom margin
     * @return bitmap with rectangel drawn on top of it
     */
    public static Bitmap getBitmapFromView(View view,int leftDiffrence,
                                           int topDifference,int rightDifference,int bottomDifference) {
        //Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        int left = leftDiffrence;
        int top = topDifference;
        int viewWidth = view.getWidth()-rightDifference;
        int viewHeight = view.getHeight()-bottomDifference;
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
        canvas.drawRect(left, top, viewWidth, viewHeight, myPaint);


        // draw the view on the canvas
        view.draw(canvas);
        //return the bitmap
        return returnedBitmap;
    }

    /**
     * This method will return cordinates of view after zoom as rectangle
     * @param view input view whose cordinates are required
     * @param realZoom input scale or current zoomed factor of view
     * @return rect cordinates of zoomed view
     */
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


    /**
     * Will return cordinates of view passed
     * @param view input view whose cordinates needs to be calculated
     * @return rect with cordinates of view
     */
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
}
