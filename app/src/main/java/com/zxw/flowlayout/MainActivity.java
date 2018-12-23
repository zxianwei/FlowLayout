package com.zxw.flowlayout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Demo class
 *
 * @author zxw
 * @date 2018/12/20
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FlowLayout flowlayout = findViewById(R.id.flowlayout);
        flowlayout.setOnitemClickListener(new FlowLayout.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int i) {
                TextView  tv= (TextView) v;
                Toast.makeText(MainActivity.this, ""+tv.getText().toString().trim(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
