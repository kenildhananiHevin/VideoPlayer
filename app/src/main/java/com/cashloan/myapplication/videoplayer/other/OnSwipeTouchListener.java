package com.cashloan.myapplication.videoplayer.other;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class OnSwipeTouchListener implements View.OnTouchListener {

   private GestureDetector gestureDetector;
   private float  previousY,previousX;

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
         float currentX = e2.getX();
         if (currentY > previousY) {
            onSwipeDown();
         } else if (currentY < previousY) {
            onSwipeUp();

         }
         if (currentX > previousX) {
            onSwipeLeft();
         } else if (currentX < previousX) {
            onSwipeRight();
         }
         previousY = currentY;
         previousX = currentX;
         Log.e("TAG", "onScroll: " + e1.getAction());
         return false;
      }

  /*    @Override
      public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
         float deltaX = e2.getX() - e1.getX();
         float deltaY = e2.getY() - e1.getY();

         if (Math.abs(deltaX) > Math.abs(deltaY)) {
            if (Math.abs(deltaX) > SWIPE_THRESHOLD && Math.abs(distanceX) > SWIPE_VELOCITY_THRESHOLD) {
               if (deltaX > 0) {
                  onSwipeRight();
               } else {
                  onSwipeLeft();
               }
               return true;
            }
         } else {
            if (Math.abs(deltaY) > SWIPE_THRESHOLD && Math.abs(distanceY) > SWIPE_VELOCITY_THRESHOLD) {
               if (deltaY > 0) {
                  onSwipeDown();
               } else {
                  onSwipeUp();
               }
               return true;
            }
         }
         return false;
      }*/

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

   public void onSwipeLeft() {
      Log.d("Swipe", "Left swipe detected");
   }


   public void onSwipeRight() {
      Log.d("Swipe", "Right swipe detected");
   }
}
