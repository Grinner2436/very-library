package xyz.grinner.verylibrary.schema;

import lombok.Data;

@Data
public class Page {
    private String fileName;
    private int pageNum;
    private String content;
}
