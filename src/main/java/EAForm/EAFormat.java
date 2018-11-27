/*
 *  Copyright (C) Jan Adamczyk (j_adamczyk@hotmail.com) 2017
 */
package EAForm;

import java.util.ArrayList;

/**
 *
 * @author jan.adamczyk
 */
public class EAFormat {

    private static final String LINEFEED_STRING = "\r\n";
    private final String eaID = "{}";
    private final String seperator = "$";
    private final String requirements = "Requirement";

    private String title = "";
    private String description = "";
    private String id = "";
    private ArrayList<String> imageLinks = new ArrayList<>();

    /**
     *
     * @param id
     * @param title
     * @param description
     * @param imageLinks
     */
    public EAFormat(String id, String title, String description, ArrayList<String> imageLinks) {
        this.title = title;
        this.description = "\"" + description;
        if (!imageLinks.isEmpty()) {
            imageLinks.forEach((link) -> {
                this.imageLinks.add("<a href=\"\"extractedImages\\" + link + "\"\"><font color=\"\"#0000ff\"\"><u>" + link + "</u></font></a>");
            });
        }
        this.id = id;
    }

    /**
     *
     * @return
     */
    public String[] getStringArray() {
//        description = '\"' + description + '\"';
        if (imageLinks.isEmpty()) {
            String[] output = {eaID + seperator + title + seperator + requirements + seperator + description + "\"" + seperator + id};
            return output;
        } else {
            String[] output = {eaID + seperator + title + seperator + requirements + seperator + description + imageLinks.toString() + "\"" + seperator + id};
            return output;
        }
    }
}
