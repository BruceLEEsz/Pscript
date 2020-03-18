package com.lsz.pscript.lexer;

import com.lsz.pscript.token.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lex {
    //private static String num = "-?[0-9]*.?[0-9]+";
    private static String complex_num="(([1-9][0-9]*)?[-]?[1-9][0-9]*i)";//型如1+2i,i前面必须有系数才能保证识别为复数
    private static String num="([\\d]+([.][\\d]+)?([Ee]-?[\\d]+)?)";
    private static String str = "(\"(\\\\\"|\\\\\\\\|\\\\n|[^\"])*\")";
    private static String op = "(==|<=|>=|!=|&&|\\|\\||\\p{Punct})";
    private static String id = "([A-Z_a-z][A-Z_a-z0-9]*)";
    public static String regexPat
            = "\\s*((//.*)|" +complex_num+"|"+ num + "|" + str +"|"+op+"|" + id+")?";
    private Pattern pattern = Pattern.compile(regexPat);
    private List<Token> list = new ArrayList<Token>();
    private boolean isEnd;
    private LineNumberReader reader;

    public Lex(Reader r) {
        isEnd = false;
        reader = new LineNumberReader(r);
    }

    public Token read() throws LexException {
        if (readToken(0))//返回一个token
            return list.remove(0);
        else//否则返回EOF
            return Token.EOF;
    }

    public void addToken(int lineNo, @NotNull Matcher matcher) {
        String m = matcher.group(1);//全局匹配
        if (m != null) {//获取
            if (matcher.group(2) == null) {//非注释
                Token token;
                if (matcher.group(3) != null)
                    token = new NumToken(lineNo, "complex_num", m);
                else if (matcher.group(5) != null)
                    token = new NumToken(lineNo, "num", m);
                else if (matcher.group(8) != null)
                    token = new StrToken(lineNo, "string", m);
                else if(matcher.group(10)!=null)
                    token = new OpToken(lineNo,"operator",m);
                else
                    token = new IdToken(lineNo, "identifier", m);
                list.add(token);
            }
        }
    }

    public void readLine() throws LexException {
        String line;
        try {
            line = reader.readLine();
        } catch (IOException e) {
            throw new LexException(e);
        }

        if (line == null) {
            isEnd = true;
            return;
        }
        int lineNo = reader.getLineNumber();
        Matcher matcher = pattern.matcher(line);
        matcher.useTransparentBounds(true).useAnchoringBounds(false);
        int pos = 0;
        int endPos = line.length();
        while (pos < endPos) {
            matcher.region(pos, endPos);
            if (matcher.lookingAt()) {
                addToken(lineNo, matcher);
                pos = matcher.end();//调整下一次读入的位置
            } else
                throw new TokenException("error in" + lineNo);
        }
        list.add(new IdToken(lineNo, "EOL", Token.EOL));
    }

    private boolean readToken(int i) throws LexException {//往list中读入几个token
        while (i >= list.size())
            if (!isEnd)
                readLine();
            else
                return false;
        return true;
    }
}
