package xyz.grinner.verylibrary.parser;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Component;
import xyz.grinner.verylibrary.schema.Page;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class PdfParser implements FileParser {
    public List<Page> parse(File file){
        List<Page> result = new ArrayList<>();
        try (FileInputStream fileInputStream = new FileInputStream(file);
             PDDocument pdDocument = PDDocument.load(fileInputStream)){
            PDFTextStripper stripper = new PDFTextStripper();
            for(int pageNum = 0; pageNum < pdDocument.getNumberOfPages(); pageNum++){
                stripper.setStartPage(pageNum);
                stripper.setEndPage(pageNum);
                String content = stripper.getText(pdDocument);
                if(content != null && content.trim().length() > 10){
                    Page page = new Page();
                    page.setFileName(file.getAbsolutePath());
                    page.setPageNum(pageNum);
                    page.setContent(content);
                    result.add(page);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
