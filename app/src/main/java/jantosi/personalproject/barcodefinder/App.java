package jantosi.personalproject.barcodefinder;

import com.orm.SugarApp;
import com.orm.SugarContext;

/**
 * Created by Kuba on 17.05.2017.
 */

public class App extends SugarApp {
    @Override
    public void onCreate() {
        super.onCreate();
        SugarContext.init(this);
    }

    @Override
    public void onTerminate() {
        SugarContext.terminate();
        super.onTerminate();
    }
}
