package to.msn.wings.ncmb_sign_up;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.nifty.cloud.mb.LogInCallback;
import com.nifty.cloud.mb.core.DoneCallback;
import com.nifty.cloud.mb.core.LoginCallback;
import com.nifty.cloud.mb.core.NCMB;
import com.nifty.cloud.mb.core.NCMBException;
import com.nifty.cloud.mb.core.NCMBUser;

/**
 * Created by 4163103 on 2017/10/16.
 */

public class Login_Home_Activity extends AppCompatActivity {

    EditText mUserName;
    EditText mPassword;



    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_home);

        NCMB.initialize(getApplication(), "b18d561e7aa78c63abe4cd7a0bab2693b84cb975fe627e805281aaf9a2cfd82b", "80c9d743a613b53e9c09f104371d32cfdf7d79449ca1ed2b8b2e89dd31de5f72");

        mUserName = (EditText) findViewById(R.id.editText_login_username);

        mPassword = (EditText) findViewById(R.id.editText_login_password);

        final NCMBUser currentUser = NCMBUser.getCurrentUser();

        if (currentUser.getUserName() != null) {

            Log.d("TAG", "0 " + currentUser.getUserName());

            //その端末でログインしていて　ログアウトボタンを押してなくログアウトしていない場合　
            Intent intent = new Intent(getApplication(), Login_success_Activity.class);
            startActivity(intent);


        }

        if (currentUser.getUserName() == null) {

            //何もしない

        }

        Button btn_login = (Button)findViewById(R.id.button_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                NCMBUser user = new NCMBUser();

                user.setUserName(mUserName.getText().toString());

                user.setPassword(mPassword.getText().toString());



                try {


                    NCMBUser.loginInBackground(mUserName.getText().toString(), mPassword.getText().toString(), new LoginCallback() {
                        @Override
                        public void done(NCMBUser ncmbUser, NCMBException e) {

                            if(e == null) {

                                if (currentUser.getUserName() != null) {
                                    // ログイン中のユーザーの取得に成功
                                    //Toast.makeText(getApplication(),  "ログイン中のユーザー:" + currentUser.getUserName(), Toast.LENGTH_SHORT).show();

                                    Log.d("TAG", "1 " + currentUser.getUserName());





                                    Intent intent = new Intent(getApplication(), Login_success_Activity.class);
                                    startActivity(intent);

                                } else if (currentUser.getUserName() == null){
                                    // 未ログインまたは取得に失敗
                                    //Toast.makeText(getApplication(),  "未ログインまたは取得に失敗", Toast.LENGTH_SHORT).show();
                                    //Log.d("TAG", "2 " + currentUser.getUserName());

                                }

                            }

                            if (e != null) {

                                Log.d("T","エラー");

                            }

                        }
                    });
                } catch (NCMBException e) {

                    e.printStackTrace();

                }

            }
        });

    }



}