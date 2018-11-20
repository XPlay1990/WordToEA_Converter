/*
 *  Copyright (C) Jan Adamczyk (j_adamczyk@hotmail.com) 2017
 */
package EAForm;

/**
 *
 * @author jan.adamczyk
 */
public class EAFormat {

    String id;
    String title;
    String text;

    public EAFormat(String id, String title, String text) {
        this.id = id;
        this.title = title;
        this.text = text;
    }

    @Override
    public String toString() {
        return "test";
    }
}
