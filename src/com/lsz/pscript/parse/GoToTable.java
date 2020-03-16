package com.lsz.pscript.parse;

import com.lsz.pscript.item.Closure;
import com.lsz.pscript.item.ItemTable;
import com.lsz.pscript.production.Production;
import com.lsz.pscript.production.ProductionList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GoToTable {
    int line;
    int row;
    //goto表
    String[][] gotoTable;
    List<Production> productionList;
    //保存跳转信息
    Map<GoTo, String> gotoMap;
    // 闭包集合，包含整个dfa的每个项目集合
    Map<String, Closure> closureMap;
    //非终结符
    List<String> variables;

    public String[][] getGotoTable() {
        return gotoTable;
    }

    //初始化一个GOTO表
    public GoToTable(List<Production> productionList, Map<GoTo, String> gotoMap,
                     Map<String, Closure> closureMap) {
        this.productionList = productionList;
        this.gotoMap = gotoMap;
        this.closureMap = closureMap;
        row = closureMap.size() + 1;
        variables = getVariables();
        line = variables.size() + 1;
        gotoTable = new String[row][line];
        //初始化gotoTable
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < line; j++) {
                gotoTable[i][j] = ".";
            }
        }
        for (int i = 1; i < variables.size() + 1; i++) {
            gotoTable[0][i] = variables.get(i - 1);
        }
        int i = 1;
        for (String setName : closureMap.keySet()) {
            gotoTable[i][0] = setName;
            i++;
        }
    }


    @Override
    public String toString() {
        StringBuilder stringBuffer = new StringBuilder();
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < line; j++) {
                stringBuffer.append(gotoTable[i][j]).append(" ");
            }
            stringBuffer.append("\n");
        }
        return stringBuffer.toString();
    }

    /**
     * 获取全部变量
     *
     * @return
     */
    public List<String> getVariables() {
        List<String> vari = new ArrayList<>();
        for (Production production : productionList) {
            for (int i = 0; i < production.getRight().length; i++) {
                if (isVariable(production.getRight()[i])
                        && !vari.contains(production.getRight()[i])) {
                    vari.add(production.getRight()[i]);
                }
            }
        }
        return vari;
    }


    /**
     * 判断是不是非终结符
     */
    public boolean isVariable(String str) {
        for (Production production : productionList)
            if (production.getLeft().equals(str)) return true;
        return false;
    }

    /**
     * 如果GOTO(Ii,A)=Ij,那么GOTO[i,A]=j
     */
    public void setGotoTable() {
        for (GoTo goTo : gotoMap.keySet()) {
            if (variables.contains(goTo.getPath())) {
                // 如果是变量的话
                if (!setTable(goTo.getClosureID(), goTo.getPath(), gotoMap.get(goTo))) {
                    throw new Error("goto产生了冲突！！！！");
                }
            }
        }

    }


    /**
     * 选定i，a对table进行赋值
     *
     * @param i
     * @param a
     * @param sj
     * @return
     */
    public boolean setTable(String i, String a, String sj) {
        System.out.println(i);
        System.out.println(a);
        System.out.println(sj);
        int y = 0;
        int x = 0;
        for (int j = 0; j < line; j++) {
            if (a.equals(gotoTable[0][j])) {
                y = j;
            }
        }
        for (int j = 0; j < row; j++) {
            if (i.equals(gotoTable[j][0])) {
                x = j;
            }
        }
        System.out.println(y);
        System.out.println(x);
        if (gotoTable[x][y].equals(".")) {
            gotoTable[x][y] = sj;
            return true;
        }
        return false;

    }

    public static void main(String[] args) throws IOException {
        ProductionList productionList = new ProductionList();
        ItemTable itemTable = new ItemTable(productionList);
        itemTable.setItemSet(itemTable.closure, "I0");
        GoToTable goToTable = new GoToTable(productionList.getProductions(),
                itemTable.gotoMap, itemTable.itemClam);
        System.out.println(goToTable.toString());
        goToTable.setGotoTable();

        System.out.println(goToTable.toString());

    }

}
