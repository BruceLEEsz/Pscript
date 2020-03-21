package com.lsz.pscript.token;

public class IdToken extends Token {
    String value;

    public IdToken(int line, String type, String value) {
        super(line, type);
        this.value = value;
    }

    public String getValue() {
        return value;
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
