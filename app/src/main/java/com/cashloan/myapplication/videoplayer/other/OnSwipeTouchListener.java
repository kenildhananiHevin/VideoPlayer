package com.cashloan.myapplication.videoplayer.other;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class OnSwipeTouchListener implements View.OnTouchListener {

   private GestureDetector gestureDetector;
   private float previousY;

   public OnSwipeTouchListener(Context c) {
      gestureDetector = new GestureDetector(c, new GestureListener());
   }

   public boolean onTouch(final View view, final MotionEvent motionEvent) {
      return gestureDetector.onTouchEvent(motionEvent);
   }

   private final class GestureListener extends GestureDetector.SimpleOnGestureListener {


      @Override
      public boolean onDown(MotionEvent e) {
         return true;
      }

      @Override
      public boolean onSingleTapUp(MotionEvent e) {
         onClick();
         return super.onSingleTapUp(e);
      }

      @Override
      public boolean onDoubleTap(MotionEvent e) {
         onDoubleClick();
         return super.onDoubleTap(e);
      }

      @Override
      public void onLongPress(MotionEvent e) {
         onLongClick();
         super.onLongPress(e);
      }

      public boolean onScroll(
              MotionEvent e1,
              MotionEvent e2,
              float velocityX,
              float velocityY
      ) {
         float currentY = e2.getY();
         if (currentY > previousY) {
            onSwipeDown();
         } else if (currentY < previousY) {
            onSwipeUp();
         }
         previousY = currentY;
         Log.e("TAG", "onScroll: " + e1.getAction());

         return false;
      }

   }

   public void onSwipeUp() {
   }

   public void onSwipeDown() {

   }

   public void onClick() {
   }

   private void onDoubleClick() {
   }

   private void onLongClick() {
   }
}
