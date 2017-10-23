package to.msn.wings.ncmb_sign_up;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nifty.cloud.mb.NCMB;
import com.nifty.cloud.mb.NCMBException;
import com.nifty.cloud.mb.NCMBUser;
import com.nifty.cloud.mb.SignUpCallback;

import java.util.Random;

import static to.msn.wings.ncmb_sign_up.R.id.button_sign;

public class Sign_Up_Activity extends AppCompatActivity {

    private EditText mUserName;
    private EditText mEmail;
    private EditText mPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign__up_);

        NCMB.initialize(getApplication(), "b18d561e7aa78c63abe4cd7a0bab2693b84cb975fe627e805281aaf9a2cfd82b", "80c9d743a613b53e9c09f104371d32cfdf7d79449ca1ed2b8b2e89dd31de5f72");


        mEmail = (EditText) findViewById(R.id.editText_sign_email);

        mUserName = (EditText) findViewById(R.id.editText_sign_name);
        mPassword = (EditText) findViewById(R.id.editText_sign_password);





        //-------------ログインボタンpush------------------
        Button btn_login_home = (Button)findViewById(R.id.button_login);
        btn_login_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplication(), Login_Home_Activity.class);
                startActivity(intent);

            }
        });








        //-----------新規ログイン登録ボタンpush-----------------
        Button btn = (Button)findViewById(button_sign);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ここに Sign up（登録）処理
                NCMBUser user = new NCMBUser();
                user.setUsername(mUserName.getText().toString());
                user.setEmail(mEmail.getText().toString());
                user.setPassword(mPassword.getText().toString());



//                Random random = new Random();
//                String n = String.valueOf(random.nextInt(100000000));
//                Toast.makeText(getApplication(), n , Toast.LENGTH_SHORT).show();




                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(NCMBException e) {
                        if (e == null) {
                            // Sign up 成功
                            Toast.makeText(getApplication(), "登録成功！", Toast.LENGTH_SHORT).show();


                            Intent intent = new Intent(getApplication(), Sign_Up_Email_Check_Activity.class);
                            startActivity(intent);



                        }

                        if (e != null) {
                            // Sign up 失敗
                            //Toast.makeText(getApplication(), "失敗！", Toast.LENGTH_SHORT).show();
                            Log.d("登録失敗", e.getMessage());
                            String re = e.getMessage();
                            //Log.d("入力", "{\"code\":\"E400003\",\"error\":\"userName or password is empty.\"}");

                            if (re.equals("{\"code\":\"E400003\",\"error\":\"userName or password is empty.\"}")) {
                                Toast.makeText(getApplication(), "ユーザーネームかパスワードがありません",Toast.LENGTH_SHORT).show();
                            }

                            if (re.equals("{\"code\":\"E400004\",\"error\":\"mailAddress is invalid format.\"}")) {
                                Toast.makeText(getApplication(), "メールドレスが入力されていないか、メールアドレスの形式が無効な形式です。",Toast.LENGTH_SHORT).show();
                            }

                            if (re.equals("{\"code\":\"E409001\",\"error\":\"userName is duplication.\"}")) {
                                Toast.makeText(getApplication(), "この名前は既に使用されています。違う名前を入力してください。",Toast.LENGTH_SHORT).show();
                            }

                            if (re.equals("{\"code\":\"E409001\",\"error\":\"mailAddress is duplication.\"}")) {
                                Toast.makeText(getApplication(), "このメールアドレスは既に使用されています。違うメールアドレスを入力してください。",Toast.LENGTH_SHORT).show();
                            }

                            if (re.equals("{\"code\":\"E400008\",\"error\":\"Either userName or authData.\"}")) {
                                Toast.makeText(getApplication(), "名前が入力されていません。",Toast.LENGTH_SHORT).show();
                            }

                            //Toast.makeText(getApplication(), "" + e.getMessage() , Toast.LENGTH_SHORT).show();





                        }
                    }
                });
            }
        });
    }



}
