package to.msn.wings.ncmb_sign_up;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.nifty.cloud.mb.core.NCMB;
import com.nifty.cloud.mb.core.NCMBUser;

import to.msn.wings.ncmb_sign_up.friend.MainActivity;

/**
 * Created by 4163103 on 2017/11/07.
 */

public class Home_Activity extends AppCompatActivity {

    NCMBUser currentUser = NCMBUser.getCurrentUser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        NCMB.initialize(getApplication(), "b18d561e7aa78c63abe4cd7a0bab2693b84cb975fe627e805281aaf9a2cfd82b", "80c9d743a613b53e9c09f104371d32cfdf7d79449ca1ed2b8b2e89dd31de5f72");


        //フレンド追加btn push
        Button fbtn = (Button)findViewById(R.id.button_fre);
        fbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), MainActivity.class);
                startActivity(intent);
            }
        });


        //プロジェクト作成btn push
        Button pbtn = (Button)findViewById(R.id.button_p);
        pbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), Create_project_Acitviyt.class);
                startActivity(intent);
            }
        });


        Log.d("Home",currentUser.getUserName());


        //メンバーとして参加btn push
        final Button mbtn = (Button)findViewById(R.id.button_m);
        mbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                mbtn.setEnabled(false);

            }
        });

    }


}
