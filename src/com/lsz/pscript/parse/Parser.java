package com.lsz.pscript.parse;

import com.lsz.pscript.item.ItemTable;
import com.lsz.pscript.production.ProductionList;

public class Parser {
    int line = 0;
    int row = 0;
    String[][] table;
    ActionTable actionTable;
    GoToTable goToTable;

    public int getLine() {
        return line;
    }

    public int getRow() {
        return row;
    }

    public String[][] getTable() {
        return table;
    }

    public Parser(ProductionList productionList) {
        ItemTable itemTable = new ItemTable(productionList);
        itemTable.setItemSet(itemTable.closure, "I0");
        GoToTable goToTable = new GoToTable(productionList.getProductions(),
                itemTable.gotoMap, itemTable.map);
        //System.out.println(goToTable.toString());
        goToTable.setGotoTable();
        //System.out.println(goToTable.toString());
        ActionTable actionTable = new ActionTable(productionList.getProductions(),
                itemTable.gotoMap, itemTable.map);
        //System.out.println(actionTable.toString());
        actionTable.setActionTable();
        //System.out.println(actionTable.toString());
        this.actionTable = actionTable;
        this.goToTable = goToTable;
        line = goToTable.line + actionTable.line - 1;
        row = goToTable.row;
        table = new String[row][line];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < line; j++) {
                if (j < actionTable.line) {
                    // System.out.println(i);
                    // System.out.println(j);
                    // System.out.println(actionTable.getActionTable());
                    table[i][j] = actionTable.getActionTable()[i][j];
                } else {
                    table[i][j] = goToTable.getGotoTable()[i][j - actionTable.line + 1];
                }
            }
        }

    }

    @Override
    public String toString() {
        StringBuilder stringBuffer = new StringBuilder();
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < line; j++) {
                stringBuffer.append(table[i][j]).append(" ");
                // System.out.println("\t" + table[i][j] + "\t");
            }
            stringBuffer.append("\n");
            // System.out.println("\n");
        }
        return stringBuffer.toString();
    }
}
