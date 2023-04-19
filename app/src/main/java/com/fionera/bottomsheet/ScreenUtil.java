package com.fionera.bottomsheet;

import android.content.Context;

/**
 * ScreenUtil
 *
 * @author fionera
 * @date 2023/3/14 in BehaviorChanges.
 */
class ScreenUtil {

   public static int getStatusHeight(Context context) {
      int statusHeight = -1;
      try {
         Class<?> clazz = Class.forName("com.android.internal.R$dimen");
         Object object = clazz.newInstance();
         int height = Integer.parseInt(clazz.getField("status_bar_height").get(object).toString());
         statusHeight = context.getResources().getDimensionPixelSize(height);
      } catch (Exception e) {
         e.printStackTrace();
      }
      return statusHeight;
   }

   public static int getNavigationHeight(Context context) {
      int statusHeight = -1;
      try {
         Class<?> clazz = Class.forName("com.android.internal.R$dimen");
         Object object = clazz.newInstance();
         int height = Integer.parseInt(clazz.getField("navigation_bar_height").get(object).toString());
         statusHeight = context.getResources().getDimensionPixelSize(height);
      } catch (Exception e) {
         e.printStackTrace();
      }
      return statusHeight;
   }

}
