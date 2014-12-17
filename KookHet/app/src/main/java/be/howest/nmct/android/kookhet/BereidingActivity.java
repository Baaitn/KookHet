package be.howest.nmct.android.kookhet;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by Jonathan on 3/12/2014.
 */
public class BereidingActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView textview = new TextView(this);
        textview.setText("test");
        this.setContentView(textview);
    }
}
