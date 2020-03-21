package com.lsz.pscript.token;

public class StrToken extends Token {

    private String value;

    public StrToken(int line, String type, String value) {
        super(line, type);
        this.value = value;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return getLineNumber() + ", " + value + ", " + getTokenType();
    }
}
