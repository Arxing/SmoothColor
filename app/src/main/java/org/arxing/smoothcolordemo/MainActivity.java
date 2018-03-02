package org.arxing.smoothcolordemo;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.arxing.smooth_color.SmoothColorUtils;
import org.arxing.viewbinder.ViewBinder;

public class MainActivity extends Activity {
    @ViewBinder.Bind(R.id.btnStartColor) Button btnStartColor;
    @ViewBinder.Bind(R.id.btnEndColor) Button btnEndColor;
    @ViewBinder.Bind(R.id.btnTransfer) Button btnTransfer;
    @ViewBinder.Bind(R.id.edtStartColor) EditText edtStartColor;
    @ViewBinder.Bind(R.id.edtEndColor) EditText edtEndColor;
    @ViewBinder.Bind(R.id.bgStartColor) View bgStartColor;
    @ViewBinder.Bind(R.id.bgEndColor) View bgEndColor;
    @ViewBinder.Bind(R.id.bgResultColor) View bgResultColor;
    @ViewBinder.Bind(R.id.edtMaxProgress) EditText edtMaxProgress;
    @ViewBinder.Bind(R.id.surfaceView) SurfaceView surfaceView;
    @ViewBinder.Bind(R.id.edtDuration) EditText edtDuration;

    private SurfaceHolder holder;
    private int startColor, endColor;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewBinder.bindViews(this);

        btnStartColor.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                try {
                    String colorText = edtStartColor.getText().toString();
                    startColor = Color.parseColor(colorText);
                    bgStartColor.setBackgroundColor(startColor);
                    bgResultColor.setBackgroundColor(startColor);
                } catch (Exception e) {
                }
            }
        });
        btnEndColor.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                try {
                    String colorText = edtEndColor.getText().toString();
                    endColor = Color.parseColor(colorText);
                    bgEndColor.setBackgroundColor(endColor);
                } catch (Exception e) {
                }
            }
        });

        btnTransfer.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                drawSurfaceView();
                drawBgResult();
            }
        });

        holder = surfaceView.getHolder();
    }

    private void drawSurfaceView() {
        Canvas canvas = holder.lockCanvas();
        try {
            canvas.drawColor(Color.TRANSPARENT);
            int cW = canvas.getWidth();
            int cH = canvas.getHeight();
            int maxProgress = Integer.parseInt(edtMaxProgress.getText().toString());
            float sectionW = cW / (float) maxProgress;
            float l, t = 0, r, b = cH;
            Log.d("tag", String.format("canvas(%d, %d), sectionWidth=%f", cW, cH, sectionW));
            Paint paint = new Paint();
            for (int progress = 0; progress <= maxProgress; progress++) {
                int color = SmoothColorUtils.smooth(startColor, endColor, progress, maxProgress);
                paint.setColor(color);
                l = progress * sectionW;
                r = l + sectionW;
                canvas.drawRect(l, t, r, b, paint);
                Log.d("tag", String.format("drawRect(%f, %f, %f, %f)", l, t, r, b));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            holder.unlockCanvasAndPost(canvas);
        }
    }

    private void drawBgResult() {
        try {
            int duration = Integer.parseInt(edtDuration.getText().toString());
            int maxProgress = Integer.parseInt(edtMaxProgress.getText().toString());
            SmoothColorUtils scu = new SmoothColorUtils().setDuration(duration)
                                                         .setMaxProgress(maxProgress)
                                                         .setSmoothColor(startColor, endColor);
            scu.smoothBackgroundColor(bgResultColor);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
