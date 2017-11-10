package to.msn.wings.ncmb_sign_up.friend;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.nifty.cloud.mb.core.FindCallback;
import com.nifty.cloud.mb.core.NCMB;
import com.nifty.cloud.mb.core.NCMBException;
import com.nifty.cloud.mb.core.NCMBObject;
import com.nifty.cloud.mb.core.NCMBQuery;

import java.util.ArrayList;
import java.util.List;

import to.msn.wings.ncmb_sign_up.R;

/**
 * Created by 4163104 on 2017/11/10.
 */

public class homeActivity extends Activity {

    ArrayList nameList;
    ArrayList idList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_f);

        NCMB.initialize(this.getApplicationContext(),"b18d561e7aa78c63abe4cd7a0bab2693b84cb975fe627e805281aaf9a2cfd82b",
                "80c9d743a613b53e9c09f104371d32cfdf7d79449ca1ed2b8b2e89dd31de5f72");

        nameList = new ArrayList();
        idList = new ArrayList();

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("処理を実行中しています");
        progressDialog.setCancelable(true);
        progressDialog.show();

        NCMBQuery<NCMBObject> query = new NCMBQuery<>("UserClass");
        query.findInBackground(new FindCallback<NCMBObject>() {
            @Override
            public void done(List<NCMBObject> list, NCMBException e) {
                if (e != null){
                    Log.i("Log","home失敗");
                }else{
                    Log.i("Log","home成功");
                    for (NCMBObject aaa : list){
                        String bbb = aaa.getString("name");
                        nameList.add(bbb);
                    }

                    for (NCMBObject obj : list){
                        String str = obj.getString("objectId");
                        idList.add(str);
                    }
                }
            }
        });
        progressDialog.dismiss();
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn1:
                Intent intent = new Intent(homeActivity.this,searchActivity.class);
                startActivity(intent);
                break;
            case R.id.btn2:
                Intent value = new Intent(homeActivity.this,MainActivity.class);
                value.putStringArrayListExtra("key1",nameList);
                value.putStringArrayListExtra("key2",idList);
                startActivity(value);
                break;
        }
    }
}
