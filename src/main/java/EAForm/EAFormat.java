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
    private final String seperator = LINEFEED_STRING + "$";
    private final String requirements = "Requirement";

    private final String title;
    private final String description;
    private final String id;
    private final String imageLink;

    /**
     *
     * @param id
     * @param title
     * @param description
     * @param imageLink
     */
    public EAFormat(String id, String title, String description, String imageLink) {
        this.title = title;
        this.description = description;
        this.imageLink = LINEFEED_STRING + "=HYPERLINK(" + '\"' + "extractedImages/" + imageLink + '\"' + ")";
        this.id = id;
    }

    /**
     *
     * @return
     */
    public String[] getStringArray() {
//        description = '\"' + description + '\"';
        String[] output = {eaID + seperator + title + seperator + requirements + seperator + description, imageLink, seperator + id};
        return output;
    }
}
