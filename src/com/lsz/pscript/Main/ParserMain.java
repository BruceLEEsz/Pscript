package com.lsz.pscript.Main;

import com.lsz.pscript.parse.Parser;
import com.lsz.pscript.production.Production;
import com.lsz.pscript.production.ProductionList;
import com.lsz.pscript.production.pStack;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ParserMain {
    public static void main(String[] args) throws IOException {
        ProductionList productionList = new ProductionList();
        Parser parser = new Parser(productionList);
        pStack stack = new pStack();
        stack.push("I0");
        String action = "";
        int wordId = 0;
        BufferedReader br = new BufferedReader(new FileReader("r.txt"));
        List<String> words = new ArrayList<>();
        String s = "";
        while ((s = br.readLine()) != null) {
            words.add(s);
        }
        while (!action.equals("ACC") && wordId <= words.size()) {
            System.out.println("\n");
            // 读取当前输入符号和栈顶部状态
            String word = words.get(wordId);
            String top = stack.peek();
            action = parser.searchActionTable(word, top);
            System.out.println("word：" + word);
            if (action.equals("ACC")) {
                break;
            } else if (action.contains("s")) {
                action = action.replaceAll("s", "");
                System.out.println("移入" + word+" 终结符");
                wordId++;
                stack.push(action);
            } else if (action.contains("r")) {
                int productionId = Integer.parseInt(action.replaceAll("r", ""));
                Production production =
                        productionList.getProductions().get(productionId);
                System.out.println("规约" + production+" 产生式");
                int r =
                        productionList.getProductions().get(productionId).getRight().length;
                while (r > 0){
                    stack.pop();
                    r--;
                }
                stack.push(parser.searchGotoTable(stack.peek(),
                        productionList.getProductions().get(productionId).getLeft()));
            } else {
                throw new Error("Parser():程序读入完全，但未达到接受条件，程序不完整");
            }
        }

        if (action.equals("ACC") && wordId == words.size() - 1) {
            System.out.println("程序规约完全，接受成功");
        }
    }
}
