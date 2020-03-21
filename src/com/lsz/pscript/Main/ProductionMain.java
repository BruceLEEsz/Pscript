package com.lsz.pscript.Main;

import com.lsz.pscript.production.ProductionList;

import java.io.IOException;

public class ProductionMain {
    /**
     * 输出所有产生式
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        ProductionList productionList = new ProductionList();
        System.out.println(productionList.toString());
    }
}
