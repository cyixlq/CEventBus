package top.cyixlq.example;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import top.cyixlq.ceventbus.CEventBus;
import top.cyixlq.example.bean.Event;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
    }

    public void postEvent(View view) {
        /*new Thread(new Runnable() {
            @Override
            public void run() {
                CEventBus.getDefault().post(new Event("来自SecondActivity的消息"));
            }
        }).start();*/

        CEventBus.getDefault().post(new Event("来自SecondActivity的消息"));
    }
}
