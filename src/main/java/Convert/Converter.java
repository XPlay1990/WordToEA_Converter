/*
 *  Copyright (C) Jan Adamczyk (j_adamczyk@hotmail.com) 2017
 */
package Convert;

import Logging.MyLogger;
import com.opencsv.CSVWriter;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipOutputStream;
import org.apache.logging.log4j.Level;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.PackagePart;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFPictureData;

/**
 *
 * @author jan.adamczyk
 */
public class Converter {

    private final File file;

    /**
     *
     * @param file
     */
    public Converter(File file) {
        this.file = file;
    }

    /**
     *
     */
    public void convert() {
        try (FileInputStream fis = new FileInputStream(file.getAbsolutePath())) {
            XWPFDocument document = new XWPFDocument(fis);

            extractImages(document);

//            XWPFWordExtractor we = new XWPFWordExtractor(document);
//            we.setConcatenatePhoneticRuns(true);
//            we.setFetchHyperlinks(true);
//            we.getText();
            extractRequirements(document);
        } catch (IOException ex) {
            MyLogger.log(Level.ERROR, ex.getMessage());
        }
    }

    private void extractImages(XWPFDocument document) {
        try {
            List<XWPFPictureData> allPictures = document.getAllPictures();
            //traverse through the list and write each image to a file
            Files.createDirectories(Paths.get("extractedImages"));
            allPictures.forEach((pic) -> {
                try (FileOutputStream outputStream = new FileOutputStream("extractedImages/" + pic.getFileName())) {
                    outputStream.write(pic.getData());
                } catch (IOException e) {
                    MyLogger.log(Level.ERROR, e.getMessage());
                }
            });
        } catch (IOException ex) {
            MyLogger.log(Level.ERROR, ex.getMessage());
        }
    }

    private void extractAllEmbeddings(XWPFDocument document) {
        FileOutputStream fos = null;
        try {
            List<PackagePart> allEmbeddedParts = document.getAllEmbeddedParts();
            fos = new FileOutputStream("test.zip");
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            ZipOutputStream zos = new ZipOutputStream(bos);
            for (PackagePart part : allEmbeddedParts) {
//                part.save((ZipOutputStream) zos);
            }
        } catch (FileNotFoundException | OpenXML4JException ex) {
            MyLogger.log(Level.ERROR, ex.getMessage());
        } finally {
            try {
                fos.close();
            } catch (IOException ex) {
            }
        }
    }

    private void extractRequirements(XWPFDocument document) {
        Writer writer = null;
        try {
            List<XWPFParagraph> paragraphs = document.getParagraphs();
            ArrayList<String[]> p = new ArrayList<>();
            String[] init = {"GUID$Name$Type$Notes$TagValue_CST_ID"};
            p.add(init);
            for (XWPFParagraph para : paragraphs) {
                String[] paragraphText = {para.getParagraphText()};
                p.add(paragraphText);
            }

            writer = Files.newBufferedWriter(Paths.get("Requirements.csv"));
            try (CSVWriter csvWriter = new CSVWriter(writer,
                    CSVWriter.DEFAULT_SEPARATOR,
                    CSVWriter.NO_QUOTE_CHARACTER,
                    CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                    CSVWriter.DEFAULT_LINE_END)) {

                csvWriter.writeAll(p);
                csvWriter.flush();
            }
        } catch (IOException ex) {
            MyLogger.log(Level.ERROR, ex.getMessage());
        } finally {
            try {
                writer.close();
            } catch (IOException ex) {
            }
        }
    }

}
