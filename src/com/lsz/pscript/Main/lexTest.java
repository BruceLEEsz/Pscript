package com.lsz.pscript.Main;

import com.lsz.pscript.lexer.Lex;
import com.lsz.pscript.lexer.LexException;
import com.lsz.pscript.token.Token;
import com.lsz.pscript.util.CodeDialog;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class lexTest {
    public static void main(String[] args) throws LexException, IOException {
        String[] k={"if","else","while","switch","define","int","double","return","class","string","break","continue"
                ,"void","do","default","define","include","import","def","var"};
        Set<String> keyWord= new HashSet<>(Arrays.asList(k));
        Lex lex = new Lex(new CodeDialog());
        //输出的三元组
        File file = new File("./result.txt");
        //对于语法分析进行预处理，简便语法分析中的一些步骤
        File file1=new File("./r.txt");
        // if file doesnt exists, then create it
        if (!file.exists()) {
            file.createNewFile();
        }
        if(!file1.exists()){
            file1.createNewFile();
        }
        FileWriter fw = new FileWriter(file.getAbsoluteFile());
        FileWriter fw1=new FileWriter(file1.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        BufferedWriter bw1=new BufferedWriter(fw1);
        for (Token t; (t = lex.read()) != Token.EOF; ) {
            //处理关键字
            if(keyWord.contains(t.getValue())){
                t.setTokenType("keyWord");
            }
            //if (t.getValue().equals("if") || t.getValue().equals("while") || t.getValue().equals("else")) {
            // t.setTokenType("keyWord");
            //}
            if (!t.getTokenType().equals("EOL")) {
                bw.write(t.toString() + "\n");
                switch (t.getTokenType()) {
                    case "identifier":
                    case "string":
                    case "num":
                    case "complex_num":
                        bw1.write("id\n");
                        break;
                    default:
                        bw1.write(t.getValue() + "\n");
                        break;
                }
                System.out.println(t.toString());
            }
        }
        bw.close();
        bw1.close();
        System.out.println("finished");
    }

}