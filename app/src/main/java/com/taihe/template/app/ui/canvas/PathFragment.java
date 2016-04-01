package com.taihe.template.app.ui.canvas;

import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Region;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.taihe.template.app.R;

/**
 * Created by Administrator on 2016/2/25.
 */
public class PathFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return new View(getContext()) {
            private Paint mPaint;
            private Path mPath;

            public void init() {
                setFocusable(true);
                mPaint = new Paint();
                mPaint.setAntiAlias(true);
                mPaint.setStrokeWidth(6);
                mPaint.setTextSize(16);
                mPaint.setTextAlign(Paint.Align.RIGHT);

                mPath = new Path();
            }

            private void drawScene(Canvas canvas) {
                canvas.clipRect(0, 0, 100, 100);

                canvas.drawColor(Color.WHITE);

                mPaint.setColor(Color.RED);
                canvas.drawLine(0, 0, 100, 100, mPaint);

                mPaint.setColor(Color.GREEN);
                canvas.drawCircle(30, 70, 30, mPaint);

                mPaint.setColor(Color.BLUE);
                canvas.drawText("Clipping", 100, 30, mPaint);
            }

            @Override
            protected void onDraw(Canvas canvas) {
                init();
                canvas.clipRect(0, 0, 500, 500);
                Path path = new Path();
                path.addCircle(250, 250, 200, Path.Direction.CCW);
                canvas.clipPath(path, Region.Op.INTERSECT);
                canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.guide1), 0,0, new Paint());
//                canvas.drawColor(Color.GRAY);
//                canvas.save();
//                canvas.translate(10, 10);
//                drawScene(canvas);
//                canvas.restore();
//
//                canvas.save();
//                canvas.translate(160, 10);
//                canvas.clipRect(10, 10, 90, 90);
//                canvas.clipRect(30, 30, 70, 70, Region.Op.DIFFERENCE);
//                drawScene(canvas);
//                canvas.restore();
//
//                canvas.save();
//                canvas.translate(10, 160);
//                mPath.reset();
//                canvas.clipPath(mPath); // makes the clip empty
//                mPath.addCircle(50, 50, 50, Path.Direction.CCW);
//                canvas.clipPath(mPath, Region.Op.REPLACE);
//                drawScene(canvas);
//                canvas.restore();
//
//                canvas.save();
//                canvas.translate(160, 160);
//                canvas.clipRect(0, 0, 60, 60);
//                canvas.clipRect(40, 40, 100, 100, Region.Op.UNION);
//                drawScene(canvas);
//                canvas.restore();
//
//                canvas.save();
//                canvas.translate(10, 310);
//                canvas.clipRect(0, 0, 60, 60);
//                canvas.clipRect(40, 40, 100, 100, Region.Op.XOR);
//                drawScene(canvas);
//                canvas.restore();
//
//                canvas.save();
//                canvas.translate(160, 310);
//                canvas.clipRect(0, 0, 60, 60);
//                canvas.clipRect(40, 40, 100, 100, Region.Op.REVERSE_DIFFERENCE);
//                drawScene(canvas);
//                canvas.restore();
            }
        };
    }
}
