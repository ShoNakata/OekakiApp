package to.msn.wings.ncmb_sign_up.friend;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import to.msn.wings.ncmb_sign_up.R;

/**
 * Created by 4163104 on 2017/11/10.
 */

public class search_hit_Activity extends Activity {

    ArrayList arrayList;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hit);

        arrayList = new ArrayList();

        Intent intent = getIntent();
        arrayList = intent.getStringArrayListExtra("key");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.base);
        listView = (ListView) findViewById(R.id.listView);

        for (Object obj : arrayList){
            String str = obj.toString();
            adapter.add(str);
        }

        listView.setAdapter(adapter);
    }
}
