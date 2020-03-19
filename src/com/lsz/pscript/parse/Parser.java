package com.lsz.pscript.parse;

import com.lsz.pscript.item.ItemTable;
import com.lsz.pscript.production.Production;
import com.lsz.pscript.production.ProductionList;
import com.lsz.pscript.production.pStack;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Parser {
    int column = 0;
    int row = 0;
    String[][] table;
    ActionTable actionTable;
    GoToTable goToTable;

    public int getColumn() {
        return column;
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
                itemTable.gotoMap, itemTable.itemClam);
        goToTable.setGotoTable();
        ActionTable actionTable = new ActionTable(productionList.getProductions(),
                itemTable.gotoMap, itemTable.itemClam);
        actionTable.setActionTable();
        this.actionTable = actionTable;
        this.goToTable = goToTable;
        column = goToTable.column + actionTable.column - 1;
        row = goToTable.row;
        table = new String[row][column];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                if (j < actionTable.column) {
                    table[i][j] = actionTable.getActionTable()[i][j];
                } else {
                    table[i][j] = goToTable.getGotoTable()[i][j - actionTable.column + 1];
                }
            }
        }

    }

    public String searchActionTable(String input, String top) {
        int y = 0;
        int x = 0;
        for (int j = 0; j < actionTable.column; j++) {
            if (input.equals(actionTable.actionTable[0][j])) {
                y = j;
            }
        }
        for (int j = 0; j < actionTable.row; j++) {
            if (top.equals(actionTable.actionTable[j][0])) {
                x = j;
            }
        }
        return actionTable.actionTable[x][y];
    }

    public String searchGotoTable(String input, String A) {
        int y = 0;
        int x = 0;
        for (int j = 0; j < goToTable.column; j++) {
            if (A.equals(goToTable.gotoTable[0][j])) {
                y = j;
            }
        }
        for (int j = 0; j < goToTable.row; j++) {
            if (input.equals(goToTable.gotoTable[j][0])) {
                x = j;
            }
        }
        return goToTable.gotoTable[x][y];
    }

    @Override
    public String toString() {
        StringBuilder stringBuffer = new StringBuilder();
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                stringBuffer.append(table[i][j]).append(" ");
                System.out.println("\t" + table[i][j] + "\t");
            }
            stringBuffer.append("\n");
            System.out.println("\n");
        }
        return stringBuffer.toString();
    }
}
