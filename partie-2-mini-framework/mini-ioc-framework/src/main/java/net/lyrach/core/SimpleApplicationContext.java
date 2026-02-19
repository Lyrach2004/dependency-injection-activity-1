package net.lyrach.core;

import java.util.HashMap;
import java.util.Map;

public class SimpleApplicationContext implements BeanFactory {

    private Map<String,Object> beans = new HashMap<String,Object>();

    public void registerBean(String name,Object bean){
        this.beans.put(name,bean);
    }

    @Override
    public Object getBean(String name) {
        return this.beans.get(name);
    }
}
