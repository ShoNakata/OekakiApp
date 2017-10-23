package to.msn.wings.ncmb_sign_up;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;

import com.nifty.cloud.mb.core.NCMB;

import to.msn.wings.ncmb_sign_up.api.ApiDrawingConfig;


public class OekakiActivity extends AppCompatActivity implements View.OnClickListener {

    DrawSurfaceView mDrawSurfaceView;
    CustomImageView mCustomImageView;
    static FrameLayout linearLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oekaki);

        NCMB.initialize(this.getApplicationContext(), ApiDrawingConfig.API_APP_KEY, ApiDrawingConfig.API_CLIENT_KEY);

        mDrawSurfaceView = new DrawSurfaceView(this);


        mCustomImageView = new CustomImageView(this);
        mCustomImageView.setImageResource(R.drawable.tech_pjin_icon);


        linearLayout = (FrameLayout) findViewById(R.id.frameLayout);

        linearLayout.addView(mCustomImageView);
        linearLayout.addView(mDrawSurfaceView);


        // フォントサイズのドロップダウンの指定
        ArrayAdapter<ListItem> fontSizeAdapter
                = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        fontSizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fontSizeAdapter.add(new ListItem("20", "20sp"));
        fontSizeAdapter.add(new ListItem("40", "40sp"));
        fontSizeAdapter.add(new ListItem("60", "60sp"));
        fontSizeAdapter.add(new ListItem("80", "80sp"));
        fontSizeAdapter.add(new ListItem("100", "100sp"));

        Spinner spFontSize = (Spinner) findViewById(R.id.spFontSize);
        spFontSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //選択された時
                ListItem item = (ListItem) parent.getSelectedItem();
                mDrawSurfaceView.setFontSize(Float.valueOf(item.key));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //なにもしない
            }
        });
        spFontSize.setAdapter(fontSizeAdapter);

        // フォントカラーのドロップダウンの指定
        ArrayAdapter<ListItem> fontColorAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        fontColorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fontColorAdapter.add(new ListItem("#000000", "黒"));
        fontColorAdapter.add(new ListItem("#ff0000", "赤"));
        fontColorAdapter.add(new ListItem("#0000ff", "青"));
        fontColorAdapter.add(new ListItem("#00ff00", "緑"));

        Spinner spFontColor = (Spinner) findViewById(R.id.spFontColor);
        spFontColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //選択された時
                ListItem item = (ListItem) parent.getSelectedItem();
                mDrawSurfaceView.setFontColor(item.key);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //なにもしない
            }
        });
        spFontColor.setAdapter(fontColorAdapter);



    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnSync:
                mDrawSurfaceView.sync();
                break;
            case R.id.btnDeleteAll:
                mDrawSurfaceView.deleteAll();
                break;

            case R.id.btnPen:
                mDrawSurfaceView.setToolPen();
                break;
            case R.id.btnEraser:
                mDrawSurfaceView.setToolEraser();
                break;
//            case R.id.undo:
//                mDrawSurfaceView.undo();
//                break;
//            case R.id.redo:
//                mDrawSurfaceView.redo();
//                break;
            case R.id.draw:
                mCustomImageView.setTranslationZ(0);

                break;
            case R.id.pinch:
                mCustomImageView.setTranslationZ(10);
                break;
        }
    }

    public void setScale(float scale){
        linearLayout.setScaleX(scale);
        linearLayout.setScaleY(scale);
    }

}

/**
 * ListItemの入れ物
 */
class ListItem {
    public String key;
    public String value;

    public ListItem(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}