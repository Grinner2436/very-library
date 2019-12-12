package xyz.grinner.verylibrary.parser;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Component;
import xyz.grinner.verylibrary.schema.Page;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class WordParser implements FileParser {

    public List<Page> parse(File file){
        List<Page> result = new ArrayList<>();
        try (FileInputStream fileInputStream = new FileInputStream(file)){
            String content = null;
            String filePath = file.getAbsolutePath();
            if(filePath.contains(".docx")){
                XWPFDocument xwpfDocument = new XWPFDocument(fileInputStream);
                XWPFWordExtractor xwpfWordExtractor = new XWPFWordExtractor(xwpfDocument);
                content = xwpfWordExtractor.getText();
            }else{
                HWPFDocument hwpfDocument = new HWPFDocument(fileInputStream);
                content = hwpfDocument.getDocumentText();
            }
            if(content != null && content.trim().length() > 0) {
                Page page = new Page();
                page.setFileName(filePath);
                page.setPageNum(0);
                page.setContent(content);
                result.add(page);
                return result;
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return result;
    }
}
