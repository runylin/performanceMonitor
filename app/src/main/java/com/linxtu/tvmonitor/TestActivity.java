package com.linxtu.tvmonitor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

/**
 * Created by runyu on 2017/8/5.
 */
public class TestActivity extends Activity implements View.OnClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.testlayout);
        Button show_Bt = (Button)findViewById(R.id.show_bt);
        Button hide_eBt = (Button)findViewById(R.id.hide_bt);
        show_Bt.setOnClickListener(this);
        hide_eBt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.show_bt:
                Intent show_intent = new Intent(this,TvMonitorService.class);
                show_intent.putExtra("command","show");
                startService(show_intent);
                break;
            case R.id.hide_bt:
                Intent hide_intent = new Intent(this,TvMonitorService.class);
                hide_intent.putExtra("command","hide");
                startService(hide_intent);
                break;
            default:
                break;
        }
    }
}
