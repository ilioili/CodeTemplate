package com.taihe.template.app;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.ilioili.appstart.R;
import com.taihe.template.app.base.BaseCircleTransitionActivity;

import java.util.Random;

public class CirclePopupActivity extends BaseCircleTransitionActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_popup);
        Random random = new Random();
        View view = findViewById(R.id.jump);
        view.setBackgroundColor(Color.argb(255, random.nextInt(255), random.nextInt(255), random.nextInt(255)));
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CirclePopupActivity.this, CirclePopupActivity.class));
            }
        });
    }
}
