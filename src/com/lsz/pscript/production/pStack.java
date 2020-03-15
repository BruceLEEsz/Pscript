package com.lsz.pscript.production;

import java.util.Enumeration;
import java.util.Stack;

public class pStack {
    Stack<String> pStack=new Stack<>();
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
            System.out.println("堆栈是空的，没有元素");
        else {
            System.out.print("堆栈中的元素：");
            Enumeration<String> items = pStack.elements(); // 得到 stack
            // 中的枚举对象
            while (items.hasMoreElements()) // 显示枚举（stack ） 中的所有元素
                System.out.print(items.nextElement() + " ");
        }
        System.out.println(); // 换行
    }

}
