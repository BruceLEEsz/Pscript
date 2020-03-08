package pscript.test;

import pscript.Lex;
import pscript.LexException;
import pscript.token.*;

public class test {
    public static void main(String[] args) throws LexException {
        Lex lex = new Lex(new CodeDialog());
        for(Token t;(t=lex.read())!=Token.EOF;)
            System.out.println(t.toString());
    }
}
