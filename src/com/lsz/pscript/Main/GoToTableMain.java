package com.lsz.pscript.Main;

import com.lsz.pscript.item.ItemTable;
import com.lsz.pscript.parse.GoToTable;
import com.lsz.pscript.production.ProductionList;

import java.io.IOException;

public class GoToTableMain {
    public static void main(String[] args) throws IOException {
        ProductionList productionList = new ProductionList();
        ItemTable itemTable = new ItemTable(productionList);
        itemTable.setItemSet(itemTable.closure, "I0");
        GoToTable goToTable = new GoToTable(productionList.getProductions(),
                itemTable.gotoMap, itemTable.itemClam);
        goToTable.setGotoTable();
        System.out.println(goToTable.showGoTo());

    }
}
