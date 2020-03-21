package com.lsz.pscript.production;

import java.util.Arrays;
import java.util.Objects;

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

    public void setLeft(String left) {
        this.left = left;
    }

    public void setRight(String[] right) {
        this.right = right;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Production that = (Production) o;
        return Objects.equals(left, that.left) &&
                Arrays.equals(right, that.right);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(left);
        result = 31 * result + Arrays.hashCode(right);
        return result;
    }

    @Override
    public String toString() {
        StringBuilder tmp = new StringBuilder();
        for (String s : right) tmp.append(s);
        return left + "->" + tmp.toString();
    }
}
