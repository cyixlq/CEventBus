package top.cyixlq.example;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import top.cyixlq.ceventbus.CEventBus;
import top.cyixlq.ceventbus.annotion.Subscribe;
import top.cyixlq.ceventbus.tag.ThreadMode;
import top.cyixlq.example.bean.Event;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CEventBus.getDefault().register(this);
    }

    @Subscribe(ThreadMode.BACKGROUND)
    public void recieveEvent(Event event) {
//        Toast.makeText(this, event.getMsg(), Toast.LENGTH_SHORT).show();
        Log.d("CYTAG", "1，当前线程 --->>> " + Thread.currentThread().getName()
                + "\t消息 --->>>" + event.getMsg());
    }

    @Subscribe(ThreadMode.BACKGROUND)
    public void getEvent(Event event) {
        Log.d("CYTAG", "2，当前线程 --->>> " + Thread.currentThread().getName()
                + "\t消息 --->>>" + event.getMsg());
    }

    public void goToSecondActivity(View view) {
        startActivity(new Intent(this, SecondActivity.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CEventBus.getDefault().unRegister(this);
    }
}
