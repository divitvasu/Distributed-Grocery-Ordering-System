package com.GroceryOrderingSystem.communication;

import java.util.concurrent.ConcurrentHashMap;

public class Store {

    private ConcurrentHashMap<String, String> hmap;
    private static Store instance = new Store();

    private Store() {
        hmap = new ConcurrentHashMap<>();
    }

//    private Store(){
//        hmap = new HashMap<>();
//    }

    public static Store getInstance() {
        return instance;
    }

    public void setMap(ConcurrentHashMap<String, String> val) {
        hmap = val;
    }

    public ConcurrentHashMap<String, String> getMap() {
        return hmap;
    }
}
