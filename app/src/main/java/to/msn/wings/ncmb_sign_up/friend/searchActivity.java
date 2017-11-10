package to.msn.wings.ncmb_sign_up.friend;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.nifty.cloud.mb.core.FindCallback;
import com.nifty.cloud.mb.core.NCMBException;
import com.nifty.cloud.mb.core.NCMBObject;
import com.nifty.cloud.mb.core.NCMBQuery;

import java.util.ArrayList;
import java.util.List;

import to.msn.wings.ncmb_sign_up.R;

/**
 * Created by 4163104 on 2017/11/10.
 */

public class searchActivity extends Activity {

    EditText editText;
    ArrayList arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        arrayList = new ArrayList();
        editText = (EditText) findViewById(R.id.editText);

    }

    public void onClick(View v){
        final NCMBQuery<NCMBObject> query = new NCMBQuery<>("UserClass");
        String str = editText.getText().toString();
//                String[] names = {"Jonathan Walsh", "Dario Wunsch", "Shawn Simon"};
//                query.whereContainedIn("name",  Arrays.asList(new String[]{str}));
        query.whereEqualTo("name",str);
        query.findInBackground(new FindCallback<NCMBObject>() {
            @Override
            public void done(List<NCMBObject> list, NCMBException e) {
                if (e != null){
                    //失敗
                    Log.i("Log","失敗");
                }else{
                    //成功
                    Log.i("Log","成功");

                    for (NCMBObject obj : list){
                        String bbb = obj.getString("name");
                        arrayList.add(bbb);
                    }
                    Intent intent = new Intent(searchActivity.this,search_hit_Activity.class);
                    intent.putStringArrayListExtra("key",arrayList);
                    startActivity(intent);


                }
            }
        });
    }
}
