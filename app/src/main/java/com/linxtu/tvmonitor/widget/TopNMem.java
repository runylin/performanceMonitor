package com.linxtu.tvmonitor.widget;

import android.app.ActivityManager;
import android.content.Context;
import android.os.SystemClock;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Formatter;

/**
 * Created by user on 2017/8/7.
 */
public class TopNMem {
    String mTime;
    String mTop1;
    String mTop2;
    String mTop3;
    String mTop4;
    String mTop5;

    public TopNMem() {
        refreshCurrentTopNMem();
    }

    public void refreshCurrentTopNMem(){
        mTime = getRunTime();
        try {
            String topNString[] = getTopNByexecCommand("dumpsys meminfo",5);
            mTop1 = topNString[0];
            mTop2 = topNString[1];
            mTop3 = topNString[2];
            mTop4 = topNString[3];
            mTop5 = topNString[4];
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String[] getTopNByexecCommand(String command,int number) throws IOException {
        if(number <= 0){
            return null;
        }
        // start the ls command running
        //String[] args =  new String[]{"sh", "-c", command};
        Runtime runtime = Runtime.getRuntime();
        Process proc = runtime.exec(command);        //这句话就是shell与高级语言间的调用
        String resultSArr[] = new String[number];
        //如果有参数的话可以用另外一个被重载的exec方法
        //实际上这样执行时启动了一个子进程,它没有父进程的控制台
        //也就看不到输出,所以我们需要用输出流来得到shell执行后的输出
        InputStream inputstream = proc.getInputStream();
        InputStreamReader inputstreamreader = new InputStreamReader(inputstream);
        BufferedReader bufferedreader = new BufferedReader(inputstreamreader);
        // read the ls output
        String line = "";
        //StringBuilder sb = new StringBuilder(line);
        while ((line = bufferedreader.readLine()) != null) {
            //System.out.println(line);
            //sb.append(line);
            //sb.append('\n');
            if(line.contains("Total PSS by process")){
                line = bufferedreader.readLine();
                for(int i=0; (i<number)&&(line!=null); i++){
                    resultSArr[i] = line;
                    line = bufferedreader.readLine();
                }
                break;
            }
        }
        //tv.setText(sb.toString());
        //使用exec执行不会等执行成功以后才返回,它会立即返回
        //所以在某些情况下是很要命的(比如复制文件的时候)
        //使用wairFor()可以等待命令执行完成以后才返回
        try {
            if (proc.waitFor() != 0) {
                System.err.println("exit value = " + proc.exitValue());
            }
        }
        catch (InterruptedException e) {
            System.err.println(e);
        }
        return resultSArr;
    }

    private String getRunTime() {
        long msec = SystemClock.uptimeMillis();
        long sec = msec / 1000;
        long min = sec / 60;
        long hr = min / 60;
        long sec_show = sec % 60;
        long min_show = min % 60;
        Formatter formatter = new Formatter();
        return formatter.format("%d:%02d:%02d",
                hr, min_show, sec_show).toString().toLowerCase();
    }
}
