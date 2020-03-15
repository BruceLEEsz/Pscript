package com.lsz.pscript.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class FileRead {
    public String getProduces(String path) throws IOException {
        File file = new File(path);
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line;
        StringBuilder stringBuffer = new StringBuilder();
        while ((line = br.readLine()) != null) {
            stringBuffer.append(line).append("\n");
        }
        br.close();
        return stringBuffer.toString().trim();
    }
}
