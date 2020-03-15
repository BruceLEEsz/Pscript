package com.lsz.pscript.parse;

import java.util.Objects;

public class GoTo {
    String closureId;
    String path;

    public GoTo(String closureId, String path) {
        super();
        this.closureId = closureId;
        this.path = path;
    }

    public String getClosureId() {
        return closureId;
    }

    public void setClosureId(String closureId) {
        this.closureId = closureId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GoTo goTo = (GoTo) o;
        return Objects.equals(closureId, goTo.closureId) &&
                Objects.equals(path, goTo.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(closureId, path);
    }

    @Override
    public String toString() {
        return "GoTo{" +
                "closureId='" + closureId + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}
