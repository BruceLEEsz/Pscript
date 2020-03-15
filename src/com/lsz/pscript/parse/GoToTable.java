package com.lsz.pscript.parse;

import com.lsz.pscript.item.Closure;
import com.lsz.pscript.production.Production;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class GoToTable {
    int line;// 列
    int row;// 行
    String[][] gotoTable;// goto表
    List<Production> proList;// 产生式集合
    Map<GoTo, String> gotoMap;// goto图，保存了所有跳转信息
    Map<String, Closure> lrMap;// 闭包集合，包含整个dfa的每个项目集合
    List<String> variables;// 变量符号
    public String[][] getGotoTable() {
        return gotoTable;
    }
    public GoToTable(List<Production> productionList, Map<GoTo, String> gotoMap,
                     Map<String, Closure> lrMap) {
        proList = productionList;
        this.gotoMap = gotoMap;
        this.lrMap = lrMap;
        row = lrMap.size() + 1;
        variables = getVariables();
        line = variables.size() + 1;
        gotoTable = new String[row][line];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < line; j++) {
                gotoTable[i][j] = ".";
            }
        }
        for (int i = 1; i < variables.size() + 1; i++) {
            gotoTable[0][i] = variables.get(i - 1);
        }
        int i = 1;
        for (String setName : lrMap.keySet()) {
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
        List<String> victs = new ArrayList<>();
        for (Production production : proList) {
            for (int i = 0; i < production.getRight().length; i++) {
                if (isVariable(production.getRight()[i])
                        && !victs.contains(production.getRight()[i])) {
                    victs.add(production.getRight()[i]);
                }
            }
        }
        return victs;
    }


    /**
     * 判断是不是终结符，如果左边没这个作为开头的，那就是终结符了。
     *
     * @param str
     * @return
     */
    public boolean isVariable(String str) {
        for (Production production : proList) {
            if (production.getLeft().equals(str)) {
                // 一旦找到左边有等于str的字符，就说明str不算终结符，返回真：是变量
                return true;
            }
        }
        return false;
    }

    /**
     * 如果GOTO(Ii,A)=Ij,那么GOTO[i,A]=j
     */
    public void setGotoTable() {
        for (GoTo goTo : gotoMap.keySet()) {
            if (variables.contains(goTo.getPath())) {
                // 如果是变量的话
                if (!setTable(goTo.getClosureId(), goTo.getPath(), gotoMap.get(goTo))) {
                    throw new Error("goto设置出现了错误！！产生了冲突！！！！");
                }
                ;
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

}
