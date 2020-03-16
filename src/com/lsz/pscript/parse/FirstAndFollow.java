package com.lsz.pscript.parse;

import com.lsz.pscript.production.Production;
import com.lsz.pscript.production.ProductionList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FirstAndFollow {
    List<Production> productions;

    public FirstAndFollow(ProductionList productionList) {
        productions = productionList.getProductions();
    }

    public boolean isToken(String str) {
        for (Production production : productions)
            if (production.getLeft().equals(str))
                return false;
        return true;
    }

    /**
     * 判断产生式集非空
     */
    public boolean isEmpty(String str) {
        for (Production production : productions)
            if (production.getLeft().equals(str))
                for (int i = 0; i < production.getRight().length; i++)
                    if (production.getRight()[i].equals("null"))
                        return true;
        return false;
    }

    public List<Production> getLeft(String B) {
        List<Production> list = new ArrayList<>();
        for (Production production : productions)
            if (production.getLeft().equals(B))
                list.add(production);
        return list;
    }

    public List<String> getFirstItem(Production production) {
        List<String> list = new ArrayList<>();// 获取包含这个str左部的产生式
        // 遍历这个产生式的每一项，其中每个产生式的每一项也需要遍历。
        for (int i = 0; i < production.getRight().length; i++) {
            //无左递归
            if (!production.getLeft().equals(production.getRight()[i])) {
                list.addAll(getFirst(production.getRight()[i]));
            }
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
        //第一项为终结符时
        if (isToken(s)) {
            list.add(s);
            return list;
        }
        List<Production> productions = getLeft(s);
        for (Production production : productions) {
            if (isEmpty(production.getRight())) {
                list.add("null");
            } else if (isToken(production.getRight()[0])
                    && !isEmpty(production.getRight())) {
                list.add(production.getRight()[0]);
            } else {
                list.addAll(getFirstItem(production));
            }
        }
        return list;
    }

    /**
     * 得到所有first集
     *
     * @param strings
     * @return
     */
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
                    if (i + 1 < production.getRight().length
                            && !production.getRight()[i + 1].equals(production.getLeft())) {
                        List<String> tmp = getFirst(production.getRight()[i + 1]);
                        if (tmp.contains("null")) {
                            // beta能够产生空,或B → αAβ是G的产生式（β多次推导后得到ε），则将FOLLOW(B)加入
                            //￿￿￿移除ε
                            tmp.remove("null");
                            list.addAll(tmp);
                            list.addAll(getF(production.getLeft()));
                            break;
                        } else {
                            // 若B → αAβ是G的产生式,则将FIRST(β) - ε 加入FOLLOW(A)
                            // beta不能产生空
                            tmp.remove("null");
                            list.addAll(tmp);
                            break;
                        }
                    } else {
                        if (!production.getLeft().equals(str)) {
                            //将FOLLOW(B) 加入到FOLLOW(A)
                            list.addAll(getF(production.getLeft()));
                            //list.addAll(new ArrayList<>(findBeta(production.getLeft())));
                        }
                        break;
                    }
                }
            }
        }
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
        list.add("$");
        for (String s : findBeta(str))
            if (!list.contains(s))
                list.add(s);
        return list;
    }

    public List<String> getF(String str) {
        return new ArrayList<>(findBeta(str));
    }
    public static void main(String[] args) throws IOException {
        ProductionList productionSet = new ProductionList();
        //System.out.println(productionSet.toString());
        FirstAndFollow firstFollow = new FirstAndFollow(productionSet);
        List<String> list = new ArrayList<>();
        list.add("=");
        list.add("R");
        System.out.println("输出first集");
        System.out.println(firstFollow.getFirst("="));
        System.out.println(firstFollow.getFirst(list));
        //System.out.println(firstFollow.getFollow("R"));;
    }
}

