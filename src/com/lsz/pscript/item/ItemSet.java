package com.lsz.pscript.item;

import com.lsz.pscript.production.Production;
import com.lsz.pscript.production.ProductionList;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * 项目集
 */
public class ItemSet {
    HashSet<Item> items = new HashSet<>();
    HashSet<String> used = new HashSet<>();

    public ItemSet() {
    }

    /**
     * @param B
     * @param productions
     * @return
     */
    public List<Production> findLeft(String B, List<Production> productions) {
        List<Production> list = new ArrayList<>();
        for (Production production : productions) {
            if (production.getLeft().equals(B)) {
                list.add(production);
            }
        }
        return list;
    }

    public void setItemSet(List<Production> productions,
                           ProductionList productionList) {
        for (Production production : productions) {
            Item tmp = new Item(production, productionList.getToken());
            items.add(tmp);
            used.add(tmp.getB());
            if (!used.contains(tmp.getB())) {
                List<Production> list = findLeft(tmp.getB(), productions);
                if (!list.isEmpty()) {
                    setItemSet(list, productionList);
                }
            }
        }
    }

    /**
     * 产生闭包
     *
     * @param productions
     */
    public void setFirstItemSet(ProductionList productions) {
        // 从第一个产生式开始产生闭包
        Item item =
                new Item(productions.getProductions().get(0), productions.getToken());
        used.add(item.getB());
        items.add(item);
        List<Production> list = findLeft(item.getB(), productions.getProductions());
        if (!list.isEmpty()) {
            setItemSet(list, productions);
        }
    }
}
