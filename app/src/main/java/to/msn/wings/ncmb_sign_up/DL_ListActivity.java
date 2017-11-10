package to.msn.wings.ncmb_sign_up;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.nifty.cloud.mb.core.FetchFileCallback;
import com.nifty.cloud.mb.core.NCMB;
import com.nifty.cloud.mb.core.NCMBException;
import com.nifty.cloud.mb.core.NCMBFile;
import com.nifty.cloud.mb.core.NCMBUser;

/**
 * Created by 4163103 on 2017/11/06.
 */

public class DL_ListActivity extends AppCompatActivity {

    ImageView imageView;

    private ProgressDialog progressDialog;

    static Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dl_list);

        imageView = (ImageView)findViewById(R.id.imageView_dl);

        NCMB.initialize(getApplication(), "b18d561e7aa78c63abe4cd7a0bab2693b84cb975fe627e805281aaf9a2cfd82b", "80c9d743a613b53e9c09f104371d32cfdf7d79449ca1ed2b8b2e89dd31de5f72");

        NCMBUser cUser = NCMBUser.getCurrentUser();
        cUser.getUserName();

        final Intent intent = getIntent();
        final String z = intent.getStringExtra("z");


        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("画像をセッティング中");
        progressDialog.setCancelable(true);
        progressDialog.show();

        //画像DL処理
        NCMBFile file = new NCMBFile(z + ".png");
        file.fetchInBackground(new FetchFileCallback() {
            @Override
            public void done(byte[] bytes, NCMBException e) {
                if (e != null) {
                    //失敗
                    Log.d("L","not2");
                } else {
                    //seokou
                    Log.d("L","OK2");
                    bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    imageView.setImageBitmap(bitmap);
                }

                progressDialog.dismiss();

            }
        });

        //uploadした同じfileName検索
//        NCMBQuery<NCMBFile> query = NCMBFile.getQuery();
//        query.whereEqualTo("fileName", z);
//        query.findInBackground(new FindCallback<NCMBFile>() {
//            @Override
//            public void done(List<NCMBFile> list, NCMBException e) {
//
//                if (e != null) {
//                    //失敗
//                    Log.d("L","not");
//                } else {
//                    //成功
//                    Log.d("L","OK");
//                    for (int i = 0; i < list.size(); i++) {
//
//                        list.get(i).fetchInBackground(new FetchFileCallback() {
//                            @Override
//                            public void done(byte[] bytes, NCMBException e) {
//                                if (e != null) {
//                                    //し
//                                    Log.d("L","not1");
//                                } else {
//                                    //せいこう
//                                    //DLして表示
//                                    Log.d("L","OK1");
//                                    NCMBFile file = new NCMBFile(z);
//                                    file.fetchInBackground(new FetchFileCallback() {
//                                        @Override
//                                        public void done(byte[] bytes, NCMBException e) {
//                                            if (e != null) {
//                                                //失敗
//                                                Log.d("L","not2");
//                                            } else {
//                                                //seokou
//                                                Log.d("L","OK2");
//                                                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//                                                imageView.setImageBitmap(bitmap);
//                                            }
//                                        }
//                                    });
//
//                                }
//                            }
//                        });
//
//                    }
//
//                }
//
//            }
//        });





        Button nOekaki = (Button)findViewById(R.id.btn_next_oekaki);
        nOekaki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent1 = new Intent(getApplication(), OekakiActivity.class);
                startActivity(intent1);

            }
        });







        //戻るbtn push
        Button backbtn = (Button)findViewById(R.id.btn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

            }
        });

    }

}
