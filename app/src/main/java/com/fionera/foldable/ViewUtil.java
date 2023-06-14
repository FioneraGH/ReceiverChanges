package com.fionera.foldable;

import android.annotation.SuppressLint;
import android.text.Layout;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * ViewUtil
 *
 * @author fionera
 * @date 2023/6/12 in BehaviorChanges.
 */
public class ViewUtil {

   @SuppressLint("ClickableViewAccessibility")
   public static void fixClickConflict(TextView emojiTextView) {
      emojiTextView.setOnTouchListener(new View.OnTouchListener() {
         long actionDownTimeStamp;

         private float mInitX = Float.MIN_VALUE;
         private float mInitY = Float.MIN_VALUE;

         private final float mScrollDistance = 100;

         @Override
         public boolean onTouch(View v, MotionEvent event) {
            TextView tv = (TextView) v;
            CharSequence text = tv.getText();
            if (text instanceof Spanned) {
               if (event.getAction() == MotionEvent.ACTION_DOWN) {
                  actionDownTimeStamp = System.currentTimeMillis();
                  mInitX = event.getX();
                  mInitY = event.getY();
                  return true;
               } else if (event.getAction() == MotionEvent.ACTION_UP) {
                  int x = (int) event.getX();
                  int y = (int) event.getY();

                  if (Math.abs(x - mInitX) >= mScrollDistance || Math.abs(y - mInitY) >= mScrollDistance) {
                     return false;
                  }

                  x -= tv.getTotalPaddingLeft();
                  y -= tv.getTotalPaddingTop();

                  x += tv.getScrollX();
                  y += tv.getScrollY();

                  Layout layout = tv.getLayout();
                  int line = layout.getLineForVertical(y);
                  int offset = layout.getOffsetForHorizontal(line, x);

                  ClickableSpan[] link = ((Spanned) text).getSpans(offset, offset, ClickableSpan.class);
                  if (link.length != 0) {
                     link[0].onClick(tv);
                     return true;
                  } else {
                     if (System.currentTimeMillis() - actionDownTimeStamp > ViewConfiguration.getLongPressTimeout()) {
                        if (!v.performLongClick() && !((View) v.getParent()).performLongClick()) {
                           doClick(v, 0);
                           return true;
                        }
                     } else {
                        doClick(v, 0);
                        return true;
                     }
                  }
               }
               return false;
            }
            return false;
         }

         void doClick(View v, int depth) {
            if (v.hasOnClickListeners()) {
               v.performClick();
            } else {
               if (v.getParent() instanceof ViewGroup && depth < 10) {
                  doClick((View) v.getParent(), depth + 1);
               }
            }
         }
      });
   }
}
