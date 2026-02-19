package net.lyrach.core;

import java.util.HashMap;
import java.util.Map;

public class SimpleApplicationContext implements BeanFactory {

    private Map<String,Object> beans = new HashMap<String,Object>();

    public void registerBean(String name,Object bean){
        this.beans.put(name,bean);
    }

    public Object createBean(String className){
        try{
            Class<?> clazz = Class.forName(className);
            return clazz.getDeclaredConstructor().newInstance();
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    public Object createBeanWithConstructor(String className, Class<?>[] paramTypes, Object[] args) {
        try {
            Class<?> clazz = Class.forName(className);
            return clazz.getConstructor(paramTypes).newInstance(args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void injectSetter(Object bean, String methodName, Object dependency) {
        try {
            var method = bean.getClass().getMethod(methodName, dependency.getClass());
            method.invoke(bean, dependency);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void injectField(Object bean, String fieldName, Object dependency) {
        try {
            var field = bean.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(bean, dependency);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object getBean(String name) {
        return this.beans.get(name);
    }
}
