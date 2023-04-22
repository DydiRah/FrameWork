package etu1984.framework;

import java.util.HashMap;

public class ModelView {
    String url;
    HashMap<String,Object> data = new HashMap<>();

    public ModelView(String url){
        setUrl(url);
    }
    public ModelView(String url,HashMap<String,Object> data){
        setUrl(url);
        setData(data);
    }

    public void addItem(String key,Object value){
        getData().put(key, value);
    }

    public HashMap<String, Object> getData() {
        return data;
    }

    public void setData(HashMap<String, Object> data) {
        this.data = data;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    
}