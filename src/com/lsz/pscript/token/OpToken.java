package com.lsz.pscript.token;

public class OpToken extends Token {
    private String value;

    public OpToken(int line, String type, String value) {
        super(line, type);
        this.value = value;
    }

    @Override
    public String toString() {
        return getLineNumber() + ", " + value + ", " + getTokenType();
    }

    public String getValue() {
        return value;
    }
}
