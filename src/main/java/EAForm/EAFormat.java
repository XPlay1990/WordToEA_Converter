/*
 *  Copyright (C) Jan Adamczyk (j_adamczyk@hotmail.com) 2017
 */
package EAForm;

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
    private String imageLink;

    /**
     *
     * @param id
     * @param title
     * @param description
     * @param imageLink
     */
    public EAFormat(String id, String title, String description, String imageLink) {
        this.title = title;
        this.description = "\"" + description;
        if (imageLink != null) {
            this.imageLink = "<a href=\"\"extractedImages\\" + imageLink + "\"\"><font color=\"\"#0000ff\"\"><u>" + imageLink + "</u></font></a>";
        }
        this.id = id;
    }

    /**
     *
     * @return
     */
    public String[] getStringArray() {
//        description = '\"' + description + '\"';
        if (imageLink == null) {
            String[] output = {eaID + seperator + title + seperator + requirements + seperator + description + "\"" + seperator + id};
            return output;
        } else {
            String[] output = {eaID + seperator + title + seperator + requirements + seperator + description + imageLink + "\"" + seperator + id};
            return output;
        }
    }
}
