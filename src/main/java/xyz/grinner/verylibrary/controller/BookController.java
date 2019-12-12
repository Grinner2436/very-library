package xyz.grinner.verylibrary.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.grinner.verylibrary.service.BookService;

@RestController
@RequestMapping("/book")
public class BookController {
    @Autowired
    private BookService bookService;

    @ResponseBody
    @GetMapping("/")
    public int addBooks(@RequestParam("path") String path){
        return bookService.addBooks(path);
    }

}
