/*
 *  Copyright (C) Jan Adamczyk (j_adamczyk@hotmail.com) 2017
 */
package EAForm;

/**
 *
 * @author jan.adamczyk
 */
public class EAFormat {

    private final String eaID = "{}";
    private final String seperator = "$";
    private final String requirements = "Requirement";

    private String title = "";
    private String description = "";
    private String id = "";

    /**
     *
     * @param id
     * @param title
     * @param description
     */
    public EAFormat(String id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    /**
     *
     * @return
     */
    public String[] getStringArray() {
//        description = '\"' + description + '\"';
        String[] output = {eaID + seperator + title + seperator + requirements + seperator + description + seperator + id};
        return output;
    }
}
