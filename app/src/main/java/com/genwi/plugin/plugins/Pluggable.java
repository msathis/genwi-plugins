package com.genwi.plugin.plugins;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.genwi.plugin.plugins.annotation.Plugin;
import com.genwi.plugin.plugins.annotation.Subscribe;
import com.genwi.plugin.plugins.model.ActionModel;
import com.genwi.plugin.plugins.model.RxEventBus;

import org.atteo.classindex.ClassIndex;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Author: sathis
 * Last Updated: 08/03/16.
 */
public class Pluggable {

    private static final String TAG = Pluggable.class.getSimpleName();

    private Context mContext;

    private Action1<ActionModel> mGlobalObserver;

    public Pluggable(Activity activity, Action1<ActionModel> observer) {
        mContext = activity;
        mGlobalObserver = observer;
        processAnnotations();
    }

    public void publish(ActionModel... models) {
        for (ActionModel model : models) {
            RxEventBus.getInstance(mGlobalObserver).post(model);
        }
    }

    private void processAnnotations() {
        for (Class<?> klass : ClassIndex.getAnnotated(Plugin.class)) {
            Method[] methods = klass.getMethods();
            try {
                Object instance = klass.newInstance();
                for (Method m : methods) {
                    if (m.isAnnotationPresent(Subscribe.class)) {
                        if (m.getReturnType().getCanonicalName().equalsIgnoreCase(Func1.class.getCanonicalName())) {
                            Subscribe subscribe = m.getAnnotation(Subscribe.class);
                            Func1 action = (Func1) m.invoke(instance, mContext);
                            RxEventBus.getInstance(mGlobalObserver).subscribe(klass.getAnnotation(Plugin.class).type(), subscribe.action(), action);
                            Log.d(TAG, "Method " + m.getName() + " in " + klass.getCanonicalName()
                                    + " is registered for subscription");
                        } else {
                            Log.e(TAG, "Method " + m.getName() + " in " + klass.getCanonicalName()
                                    + " is not returning " + Func1.class.getCanonicalName()
                                    + " , so not registered for subscription");
                        }
                    }
                }
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
}
