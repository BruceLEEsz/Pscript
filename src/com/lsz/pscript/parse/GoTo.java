package com.lsz.pscript.parse;

import java.util.Objects;

public class GoTo {
    String closureID;// 闭包初始名称
    String path;

    public GoTo(String closureID, String path) {
        super();
        this.closureID = closureID;
        this.path = path;
    }// 路径

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return new String("(" + closureID + "," + path + ")");
    }

    public String getClosureID() {
        return closureID;
    }

    public void setClosureID(String closureID) {
        this.closureID = closureID;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (null == obj) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }

        GoTo user = (GoTo) obj;
        String tmp = user.closureID + user.path;

        if (tmp.equals(this.closureID + this.path)) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + closureID.hashCode();
        hash = 31 * hash + path.hashCode();
        return hash;
    }

}
