package com.lsz.pscript.parse;

import com.lsz.pscript.item.Closure;
import com.lsz.pscript.item.Item;
import com.lsz.pscript.production.Production;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ActionTable {
    int column;// 列
    int row;// 行
    String[][] actionTable;// action表
    List<Production> productionList;// 产生式集合
    //goto表
    Map<GoTo, String> gotoMap;
    //闭包之间的关系
    Map<String, Closure> closureMap;
    List<String> token;


    public String[][] getActionTable() {
        return actionTable;
    }

    public ActionTable(List<Production> productionList, Map<GoTo, String> gotoMap,
                       Map<String, Closure> closureMap) {
        this.productionList = productionList;
        this.gotoMap = gotoMap;
        this.closureMap = closureMap;
        row = closureMap.size() + 1;
        token = getToken();
        column = token.size() + 1;
        actionTable = new String[row][column];
        //初始化action表
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                actionTable[i][j] = ".";
            }
        }
        //标记终结符
        for (int i = 1; i < token.size() + 1; i++) {
            actionTable[0][i] = token.get(i - 1);
        }
        int i = 1;
        for (String setName : closureMap.keySet()) {
            actionTable[i][0] = setName;
            i++;
        }
    }

    /*
    判断终结符
     */
    public List<String> getToken() {
        List<String> token = new ArrayList<>();
        token.add("$");
        for (Production production : productionList) {
            for (int i = 0; i < production.getRight().length; i++) {
                if (!isVariable(production.getRight()[i])
                        && !token.contains(production.getRight()[i])) {
                    token.add(production.getRight()[i]);
                }
            }
        }
        return token;
    }


    /**
     * 判断非终结符
     * 检查产生式左侧是否有str
     */
    public boolean isVariable(String str) {
        for (Production production : productionList) {
            if (production.getLeft().equals(str)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 设置动作表
     */
    public void setActionTable() {
        for (String setName : closureMap.keySet()) {
            Closure tmp = closureMap.get(setName);
            for (Item item : tmp.items) {
                if (item.getNextB().equals("") && !item.getLeft().equals("S'")) {
                    // 找到终结符并设置
                    for (int i = 0; i < item.getA().length; i++) {
                        if (!setTable(setName, item.getA()[i],
                                "r" + getProductionId(item))) {
                            throw new Error("setActionTable（）：发生错误");
                        }
                    }
                } else if (item.getLeft().equals("S'") && item.getNextB().equals("")
                        && item.accept()) {//接受情况
                    setTable(setName, "$", "ACC");
                } else if (token.contains(item.getNextB())) {//查找goto表
                    String result = findGotoTable(setName, item.getNextB());
                    if (!result.equals("")) {
                        if (!setTable(setName, item.getNextB(), "s" + result)) {
                            throw new Error("setActionTable（）：发生错误");
                        }
                    }
                }
            }
        }
    }

    public int getProductionId(Item item) {
        for (Production production : productionList)
            if (item.getProduction().equals(production.toString()))
                return productionList.indexOf(production);
        return 0;
    }

    public boolean setTable(String name, String i, String type) {
        int y = 0;
        int x = 0;
        for (int j = 0; j < column; j++)
            if (i.equals(actionTable[0][j])) y = j;
        for (int j = 0; j < row; j++)
            if (name.equals(actionTable[j][0])) x = j;
        if (actionTable[x][y].equals(".") || actionTable[x][y].equals(type)) {
            actionTable[x][y] = type;
            return true;
        }
        return false;
    }

    public String findGotoTable(String Ii, String a) {
        for (GoTo goto_tmp : gotoMap.keySet())
            if (goto_tmp.closureID.equals(Ii) && goto_tmp.path.equals(a))
                return gotoMap.get(goto_tmp);
        return "";
    }

    public String showAction() {
        StringBuilder stringBuffer = new StringBuilder();
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++)
                stringBuffer.append(actionTable[i][j]).append("     ");
            stringBuffer.append("\n");
        }
        return stringBuffer.toString();
    }
}
