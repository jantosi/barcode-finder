package jantosi.personalproject.barcodefinder.model;

import com.orm.SugarRecord;

/**
 * Created by Kuba on 17.05.2017.
 */

public class BarcodeToFind extends SugarRecord {
    String text;
    MatchMode matchMode;

    public BarcodeToFind() {
    }

    public BarcodeToFind(String text, MatchMode matchMode) {
        this.text = text;
        this.matchMode = matchMode;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public MatchMode getMatchMode() {
        return matchMode;
    }

    public void setMatchMode(MatchMode matchMode) {
        this.matchMode = matchMode;
    }
}
