package com.cinder.filefragment.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FragmentFile {
    private String fileName;
    private Integer index;
    private Integer total;
    private Long start;
    private Integer length;
    private byte[] data;
    private BufferedInputStream inputStream;
    private BufferedOutputStream outputStream;
    private File file;
    private String secretKey;

    public FragmentFile(String fileName, Integer index, Integer total, Long start, Integer length,File file,BufferedOutputStream outputStream,String secretKey) throws FileNotFoundException {
        this.fileName = fileName;
        this.index = index;
        this.total = total;
        this.start = start;
        this.length = length;
        this.file = file;
        this.inputStream = new BufferedInputStream(new FileInputStream(file));
        this.outputStream = outputStream;
        this.secretKey = secretKey;
    }
}
