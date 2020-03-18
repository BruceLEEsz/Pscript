package com.lsz.pscript.item;

import com.lsz.pscript.parse.FirstAndFollow;
import com.lsz.pscript.production.Production;
import com.lsz.pscript.production.ProductionList;

import java.io.IOException;
import java.util.*;

public class Closure {
    ProductionList productionList;
    List<Production> productions;
    public List<Item> items;
    FirstAndFollow firstAndFollow;
    List<String> used;

    /**
     * 初始化LR，输入产生式集合
     */
    public Closure(ProductionList productionList) {
        this.productionList = productionList;
        productions = productionList.getProductions();
        items = new ArrayList<>();
        firstAndFollow = new FirstAndFollow(productionList);
        used = new ArrayList<>();
    }

    /**
     * 查找指定左部的产生式集合
     */
    public List<Production> findProduction(String B) {
        List<Production> list = new ArrayList<>();
        for (Production production : productions) {
            if (production.getLeft().equals(B)) {
                list.add(production);
            }
        }
        return list;
    }


    /**
     * 从一个item出发，求他的闭包,嵌套求到全部的闭包
     */
    public List<Item> getNextClosureItem(Item item) {
        List<Item> items = new ArrayList<>();
        List<String> beta_a = new ArrayList<>(Arrays.asList(item.getBeta()));
        beta_a = firstAndFollow.getFirst(beta_a);
        if (beta_a.isEmpty()) {
            for (int i = 0; i < item.getA().length; i++) {
                if (!beta_a.contains(item.getA()[i])) {
                    beta_a.add(item.getA()[i]);
                }
            }
        }
        for (Production pro : findProduction(item.getNextB())) {
            beta_a.remove("");
            Item tmp = new Item(pro, beta_a, productionList.getToken());
            items.add(tmp);
            used.add(tmp.getLeft());
        }
        return items;
    }

    /**
     * 构造闭包项目
     */
    public void setClosureItem(Production production) {
        Item item = new Item(production, productionList.getToken());// 用产生式构造项
        items.add(item);
        used.add(item.getLeft());
        setClosureItem(items);
    }

    public void setClosureItem(List<Item> items) {
        List<Item> tmpItems = new ArrayList<>();
        boolean changed = false;
        // 求所有项目闭包
        for (Item item : items) {
            List<Item> result = getNextClosureItem(item);
            used.add(item.getNextB());
            for (Item tmp : result) {
                if (!tmpItems.contains(item)) {
                    tmpItems.add(tmp);
                }
            }
        }
        for (Item item : tmpItems) {
            if (!this.items.contains(item)) {
                this.items.add(item);
                changed = true;
            }
        }
        if (!tmpItems.isEmpty() && changed) {
            setClosureItem(tmpItems);
        }
    }


    /**
     * 返回它跳到下一条路径的转移符号
     */
    public List<String> gotoPath() {
        List<String> list = new ArrayList<>();
        for (Item string : items) {
            if (!string.getNextB().equals("") && !list.contains(string.getNextB())) {
                list.add(string.getNextB());
            }
        }
        return list;
    }


    /**
     * 给定路径path，就这个产生式的下一个闭包集合
     */
    public Closure getNextClosure(String path) {
        Closure c = new Closure(productionList);
        for (Item item : items) {
            if (path.equals(item.getNextB())) {
                Item tmp = item.move();
                c.items.add(tmp);
            }
        }
        c.setClosureItem(c.items);
        return c;
    }


    @Override
    public int hashCode() {
        StringBuilder stringBuffer2 = new StringBuilder();
        for (Item item : this.items) {
            stringBuffer2.append(item);
        }
        int hash = 7;
        hash = 31 * hash + stringBuffer2.hashCode();
        return hash;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (null == obj)
            return false;
        if (this.getClass() != obj.getClass())
            return false;
        Closure user = (Closure) obj;
        StringBuilder stringBuffer = new StringBuilder();
        for (Item item : user.items)
            stringBuffer.append(item);
        StringBuilder stringBuffer2 = new StringBuilder();
        for (Item item : this.items)
            stringBuffer2.append(item);
        return stringBuffer.toString().equals(stringBuffer2.toString());
    }

    @Override
    public String toString() {
        return items.toString();
    }

}
