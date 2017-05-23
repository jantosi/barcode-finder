package jantosi.personalproject.barcodefinder.model.viewmapper;

import android.support.annotation.IdRes;

import jantosi.personalproject.barcodefinder.R;
import jantosi.personalproject.barcodefinder.model.MatchMode;

/**
 * Created by Kuba on 17.05.2017.
 */

public class MatchModeRadioButtonMapper {
    public static MatchMode fromId(@IdRes int id) {
        switch (id){
            case R.id.barcodeMatchModeStartsWith: return MatchMode.STARTS_WITH;
            case R.id.barcodeMatchModeEndsWith: return MatchMode.ENDS_WITH;
            case R.id.barcodeMatchModeContains: return MatchMode.CONTAINS;
            case R.id.barcodeMatchModeExact: return MatchMode.EXACT;
        }
        return null;
    }
}
