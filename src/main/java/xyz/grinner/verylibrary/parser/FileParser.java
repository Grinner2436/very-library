package xyz.grinner.verylibrary.parser;

import xyz.grinner.verylibrary.schema.Page;

import java.io.File;
import java.util.List;

@FunctionalInterface
public interface FileParser {
    List<Page> parse(File file);
}
