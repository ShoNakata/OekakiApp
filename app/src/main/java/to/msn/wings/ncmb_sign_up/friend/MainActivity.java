package to.msn.wings.ncmb_sign_up.friend;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.nifty.cloud.mb.core.DoneCallback;
import com.nifty.cloud.mb.core.FetchCallback;
import com.nifty.cloud.mb.core.NCMB;
import com.nifty.cloud.mb.core.NCMBBase;
import com.nifty.cloud.mb.core.NCMBException;
import com.nifty.cloud.mb.core.NCMBObject;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import to.msn.wings.ncmb_sign_up.CustomImageView;
import to.msn.wings.ncmb_sign_up.DrawSurfaceView;
import to.msn.wings.ncmb_sign_up.R;
import to.msn.wings.ncmb_sign_up.api.ApiDrawingConfig;

/**
 * Created by 4163103 on 2017/10/20.
 */

public class MainActivity extends AppCompatActivity {

    private EditText myId;
    private EditText friendId;
    private EditText remoId;
    private ArrayList<String> friendList1;
    String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NCMB.initialize(this.getApplicationContext(), ApiDrawingConfig.API_APP_KEY, ApiDrawingConfig.API_CLIENT_KEY); myId = (EditText) findViewById(R.id.myId);//オブジェクトID
        friendId = (EditText) findViewById(R.id.friendId);//追加したいオブジェクトID
        remoId = (EditText) findViewById(R.id.removeId);//削除したいオブジェクトID
        friendList1 = new ArrayList();

    }

    public void onClick(View v) {
        switch (v.getId()) {
            //ID入力してfriendを取得
            case R.id.btn1:
                final NCMBObject friend1 = new NCMBObject("UserClass");
                text = myId.getText().toString();
                friend1.setObjectId(text);
                friend1.fetchInBackground(new FetchCallback() {
                    @Override
                    public void done(NCMBBase ncmbBase, NCMBException e) {
                        if (e != null) {
                            Toast.makeText(MainActivity.this, "失敗", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "成功", Toast.LENGTH_SHORT).show();
                            JSONArray data = friend1.getJSONArray("friend");
                            for (int i = 0; i < data.length(); i++) {
                                try {
                                    friendList1.add(data.getString(i).toString());
                                } catch (JSONException e1) {
                                    e1.printStackTrace();
                                }
                            }
                        }
                    }
                });
                break;


            //画面遷移して一覧表示
            case R.id.btn2:
                Intent intent = new Intent(MainActivity.this, ListActivity.class);
                intent.putStringArrayListExtra("key", friendList1);
                startActivity(intent);
                break;


            //friendにIDを追加
            case R.id.btn3:
                final NCMBObject friend2 = new NCMBObject("UserClass");
                friend2.setObjectId(text);
                String friendid = friendId.getText().toString();
                JSONArray jsonArray = new JSONArray();
                for (String friendId : friendList1) {
                    jsonArray.put(friendId);
                }
                int id = friendList1.indexOf(friendid);

                if (id != -1) {
                    Toast.makeText(MainActivity.this, "既に登録されています。", Toast.LENGTH_SHORT).show();
                } else {
                    jsonArray.put(friendid);
                    //TODO
                    friend2.put("friend", jsonArray);
                    friend2.saveInBackground(new DoneCallback() {
                        @Override
                        public void done(NCMBException e) {
                            if (e != null) {
                                Toast.makeText(MainActivity.this, "失敗", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainActivity.this, "成功", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    friendList1.add(friendid);
                }
                break;


            //friendからIDを削除
            case R.id.btn4:
                final NCMBObject friend3 = new NCMBObject("UserClass");
                friend3.setObjectId(text);
                String refriend = remoId.getText().toString();

                JSONArray data = new JSONArray();
                for (String friendId : friendList1) {
                    data.put(friendId);
                }

                int id1 = friendList1.indexOf(refriend);
                if (id1 != -1) {
                    friendList1.remove(id1);
                    friend3.put("friend", friendList1);
                    friend3.saveInBackground(new DoneCallback() {
                        @Override
                        public void done(NCMBException e) {
                            if (e != null) {
                                Toast.makeText(MainActivity.this, "失敗", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainActivity.this, "成功", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(MainActivity.this, "入力されたIDは存在しません。", Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }
}