package cl.ucn.disc.dam.discnews.activities;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.BaseAdapter;

public class MainActivity extends ListActivity {

    private BaseAdapter articleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        super.setListAdapter(articleAdapter);


    }


}
