package to.msn.wings.oekakiapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.nifty.cloud.mb.core.DoneCallback;
import com.nifty.cloud.mb.core.NCMB;
import com.nifty.cloud.mb.core.NCMBAcl;
import com.nifty.cloud.mb.core.NCMBException;
import com.nifty.cloud.mb.core.NCMBFile;
import com.nifty.cloud.mb.core.NCMBObject;
import com.nifty.cloud.mb.core.NCMBUser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 4163103 on 2017/11/06.
 */

public class Create_project_Acitviyt extends AppCompatActivity {

    private static final int READ_REQUEST_CODE = 10000;
    ImageView imageView;

    NCMBUser cUser;

    EditText ProjectName;
    String fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_project);

        imageView = (ImageView)findViewById(R.id.imageView_dl);

        NCMB.initialize(getApplication(), "b18d561e7aa78c63abe4cd7a0bab2693b84cb975fe627e805281aaf9a2cfd82b", "80c9d743a613b53e9c09f104371d32cfdf7d79449ca1ed2b8b2e89dd31de5f72");

        //imageViewタップ
        final ImageView imageView = (ImageView)findViewById(R.id.imageView_dl);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent1 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent1.setType("image/*");

                startActivityForResult(intent1, READ_REQUEST_CODE);

            }
        });


        ProjectName = (EditText)findViewById(R.id.editText);

        //次へボタンpush
        Button pbtn = (Button)findViewById(R.id.button);
        pbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //NCMBにprojectnameとimagename保存
                NCMBObject object = new NCMBObject("ProjectClass");
                object.put("projectName", ProjectName.getText().toString());
                object.put("image",fileName.toString());
                //object.put("member",);
                object.saveInBackground(new DoneCallback() {
                    @Override
                    public void done(NCMBException e) {

                        if (e != null) {
                            Log.d("","失敗");
                        } else {
                            Log.d("","成功");

                            Intent intent = new Intent(getApplication(), DL_ListActivity.class);
                            intent.putExtra("z",fileName);
                            startActivity(intent);
//                            //ロール作成ProjectNameで
//                            NCMBRole role = new  NCMBRole(ProjectName.getText().toString());
//                            role.createRoleInBackground(new DoneCallback() {
//                                @Override
//                                public void done(NCMBException e) {
//
//                                    if (e != null) {
//
//                                        //エラー処理
//
//                                    } else {
//
//
//
//                                    }
//
//                                }
//                            });

                        }

                    }
                });
                //Intentで次の画面へ遷移の処理

//                NCMBRole role = new  NCMBRole(ProjectName.getText().toString());
//                role.createRoleInBackground(new DoneCallback() {
//                    @Override
//                    public void done(NCMBException e) {
//
//                        if (e != null) {
//
//                            //エラー処理
//
//                        }
//
//                    }
//                });

            }
        });


    }






    public void onActivityResult(int requesutCode, int resultCode, Intent resultData) {

        if (requesutCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            //File filepath = new File(String.valueOf(path));
            Uri uri = null;
            if (resultData != null) {

                uri = resultData.getData();

                try {

                    final Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream);
                    byte[] dataByte = byteArrayOutputStream.toByteArray();

                    //読み込み:可 , 書き込み:可
                    NCMBAcl acl = new NCMBAcl();
                    acl.setPublicReadAccess(true);
                    acl.setPublicWriteAccess(true);

                    fileName = cUser.getCurrentUser().getUserName().toString() + "_" + getNowDate();

                    //通信処理デース
                    NCMBFile file = new NCMBFile( fileName + ".png" , dataByte, acl);
                    file.saveInBackground(new DoneCallback() {
                        @Override
                        public void done(NCMBException e) {
                            //String result;
                            if (e != null) {
                                Log.d("画像","失敗");
                            } else {
                                imageView.setImageBitmap(bitmap);
                                Log.d("","OKbit");
                            }

                        }
                    });
                    //imageView.setImageBitmap(bitmap);
                } catch (IOException e) {

                    e.printStackTrace();

                }
            }
        }

    }
    public static String getNowDate(){

        final DateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss");
        final Date date = new Date(System.currentTimeMillis());
        return df.format(date);

    }

}
