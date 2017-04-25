package com.mxth.zoomview;

/**
 * Created by Administrator on 2017/4/25.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

public class ZoomTextView extends TextView {

    private static final String TAG = "ZoomTextView";

    private float textSize;
    private int mode;
    private float oldDist;

    public ZoomTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public ZoomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ZoomTextView(Context context) {
        super(context);
    }

    /**
     * 处理TextView的触摸事件
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //在一开始，计算当前字体的大小
        if (textSize == 0) {
            textSize = this.getTextSize();
        }
        // 获取触摸事件的类型，如果是单点是event.getAction()，当涉及到多点触控时，就使用getActionMasked来获取触摸事件类型
        switch (event.getActionMasked()) {

            case MotionEvent.ACTION_POINTER_DOWN:
                // 当手指按下的时候，就去获取当前手指的间距
                oldDist = spacing(event);
                break;
            case MotionEvent.ACTION_MOVE:
                // 获取当前触摸点的个数
                if (event.getPointerCount() >= 2) {
                    // 如果触摸点>=2 获取当前两个手指的距离，然后进行缩放
                    float newDist = spacing(event);
                    zoom(newDist / oldDist);
                    //重新置位
                    oldDist = newDist;
                }
                break;
        }
        return true;
    }

    /**
     * 不断进行缩放
     *
     * @param f
     */
    private void zoom(float f) {
        textSize *= f;
        this.setTextSize(px2sp(getContext(), textSize));
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param context
     * @param pxValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 计算两个手指的大小
     *
     * @param event
     * @return
     */
    private float spacing(MotionEvent event) {
        //获取第一个点的x坐标和第二个点的x坐标
        float x = event.getX(0) - event.getX(1);
        //分别获取y坐标
        float y = event.getY(0) - event.getY(1);
        //使用勾股定理计算两点距离
        return (float) Math.sqrt(x * x + y * y);
    }
}