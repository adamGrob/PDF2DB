package com.PDF2DB;

import com.PDF2DB.parser.Parser;
import org.apache.tika.exception.TikaException;
import org.xml.sax.SAXException;

import java.io.IOException;

public class Main {

    public static void main(final String[] args) {

        String pdfToParse = "/home/grobadam/IdeaProjects/PDF2DB/src/main/resources/RollCall.pdf";

        Parser parser = new Parser(pdfToParse);
        try {
            parser.parse();
        } catch (IOException | TikaException | SAXException e) {
            e.printStackTrace();
        }


    }


}