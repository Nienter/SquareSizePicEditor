package com.x.codeX;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import androidx.annotation.Keep;

import com.x.codeX.agent.xInterface.DInterface;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Keep
public class InstrumentationProxy extends Instrumentation {


    public static final String EXEC_START_ACTIVITY = "execStartActivity";


    public Instrumentation oldInstrumentation;


    private Field mIsShowField;
    private Field mAgentField;
    private Object mAgentObject;
    private Class mAgentClass;
    private Method mAgentShowInMethod;

    public InstrumentationProxy(Instrumentation mInstrumentation) {
        oldInstrumentation = mInstrumentation;
    }

    public ActivityResult execStartActivity(Context who, IBinder contextThread, IBinder token, Activity target,
                                            Intent intent, int requestCode, Bundle options) {
       /* Semaphore semaphore = new Semaphore(0);
        final Semaphore semaphore2 = semaphore;*/
        try {
            //得到Worker类中的mAgent字段
            try {
                mAgentField = target.getClass().getField("adAgent");
            } catch (Exception e) {
                Instrumentation.class.getInterfaces().getClass();
                Method execStartActivity = Instrumentation.class.getDeclaredMethod(
                        EXEC_START_ACTIVITY,
                        Context.class, IBinder.class, IBinder.class, Activity.class,
                        Intent.class, int.class, Bundle.class);
                execStartActivity.setAccessible(true);
                return (ActivityResult) execStartActivity.invoke(oldInstrumentation, who,
                        contextThread, token, target, intent, requestCode, options);
            }

            mAgentField.setAccessible(true);
            //实例化mAgent对象  
            mAgentObject = mAgentField.get(target);
            //得到mAgent对象对应类的Class  
            mAgentClass = Class.forName(mAgentObject.getClass().getName());
            //反射出该Class类中的方法  
            mAgentShowInMethod = mAgentClass.getDeclaredMethod("showIn");
            //取消访问私有方法的合法性检查
            mAgentShowInMethod.setAccessible(true);
            mIsShowField = mAgentClass.getDeclaredField("isShowInAd");
            mIsShowField.setAccessible(true);

            Boolean isShow = (Boolean) mIsShowField.get(mAgentObject);

            if (isShow) {
                Field listenerField = mAgentClass.getDeclaredField("dInterface");
                if (listenerField != null) {
                    listenerField.setAccessible(true);  //无视访问修饰符
                    listenerField.set(mAgentObject, new DInterface() {
                        @Override
                        public void geNext() {
                            Instrumentation.class.getInterfaces().getClass();
                            Method execStartActivity = null;
                            try {
                                execStartActivity = Instrumentation.class.getDeclaredMethod(
                                        EXEC_START_ACTIVITY,
                                        Context.class, IBinder.class, IBinder.class, Activity.class,
                                        Intent.class, int.class, Bundle.class);

                                execStartActivity.setAccessible(true);
                                execStartActivity.invoke(oldInstrumentation, who,
                                        contextThread, token, target, intent, requestCode, options);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            } catch (NoSuchMethodException e) {
                                e.printStackTrace();
                            }
//                        who.startActivity(intent);
                        }
                    });
                    //调用show()方法
                    mAgentShowInMethod.invoke(mAgentObject);
                }else {

                }

            } else {
                Instrumentation.class.getInterfaces().getClass();
                Method execStartActivity = Instrumentation.class.getDeclaredMethod(
                        EXEC_START_ACTIVITY,
                        Context.class, IBinder.class, IBinder.class, Activity.class,
                        Intent.class, int.class, Bundle.class);
                execStartActivity.setAccessible(true);
                return (ActivityResult) execStartActivity.invoke(oldInstrumentation, who,
                        contextThread, token, target, intent, requestCode, options);
            }
         /*   semaphore.acquire();//消耗通路
            Instrumentation.class.getInterfaces().getClass();
            Method execStartActivity = Instrumentation.class.getDeclaredMethod(
                    EXEC_START_ACTIVITY,
                    Context.class, IBinder.class, IBinder.class, Activity.class,
                    Intent.class, int.class, Bundle.class);
            execStartActivity.setAccessible(true);
            return (ActivityResult) execStartActivity.invoke(oldInstrumentation, who,
                    contextThread, token, target, intent, requestCode, options);*/
        } catch (Exception e) {
            throw new RuntimeException("if Instrumentation paramerter is mInstrumentation, hook will fail");
        }
        return null;
    }
}