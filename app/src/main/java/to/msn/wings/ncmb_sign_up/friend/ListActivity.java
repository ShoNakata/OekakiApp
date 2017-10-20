package to.msn.wings.ncmb_sign_up.friend;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import to.msn.wings.ncmb_sign_up.R;

/**
 * Created by 4163103 on 2017/10/20.
 */

public class ListActivity  extends AppCompatActivity {
    ListView listView;
    ArrayList arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
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

    public void onClick(View v){
        finish();
    }
}