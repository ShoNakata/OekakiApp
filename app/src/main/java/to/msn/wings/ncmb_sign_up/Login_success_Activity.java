package to.msn.wings.ncmb_sign_up;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.nifty.cloud.mb.core.DoneCallback;
import com.nifty.cloud.mb.core.FindCallback;
import com.nifty.cloud.mb.core.NCMB;
import com.nifty.cloud.mb.core.NCMBException;
import com.nifty.cloud.mb.core.NCMBQuery;
import com.nifty.cloud.mb.core.NCMBUser;

import java.util.List;

import to.msn.wings.ncmb_sign_up.friend.MainActivity;

/**
 * Created by 4163103 on 2017/10/18.
 */

public class Login_success_Activity extends AppCompatActivity {

    TextView textView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        NCMB.initialize(getApplication(), "b18d561e7aa78c63abe4cd7a0bab2693b84cb975fe627e805281aaf9a2cfd82b", "80c9d743a613b53e9c09f104371d32cfdf7d79449ca1ed2b8b2e89dd31de5f72");

        //final NCMBUser currentUser = NCMBUser.getCurrentUser();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_success);

        final NCMBUser currentUser = NCMBUser.getCurrentUser();

        //プロジェクト作成ボタンpush
        Button btn_new_project = (Button)findViewById(R.id.project_btn);
        btn_new_project.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplication(), MainActivity.class);
                startActivity(intent);

//                NCMBQuery<NCMBUser> query = new NCMBQuery<NCMBUser>("user");
//
//                query.findInBackground(new FindCallback<NCMBUser>() {
//                    @Override
//                    public void done(List<NCMBUser> list, NCMBException e) {
//
//
//
//                        //list.toString();
//
//                        Log.d("",list.toString());
//                        Log.d("", String.valueOf(currentUser));
//
//
//                    }
//                });



            }
        });



        //おえかきボタンpush
        Button oekaki_btn = (Button)findViewById(R.id.oekakiBotton);
        oekaki_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplication(), OekakiActivity.class);
                startActivity(intent);

            }
        });



        //ログアウトボタンpush
        Button btn_logout = (Button)findViewById(R.id.logout_button);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                NCMBUser.logoutInBackground(new DoneCallback() {
                    @Override
                    public void done(NCMBException e) {

                        //Toast.makeText(getApplication(),  "ログイン中のユーザー:" + currentUser.getUserName(), Toast.LENGTH_SHORT).show();
                        finish();

                    }
                });

            }
        });

    }

}
