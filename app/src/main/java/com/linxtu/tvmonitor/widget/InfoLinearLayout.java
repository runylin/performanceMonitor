package com.linxtu.tvmonitor.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.linxtu.tvmonitor.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
/**
 * Created by runyu on 2017/8/6.
 */
public class InfoLinearLayout extends LinearLayout {
    private Timer timer = new Timer();
    private Context mContext;

    private MemInfo memInfo;
    private ListView lv_meminfo;
    private SimpleAdapter meminfoAdapter;
    private List<Map<String, Object>> meminfoList = new ArrayList<Map<String, Object>>();
    private final int MEMINFO_LIST_NUM = 5;
    private final int MSG_MEMINFO_UPDATE = 1;

    private TopNMem topNMem = new TopNMem();
    private ListView lv_topN;
    private SimpleAdapter topNAdapter;
    private List<Map<String, Object>> topNList = new ArrayList<Map<String, Object>>();
    private final int TOPN_LIST_NUM = 3;
    private final int MSG_TOPN_UPDATE = 2;

    private final long PERIOD_TIME = 5000;

    public InfoLinearLayout(Context context) {
        super(context);
        mContext = context;
        memInfo = new MemInfo(mContext);
        LayoutInflater mInflater = LayoutInflater.from(context);
        View meminfoView = mInflater.inflate(R.layout.linearlayout_info, null);
        addView(meminfoView);
        //init meminfo listview
        lv_meminfo = (ListView) findViewById(R.id.lv_meminfo);
        initMeminfoListView();
        //init topN listview
        lv_topN = (ListView) findViewById(R.id.lv_topN);
        initTopNListView();
    }

    public void updateInfo(){
        timer.schedule(task, 0 , PERIOD_TIME);
    }

    public void cancleUpdateInfo(){
        timer.cancel();
    }

    TimerTask task= new TimerTask() {
        @Override
        public void run() {
            memInfo.refreshCurrentMeminfo();
            Message meminfoMessage = new Message();
            meminfoMessage.what = MSG_MEMINFO_UPDATE;
            handler.sendMessage(meminfoMessage);

            topNMem.refreshCurrentTopNMem();
            Message topNMessage = new Message();
            topNMessage.what = MSG_TOPN_UPDATE;
            handler.sendMessage(topNMessage);
        }
    };

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MSG_MEMINFO_UPDATE:
					if(memInfo != null) {
                        updateMeminfoListView(memInfo);
                    }
                    break;
                case MSG_TOPN_UPDATE:
                    if(topNMem != null) {
                        updateTopNListView(topNMem);
                    }
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private void initMeminfoListView(){
        Map<String, Object> map_title = new HashMap<String, Object>();
        map_title.put("Time", "Time");
        map_title.put("MemFree", "MemFree");
        map_title.put("Cached", "Cached");
        map_title.put("MediaMemIdle", "MediaMemIdle");
        map_title.put("RealFree", "RealFree");
        map_title.put("AvaliMem", "AvaliMem");
        meminfoList.add(map_title);

        for(int i=1; i<=MEMINFO_LIST_NUM ;i++){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("Time", "NotInit"+i);
            map.put("MemFree", "NotInit"+i);
            map.put("Cached", "NotInit"+i);
            map.put("MediaMemIdle", "NotInit"+i);
            map.put("RealFree", "NotInit"+i);
            map.put("AvaliMem", "NotInit"+i);
            meminfoList.add(map);
        }

        meminfoAdapter = new SimpleAdapter(mContext,meminfoList,R.layout.linearlayout_meminfo,
                new String[]{"Time","MemFree","Cached","MediaMemIdle","RealFree","AvaliMem"},
                new int[]{R.id.Time,R.id.MemFree,R.id.Cached,R.id.MediaMemIdle,R.id.RealFree,R.id.AvaliMem});
        lv_meminfo.setAdapter(meminfoAdapter);
    }

    private void initTopNListView(){
        for(int i=0; i<TOPN_LIST_NUM ;i++){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("Time", "NotInit"+i);
            map.put("Top1", "NotInit"+i);
            map.put("Top2", "NotInit"+i);
            map.put("Top3", "NotInit"+i);
            map.put("Top4", "NotInit"+i);
            map.put("Top5", "NotInit"+i);
            topNList.add(map);
        }

        topNAdapter = new SimpleAdapter(mContext,topNList,R.layout.linearlayout_topn,
                new String[]{"Time","Top1","Top2","Top3","Top4","Top5"},
                new int[]{R.id.Time2,R.id.Top1,R.id.Top2,R.id.Top3,R.id.Top4,R.id.Top5});
        lv_topN.setAdapter(topNAdapter );
    }

    private void updateMeminfoListView(MemInfo memInfo){
        if((memInfo == null) || (meminfoList == null)){
            return;
        }
        meminfoList.remove(1);
        Map<String, Object> mapiAndone = new HashMap<>();

        mapiAndone.put("Time",memInfo.mTime);
        mapiAndone.put("MediaMemIdle",memInfo.mMediaMemIdle+"MB");
        mapiAndone.put("MemFree",memInfo.mMemFree+"MB");
        mapiAndone.put("Cached",memInfo.mCached+"MB");
        mapiAndone.put("RealFree",memInfo.mRealFree+"MB");
        mapiAndone.put("AvaliMem",memInfo.mAvaliMem+"MB");
        meminfoList.add(MEMINFO_LIST_NUM,mapiAndone);
        meminfoAdapter.notifyDataSetChanged();
    }

    private void updateTopNListView(TopNMem topNMem){
        if((topNMem == null) || (topNList == null)){
            return;
        }
        topNList.remove(0);
        Map<String, Object> mapiAndone = new HashMap<>();

        mapiAndone.put("Time",topNMem.mTime);
        mapiAndone.put("Top1",topNMem.mTop1);
        mapiAndone.put("Top2",topNMem.mTop2);
        mapiAndone.put("Top3",topNMem.mTop3);
        mapiAndone.put("Top4",topNMem.mTop4);
        mapiAndone.put("Top5",topNMem.mTop5);
        topNList.add(TOPN_LIST_NUM-1,mapiAndone);
        topNAdapter .notifyDataSetChanged();
    }
}