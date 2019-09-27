package top.cyixlq.ceventbus;

import android.os.Handler;
import android.os.Looper;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import top.cyixlq.ceventbus.annotion.Subscribe;
import top.cyixlq.ceventbus.bean.MethodManager;

public final class CEventBus {

    private static volatile CEventBus instance;
    private Map<Object, List<MethodManager>> map;
    private Handler handler;
    private ExecutorService executorService;

    public static CEventBus getDefault() {
        if (instance == null) {
            synchronized (CEventBus.class) {
                if (instance == null) {
                    instance = new CEventBus();
                }
            }
        }
        return instance;
    }

    private CEventBus() {
        map = new HashMap<>();
        handler = new Handler(Looper.getMainLooper());
        executorService = Executors.newCachedThreadPool();
    }

    public void register(Object object) {
        List<MethodManager> methodManagers = map.get(object);
        if (methodManagers == null || methodManagers.isEmpty()) {
            map.put(object, findMethodsNotNull(object));
        }
    }

    private List<MethodManager> findMethodsNotNull(Object object) {
        final Class<?> clazz = object.getClass();
        final List<MethodManager> list = new ArrayList<>();
        final Method[] declaredMethods = clazz.getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            Subscribe annotation = declaredMethod.getAnnotation(Subscribe.class);
            if (annotation == null) {
                continue;
            }
            Class<?>[] parameterTypes = declaredMethod.getParameterTypes();
            if (parameterTypes.length > 1) {
                continue;
            }
            MethodManager methodManager = new MethodManager();
            methodManager.setType(parameterTypes[0]);
            methodManager.setMethod(declaredMethod);
            methodManager.setThreadMode(annotation.value());
            list.add(methodManager);
        }
        return list;
    }

    public void unRegister(Object object) {
        map.remove(object);
    }

    public void post(final Object value) {
        for (final Object next : map.keySet()) {
            final List<MethodManager> list = map.get(next);
            if (list == null) continue;
            for (final MethodManager methodManager : list) {
                if (methodManager.getType() == value.getClass()) {
                    switch (methodManager.getThreadMode()) {
                        case POSTING:
                            invoke(next, methodManager.getMethod(), value);
                            break;
                        case MAIN:
                            if (isMainThread()) {
                                invoke(next, methodManager.getMethod(), value);
                            } else {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        invoke(next, methodManager.getMethod(), value);
                                    }
                                });
                            }
                            break;
                        case BACKGROUND:
                            if (isMainThread()) {
                                executorService.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        invoke(next, methodManager.getMethod(), value);
                                    }
                                });
                            } else {
                                invoke(next, methodManager.getMethod(), value);
                            }
                            break;
                        default:
                            break;
                    }
                }
            }
        }
    }

    private void invoke(Object object, Method method, Object value) {
        try {
            method.invoke(object, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isMainThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }

}
