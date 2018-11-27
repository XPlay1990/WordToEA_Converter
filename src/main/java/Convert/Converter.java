/*
 *  Copyright (C) Jan Adamczyk (j_adamczyk@hotmail.com) 2017
 */
package Convert;

import EAForm.EAFormat;
import Logging.MyLogger;
import com.opencsv.CSVWriter;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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
    private static final String[] LINK_STRINGS = {"<", ">"};

    /**
     *
     * @param file
     */
    public Converter(File file) {
        this.file = file;
    }

    /**
     *
     * @throws java.io.FileNotFoundException
     * @throws org.apache.poi.openxml4j.exceptions.OpenXML4JException
     */
    public void convert() throws FileNotFoundException, IOException, OpenXML4JException {
        try (FileInputStream fis = new FileInputStream(file.getAbsolutePath())) {
            XWPFDocument document = new XWPFDocument(fis);

            extractImages(document);
            extractAllEmbeddings(document);

//            XWPFWordExtractor we = new XWPFWordExtractor(document);
//            we.setConcatenatePhoneticRuns(true);
//            we.setFetchHyperlinks(true);
//            we.getText();
            extractRequirements(document);
        }
    }

    private void extractImages(XWPFDocument document) throws IOException {
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
    }

    private void extractAllEmbeddings(XWPFDocument document) throws IOException, OpenXML4JException {
        List<PackagePart> allEmbeddedParts = document.getAllEmbeddedParts();
        File embeddingsFile = new File("embeddings.zip");
        for (PackagePart part : allEmbeddedParts) {
            OPCPackage aPackage = part.getPackage();
            aPackage.save(embeddingsFile);
        }
    }

    private void extractRequirements(XWPFDocument document) throws IOException {
        String lastID;
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
                    ArrayList<String> imageLinks = new ArrayList<>();
                    String title = ((XWPFParagraph) itr.next()).getParagraphText();
                    String description = "";

                    while (itr.hasNext()) {
                        String text = ((XWPFParagraph) itr.next()).getParagraphText();
                        if (text.contains("ID")) {
                            lastID = text;
                            EAFormat eaFormat = new EAFormat(id, title, description, (ArrayList<String>) imageLinks.clone());
                            eaFormatsHolder.add(eaFormat.getStringArray());
                            imageLinks.clear();
                            break;
                        } else {
                            if (text.contains("<image")) {
                                for (String replace : LINK_STRINGS) {
                                    text = text.replaceAll(replace, "");
                                }
                                imageLinks.add(text);
                            } else {
                                description += text;
                            }
                        }
                    }
                }
            }
        }

        BufferedWriter writer = Files.newBufferedWriter(Paths.get("Requirements.csv"));
        try (CSVWriter csvWriter = new CSVWriter(writer,
                CSVWriter.NO_ESCAPE_CHARACTER, //CSVWriter.DEFAULT_SEPARATOR
                CSVWriter.NO_QUOTE_CHARACTER,
                CSVWriter.NO_ESCAPE_CHARACTER,
                CSVWriter.DEFAULT_LINE_END)) {

            csvWriter.writeAll(eaFormatsHolder);
            csvWriter.flush();
        }
        try {
            writer.close();
        } catch (IOException ex) {
            MyLogger.log(Level.ERROR, ExceptionUtils.getStackTrace(ex));
        }
    }
}
