package xyz.grinner.verylibrary.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.grinner.verylibrary.manager.BookManager;
import xyz.grinner.verylibrary.parser.PdfParser;
import xyz.grinner.verylibrary.parser.PowerPointParser;
import xyz.grinner.verylibrary.parser.WordParser;
import xyz.grinner.verylibrary.schema.Page;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Service
public class BookService {

    @Autowired
    private BookManager bookManager;

    @Autowired
    private PowerPointParser pptParser;

    @Autowired
    private WordParser wordParser;

    @Autowired
    private PdfParser pdfParser;

    public int addBooks(String filepath){
        int total = 0;
        File inputFile = new File(filepath);
        LinkedList<File> filesToParse = new LinkedList<>();
        filesToParse.add(inputFile);
        File fileToParse = null;
        while ((fileToParse= filesToParse.poll()) != null){
            if(fileToParse.isFile()){
                String fileName = fileToParse.getName();
                List<Page> pages = new ArrayList<>();
                if(fileName.endsWith(".doc")||fileName.endsWith(".docx")){
                    pages = wordParser.parse(fileToParse);
                }else if(fileName.endsWith(".ppt")||fileName.endsWith(".pptx")){
                    pages = pptParser.parse(fileToParse);
                }else if(fileName.endsWith(".pdf")){
                    pages = pdfParser.parse(fileToParse);
                }
                bookManager.index(pages);
                total += pages.size();
            }else {
                File[] files = inputFile.listFiles();
                filesToParse.addAll(Arrays.asList(files));
            }
        }
        return total;
    }


}
