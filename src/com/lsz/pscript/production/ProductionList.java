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
     * 从所有产生式中将终结符选出
     */
    public List<String> setToken() {
        List<String> token = new ArrayList<>();
        token.add("$");
        for (Production production : productions) {
            for (int i = 0; i < production.getRight().length; i++) {
                //不是非终结符且未加入token
                if (!isVariable(production.getRight()[i])
                        && !token.contains(production.getRight()[i])) {
                    token.add(production.getRight()[i]);
                }
            }
        }
        //System.out.println(token);
        return token;
    }

    private boolean isVariable(String s) {
        for (Production production : productions) {
            if (production.getLeft().equals(s)) {
                return true;
            }
        }
        return false;
    }

    public void read() throws IOException {
        String[] productions = new FileRead().getProduces(path).split("\n");
        String[] part;
        String[] rightItems;
        String[] rightP;
        for (String p : productions) {
            part = p.split(" -> ");
            if (part.length == 2) {
                rightItems = part[1].split("\\|");
                for (String rightItem : rightItems) {
                    rightP = rightItem.split(" ");
                    // System.out.println(rightItems[j]);
                    Production production = new Production(part[0], rightP);
                    this.productions.add(production);
                }
            } else throw new Error("产生式错误");
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
        ProductionList productionList = new ProductionList();
        //System.out.println("产生式集合：——————————————————");
        //产生式集合
        System.out.println(productionList.toString());
        //System.out.println("——————————————————————————————");
    }

}