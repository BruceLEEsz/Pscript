package com.lsz.pscript.parse;

import com.lsz.pscript.production.Production;
import com.lsz.pscript.production.ProductionList;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FirstAndFollow {
    List<Production> productions;

    public FirstAndFollow(ProductionList productionList) {
        productions = productionList.getProductions();
    }

    public boolean isVariable(String str) {
        for (Production production : productions)
            if (production.getLeft().equals(str))
                return true;
        return false;
    }

    /**
     * 判断产生式集是否为空
     *
     * @param str
     * @return
     */
    public boolean isEmpty(String str) {
        for (Production production : productions)
            if (production.getLeft().equals(str))
                for (int i = 0; i < production.getRight().length; i++)
                    if (production.getRight()[i].equals("null"))
                        return true;
        return false;
    }

    /**
     * 返回左部产生式集合
     *
     * @param B
     * @return
     */
    public List<Production> getLeft(String B) {
        List<Production> list = new ArrayList<>();
        for (Production production : productions)
            if (production.getLeft().equals(B))
                list.add(production);
        return list;
    }

    /**
     * 得到所有的first集
     *
     * @param production
     * @return
     */
    public List<String> getFirstItem(Production production) {
        List<String> list = new ArrayList<>();// 获取包含这个str左部的产生式
        // 遍历这个产生式的每一项，其中每个产生式的每一项也需要遍历。
        for (int i = 0; i < production.getRight().length; i++) {
            if (!production.getLeft().equals(production.getRight()[i])) {
                list.addAll(getFirst(production.getRight()[i]));
                // System.out.println(production.getRight()[i]);
            } // 没有左递归
            if (!isEmpty(production.getRight()[i])) {
                // 这个项里没有包含空产生式的话，就继续求解，否则结束。
                return list;
            }
        }
        return list;
    }

    /**
     * 得到first集
     *
     * @param s
     * @return
     */
    public List<String> getFirst(String s) {
        List<String> list = new ArrayList<>();
        if (!isVariable(s)) {
            list.add(s);
            return list;
        }
        List<Production> productions = getLeft(s);
        // System.out.println(productions);
        for (Production production : productions) {
            if (isEmpty(production.getRight())) {
                // System.out.println("-------------------null------------------");
                // 检查X->null是否成立
                list.add("null");
            } else if (!isVariable(production.getRight()[0])
                    && !isEmpty(production.getRight())) {
                // 是终结符的话就直接加入。
                // System.out.println("-------------------vict------------------");
                list.add(production.getRight()[0]);
            } else {
                // System.out.println("-------------------set------------------");
                list.addAll(getFirstItem(production));
            }
        }
        return list;
    }

    public List<String> getFirst(List<String> strings) {
        List<String> list = new ArrayList<>();
        for (String string : strings) {
            list.addAll(getFirst(string));
            if (!list.contains("null")) {
                return list;
            } else {
                list.remove("null");
            }
        }
        return list;
    }

    public boolean isEmpty(String[] strings) {
        for (String string : strings)
            if (string.equals("null"))
                return true;
        return false;
    }

    /**
     * 查找A的beta集合
     *
     * @param str
     * @return
     */
    public List<String> findBeta(String str) {
        List<String> list = new ArrayList<>();
        for (Production production : productions) {
            for (int i = 0; i < production.getRight().length; i++) {
                if (production.getRight()[i].equals(str)) {
                    // System.out.println(production);
                    // 找到了A的话，A的后面一个就是beta
                    if (i + 1 < production.getRight().length
                            && !production.getRight()[i + 1].equals(production.getLeft())) {
                        // 有beta,且自身不等于自身
                        List<String> tList = getFirst(production.getRight()[i + 1]);
                        // System.out.println(production.getRight()[i + 1]);
                        // System.out.println(production.getLeft());
                        // System.out.println("*****************************");
                        // System.out.println(tList);
                        if (tList.contains("null")) {
                            // beta能够产生空,或B → αAβ是G的产生式（β 多次推导后得到ε ），则将FOLLOW(B)
                            // 加入到FOLLOW(A)
                            tList.remove("null");
                            list.addAll(tList);
                            // System.out.println("------**-----");
                            list.addAll(getFollowMid(production.getLeft()));
                            break;
                        } else {
                            // 若B → αAβ是G的产生式,则将FIRST(β) - ε 加入FOLLOW(A)
                            // beta不能产生空
                            tList.remove("null");
                            list.addAll(tList);
                            break;
                        }
                    } else {
                        // 没有beta,若B → αA是G的产生式,则将FOLLOW(B) 加入到FOLLOW(A)
                        // System.out.println("-------------");
                        // System.out.println(production.getLeft());
                        if (!production.getLeft().equals(str)) {
                            list.addAll(getFollowMid(production.getLeft()));
                        }
                        break;
                    }
                }
            }
        }
        // System.out.println("FirstFollow.findBeta()");
        // System.out.println(list);
        return list;
    }

    /**
     * 求follow(A)的集合
     *
     * @param str A
     * @return
     */
    public List<String> getFollow(String str) {
        List<String> list = new ArrayList<>();
        list.add("$");// 如果A是开始符号，一开始就需要把$放到follow里面
        // 检查左部是str的式子的形式，若B → αAβ是G的产生式，则将FIRST(β) - ε 加入FOLLOW(A)
        // 若B → αA是G的产生式，或B → αAβ是G的产生式（β 多次推导后得到ε ），
        // 则将FOLLOW(B) 加入到FOLLOW(A)
        // 【因为把B用αA替换之后，B后面紧跟的字符就是A后面紧跟的字符】
        for (String string : findBeta(str)) {
            if (!list.contains(string)) {
                list.add(string);
            }
        }
        // xslist.addAll(findBeta(str));
        return list;
    }

    /**
     * 用于迭代求follow集合，防止多次加入$符号
     *
     * @param str
     * @return
     */
    public List<String> getFollowMid(String str) {
        List<String> list = new ArrayList<>();
        // 如果A是开始符号，一开始就需要把$放到follow里面
        // 检查左部是str的式子的形式，若B → αAβ是G的产生式，则将FIRST(β) - ε 加入FOLLOW(A)
        // 若B → αA是G的产生式，或B → αAβ是G的产生式（β 多次推导后得到ε ），
        // 则将FOLLOW(B) 加入到FOLLOW(A)
        // 【因为把B用αA替换之后，B后面紧跟的字符就是A后面紧跟的字符】
        list.addAll(findBeta(str));
        return list;
    }


}

