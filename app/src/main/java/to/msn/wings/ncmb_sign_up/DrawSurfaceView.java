package to.msn.wings.ncmb_sign_up;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.nifty.cloud.mb.core.DoneCallback;
import com.nifty.cloud.mb.core.FindCallback;
import com.nifty.cloud.mb.core.NCMBException;
import com.nifty.cloud.mb.core.NCMBObject;
import com.nifty.cloud.mb.core.NCMBQuery;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class DrawSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    final float VIEW_WIDTH = 400;
    final float VIEW_HEIGHT = 600;

    // 定数
    private static final String TAG = "DrawSurfaceView";
    public static final int TOOL_ERASER = 0; //消しゴム
    public static final int TOOL_PEN = 1; //ペン

    public static final String DEF_FONT_COLOR = "#000000";
    public static final float DEF_FONT_SIZE = 20.0f;

    private SurfaceHolder mHolder;
    private Paint mPaint;
    private Path mPath;
    private Bitmap mLastDrawBitmap;
    private Canvas mCanvas;
    private Canvas mLastDrawCanvas;

    private Handler mHandler;

    // 送信用描画データ
    DrawData mDrawData = null;

    float scale;
    //描画の重なり
    private Context parent;

    private Utils mUtils;

    // ペンの初期設定
    private String mFontColor = DEF_FONT_COLOR;
    private float mFontSize = DEF_FONT_SIZE;
    private int mTool_category = 1;// 0=消しゴム、1=ペン

    /**
     * コンストラクター
     *
     * @param context
     */
    public DrawSurfaceView(Context context) {
        super(context);
        parent = context;
        mUtils = new Utils(parent);
        init();
        getDrawing();
    }

    /**
     * 初期化処理
     */
    private void init() {
        mHolder = getHolder();

        // 透過します。
        setZOrderOnTop(true);
        mHolder.setFormat(PixelFormat.TRANSPARENT);

        // コールバックを設定します。
        mHolder.addCallback(this);

        // ペンを設定します。
        setToolPen();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {


        float scaleX = getWidth() / VIEW_WIDTH;
        float scaleY = getHeight() / VIEW_HEIGHT;
        scale = scaleX > scaleY ? scaleY : scaleX;

        Log.i("surfaceCreated", "scaleX:" + scaleX);
        Log.i("surfaceCreated", "scaleY:" + scaleY);

        // 描画状態を保持するBitmapを生成します。
        clearLastDrawBitmap();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mLastDrawBitmap.recycle();
    }

    private void clearLastDrawBitmap() {
        if (mLastDrawBitmap == null) {
            mLastDrawBitmap = Bitmap.createBitmap(getWidth(), getHeight(),
                    Bitmap.Config.ARGB_8888);
        }

        if (mLastDrawCanvas == null) {
            mLastDrawCanvas = new Canvas(mLastDrawBitmap);
        }

        mLastDrawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchedX = event.getX() / scale;
        float touchedY = event.getY() / scale;

        Log.i("onTouchEvent", "touxhedX:" + touchedX);
        Log.i("onTouchEvent", "touxhedY:" + touchedY);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                // フォントサイズとカラーの設定
                mPaint.setStrokeWidth(mFontSize);
                mPaint.setColor(Color.parseColor(mFontColor));

                mPath = new Path();
                mPath.moveTo(touchedX, touchedY);

                // 受け渡し用
                mDrawData = new DrawData();
                mDrawData.setFontColor(mFontColor);
                mDrawData.setFontSize(mFontSize);
                break;

            case MotionEvent.ACTION_MOVE:
                mPath.lineTo(touchedX, touchedY);
                drawLine(mPath);
                break;

            case MotionEvent.ACTION_UP:
                mPath.lineTo(touchedX, touchedY);
                drawLine(mPath);

                mLastDrawCanvas.drawPath(mPath, mPaint);

                // 描画データを送信
                sendDrawing(mDrawData);
                break;

            default:
                break;
        }
        // 受け渡し用
        mDrawData.setPath(touchedX, touchedY);
        return true;
    }

    /**
     * 描画データを送信
     *
     * @param drawData
     */
    private void sendDrawing(DrawData drawData) {

        // 通信処理
        NCMBObject obj = new NCMBObject("DrawingClass");
        obj.put("projectId", 1);//とりあえず
        obj.put("userId", 1);//とりあえず
        obj.put("state", 1);//0表示しない:1表示する
        obj.put("fontSize", drawData.getFontSize());
        obj.put("fontColor", drawData.getFontColor());
        JSONArray pathArray = new JSONArray();
        try {
            JSONObject kv;
            for (final DrawPath path : drawData.getPathList()) {
                kv = new JSONObject();
                kv.put("x", path.x);
                kv.put("y", path.y);
                pathArray.put(kv);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        obj.put("path", pathArray);
        obj.put("toolCategory", mTool_category);
        obj.saveInBackground(new DoneCallback() {
            @Override
            public void done(NCMBException e) {
                if (e != null) {
                    //保存失敗
                    Log.e(TAG, "保存失敗");
                } else {
                    //保存成功
                    Log.d(TAG, "保存成功");
                }
            }
        });
    }

    /**
     * 描画データを取得
     */
    private void getDrawing() {
        //ダイアログ表示
        mUtils.progressShow("通信中", "描画データを読み込み中です");

        NCMBQuery<NCMBObject> query = new NCMBQuery<>("DrawingClass");
        query.findInBackground(new FindCallback<NCMBObject>() {
            @Override
            public void done(List<NCMBObject> results, NCMBException e) {
                if (e != null) {
                    //検索失敗時の処理
                } else {
                    //検索成功時の処理
                    NCMBObject data;
                    int resultsSize = results.size();
                    for (int i = 0; i < resultsSize; i++) {
                        data = results.get(i);
                        remoteDrawLine(data.getInt("toolCategory"), data.getString("fontColor"), data.getInt("fontSize"), data.getJSONArray("path"),data.getInt("state"));
                    }
                    mUtils.progressDismiss();
                }
            }
        });
    }

    /**
     * サーバの描画データをCanvasへ描き込む
     *
     * @param fontColor
     * @param fontSize
     * @param pathArray
     */

    private void remoteDrawLine(int category, String fontColor, int fontSize, JSONArray pathArray, int state) {
        //ツールの切り替え
        switch (category) {
            case TOOL_ERASER:
                mPaint = new Paint();
                mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                mPaint.setARGB(0, 0, 0, 0);
                mPaint.setAntiAlias(true);
                mPaint.setStrokeWidth(fontSize);
                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setStrokeCap(Paint.Cap.ROUND);
                break;

            case TOOL_PEN:
                mPaint = new Paint();
                mPaint.setAntiAlias(true);
                mPaint.setColor(Color.parseColor(fontColor));
                mPaint.setStrokeWidth(fontSize);
                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setStrokeCap(Paint.Cap.ROUND);
                break;
        }

        //パスの生成
        Path path = new Path();
        boolean flgFirst = true;
        int pathArrayLength = pathArray.length();
        for (int i = 0; i < pathArrayLength; i++) {
            try {
                JSONObject data = pathArray.getJSONObject(i);
                float x = Float.parseFloat(data.getString("x"));
                float y = Float.parseFloat(data.getString("y"));
                if (flgFirst) {
                    path.moveTo(x, y);
                    flgFirst = false;
                }
                path.lineTo(x, y);
            } catch (JSONException e3) {
                e3.printStackTrace();
            }
        }
        if (state == 1){
            //線の描画
            drawLine(path);
            mLastDrawCanvas.drawPath(path, mPaint);
        }
    }

    /**
     * 描画処理
     *
     * @param path
     */
    private void drawLine(Path path) {
        // ロックしてキャンバスを取得します。
        mCanvas = mHolder.lockCanvas();

        //比率に応じてキャンパスサイズを指定
        mCanvas.scale(scale, scale);

        // キャンバスをクリアします。
        mCanvas.drawColor(0, PorterDuff.Mode.CLEAR);

        // 前回描画したビットマップをキャンバスに描画します。
        mCanvas.drawBitmap(mLastDrawBitmap, 0, 0, null);

        // パスを描画します。
        mCanvas.drawPath(path, mPaint);

        // ロックを外します。
        mHolder.unlockCanvasAndPost(mCanvas);
    }

    /**
     * リモートの描画データと同期する
     */
    public void sync() {
        // ロックしてキャンバスを取得します。
        mCanvas = mHolder.lockCanvas();

        //描画データを初期化
        mLastDrawBitmap = null;
        mLastDrawCanvas = null;
        clearLastDrawBitmap();

        // キャンバスをクリアします。
        mCanvas.drawColor(0, PorterDuff.Mode.CLEAR);

        // ロックを外します。
        mHolder.unlockCanvasAndPost(mCanvas);

        //init();
        getDrawing();
    }

    /**
     * undo
     */
    public void undo() {
        NCMBQuery<NCMBObject> query = new NCMBQuery<>("DrawingClass");
        //データを昇順で取得するためのフィールドを設定
        query.addOrderByAscending("state");
        //状態が1
        query.whereEqualTo("state",1);
        //データを降順で取得するためのフィールドを設定


        query.findInBackground(new FindCallback<NCMBObject>() {

            @Override
            public void done(List<NCMBObject> results, NCMBException e) {
                NCMBObject obj = new NCMBObject("DrawingClass");
                if (e != null) {
                    //検索失敗時の処理
                } else {
                    //検索成功時の処理
                    try {
                        NCMBObject data = results.get(0);
                        String mObjectId = data.getString("objectId");
                        String mState =  data.getString("state");
                        obj.increment("state",0);
                        Log.e("エラー","検索成功" + mObjectId + mState);
                    } catch (NCMBException e1) {
                        Log.e("エラー","検索失敗");
                    }
                }
                mUtils.progressDismiss();
            }
        });
        sync();
    }

    /**
     * redo
     */
    public void redo() {
        NCMBQuery<NCMBObject> query = new NCMBQuery<>("DrawingClass");
        //データを昇順で取得するためのフィールドを設定
        //query.addOrderByAscending("objectId");
        //状態が0
        query.whereEqualTo("state",0);
        //データを降順で取得するためのフィールドを設定
        query.addOrderByDescending("state");

        query.findInBackground(new FindCallback<NCMBObject>() {
            NCMBObject obj = new NCMBObject("DrawingClass");
            @Override
            public void done(List<NCMBObject> results, NCMBException e) {
                if (e != null) {
                    //検索失敗時の処理
                } else {
                    //検索成功時の処理
                    try {
                        obj.increment("state", 1);
                    } catch (NCMBException e1) {
                        e1.printStackTrace();
                    }
                }
                mUtils.progressDismiss();
            }
        });
        //getDrawing();
    }


    /**
     * 消しゴム（OekakiActivityから呼び出し用）
     */
    public void setToolEraser() {
        mTool_category = TOOL_ERASER;

        mPaint = new Paint();
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        mPaint.setARGB(0, 0, 0, 0);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(mFontSize);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    /**
     * ペンOekakiActivityから呼び出し用）
     */
    public void setToolPen() {
        mTool_category = TOOL_PEN;

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(mFontSize);
        mPaint.setColor(Color.parseColor(mFontColor));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
    }



    /**
     * 描画データを全て削除して同期する
     */
    public void deleteAll() {
        mHandler = new Handler();

        mUtils.progressShow("通信中", "削除リクエストを送信中です");

        final NCMBObject obj = new NCMBObject("DrawingClass");
        NCMBQuery<NCMBObject> query = new NCMBQuery<>("DrawingClass");
        query.findInBackground(new FindCallback<NCMBObject>() {
            @Override
            public void done(List<NCMBObject> results, NCMBException e) {
                if (e != null) {
                    //検索失敗時の処理
                } else {
                    //検索成功時の処理
                    for (final NCMBObject result : results) {
                        obj.setObjectId(result.getObjectId());
                        obj.deleteObjectInBackground(new DoneCallback() {
                            @Override
                            public void done(NCMBException e) {
                                if (e != null) {
                                    Log.d(TAG, "ObjectId:" + result.getObjectId() + "の削除リクエストを送りました");
                                }
                            }
                        });
                    }
                }
            }
        });

        // 1秒待ってから描画データを更新する
        new Thread(new Runnable() {
            @Override
            public void run() {
                // DBが更新されるまで待機
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mUtils.progressDismiss();

                // 描画データを更新
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        sync();
                    }
                });
            }
        }).start();
    }

    public void setFontSize(float fontSize) {
        mFontSize = fontSize;
        mPaint.setStrokeWidth(fontSize);
    }

    public void setFontColor(String fontColor) {
        mFontColor = fontColor;
        mPaint.setColor(Color.parseColor(fontColor));
    }
}

/**
 * 送信用描画データの入れ物
 */
final class DrawData {
    private float fontSize;
    private String fontColor;
    private ArrayList<DrawPath> pathList = new ArrayList<>();

    public void setFontSize(float textSize) {
        this.fontSize = textSize;
    }

    public float getFontSize() {
        return this.fontSize;
    }

    public void setFontColor(String textColoer) {
        this.fontColor = textColoer;
    }

    public String getFontColor() {
        return fontColor;
    }

    public void setPath(float x, float y) {
        final DrawPath path = new DrawPath();
        path.x = x;
        path.y = y;
        pathList.add(path);
    }

    public ArrayList<DrawPath> getPathList() {
        return pathList;
    }
}

/**
 * 送信用描画データのパス情報
 */
final class DrawPath {
    float x;
    float y;
}