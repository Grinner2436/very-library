package xyz.grinner.verylibrary.parser;

import org.apache.poi.hslf.usermodel.HSLFSlideShow;
import org.apache.poi.sl.extractor.SlideShowExtractor;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.springframework.stereotype.Component;
import xyz.grinner.verylibrary.schema.Page;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class PowerPointParser implements FileParser {
    public List<Page> parse(File file){
        List<Page> result = new ArrayList<>();
        try (FileInputStream fileInputStream = new FileInputStream(file)){
            String content = null;
            String filePath = file.getAbsolutePath();
            if(filePath.contains(".pptx")){
                XMLSlideShow xslfSlideShow = new XMLSlideShow(fileInputStream);
                SlideShowExtractor slideShowExtractor = new SlideShowExtractor(xslfSlideShow);
                StringBuffer resultString = new StringBuffer();
                xslfSlideShow.getSlides().stream().map(slide -> slide.getNotes())
                        .filter(notes -> notes != null)
                        .map(notes -> notes.getTextParagraphs())
                        .flatMap(Collection::stream)
                        .flatMap(Collection::stream)
                        .forEach(p -> {
                            String s = p.toString();
                            resultString.append(s);
                        });
                content = slideShowExtractor.getText();
                resultString.append(content);
            }else{
                HSLFSlideShow hslfSlideShow = new HSLFSlideShow(fileInputStream);
                SlideShowExtractor slideShowExtractor = new SlideShowExtractor(hslfSlideShow);
                StringBuffer resultString = new StringBuffer();
                hslfSlideShow.getNotes().stream().map(note -> note.getTextParagraphs())
                        .flatMap(Collection::stream)
                        .flatMap(Collection::stream)
                        .forEach(p -> {
                            String s = p.toString();
                            resultString.append(s);
                        });
                content = slideShowExtractor.getText();
                resultString.append(content);
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
