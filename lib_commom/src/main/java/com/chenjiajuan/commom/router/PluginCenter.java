package com.chenjiajuan.commom.router;

import android.support.annotation.NonNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * create by chenjiajuan on 2018/10/4
 */
public class PluginCenter {
    private static final PluginCenter ourInstance = new PluginCenter();

    /**
     * 插件中心
     *
     * @return
     */
    public static PluginCenter get() {
        return ourInstance;
    }

    private List<Object> pluginList = new ArrayList<>();

    private PluginCenter() {
    }

    void registerPlugin(Object object) {
        pluginList.add(object);
    }

    /**
     * 通知插件事件变化
     *
     * @param eventAnnotation 事件类型
     */
    public void notifyPluginEvent(@NonNull Class<? extends Annotation> eventAnnotation) {
        for (Object object : pluginList) {
            notifyPluginEvent(object, eventAnnotation);
        }
    }

    private void notifyPluginEvent(Object plugin, Class<? extends Annotation> eventAnnotation) {
        for (Method method : plugin.getClass().getDeclaredMethods()) {
            if (!method.isAnnotationPresent(eventAnnotation)) {
                continue;
            }
            try {
                method.invoke(plugin);
            } catch (IllegalAccessException e) {
            } catch (IllegalArgumentException e) {
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e.getCause());
            }
            break;
        }
    }
}
