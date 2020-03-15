package com.lsz.pscript.production;

import com.lsz.pscript.util.FileRead;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ProductionList {
    List<Production> productions = new ArrayList<Production>();
    List<String> token;
    String path = "produce.txt";

    public ProductionList() throws IOException {
        read();
        token = setToken();
    }

    public List<Production> getProductions() {
        return productions;
    }

    public void setProductions(List<Production> productions) {
        this.productions = productions;
    }

    public List<String> getToken() {
        return token;
    }

    public void setToken(List<String> token) {
        this.token = token;
    }

    /**
     * 设置终结符
     */
    public List<String> setToken() {
        List<String> token = new ArrayList<>();
        token.add("$");
        for (Production production : productions) {
            for (int i = 0; i < production.getRight().length; i++) {
                if (!isVariable(production.getRight()[i])
                        && !token.contains(production.getRight()[i])) {
                    token.add(production.getRight()[i]);
                }
            }
        }
        System.out.println(token);
        return token;
    }

    /**
     * 判断是终结符还是非终结符
     *
     * @param s
     * @return
     */
    private boolean isVariable(String s) {
        for (Production production : productions) {
            if (production.getLeft().equals(s)) {
                return true;
            }
        }
        return false;
    }

    public void read() throws IOException {
        String productions = new FileRead().getProduces(path);
        String[] produces = productions.split("\n");
        String[] part;// 左右部分分组
        String[] rightItems;// 右边的项分组
        String[] rightPro;
        for (String produce : produces) {
            // System.out.println(produce);
            part = produce.split(" -> ");
            if (part.length == 2) {
                rightItems = part[1].split("\\|");
                for (String rightItem : rightItems) {
                    rightPro = rightItem.split(" ");
                    // System.out.println(rightItems[j]);
                    Production production = new Production(part[0], rightPro);
                    this.productions.add(production);
                }
                // System.out.println(proList);
            } else {
                throw new Error("一行产生式出现了两个箭头！读取错误");
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder tmp = new StringBuilder();
        for (Production production : productions) {
            tmp.append(production.toString()).append("\n");
        }
        return tmp.toString();
    }

    public static void main(String[] args) throws IOException {
        ProductionList productionSet = new ProductionList();
        System.out.println("产生式集合：——————————————————");
        System.out.println(productionSet.toString());
        System.out.println("——————————————————————————————");
    }

}
