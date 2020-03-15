package com.lsz.pscript.item;

import com.lsz.pscript.parse.FirstAndFollow;
import com.lsz.pscript.production.Production;
import com.lsz.pscript.production.ProductionList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Closure {
    ProductionList productionList;
    List<Production> productions;
    public List<Item> items;
    FirstAndFollow firstAndFollow;
    List<String> used;


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (null == obj) return false;
        if (this.getClass() != obj.getClass()) return false;
        Closure user = (Closure) obj;
        StringBuilder stringBuffer = new StringBuilder();
        for (Item item : user.items) {
            stringBuffer.append(item);
        }
        StringBuilder stringBuffer2 = new StringBuilder();
        for (Item item : this.items) {
            stringBuffer2.append(item);
        }
        // System.out.println("LRClosure.equals()");
        // System.out.println(stringBuffer);
        // System.out.println(stringBuffer2);
        return stringBuffer.toString().equals(stringBuffer2.toString());
    }


    @Override
    public int hashCode() {
        StringBuilder stringBuffer2 = new StringBuilder();
        for (Item item : this.items)
            stringBuffer2.append(item);
        int hash = 7;
        hash = 31 * hash + stringBuffer2.hashCode();
        return hash;
    }

    public Closure(ProductionList productionList) {
        this.productionList = productionList;
        productions = productionList.getProductions();
        items = new ArrayList<>();
        firstAndFollow = new FirstAndFollow(productionList);
        used = new ArrayList<>();
    }

    /**
     * 查找指定左部的产生式集合
     *
     * @param B
     * @return
     */
    public List<Production> findProduction(String B) {
        List<Production> pro = new ArrayList<>();
        for (Production production : productions)
            if (production.getLeft().equals(B))
                pro.add(production);
        return pro;
    }

    public List<Item> getNextClosureItem(Item item) {
        List<Item> items = new ArrayList<>();
        List<String> a = new ArrayList<>(Arrays.asList(item.getBeta()));
        a = firstAndFollow.getFirst(a);
        if (a.isEmpty())
            for (int i = 0; i < item.getA().length; i++)
                if (!a.contains(item.getA()[i]))
                    a.add(item.getA()[i]);
        for (Production pro : findProduction(item.getNextB())) {
            a.remove("");
            Item tmp = new Item(pro, a, productionList.getToken());
            items.add(tmp);
            used.add(tmp.getLeft());
        }
        // System.out.println(items);
        return items;
    }

    /**
     * 构造闭包项目
     *
     * @param production 产生式构造闭包
     */
    public void setClosureItem(Production production) {
        Item item = new Item(production, productionList.getToken());// 用产生式构造项
        items.add(item);
        // System.out.println(item);
        used.add(item.getLeft());
        setClosureItem(items);
    }

    public void setClosureItem(List<Item> items) {
        // System.out.println("----------------全部式子--------------");
        // System.out.println(this.items);
        List<Item> tmpItems = new ArrayList<>();
        boolean added = false;
        // 对项目集全部的项求闭包
        for (Item item : items) {
            List<Item> result;
            // if (!used.contains(item.getNextB())) {
            result = getNextClosureItem(item);
            used.add(item.getNextB());
            // 对全部的B->r项，把B->·r,b加入到集合里来。
            for (Item tmp : result) {
                if (!tmpItems.contains(item)) {
                    tmpItems.add(tmp);
                }
            }
            // }
        }
        for (Item item : tmpItems) {
            if (!this.items.contains(item)) {
                this.items.add(item);
                added = true;
                System.out.println(item);
                System.out.println(".............................");
            }
        }
        if (!tmpItems.isEmpty() && added) {
            // 如果tmp为空，而且有增加
            /// System.out.println("----------------进入下一层迭代--------------");
            System.out.println(tmpItems);
            System.out.println("----------------上一层产物--------------");
            setClosureItem(tmpItems);
        }
    }


    /**
     * 返回它跳到下一条路径的转移符号
     *
     * @return
     */
    public List<String> gotoPath() {
        List<String> list = new ArrayList<>();
        for (Item string : items) {
            // System.err.println(string);
            if (!string.getNextB().equals("") && !list.contains(string.getNextB())) {
                list.add(string.getNextB());
            }
        }
        // System.out.println(list);
        return list;
    }


    /**
     * 给定路径path，就这个产生式的下一个闭包集合
     *
     * @param path
     * @return
     */
    public Closure getNextClosure(String path) {
        Closure closure = new Closure(productionList);
        for (Item item : items)
            if (path.equals(item.getNextB())) {
                Item tmp = item.move();
                closure.items.add(tmp);
            }
        closure.setClosureItem(closure.items);
        return closure;
    }


    @Override
    public String toString() {
        return items.toString();
    }


}
