package com.leocai.multidevicesalign;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.leocai.publiclibs.ShakingData;
import com.leocai.publiclibs.multidecicealign.MySensorManager;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

public class SimpleDataActivity extends Activity implements Observer {

    TextView tv_log;
    Button btn_start;
    private boolean start;
    private MySensorManager mySensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_data);
        tv_log = (TextView)findViewById(R.id.tv_log);
        btn_start = (Button)findViewById(R.id.btn_start);
        mySensorManager = new MySensorManager(this);
        try {
            mySensorManager.setFileName("simple.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
        mySensorManager.addObserver(this);
        mySensorManager.startSensor();
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!start){
                    start = true;
                    mySensorManager.startDetection();
                    btn_start.setText("STOP");
                }else{
                    start = false;
                    mySensorManager.stop();
                    btn_start.setText("START");
                }
            }
        });



    }

    @Override
    public void update(Observable observable, Object data) {
        final ShakingData newData = (ShakingData) data;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String info = "";
                double[] lacc = newData.getLinearAccData();
                double[] globalAcc = newData.getConvertedData();
                info += "LinearAcc:\n";
                for (int i = 0; i < 3; i++) {
                    info+= String.format("%.2f",lacc[i])+"\n";
                }
                info += "\nGlobalAcc:\n";
                for (int i = 0; i < 3; i++) {
                    info+= String.format("%.2f",globalAcc[i])+"\n";
                }
                tv_log.setText(info);
            }
        });

    }
}
