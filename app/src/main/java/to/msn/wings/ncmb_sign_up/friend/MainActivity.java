package to.msn.wings.ncmb_sign_up.friend;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.nifty.cloud.mb.core.FetchCallback;
import com.nifty.cloud.mb.core.NCMBBase;
import com.nifty.cloud.mb.core.NCMBException;
import com.nifty.cloud.mb.core.NCMBObject;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

import to.msn.wings.ncmb_sign_up.R;

/**
 * Created by 4163103 on 2017/10/20.
 */

public class MainActivity extends AppCompatActivity {

    ArrayList nameList;
    ArrayList idList;
    ArrayList friendList;
    ListView listView;
    String name;
    String objectid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameList = new ArrayList();
        idList = new ArrayList();
        friendList = new ArrayList();


        Intent intent = getIntent();
        nameList = intent.getStringArrayListExtra("key1");
        idList = intent.getStringArrayListExtra("key2");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.base);
        listView = (ListView) findViewById(R.id.listView);

        final HashMap<String,String> data = new HashMap<>();
        for (int i = 0; i < nameList.size(); i++){
            data.put(nameList.get(i).toString(),idList.get(i).toString());
        }

        for (Object obj : nameList){
            String str = obj.toString();
            adapter.add(str);
        }

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        name = nameList.get(position).toString();
                        objectid = data.get(name);
                        add();
                        dialog();
                        break;
                    default:
                        name = nameList.get(position).toString();
                        objectid = data.get(name);
                        add();
                        dialog();
                        break;
                }
            }
        });
    }

    public void dialog(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("確認");
        dialog.setMessage(name + "でよろしいですか？");
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //OK
                Intent intent = new Intent(MainActivity.this,friendActivity.class);
                intent.putStringArrayListExtra("key1",friendList);
                intent.putExtra("key2",objectid);
                startActivity(intent);
            }
        });
        dialog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //NG
            }
        });
        dialog.create().show();
    }

    public void add(){
        final NCMBObject object = new NCMBObject("UserClass");
        object.setObjectId(objectid);
        object.fetchInBackground(new FetchCallback() {
            @Override
            public void done(NCMBBase ncmbBase, NCMBException e) {
                if (e != null){
                    Log.i("Log","Main失敗");
                } else {
                    Log.i("Log","Main成功");
                    JSONArray data = object.getJSONArray("friend");
                    for (int i = 0; i < data.length(); i++) {
                        try {
                            friendList.add(data.getString(i).toString());
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            }
        });
    }
}