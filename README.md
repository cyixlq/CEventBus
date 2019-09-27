# CEventBus
疯狂地重复造轮子系列——CEventBus，事件发布总线框架，
模仿EventBus实现的简易版，使用方法与EventBus大同小异。

## 使用方法

在Activity的onCreate方法中进行注册： 
```
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    CEventBus.getDefault().register(this);
}
```

在Activity的onDestroy方法中进行反注册： 
```
@Override
protected void onDestroy() {
    super.onDestroy();
    CEventBus.getDefault().unRegister(this);
}
```

在需要接收事件的方法上添加@Subscribe，并且接收事件的方法只能有一个形参，例如：
```
@Subscribe
public void getEvent(Event event) { // 正确，可以接收到事件
    Log.d("CYTAG", "2，当前线程 --->>> " + Thread.currentThread().getName()
            + "\t消息 --->>>" + event.getMsg());
}

@Subscribe
public void getEvent(Event event, String msg) { // 错误，不会接收到事件
    Log.d("CYTAG", "2，当前线程 --->>> " + Thread.currentThread().getName()
            + "\t消息 --->>>" + event.getMsg());
}
```

发送事件 
```
CEventBus.getDefault().post(new Event("来自SecondActivity的消息"));
``` 

注解说明，@Subscribe注解可以传入三个值：
1. @Subscribe(ThreadMode.POSTING)，默认值，发送事件时在什么线程，接收事件的方法就在什么线程执行。
2. @Subscribe(ThreadMode.MAIN)，无论发送事件时在什么线程，接收事件的方法始终在主线程执行。
3. @Subscribe(ThreadMode.BACKGROUND)，无论发送事件时在什么线程，接收事件的方法始终在子(后台)线程执行。

## 后记
基于反射实现：<br> 注册时通过反射获取接收事件方法添加到集合中<br>
发送事件时通过获取形参为相同类型的的接收事件方法通过invoke反射执行<br>
反注册不需要反射