package to.msn.wings.ncmb_sign_up;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
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


public class DrawSurfaceView extends SurfaceView implements SurfaceHolder.Callback{
    Resources res = this.getContext().getResources();
    Bitmap bmp = BitmapFactory.decodeResource(res, R.drawable.tatenaga);
    Bitmap reBmp;

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
    private Path path;
    //private Bitmap mLastDrawBitmap;
    private Canvas mCanvas;
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

    private float mScale = 0.5f; // 描画する倍率
    private ScaleGestureDetector mScaleDetector;

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
//        mScaleDetector = new ScaleGestureDetector(context,
//                new ScaleGestureDetector.OnScaleGestureListener() {
//                    @Override
//                    public boolean onScale(ScaleGestureDetector detector) {
//                        // ピンチイン・アウト中に継続して呼び出される
//                        // getScaleFactor()は
//                        // 『今回の2点タッチの距離/前回の2点タッチの距離』を返す
//                        Log.d("Pinch", "onScale factor:" +
//                                detector.getScaleFactor());
//
//                        // 表示倍率の計算
//                        mScale *= detector.getScaleFactor();
//                        Log.e("ttttttttttttt", String.valueOf(scale+mScale));
//                        invalidate();
//                        return true;
//                    }
//
//                    @Override
//                    public boolean onScaleBegin(ScaleGestureDetector detector) {
//                        Log.d("Pinch", "onScale.onScaleBegin");
//                        return true;
//                    }
//
//                    @Override
//                    public void onScaleEnd(ScaleGestureDetector detector) {
//                        Log.d("Pinch", "onScale.onScaleEnd");
//                    }
//                });
    }
    /**
     * 初期化処理
     */
    private void init() {
        mHolder = getHolder();
        mPath = new Path();
        path = new Path();

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
        // 描画状態を保持するBitmapを生成します。
        //clearLastDrawBitmap();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        int src_width =bmp.getWidth();
        int src_height = bmp.getHeight();
        float xScale = (float) width/scale / src_width;
        float yScale = (float) height/scale / src_height;
        if (xScale < yScale){
            xScale = (float) width/scale / src_width;
            yScale = (float) (src_height * xScale) / src_height;
        }else{
            xScale = (float) (src_width * yScale) / src_width;
            yScale = (float) height/scale / src_height;
        }
        Matrix matrix = new Matrix();
        matrix.postScale(xScale, yScale);
        reBmp = Bitmap.createBitmap(bmp, 0, 0, src_width, src_height, matrix, true);
        //TODO　変化があるごと画像をセット
        // drawImage();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }




    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        Log.e("---------------", String.valueOf(scale + mScale));
//        // ロックしてキャンバスを取得します。
//        mCanvas = mHolder.lockCanvas();
//        //比率に応じてキャンパスサイズを指定
//        mCanvas.scale(scale + mScale, scale + mScale);
//        // キャンバスをクリアします。
//        mCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
//        mCanvas.drawBitmap(reBmp,0,0,null);
//        // 前回描画したビットマップをキャンバスに描画します。
//        mCanvas.drawBitmap(mLastDrawBitmap, 0, 0, null);
//        // ロックを外します。
//        mHolder.unlockCanvasAndPost(mCanvas);
//        return mScaleDetector.onTouchEvent(event);
        float touchedX = event.getX() / scale;
        float touchedY = event.getY() / scale;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                // フォントサイズとカラーの設定
                mPaint.setStrokeWidth(mFontSize);
                mPaint.setColor(Color.parseColor(mFontColor));

                //mPath = new Path();
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
        //path = new Path();

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
        //線の描画
        drawLine(path);
    }

    /**
     * 描画処理
     *
     * @param path
     */
    private void drawLine(Path path) {
        // ロックしてキャンバスを取得します。
        mCanvas = mHolder.lockCanvas();
        if (mCanvas != null){
            //------------------------------------------------
            mCanvas.drawColor(Color.WHITE);
            //比率に応じてキャンパスサイズを指定
            mCanvas.scale(scale, scale);
            //mCanvas.drawBitmap(reBmp,0,0,null);
            // パスを描画します。
            mCanvas.drawPath(path, mPaint);
            //------------------------------------------------
            // ロックを外します。
            mHolder.unlockCanvasAndPost(mCanvas);

        }
    }

    /**
     * 画像のセット
     *
     */
//    private void drawImage() {
//        // ロックしてキャンバスを取得します。
//        mCanvas = mHolder.lockCanvas();
//        //比率に応じてキャンパスサイズを指定
//        mCanvas.scale(scale, scale);
//        mCanvas.drawBitmap(reBmp,0,0,null);
//        // 前回描画したビットマップをキャンバスに描画します。
//        mCanvas.drawBitmap(mLastDrawBitmap, 0, 0, null);
//        // ロックを外します。
//        mHolder.unlockCanvasAndPost(mCanvas);
//    }

    /**
     * リモートの描画データと同期する
     */
    public void sync() {
        mCanvas = null;
        // ロックしてキャンバスを取得します。
        mCanvas = mHolder.lockCanvas();
        // キャンバスをクリアします。
        // init();
        mCanvas.scale(scale, scale);
        //mCanvas.drawBitmap(reBmp,0,0,null);
        // ロックを外します。
        mHolder.unlockCanvasAndPost(mCanvas);
        getDrawing();
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
                        mCanvas = null;
                        // ロックしてキャンバスを取得します。
                        mCanvas = mHolder.lockCanvas();
                        // キャンバスをクリアします。
                        init();
                        mCanvas.scale(scale, scale);
                        mCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
                        //mCanvas.drawBitmap(reBmp,0,0,null);
                        // ロックを外します。
                        mHolder.unlockCanvasAndPost(mCanvas);
                        getDrawing();
//                        sync();
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