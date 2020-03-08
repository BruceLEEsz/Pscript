package pscript.token;

public abstract class Token {

    private String value="";
    private String tokenType;
    private int lineNumber;
    public static final Token EOF = new Token(-1, "EOF") {
    };
    public static final String EOL = "\\n";

    public Token(int i, String s) {
        lineNumber = i;
        tokenType = s;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getValue() {
        return value;
    }

    /**
    public boolean isIdentifier() {
        return tokenType.equals("identifier");
    }

    public boolean isNumber() {
        return tokenType.equals("number");
    }

    public boolean isString() {
        return tokenType.equals("string");
    }

    public boolean isOperator() {
        return tokenType.equals("operator");
    }
*/
    public String toString() {
        return "";
    }
}

