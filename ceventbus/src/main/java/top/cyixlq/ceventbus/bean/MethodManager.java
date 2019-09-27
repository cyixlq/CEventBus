package top.cyixlq.ceventbus.bean;

import java.lang.reflect.Method;

import top.cyixlq.ceventbus.tag.ThreadMode;

public class MethodManager {

    private ThreadMode threadMode;
    private Method method;
    private Class<?> type;

    public ThreadMode getThreadMode() {
        return threadMode;
    }

    public void setThreadMode(ThreadMode threadMode) {
        this.threadMode = threadMode;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }
}
