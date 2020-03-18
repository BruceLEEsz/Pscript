package com.lsz.pscript.parse;

import java.util.Objects;

public class GoTo {
    String closureID;
    String path;

    public GoTo(String closureID, String path) {
        super();
        this.closureID = closureID;
        this.path = path;
    }// 路径

    public String getClosureID() {
        return closureID;
    }

    public String getPath() {
        return path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GoTo goTo = (GoTo) o;
        return Objects.equals(closureID, goTo.closureID) &&
                Objects.equals(path, goTo.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(closureID, path);
    }

    @Override
    public String toString() {
        return "GoTo{" +
                "closureID='" + closureID + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}
