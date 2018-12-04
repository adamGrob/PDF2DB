package com.PDF2DB;

import com.PDF2DB.controller.SegmentCreator;
import com.PDF2DB.controller.parser.Parser;
import org.apache.tika.exception.TikaException;
import org.xml.sax.SAXException;

import java.io.IOException;

public class Main {

    public static void main(final String[] args) throws TikaException, IOException, SAXException {

        String pdfToParse = "/home/grobadam/IdeaProjects/PDF2DB/src/main/resources/RollCall.pdf";
        Parser parser = new Parser(pdfToParse);
        SegmentCreator segmentCreator = new SegmentCreator(parser.getStringFromPdf());
        segmentCreator.createVoteSegments();
        segmentCreator.printSegments();


    }


}