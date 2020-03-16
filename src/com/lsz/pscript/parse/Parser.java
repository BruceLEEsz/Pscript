package com.lsz.pscript.parse;

import com.lsz.pscript.item.ItemTable;
import com.lsz.pscript.production.Production;
import com.lsz.pscript.production.ProductionList;
import com.lsz.pscript.production.pStack;
import sun.jvm.hotspot.tools.PStack;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
                itemTable.gotoMap, itemTable.itemClam);
        //System.out.println(goToTable.toString());
        goToTable.setGotoTable();
        //System.out.println(goToTable.toString());
        ActionTable actionTable = new ActionTable(productionList.getProductions(),
                itemTable.gotoMap, itemTable.itemClam);
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

    public String searchActionTable(String input, String top) {
        int y = 0;
        int x = 0;
        for (int j = 0; j < actionTable.line; j++) {
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
        for (int j = 0; j < goToTable.line; j++) {
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
            for (int j = 0; j < line; j++) {
                stringBuffer.append(table[i][j]).append(" ");
                // System.out.println("\t" + table[i][j] + "\t");
            }
            stringBuffer.append("\n");
            // System.out.println("\n");
        }
        return stringBuffer.toString();
    }
/**
    public static List<String> getFileContext(String path) {
        FileReader fileReader = null;

        BufferedReader bufferedReader = null;
        List<String> list = new ArrayList<String>();
        String str = "";
        try {
            fileReader = new FileReader(path);
            bufferedReader = new BufferedReader(fileReader);
            while ((str = bufferedReader.readLine()) != null) {
                if (str.trim().length() > 2) {
                    list.add(str);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }

            try {
                if (fileReader != null) {
                    fileReader.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return list;
    }
*/
    public static void main(String[] args) throws IOException {
        ProductionList productionList = new ProductionList();
        Parser parser = new Parser(productionList);
        pStack stack = new pStack();
        stack.push("I0");
        String action = "";
        int wordId = 0;
        BufferedReader br = new BufferedReader(new FileReader("r.txt"));
        List<String> words = new ArrayList<>();
        String s = "";
        while ((s = br.readLine()) != null) {
            words.add(s);
        }
        while (!action.equals("ACC") && wordId <= words.size()) {
            String word = words.get(wordId);
            String top = stack.getTop();
            action = parser.searchActionTable(word, top);
            System.out.println("word：" + word);
            System.out.println("top：" + top);
            System.out.println("action：" + action);
            if (action.equals("ACC")) {
                break;
            } else if (action.contains("s")) {
                action = action.replaceAll("s", "");
                System.out.println("移入" + word);
                wordId++;
                stack.push(action);
            } else if (action.contains("r")) {
                stack.printStack();
                int productionId = Integer.parseInt(action.replaceAll("r", ""));
                Production production =
                        productionList.getProductions().get(productionId);
                System.out.println("规约" + production);
                int r =
                        productionList.getProductions().get(productionId).getRight().length;
                while (r > 0) {
                    System.out.println("Parse.main()");
                    System.out.println(stack.pop());
                    r--;
                }
                // word = production.getLeft();
                System.out.println("__________________________________");
                System.out.println(production.getLeft());
                System.out.println(parser.searchGotoTable(stack.getTop(),
                        productionList.getProductions().get(productionId).getLeft()));
                stack.push(parser.searchGotoTable(stack.getTop(),
                        productionList.getProductions().get(productionId).getLeft()));
            } else {
                throw new Error("进入错误处理！");
            }
        }

        if (action.equals("ACC") && wordId == words.size() - 1) {
            System.out.println("接收!!!!!!!!!!!!!!!!!!!");
        }
    }
}
