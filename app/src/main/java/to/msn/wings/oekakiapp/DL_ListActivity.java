package to.msn.wings.oekakiapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

    Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dl_list);

        imageView = (ImageView)findViewById(R.id.imageView_dl);

        NCMB.initialize(getApplication(), "b18d561e7aa78c63abe4cd7a0bab2693b84cb975fe627e805281aaf9a2cfd82b", "80c9d743a613b53e9c09f104371d32cfdf7d79449ca1ed2b8b2e89dd31de5f72");

        NCMBUser cUser = NCMBUser.getCurrentUser();
        cUser.getUserName();

        Intent intent = getIntent();
        final String z = intent.getStringExtra("z");



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










    }

}
