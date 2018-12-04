package com.PDF2DB.parser;

import com.PDF2DB.db.DBContract;
import com.PDF2DB.db.PostgresHelper;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.pdf.PDFParser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    private String pdfToParseLocation;

    public Parser(String pdfToParseLocation) {
        this.pdfToParseLocation = pdfToParseLocation;
    }

    public void parse () throws IOException, TikaException, SAXException {
        BodyContentHandler handler = new BodyContentHandler();
        Metadata metadata = new Metadata();
        FileInputStream inputstream = new FileInputStream(new File(pdfToParseLocation));
        ParseContext pcontext = new ParseContext();

        //parsing the document using PDF parser
        PDFParser pdfparser = new PDFParser();
        pdfparser.parse(inputstream, handler, metadata,pcontext);

        //getting the content of the document
        String pdfString = handler.toString();
        System.out.println("Contents of the PDF :" + pdfString);
        printVotes(pdfString);

        //getting metadata of the document
        System.out.println("Metadata of the PDF:");
        String[] metadataNames = metadata.names();

        for(String name : metadataNames) {
            System.out.println(name+ " : " + metadata.get(name));
        }

        PostgresHelper postgresHelper = new PostgresHelper(DBContract.HOST, DBContract.DB_NAME, DBContract.PASSWORD, DBContract.USERNAME);
        try {
            postgresHelper.execQuery("");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void printVotes(String text) {
        // (?s)(EN\s)(.{1,300})(\s\n\n\d+\.\d+\.\sFinal\svote\s\n\n)(\d+\s[\+\-0]\s\n{2})(.*?)(Corrections to vote)  this is the greater pattern

        String patternString = "(?s)(\\d+\\s[\\+\\-0]\\s\\n{2})(.*?)(\\s\\n\\n\\s\\n\\n)";  // this pattern can cut down all the voting segments
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(text);
        System.out.println("-------------------------------------------------------------------------------------------");
        int count = 0;
        while(matcher.find()) {
            count++;
            System.out.println("found: " + count + " : "
                    + matcher.start() + " - " + matcher.end());
            System.out.println(text.substring(matcher.start(), matcher.end()));
        }

    }
}
