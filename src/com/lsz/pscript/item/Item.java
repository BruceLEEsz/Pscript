package com.lsz.pscript.item;

import com.lsz.pscript.production.Production;
import com.lsz.pscript.production.ProductionList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Item {
    //点的位置，初始为0
    int dot;
    //A->∂·Bß
    //以下参数参照上式
    private String left;
    private String[] alpha = {};
    private String[] B;
    private String[] beta = {};
    //向前搜索符，初始为$
    private String[] a = {"$"};
    List<String> token;

    /**
     * 初始化项目集中的一个项目
     */
    public Item(String left, String[] alpha, String[] b, String[] beta, String[] a, List<String> token) {
        this.dot = 0;
        this.left = left;
        this.alpha = alpha;
        B = b;
        this.beta = beta;
        this.a = a;
        this.token = token;
    }

    public boolean accept() {
        for (String s : a) {
            if (!s.equals("$"))
                return false;
        }
        return true;
    }

    //活前缀右移，构造活前缀

    /**
     *
     * @return new Item(left, newAlpha, newB, newBeta, a, token)
     */
    public Item move() {
        dot = dot + 1;
        String[] newAlpha = new String[alpha.length + 1];
        System.arraycopy(alpha, 0, newAlpha, 0, alpha.length);
        newAlpha[alpha.length] = B[0];
        if (B.length > 1) {
            String[] newB = new String[B.length - 1];
            System.arraycopy(B, 1, newB, 0, newB.length);
            return new Item(left, newAlpha, newB, beta, a, token);
        } else {
            if (beta.length < 1) {
                //末尾
                String[] newBeta = {};
                String[] newB = {""};
                return new Item(left, newAlpha, newB, newBeta, a, token);
            } else if (beta.length == 1) {// 还剩下一个beta字符
                String[] newBeta = {};
                String[] newB = beta;
                return new Item(left, newAlpha, newB, newBeta, a, token);
            } else {// 还有beta字符，数量大于等于2
                List<String> B_tmp = new ArrayList<>();
                int newlengthB = 0;
                for (; newlengthB < beta.length; newlengthB++) {
                    B_tmp.add(beta[newlengthB]);
                    if (!token.contains(beta[newlengthB])) {
                        // 当不再是终结符的时候
                        newlengthB++;
                        break;
                    }
                }
                String[] newB = B_tmp.toArray(new String[0]);
                String[] newBeta = new String[beta.length - newlengthB];
                System.arraycopy(beta, newlengthB, newBeta, 0, newBeta.length);
                return new Item(left, newAlpha, newB, newBeta, a, token);
            }
        }
    }

    /**
     * 由产生式和终结符表生产Item
     */
    public Item(Production production, List<String> token) {
        this.token = token;
        this.dot = 0;
        List<String> B_tmp = new ArrayList<>();
        int lengthB = 0;
        for (; lengthB < production.getRight().length; lengthB++) {
            B_tmp.add(production.getRight()[lengthB]);
            if (!token.contains(production.getRight()[lengthB])) {
                // 当不再是终结符的时候
                lengthB++;
                break;
            }
        }
        if (B_tmp.isEmpty()) {
            this.B = new String[]{""};
        } else {
            this.B = B_tmp.toArray(new String[0]);
        }
        if (production.getRight().length - lengthB > 0) {
            //为beta赋值
            String[] strings = new String[production.getRight().length - lengthB];
            for (int i = 0; i < strings.length; i++) {
                strings[i] = production.getRight()[lengthB + i];
            }
            this.beta = strings;
        } else {
            this.beta = new String[]{};
        }
        this.left = production.getLeft();
    }

    /**
     * 由产生式，终结符集和a
     */
    public Item(Production production, List<String> a, List<String> token) {
        this.token = token;
        this.dot = 0;
        List<String> B_tmp = new ArrayList<>();
        int lengthB = 0;
        for (; lengthB < production.getRight().length; lengthB++) {
            B_tmp.add(production.getRight()[lengthB]);
            if (!token.contains(production.getRight()[lengthB])) {
                // 当不再是终结符的时候
                lengthB++;
                break;
            }
        }
        if (B_tmp.isEmpty()) {
            this.B = new String[]{""};
        } else {
            this.B = B_tmp.toArray(new String[0]);
        }
        if (production.getRight().length - lengthB > 0) {
            // 产生式的右部的长度大于B的长度
            String[] strings = new String[production.getRight().length - lengthB];
            for (int i = 0; i < strings.length; i++) {
                strings[i] = production.getRight()[lengthB + i];
            }
            this.beta = strings;
        } else {
            this.beta = new String[]{};
        }
        this.left = production.getLeft();
        String[] aStrings = new String[a.size()];
        for (int i = 0; i < aStrings.length; i++) {
            aStrings[i] = a.get(i);
        }
        this.a = aStrings;
    }

    public int getDot() {
        return dot;
    }

    public void setDot(int dot) {
        this.dot = dot;
    }

    public String getLeft() {
        return left;
    }

    public void setLeft(String left) {
        this.left = left;
    }

    public String[] getAlpha() {
        return alpha;
    }

    public void setAlpha(String[] alpha) {
        this.alpha = alpha;
    }

    public String getB() {
        if (B.length <= 0) {
            return "";
        } else {
            return B[B.length - 1];
        }
    }

    public String getProduction() {
        StringBuilder tmp = new StringBuilder();
        for (String s : beta) {
            tmp.append(s);
        }
        StringBuilder tmp2 = new StringBuilder();
        for (String s : alpha) {
            tmp2.append(s);
        }
        StringBuilder tmp3 = new StringBuilder();
        for (String s : B) {
            tmp3.append(s);
        }
        return left + "->" + tmp2 + tmp3 + tmp;
    }


    public void setB(String[] b) {
        B = b;
    }

    public String getNextB() {
        return B[0];
    }

    public String[] getBeta() {
        return beta;
    }

    public void setBeta(String[] beta) {
        this.beta = beta;
    }

    public String[] getA() {
        return a;
    }

    public void setA(String[] a) {
        this.a = a;
    }

    public List<String> getToken() {
        return token;
    }

    public void setToken(List<String> token) {
        this.token = token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (null == o) {
            return false;
        }
        if (this.getClass() != o.getClass()) {
            return false;
        }
        StringBuilder tmp = new StringBuilder();
        for (String s : beta) tmp.append(s);
        StringBuilder tmp2 = new StringBuilder();
        for (String s : alpha) tmp2.append(s);
        StringBuilder tmp3 = new StringBuilder();
        for (String s : a) tmp3.append(s).append("/");
        StringBuilder tmp4 = new StringBuilder();
        for (String s : B) tmp4.append(s);
        String tmpStr = left + "->" + tmp2 + "." + tmp4 + "~" + tmp + "," + tmp3;
        return tmpStr.equals(o.toString());
    }

    @Override
    public int hashCode() {
        StringBuilder tmp = new StringBuilder();
        for (String s : beta) tmp.append(s);
        StringBuilder tmp2 = new StringBuilder();
        for (String s : alpha) tmp2.append(s);
        StringBuilder tmp3 = new StringBuilder();
        for (String s : a) tmp3.append(s).append("/");
        StringBuilder tmp4 = new StringBuilder();
        for (String s : B) tmp4.append(s);
        String tmpStr = left + "->" + tmp2 + "." + tmp4 + "~" + tmp + "," + tmp3;
        int hash = 7;
        hash = 31 * hash + tmpStr.hashCode();
        return hash;
    }

    @Override
    public String toString() {
        StringBuilder tmp = new StringBuilder();
        for (String s : beta) tmp.append(s);
        StringBuilder tmp2 = new StringBuilder();
        for (String s : alpha) tmp2.append(s);
        StringBuilder tmp3 = new StringBuilder();
        for (String s : a) tmp3.append(s).append("/");
        StringBuilder tmp4 = new StringBuilder();
        for (String s : B) tmp4.append(s);
        return left + "->" + tmp2 + "." + tmp4 + "~" + tmp + "," + tmp3;
    }

}
