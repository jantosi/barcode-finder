package jantosi.personalproject.barcodefinder;

import java.util.ArrayList;
import java.util.List;

import jantosi.personalproject.barcodefinder.model.BarcodeToFind;
import jantosi.personalproject.barcodefinder.model.MatchMode;

/**
 * Created by Kuba on 17.05.2017.
 */

public class BarcodeMatcher {
    public static List<BarcodeToFind> matches(String key) {

        List<BarcodeToFind> result = new ArrayList<>();
        result.addAll(byExactMatch(key));
        result.addAll(byStart(key));
        result.addAll(byEnd(key));
        result.addAll(byContaining(key));
        return result;
    }

    private static List<BarcodeToFind> byExactMatch(String key) {
        return BarcodeToFind.find(BarcodeToFind.class, "text = ? and match_mode = ?", key, MatchMode.EXACT.toString());
    }

    private static List<BarcodeToFind> byStart(String key) {
        return BarcodeToFind.find(BarcodeToFind.class, "? LIKE text || '%' and match_mode = ?", key+"%", MatchMode.STARTS_WITH.toString());
    }

    private static List<BarcodeToFind> byEnd(String key) {
        return BarcodeToFind.find(BarcodeToFind.class, "? LIKE '%' || text and match_mode = ?", "%"+key, MatchMode.ENDS_WITH.toString());
    }

    private static List<BarcodeToFind> byContaining(String key) {
        return BarcodeToFind.find(BarcodeToFind.class, "? LIKE '%' || text || '%' and match_mode = ?", "%"+key+"%", MatchMode.CONTAINS.toString());
    }
}
