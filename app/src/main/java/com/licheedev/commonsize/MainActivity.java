package com.licheedev.commonsize;

import android.content.res.Configuration;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;
import com.licheedev.commonsizedemo.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tvSw = (TextView) findViewById(R.id.tv_sw);

        System.out.println(R.dimen.sw_0dp);

        Configuration config = getResources().getConfiguration();
        int smallestScreenWidthDp = config.smallestScreenWidthDp;
        tvSw.setText(String.format("match_parent, smallest width is %d", smallestScreenWidthDp));
        System.out.println(smallestScreenWidthDp);
    }
}
