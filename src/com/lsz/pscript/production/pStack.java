package com.lsz.pscript.production;

import java.util.Stack;

public class pStack {
    Stack<String> pStack = new Stack<>();

    public void pop() {
        pStack.pop();
    }

    public void push(String setName) {
        pStack.push(setName);
    }

    public String peek() {
        return pStack.peek();
    }
}
