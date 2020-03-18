package com.lsz.pscript.Main;

import com.lsz.pscript.item.ItemTable;
import com.lsz.pscript.parse.ActionTable;
import com.lsz.pscript.production.ProductionList;

import java.io.IOException;

public class ActionTableMain {
    public static void main(String[] args) throws IOException {
        ProductionList productionList = new ProductionList();
        ItemTable itemTable = new ItemTable(productionList);
        itemTable.setItemSet(itemTable.closure, "I0");
        ActionTable actionTable = new ActionTable(productionList.getProductions(),
                itemTable.gotoMap, itemTable.itemClam);
        actionTable.setActionTable();
        System.out.println(actionTable.showAction());

    }

}
