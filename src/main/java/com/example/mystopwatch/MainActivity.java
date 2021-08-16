package com.example.mystopwatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private ScrollView scroll;
    private TextView timer,recode;
    private Button bt_sta,bt_rec;

    //상태를 표시하는 상수 지정...
    public static final int INIT = 0; // 처음
    public static final int RUN = 1;    // 실행중
    public static final int PAUSE = 2;  //정지 상태

    //상태값 체크 변수
    public static int status = INIT;

    //기록 순서 체크 변수
    private int cnt = 1;

    //타이머 시간 값들을 저장하는 변수
    private long baseTime,pauseTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timer = findViewById(R.id.timer);
        recode = findViewById(R.id.record);
        bt_sta = findViewById(R.id.bt_sta);
        bt_rec = findViewById(R.id.bt_rec);
        scroll = findViewById(R.id.scroll);

        bt_rec.setEnabled(false);

        bt_sta.setOnClickListener(click);
        bt_rec.setOnClickListener(click);
    }

    private Handler handler = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            timer.setText(getTime());
            handler.sendEmptyMessage(0);
        }
    };

    private String getTime(){
        long nowTime = SystemClock.elapsedRealtime();

        long overTime = nowTime - baseTime;

        long ms = overTime % 1000;
        long s = overTime / 1000 % 60;
        long m = overTime / 1000 / 60 % 60;

        String recTime = String.format("%02d : %02d . %03d",m,s,ms);

        return recTime;
    }

    private View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.bt_sta:
                    bt_staAction();
                    break;
                case R.id.bt_rec:
                    bt_recAction();
                    break;
            }
        }
    };

    private void bt_recAction() {
        switch (status){
            case RUN:
                recRun();
                break;
            case PAUSE:
                recPause();
                break;
        }
    }

    private void recPause() {

        bt_sta.setText("スタート");
        bt_rec.setText("記録");
        bt_rec.setEnabled(false);

        timer.setText("00 : 00 . 000");

        recode.setText("");

        baseTime = 0;
        pauseTime = 0;
        cnt = 1;

        status = INIT;
    }

    private void recRun() {
        String timeList = recode.getText().toString();
        timeList += String.format("%2d. %s\n",cnt,getTime());

        recode.setText(timeList);

        scroll.scrollTo(0,recode.getHeight());
        cnt++;
    }

    private void bt_staAction() {
        switch(status){
            case INIT:
                staInit();
                break;
            case RUN:
                staRun();
                break;
            case PAUSE:
                staPause();
        }
    }

    private void staPause() {
        long reStart = SystemClock.elapsedRealtime();

        baseTime += (reStart - pauseTime);

        handler.sendEmptyMessage(0);
        bt_sta.setText("ストップ");
        bt_rec.setText("記録");

        status = RUN;
    }

    private void staRun() {
        handler.removeMessages(0);

        pauseTime = SystemClock.elapsedRealtime();

        bt_sta.setText("スタート");
        bt_rec.setText("リセット");

        status = PAUSE;
    }

    private void staInit() {
        baseTime = SystemClock.elapsedRealtime();

        handler.sendEmptyMessage(0);

        bt_sta.setText("ストップ");
        bt_rec.setEnabled(true);

        status = RUN;
    }


}
























