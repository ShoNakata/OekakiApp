package to.msn.wings.oekakiapp;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.nifty.cloud.mb.core.DoneCallback;
import com.nifty.cloud.mb.core.NCMB;
import com.nifty.cloud.mb.core.NCMBException;
import com.nifty.cloud.mb.core.NCMBObject;
import com.nifty.cloud.mb.core.NCMBQuery;
import com.nifty.cloud.mb.core.NCMBUser;

/**
 * Created by 4163103 on 2017/11/05.
 */

public class Sign_Up_Email_Check_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_email_check);

        NCMB.initialize(this.getApplicationContext(),"b18d561e7aa78c63abe4cd7a0bab2693b84cb975fe627e805281aaf9a2cfd82b",
                "80c9d743a613b53e9c09f104371d32cfdf7d79449ca1ed2b8b2e89dd31de5f72");

        Intent intent = getIntent();
        final String name = intent.getStringExtra("sign_up_Name");
        final String email = intent.getStringExtra("sign_up_Em");

        Button ok_btn = (Button)findViewById(R.id.button_ec_btn);
        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //ここにデータストアのUserClassにuserを登録する処理
                NCMBObject object = new NCMBObject("UserClass");
//                object.setObjectId(s);
//                object.put("name",currentUser.getUserName().toString());
//                object.put("Email",currentUser.getMailAddress().toString());
                object.put("name",name);
                object.put("Email",email);

                object.saveInBackground(new DoneCallback() {
                    @Override
                    public void done(NCMBException e) {
                        if (e != null) {
                            //error
                        } else {
                            //成功
                            Intent intent = new Intent(getApplication(), Login_Home_Activity.class);
                            startActivity(intent);
                        }
                    }
                });


            }
        });

    }

}
