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



    @Override
    public int hashCode() {
        StringBuffer stringBuffer2 = new StringBuffer();
        for (Iterator<Item> iterator = this.items.iterator(); iterator.hasNext();) {
            Item item = (Item) iterator.next();
            stringBuffer2.append(item);
        }
        int hash = 7;
        hash = 31 * hash + stringBuffer2.hashCode();
        return hash;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (null == obj) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }

        Closure user = (Closure) obj;
        StringBuffer stringBuffer = new StringBuffer();
        for (Iterator<Item> iterator = user.items.iterator(); iterator.hasNext();) {
            Item item = (Item) iterator.next();
            stringBuffer.append(item);
        }
        StringBuffer stringBuffer2 = new StringBuffer();
        for (Iterator<Item> iterator = this.items.iterator(); iterator.hasNext();) {
            Item item = (Item) iterator.next();
            stringBuffer2.append(item);
        }
        // System.out.println("LRClosure.equals()");
        // System.out.println(stringBuffer);
        // System.out.println(stringBuffer2);
        if (stringBuffer.toString().equals(stringBuffer2.toString())) {
            return true;
        }
        return false;
    }


    /**
     * 初始化LR，输入产生式集合
     * @param productionList
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
     * @param B
     * @return
     */
    public List<Production> findProduction(String B) {
        List<Production> pro = new ArrayList<>();
        for (Iterator<Production> iterator = productions.iterator(); iterator
                .hasNext();) {
            Production production = (Production) iterator.next();
            if (production.getLeft().equals(B)) {
                pro.add(production);
            }
        }
        return pro;
    }


    /**
     * 从一个item出发，求他的闭包,嵌套求到全部的闭包
     * @param item
     */
    public List<Item> getNextClosureItem(Item item) {
        List<Item> items = new ArrayList<>();
        List<String> beta_a = new ArrayList<>();
        for (int i = 0; i < item.getBeta().length; i++) {
            beta_a.add(item.getBeta()[i]);
            // System.out.println(item.getBeta()[i] + "---**-----");
        }
        // System.out.println(beta_a);
        beta_a = firstAndFollow.getFirst(beta_a);
        // System.out.println("[][][][][][][][][][][][]");
        // System.out.println(beta_a);
        if (beta_a.isEmpty()) {
            for (int i = 0; i < item.getA().length; i++) {
                if (!beta_a.contains(item.getA()[i])) {
                    beta_a.add(item.getA()[i]);
                }
                // System.out.println("--------------------------");
                // System.out.println(item.getA()[i]);
                // System.out.println("--------------------------");
            }
        }
        for (Iterator<Production> iterator =
             findProduction(item.getNextB()).iterator(); iterator.hasNext();) {
            Production pro = (Production) iterator.next();
            // System.out.println(pro);
            // System.out.println(pro);
            // beta_a.add(item.getA());
            // Item item2 = new Item(pro, productionList.getVicts());

      /* if (beta_a.contains("null") || beta_a.isEmpty()) {
        // beta_a求first集合得到空，或者beta_a是空
        System.out.println("beta_a求first集合得到空，或者beta_a是空");
        // if (!beta_a.contains("$")) {
        // beta_a.add("$");
        // }
        // 如果求得的first集合包含了空，就把a加到beta里面
        for (int i = 0; i < item.getA().length; i++) {
          beta_a.add(item.getA()[i]);
          System.out.println("iiiiiiiiiiiiiiiiiii");
          System.out.println(item.getA()[i]);
        }
      }*/
            // System.out.println("*************************");
            // System.out.println(beta_a);
            beta_a.remove("");
            Item tmp = new Item(pro, beta_a, productionList.getToken());
            // System.out.println(tmp);
            items.add(tmp);
            used.add(tmp.getLeft());
            // if (!used.contains(tmp.getB())) {
            // System.out.println(tmp.getB());
            // getNextClosureItem(tmp);
            // System.out.println(used);
            // }
        }
        // System.out.println(items);
        return items;
    }
    /**
     * 构造闭包项目
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
        boolean addded = false;
        // 对项目集全部的项求闭包
        for (Iterator<Item> iterator = items.iterator(); iterator.hasNext();) {
            Item item = (Item) iterator.next();
            List<Item> result = new ArrayList<>();
            // if (!used.contains(item.getNextB())) {
            result = getNextClosureItem(item);
            used.add(item.getNextB());
            // 对全部的B->r项，把B->·r,b加入到集合里来。
            for (Iterator<Item> iterator2 = result.iterator(); iterator2.hasNext();) {
                Item tmp = (Item) iterator2.next();
                if (!tmpItems.contains(item)) {
                    tmpItems.add(tmp);
                }
            }
            // }
        }
        for (Iterator<Item> iterator = tmpItems.iterator(); iterator.hasNext();) {
            Item item = (Item) iterator.next();
            if (!this.items.contains(item)) {
                this.items.add(item);
                addded = true;
                System.out.println(item);
                System.out.println(".............................");
            }
        }
        if (!tmpItems.isEmpty() && addded) {
            // 如果tmp为空，而且有增加
            /// System.out.println("----------------进入下一层迭代--------------");
            System.out.println(tmpItems);
            System.out.println("----------------上一层产物--------------");
            setClosureItem(tmpItems);
        }
    }


    /**
     * 返回它跳到下一条路径的转移符号
     * @return
     */
    public List<String> gotoPath() {
        List<String> list = new ArrayList<>();
        for (Iterator<Item> iterator = items.iterator(); iterator.hasNext();) {
            Item string = (Item) iterator.next();
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
     * @param path
     * @return
     */
    public Closure getNextClosure(String path) {
        Closure lrClosure = new Closure(productionList);
        // System.out.println(lrClosure);
        // List<Item> items = new ArrayList<>();
        // List<Item> tmpItems=new ArrayList<>();
        for (Iterator<Item> iterator = items.iterator(); iterator.hasNext();) {
            Item item = (Item) iterator.next();
            // System.out.println("LRClosure.getNextClosure()");
            // System.out.println(item);
            if (path.equals(item.getNextB())) {
                Item tmp = item.move();
                // System.out.println("^^^^^^^^^");
                // System.out.println(tmp);
                lrClosure.items.add(tmp);
            }
        }
        lrClosure.setClosureItem(lrClosure.items);
        return lrClosure;
    }



    @Override
    public String toString() {
        return items.toString();
    }



    public static void main(String[] args) throws IOException {
        ProductionList productionList = new ProductionList();
        Closure lrClosure = new Closure(productionList);
        lrClosure.setClosureItem(productionList.getProductions().get(0));
        System.out.println(lrClosure.gotoPath());
        System.out.println(lrClosure.toString());
    }

}
