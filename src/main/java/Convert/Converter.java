/*
 *  Copyright (C) Jan Adamczyk (j_adamczyk@hotmail.com) 2017
 */
package Convert;

import EAForm.EAFormat;
import Logging.MyLogger;
import com.opencsv.CSVWriter;
import com.sun.nio.zipfs.ZipPath;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Level;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
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
            extractAllEmbeddings(document);

//            XWPFWordExtractor we = new XWPFWordExtractor(document);
//            we.setConcatenatePhoneticRuns(true);
//            we.setFetchHyperlinks(true);
//            we.getText();
            extractRequirements(document);
        } catch (IOException ex) {
            MyLogger.log(Level.ERROR, ExceptionUtils.getStackTrace(ex));
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
                } catch (IOException ex) {
                    MyLogger.log(Level.ERROR, ExceptionUtils.getStackTrace(ex));
                }
            });
        } catch (IOException ex) {
            MyLogger.log(Level.ERROR, ExceptionUtils.getStackTrace(ex));
        }
    }

    private void extractAllEmbeddings(XWPFDocument document) {
        try {
            List<PackagePart> allEmbeddedParts = document.getAllEmbeddedParts();
            File embeddingsFile = new File("embeddings.zip");
            for (PackagePart part : allEmbeddedParts) {
                OPCPackage aPackage = part.getPackage();
                aPackage.save(embeddingsFile);
            }
        } catch (OpenXML4JException | IOException ex) {
            MyLogger.log(Level.ERROR, ExceptionUtils.getStackTrace(ex));
        }
    }

    private void extractRequirements(XWPFDocument document) {
        Writer writer = null;
        String lastID;
        try {
            List<XWPFParagraph> paragraphs = document.getParagraphs();
            ArrayList<String[]> eaFormatsHolder = new ArrayList<>();
            String[] init = {"GUID$Name$Type$Notes$TagValue_CST_ID"};
            eaFormatsHolder.add(init);

            Iterator itr = paragraphs.iterator();

            while (itr.hasNext()) {
                XWPFParagraph paragraph = (XWPFParagraph) itr.next();
                String paragraphText = paragraph.getParagraphText();
                if (!(paragraphText.contains("ID"))) {
                    itr.next();
                } else {
                    lastID = paragraphText;
                    while (itr.hasNext()) {
                        String id = lastID;
                        String title = ((XWPFParagraph) itr.next()).getParagraphText();
                        String description = "";

                        while (itr.hasNext()) {
                            String text = ((XWPFParagraph) itr.next()).getParagraphText();
                            if (text.contains("ID")) {
                                lastID = text;
                                EAFormat eaFormat = new EAFormat(id, title, description);
                                eaFormatsHolder.add(eaFormat.getStringArray());
                                break;
                            } else {
                                description += text;
                            }
                        }
                    }
                }
            }

            writer = Files.newBufferedWriter(Paths.get("Requirements.csv"));
            try (CSVWriter csvWriter = new CSVWriter(writer,
                    CSVWriter.DEFAULT_SEPARATOR,
                    CSVWriter.NO_QUOTE_CHARACTER,
                    CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                    CSVWriter.DEFAULT_LINE_END)) {

                csvWriter.writeAll(eaFormatsHolder);
                csvWriter.flush();
            }
        } catch (IOException ex) {
            MyLogger.log(Level.ERROR, ExceptionUtils.getStackTrace(ex));
        } finally {
            try {
                writer.close();
            } catch (IOException ex) {
                MyLogger.log(Level.ERROR, ExceptionUtils.getStackTrace(ex));
            }
        }
    }

}
