package com.linxtu.tvmonitor;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.Toast;

import com.linxtu.tvmonitor.widget.InfoLinearLayout;


/**
 * Created by runyu on 2017/8/4.
 */
public class TvMonitorService extends Service{
    private final Boolean DEBUG = true;
    private final String TAG = "TvMonitorService";

    private WindowManager wManager;// 窗口管理者
    private WindowManager.LayoutParams mParams;// 窗口的属性
    private InfoLinearLayout infoLinearLayout;
    private Context mContext;
    private Boolean isWindowShowing = false;

    @Override
    public void onCreate() {
        if(DEBUG){
            Log.d(TAG,"onCreate");
        }
        super.onCreate();
        mContext = getApplicationContext();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String command = intent == null ? null : intent.getStringExtra("command");
        if(DEBUG){
            Log.d(TAG,"onStartCommand: " + command);
            Log.d(TAG,"isWindowShowing: " + isWindowShowing);
        }
        if ("show".equals(command)) {
            if(!isWindowShowing) {
                // init WindowManager
                isWindowShowing = true;
                initWindow();
                infoLinearLayout = new InfoLinearLayout(mContext);
                wManager.addView(infoLinearLayout, mParams);
                infoLinearLayout.updateInfo();
            }
            else{
                Toast toast = Toast.makeText(mContext, "what?已经显示了", Toast.LENGTH_SHORT);
                toast.show();
            }
        } else if ("hide".equals(command)) {
            if(isWindowShowing){
                wManager.removeView(infoLinearLayout);
                infoLinearLayout.cancleUpdateInfo();
                mParams = null;
                isWindowShowing = false;
            }
            else{
                Toast toast = Toast.makeText(mContext, "what?都没显示", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    protected void initWindow(){
        wManager = (WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE);
        mParams = new WindowManager.LayoutParams();
        mParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;// 系统提示window
        mParams.format = PixelFormat.TRANSLUCENT;// 支持透明
        //mParams.format = PixelFormat.RGBA_8888;
        mParams.flags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;// 焦点
        mParams.flags |= WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        mParams.width = displayMetrics.widthPixels/2;//窗口的宽和高
        mParams.height = displayMetrics.heightPixels;
        mParams.x = displayMetrics.widthPixels/2;//窗口位置的偏移量
        mParams.y = 0;
    }


    @Override
    public void onDestroy() {
        if(DEBUG){
            Log.d(TAG,"onDestroy");
        }
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
