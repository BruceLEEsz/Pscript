package com.lsz.pscript.token;

public class NumToken extends Token {
    private String value;

    public String getValue() {
        return value;
    }

    public NumToken(int line, String type, String value) {
        super(line, type);
        this.value = value;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return getLineNumber() + ", " + value + ", " + getTokenType();
    }
}
