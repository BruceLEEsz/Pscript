package com.lsz.pscript.item;

import com.lsz.pscript.parse.GoTo;
import com.lsz.pscript.production.ProductionList;

import java.util.HashMap;
import java.util.Map;

public class ItemTable {
    public Closure closure;// 初始的I0集合
    //项目集族
    public Map<String, Closure> itemClam;
    public int key = 0;
    //goto表
    public Map<GoTo, String> gotoMap;

    public ItemTable(ProductionList productionList) {
        closure = new Closure(productionList);// 初始的I0集合
        itemClam = new HashMap<>();
        gotoMap = new HashMap<>();
    }

    public void setItemSet(Closure closure, String setName) {
        Map<String, Closure> c = new HashMap<>();
        itemClam.put(setName, closure);
        key++;
        closure.setClosureItem(closure.productions.get(0));// 初始第一个闭包
        for (String path : closure.gotoPath()) {
            Closure tmp = closure.getNextClosure(path);
            if (!itemClam.containsValue(tmp)) {
                String name = "I" + key;
                itemClam.put(name, tmp);
                c.put(name, tmp);
                gotoMap.put(new GoTo(setName, path), name);
                key++;
            } else {
                gotoMap.put(new GoTo(setName, path), getOutClosure(itemClam, tmp));
            }
        }
        solveClosure(c);
    }

    /**
     * 求闭包
     */
    public void solveClosure(Map<String, Closure> m) {
        Map<String, Closure> tmpMap = new HashMap<>();
        System.out.println(m);
        boolean hasChanged = false;
        for (String setName : m.keySet()) {
            System.out.println(setName);
            for (String path : m.get(setName).gotoPath()) {
                Closure tmp = m.get(setName).getNextClosure(path);
                if (!itemClam.containsValue(tmp)) {
                    String name = "I" + key;
                    itemClam.put(name, tmp);
                    tmpMap.put(name, tmp);
                    gotoMap.put(new GoTo(setName, path), name);
                    key++;
                    //存在新闭包
                    hasChanged = true;
                } else {
                    gotoMap.put(new GoTo(setName, path), getOutClosure(itemClam, tmp));
                }
            }
        }
        if (hasChanged) {
            solveClosure(tmpMap);
        }
    }

    /**
     * 在closure图里面找对应值的键值名称，返回该键值
     */
    private String getOutClosure(Map<String, Closure> lrMap, Closure closure) {
        for (String type : lrMap.keySet())
            if (lrMap.get(type).equals(closure))
                return type;
        return "";
    }

}
