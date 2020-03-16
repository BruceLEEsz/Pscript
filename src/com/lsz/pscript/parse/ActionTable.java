package com.lsz.pscript.parse;

import com.lsz.pscript.item.Closure;
import com.lsz.pscript.item.Item;
import com.lsz.pscript.item.ItemTable;
import com.lsz.pscript.production.Production;
import com.lsz.pscript.production.ProductionList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ActionTable {
    int line;// 列
    int row;// 行
    String[][] actionTable;// action表
    List<Production> proList;// 产生式集合
    Map<GoTo, String> gotoMap;// goto图，保存了所有跳转信息
    Map<String, Closure> lrMap;// 闭包集合，包含整个dfa的每个项目集合
    List<String> victs;// 终结符符号



    public String[][] getActionTable() {
        return actionTable;
    }

    public void setActionTable(String[][] actionTable) {
        this.actionTable = actionTable;
    }

    public ActionTable(List<Production> productionList, Map<GoTo, String> gotoMap,
                       Map<String, Closure> lrMap) {
        proList = productionList;
        this.gotoMap = gotoMap;
        this.lrMap = lrMap;
        row = lrMap.size() + 1;
        victs = getVicts();
        line = victs.size() + 1;
        actionTable = new String[row][line];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < line; j++) {
                actionTable[i][j] = ".";
            }
        }
        for (int i = 1; i < victs.size() + 1; i++) {
            actionTable[0][i] = victs.get(i - 1);
        }
        int i = 1;
        for (String setName : lrMap.keySet()) {
            actionTable[i][0] = setName;
            i++;
        }
    }

    /**
     * 获取全部终结符
     * @return
     */
    public List<String> getVicts() {
        List<String> victs = new ArrayList<>();
        victs.add("$");
        for (Production production : proList) {
            for (int i = 0; i < production.getRight().length; i++) {
                if (!isVariable(production.getRight()[i])
                        && !victs.contains(production.getRight()[i])) {
                    victs.add(production.getRight()[i]);
                }
            }
        }
        return victs;
    }


    /**
     * 判断是不是终结符，如果左边没这个作为开头的，那就是终结符了。
     * @param str
     * @return
     */
    public boolean isVariable(String str) {
        for (Iterator<Production> iterator = proList.iterator(); iterator
                .hasNext();) {
            Production production = (Production) iterator.next();
            if (production.getLeft().equals(str)) {
                // 一旦找到左边有等于str的字符，就说明str不算终结符，返回真：是变量
                return true;
            }
        }
        return false;
    }

    /**
     * 设置动作表
     */
    public void setActionTable() {
        // System.out.println(gotoMap);
        // System.out.println(lrMap);
        for (String setName : lrMap.keySet()) {
            Closure tmp = lrMap.get(setName);
            // System.out.println(setName);
            for (Item item : tmp.items) {
                // System.out.println("ActionTable.setActionTable()");
                // System.out.println(item);
                // System.out.println("----------------------------------------");
                // System.out.println(item.getNextB());
                if (item.getNextB().equals("") && !item.getLeft().equals("S'")) {
                    // 如果[A->alpha·,a]在Ii里，而且A!=S’,那么将Action[i,a]设置为规约A->alpha
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i = 0; i < item.getAlpha().length; i++) {
                        stringBuilder.append(item.getAlpha()[i]);
                    }
                    for (int i = 0; i < item.getA().length; i++) {
                        if (!setTable(setName, item.getA()[i],
                                "r" + getProductionId(item))) {
                            throw new Error("规约表设置出现了错误！！产生了语义冲突！！！！");
                        }
                    }
                } else if (item.getLeft().equals("S'") && item.getNextB().equals("")
                        && item.accept()) {
                    setTable(setName, "$", "ACC");
                } else if (victs.contains(item.getNextB())) {// 如果不是变量，就去找goto表
                    // 查找有没有GoTo（Ii，a）=Ij,有的话就把action[i,a]设置为移入j，sj
                    String result = findGotoTable(setName, item.getNextB());
                    // System.out.println("????????????????????");
                    // System.out.println(result);
                    if (!result.equals("")) {
                        // 把action[i,a]设置为移入j，sj
                        // System.out.println(setName);
                        // System.out.println(item.getNextB());
                        // System.out.println("s" + result);
                        if (!setTable(setName, item.getNextB(), "s" + result)) {
                            throw new Error("移入表设置出现了错误！！产生了语义冲突！！！！");
                        }
                    }
                }
            }
        }
    }

    public int getProductionId(Item item) {
        for (Production production : proList) {
            // System.out.println(production.toString());
            // System.out.println(item.getProduction());
            if (item.getProduction().equals(production.toString())) {
                return proList.indexOf(production);
            }
        }
        return 0;
    }



    /**
     * 选定i，a对table进行赋值
     * @param i
     * @param a
     * @param sj
     * @return
     */
    public boolean setTable(String i, String a, String sj) {
        // System.out.println(i);
        // System.out.println(a);
        // System.out.println(sj);
        // System.out
        // .println("--------------ActionTable.setActionTable()-----------");
        int y = 0;
        int x = 0;
        for (int j = 0; j < line; j++) {
            if (a.equals(actionTable[0][j])) {
                y = j;
            }
        }
        for (int j = 0; j < row; j++) {
            if (i.equals(actionTable[j][0])) {
                x = j;
            }
        }
        // System.out.println(y);
        // System.out.println(x);
        if (actionTable[x][y].equals(".") || actionTable[x][y].equals(sj)) {
            actionTable[x][y] = sj;
            return true;
        }
        // System.out.println(actionTable[x][y]);
        return false;

    }

    /**
     * 查找goto表有没有这个关系，有返回true，没有返回false；
     */
    public String findGotoTable(String Ii, String a) {
        for (GoTo goto_tmp : gotoMap.keySet()) {
            if (goto_tmp.closureID.equals(Ii) && goto_tmp.path.equals(a)) {
                return gotoMap.get(goto_tmp);
            }
        }
        return "";
    }


    @Override
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < line; j++) {
                stringBuffer.append(actionTable[i][j] + " ");
            }
            stringBuffer.append("\n");
        }
        return stringBuffer.toString();
    }


    public static void main(String[] args) throws IOException {
        ProductionList productionList = new ProductionList();
        ItemTable itemTable = new ItemTable(productionList);
        itemTable.setItemSet(itemTable.closure, "I0");
        ActionTable actionTable = new ActionTable(productionList.getProductions(),
                itemTable.gotoMap, itemTable.itemClam);
        System.out.println(actionTable.toString());
        actionTable.setActionTable();
        System.out.println(actionTable.toString());

    }



}
