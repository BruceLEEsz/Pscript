package com.lsz.pscript.production;

import java.util.Enumeration;
import java.util.Stack;

public class pStack {
    Stack<String> pStack = new Stack<>();

    public String pop() {
        return pStack.pop();
    }

    public void push(String setName) {
        pStack.push(setName);
    }

    public String getTop() {
        return pStack.peek();
    }

    public void printStack() {
        if (pStack.empty())
            System.out.println("堆栈为空");
        else {
            System.out.print("堆栈中的元素：");
            Enumeration<String> items = pStack.elements();
            while (items.hasMoreElements())
                System.out.print(items.nextElement() + " ");
        }
        System.out.println(); // 换行
    }

}
