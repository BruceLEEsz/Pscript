package com.lsz.pscript.util;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.io.*;


public class CodeDialog extends Reader {
    private String buffer = null;
    //File file = new File("./input.txt");
   // FileWriter fw=new FileWriter(file.getAbsoluteFile());
    //BufferedWriter bw = new BufferedWriter(fw);
    private int pos = 0;

    public CodeDialog() throws IOException {
    }

    protected String Dialog() {
        JTextArea area = new JTextArea(20, 40);
        JScrollPane pane = new JScrollPane(area);
        int result = JOptionPane.showOptionDialog(null, pane, "Code"
                , JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
        if (result == JOptionPane.OK_OPTION)
            return area.getText();
        else
            return null;
    }

    @Override
    public int read(@NotNull char[] cbuf, int off, int len) throws IOException {
        if (buffer == null) {
            String in = Dialog();
            if (in == null)
                return -1;
            else {
                System.out.println(in);
                buffer = in + "\n";
                pos = 0;
            }
        }

        int size = 0;
        int length = buffer.length();
        while (pos < length && size < len)
            cbuf[off + size++] = buffer.charAt(pos++);
        if (pos == length)
            buffer = null;
        return size;
    }

    @Override
    public void close() throws IOException {
       // bw.close();
    }

}