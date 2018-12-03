package com.PDF2DB;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.PDF2DB.db.DBContract;
import com.PDF2DB.db.PostgresHelper;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.pdf.PDFParser;
import org.apache.tika.sax.BodyContentHandler;

import org.xml.sax.SAXException;

public class PdfParse {

    public static void main(final String[] args) throws IOException, TikaException, SAXException {

        BodyContentHandler handler = new BodyContentHandler();
        Metadata metadata = new Metadata();
        FileInputStream inputstream = new FileInputStream(new File("/home/grobadam/IdeaProjects/PDF2DB/src/main/resources/RollCall.pdf"));
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


    public static void printVotes(String text) {
        String patternString = "(?s)(\\d+\\s[\\+\\-0]\\s\\n{2})(.*?)(\\s\\n\\n\\s\\n\\n)";
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