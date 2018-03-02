package org.arxing.smooth_color;

import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Arxing on 2018/3/1.
 */
public class SmoothColorUtils {
    private int progress;
    private int maxProgress;
    private int startColor;
    private int endColor;
    private long duration;
    private Timer timer;
    private Handler handler;

    public SmoothColorUtils() {
        maxProgress = 100;
        duration = 200;
        handler = new Handler();
    }

    public static int smooth(int start, int end, int progress, int maxProgress) {
        int a, r, g, b;
        a = Color.alpha(start) + (Color.alpha(end) - Color.alpha(start)) * progress / maxProgress;
        r = Color.red(start) + (Color.red(end) - Color.red(start)) * progress / maxProgress;
        g = Color.green(start) + (Color.green(end) - Color.green(start)) * progress / maxProgress;
        b = Color.blue(start) + (Color.blue(end) - Color.blue(start)) * progress / maxProgress;
        return Color.argb(a, r, g, b);
    }

    public SmoothColorUtils setMaxProgress(int maxProgress) {
        this.maxProgress = maxProgress;
        return this;
    }

    public SmoothColorUtils setDuration(long duration) {
        this.duration = duration;
        return this;
    }

    public SmoothColorUtils setSmoothColor(int startColor, int endColor) {
        this.startColor = startColor;
        this.endColor = endColor;
        return this;
    }

    public void smooth(final OnColorChangeListener listener) {
        progress = 0;
        long period = (long) (duration / (float) maxProgress);
        TimerTask task = new TimerTask() {
            @Override public void run() {
                final int color = smooth(startColor, endColor, progress++, maxProgress);
                handler.post(new Runnable() {
                    @Override public void run() {
                        listener.onColorChange(color);
                    }
                });
                if (progress > maxProgress) {
                    timer.cancel();
                }
            }
        };
        if (timer != null)
            timer.cancel();
        timer = new Timer();
        timer.schedule(task, 0, period);
    }

    public void smoothTextColor(final TextView textView) {
        smooth(new OnColorChangeListener() {
            @Override public void onColorChange(int color) {
                textView.setTextColor(color);
            }
        });
    }

    public void smoothBackgroundColor(final View view) {
        smooth(new OnColorChangeListener() {
            @Override public void onColorChange(int color) {
                view.setBackgroundColor(color);
            }
        });
    }

    public interface OnColorChangeListener {
        void onColorChange(int color);
    }
}
