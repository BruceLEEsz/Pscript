package com.lsz.pscript.production;

public class Production {
    //左侧产生式
    private String left;
    //右侧产生式
    private String[] right;

    public String getLeft() {
        return left;
    }

    public String[] getRight() {
        return right;
    }

    public Production(String left, String[] right) {
        this.left = left;
        this.right = right;
        for (String s : right) {
            if (s.equals("null")) {
                this.right = new String[]{""};
                break;
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder tmp = new StringBuilder();
        for (String s : right) tmp.append(s);
        return left + "->" + tmp.toString();
    }
}
