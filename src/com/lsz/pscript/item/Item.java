package com.lsz.pscript.item;

import com.lsz.pscript.production.Production;
import com.lsz.pscript.production.ProductionList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 项目,测试完成
 */
public class Item {
    //点的位置，初始为0
    int dot;
    //A->∂·Bß
    //以下参数参照上式
    private String left;
    private String[] alpha = {};
    private String[] B;
    private String[] beta = {};
    private String[] a = {"$"};
    List<String> token;

    /**
     * 初始化项目集中的一个项目
     *
     * @param left
     * @param alpha
     * @param b
     * @param beta
     * @param a
     * @param token
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
    public Item move() {
        // System.err.println("move");
        dot = dot + 1;
        String[] newAlpha = new String[alpha.length + 1];
        for (int i = 0; i < alpha.length; i++) {
            newAlpha[i] = alpha[i];
            // System.out.println(newAlpha[i]);
        }
        newAlpha[alpha.length] = B[0];
        // newAlpha[alpha.length] = B;
        // for (String s : alpha) {
        // System.out.println(s + " alpha");
        // }
        // for (String s : B) {
        // System.out.println(s + " B");
        // }
        // for (String s : beta) {
        // System.out.println(s + " beta");
        // }
        if (B.length > 1) {// 如果B的长度大于1，只修改B和alpha的值，不修改beta，直到B长度等于1，移动需要新的字符。
            // System.out.println("如果B的长度大于1，只修改B和alpha的值，不修改beta，直到B长度等于1，移动需要新的字符。");
            String[] newB = new String[B.length - 1];
            for (int i = 0; i < newB.length; i++) {
                newB[i] = B[i + 1];
            }
            // for (String s : newAlpha) {
            // System.out.println(s + " newAlpha");
            // }
            // for (String s : newB) {
            // System.out.println(s + "----newB");
            // }
            return new Item(left, newAlpha, newB, beta, a, token);
        } else {
            if (beta.length < 1) {
                // 已经没有beta字符了，b的长度也是1，那么一移动，就到了末尾，不能再移动
                String[] newBeta = {};
                String[] newB = {""};
                return new Item(left, newAlpha, newB, newBeta, a, token);
            } else if (beta.length == 1) {// 还剩下一个beta字符
                // System.out.println("????");
                String[] newBeta = {};
                String[] newB = beta;
                return new Item(left, newAlpha, newB, newBeta, a, token);
            } else {// 还有beta字符，数量大于等于2
                List<String> B_tmp = new ArrayList<>();
                int newlengthB = 0;

                // System.out.println(newlengthB + "..............");
                // for (String s : beta) {
                // System.out.println(s + "..............");
                // }
                for (; newlengthB < beta.length; newlengthB++) {
                    B_tmp.add(beta[newlengthB]);
                    // System.out.println(newlengthB);
                    // System.out.println(beta[newlengthB]);
                    if (!token.contains(beta[newlengthB])) {
                        // 当不再是终结符的时候
                        newlengthB++;
                        break;
                    }
                }
                String[] strs1 = B_tmp.toArray(new String[B_tmp.size()]);
                String[] newB = strs1;
                // 以上是对B进行赋值，这里B必须是读取到非终结符为止的符号
                String[] newBeta = new String[beta.length - newlengthB];
                for (int i = 0; i < newBeta.length; i++) {
                    newBeta[i] = beta[i + newlengthB];
                }
                // for (String s : newAlpha) {
                // System.out.println(s + " newAlpha");
                // }
                // for (String s : newB) {
                // System.out.println(s + "----newB");
                // }
                // for (String s : newBeta) {
                // System.out.println(s + "-**-newBeta");
                // }
                // System.err.println("=----------------=");
                // System.err.println(new Item(Left, newAlpha, newB, newBeta,
                // a));
                return new Item(left, newAlpha, newB, newBeta, a, token);
            }
        }


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

    /**
     * 由产生式和终结符表生产Item
     *
     * @param production
     * @param token
     */
    public Item(Production production, List<String> token) {
        // System.out.println(production);
        this.token = token;
        this.dot = 0;
        List<String> B_tmp = new ArrayList<>();
        int lengthB = 0;
        for (; lengthB < production.getRight().length; lengthB++) {
            B_tmp.add(production.getRight()[lengthB]);
            if (!token.contains(production.getRight()[lengthB])) {// 当不再是终结符的时候
                lengthB++;
                break;
            }
        }
        // System.out.println(B_tmp);
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
     *
     * @param production
     * @param a
     * @param token
     */
    public Item(Production production, List<String> a, List<String> token) {
        this.token = token;
        this.dot = 0;
        List<String> B_tmp = new ArrayList<>();
        int lengthB = 0;
        for (; lengthB < production.getRight().length; lengthB++) {
            B_tmp.add(production.getRight()[lengthB]);
            if (!token.contains(production.getRight()[lengthB])) {// 当不再是终结符的时候
                lengthB++;
                break;
            }
        }
        if (B_tmp.isEmpty()) {
            this.B = new String[]{""};// 对B赋值
        } else {
            this.B = B_tmp.toArray(new String[0]);// 对B赋值
        }
        if (production.getRight().length - lengthB > 0) {
            // 产生式的右部的长度大于B的长度
            String[] strings = new String[production.getRight().length - lengthB];
            for (int i = 0; i < strings.length; i++) {
                strings[i] = production.getRight()[lengthB + i];
                // System.out.println(strings[i]);
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
        // System.out.println(B);
        return new String(left + "->" + tmp2 + tmp3 + tmp);
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
    public static void main(String[] args) throws IOException {
        // Item item=new Item(left, right, alpha);
        ProductionList productionList = new ProductionList();
        System.out.println(productionList.getProductions());
        Item item = new Item(productionList.getProductions().get(0),
                productionList.getToken());
        Item item2 = new Item(productionList.getProductions().get(1),
                productionList.getToken());
        Item item3 = new Item(productionList.getProductions().get(2),
                productionList.getToken());
        Item item4 = new Item(productionList.getProductions().get(3),
                productionList.getToken());
        Item item5 = new Item(productionList.getProductions().get(4),
                productionList.getToken());
        Item item6 = new Item(productionList.getProductions().get(5),
                productionList.getToken());
        Item item7 = new Item(productionList.getProductions().get(6),
                productionList.getToken());
        Item item8 = new Item(productionList.getProductions().get(7),
                productionList.getToken());
        // Item item5 = new Item(productionList.getProductions().get(4));
        System.out.println(productionList.getToken());
        // System.out.println(item);
        System.out.println(item);

        System.out.println(item2);

        System.out.println(item3);
        // System.out.println(item3);
        // System.out.println(item4);
        item2 = item2.move();
        System.out.println(item2);
        item2 = item2.move();
        System.out.println(item2);
        item2 = item2.move();
        System.out.println(item2);
        item = item.move();
        System.out.println(item);
        item4 = item4.move();
        System.out.println(item4);
        item4 = item4.move();
        System.out.println(item4);
        item3 = item3.move();
        System.out.println(item3);
        System.out.println(item4);
        System.out.println(item5);
        System.out.println(item6);
        System.out.println(item7);
        System.out.println(item8);

        // System.out.println(item5);
    }

}
