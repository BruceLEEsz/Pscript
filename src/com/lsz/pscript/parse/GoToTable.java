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
    int column;
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
        column = variables.size() + 1;
        gotoTable = new String[row][column];
        //初始化gotoTable
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
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
    public List<String> getVariables() {
        List<String> variable = new ArrayList<>();
        for (Production production : productionList)
            for (int i = 0; i < production.getRight().length; i++)
                if (isVariable(production.getRight()[i]) && !variable.contains(production.getRight()[i]))
                    variable.add(production.getRight()[i]);
        return variable;
    }

    /**
     * 判断是不是非终结符
     */
    public boolean isVariable(String str) {
        for (Production production : productionList)
            if (production.getLeft().equals(str)) return true;
        return false;
    }

    public void setGotoTable() {
        for (GoTo goTo : gotoMap.keySet())
            if (variables.contains(goTo.getPath()))
                // 如果是变量的话
                if (!setTable(goTo.getClosureID(), goTo.getPath(), gotoMap.get(goTo)))
                    throw new Error("setGotoTable():设置goto表冲突");
    }

    /**
     * 对table进行赋值
     */
    public boolean setTable(String name, String i, String type) {
        int x = 0;
        int y = 0;
        for (int j = 0; j < column; j++) {
            if (i.equals(gotoTable[0][j])) {
                y = j;
            }
        }
        for (int j = 0; j < row; j++) {
            if (name.equals(gotoTable[j][0])) {
                x = j;
            }
        }
        if (gotoTable[x][y].equals(".")) {
            gotoTable[x][y] = type;
            return true;
        }
        return false;
    }

    public String showGoTo() {
        StringBuilder stringBuffer = new StringBuilder();
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++)
                stringBuffer.append(gotoTable[i][j]).append(" ");
            stringBuffer.append("\n");
        }
        return stringBuffer.toString();
    }
}
