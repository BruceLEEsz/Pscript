package pscript;

import pscript.token.Token;

import java.io.IOException;

public class LexException extends Exception {
    public LexException(String msg, Token t){
        super("error around "+location(t)+":"+msg);
    }
    private static String location(Token t){
        if(t==Token.EOF)
            return "the last line";
        return "\"" + t.getValue() + "\" at line " + t.getLineNumber();
    }
    public LexException(String msg){
        super(msg);
    }
    public LexException(IOException e){
        super(e);
    }
}
