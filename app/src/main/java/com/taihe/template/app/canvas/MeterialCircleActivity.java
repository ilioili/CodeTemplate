package com.taihe.template.app.canvas;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.ilioili.appstart.R;
import com.taihe.template.app.base.AppBaseActivity;
import com.taihe.template.app.widget.CircleAnimationFrame;
import com.taihe.template.base.injection.Id;
import com.taihe.template.base.injection.Layout;

import java.util.Random;

@Layout(R.layout.activity_meterial_circle)
public class MeterialCircleActivity extends AppBaseActivity {
    @Id(R.id.rootView)
    private CircleAnimationFrame circleAnimationFrame;

    @Id(R.id.text)
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        randomActivityAppearance();
        circleAnimationFrame.expand(getIntent().getIntExtra("x", 0), getIntent().getIntExtra("y", 0), 500);
        textView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()== MotionEvent.ACTION_UP){
                    Intent intent = MeterialCircleActivity.newIntent(MeterialCircleActivity.this, (int) (event.getRawX()), (int) (event.getRawY()));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                }
                return true;
            }
        });
    }

    private void randomActivityAppearance() {
        Random random = new Random();
        int red = random.nextInt(255);
        int green = random.nextInt(255);
        int blue = random.nextInt(255);
        int bgColor = Color.rgb(red, green, blue);
        int textColor = Color.rgb(-red, -green, -blue);
        textView.setBackgroundColor(bgColor);
        textView.setTextColor(textColor);
        textView.setTextSize(random.nextFloat()*100+50);
    }

    public static Intent newIntent(Context context, int xLocationInWindow, int yLocationInWindow){
        Intent it = new Intent(context, MeterialCircleActivity.class);
        it.putExtra("x", xLocationInWindow);
        it.putExtra("y", yLocationInWindow);
        return it;
    }

    private boolean isFinishing;
    @Override
    public void finish() {
        if(isFinishing){
            return;
        }else{
            isFinishing = true;
        }
        circleAnimationFrame.collpase(getIntent().getIntExtra("x", 0), getIntent().getIntExtra("y", 0), 500, new CircleAnimationFrame.CompleteListener() {
            @Override
            public void onComplete() {
                MeterialCircleActivity.super.finish();
                overridePendingTransition(0, 0);
            }
        });
    }
}
