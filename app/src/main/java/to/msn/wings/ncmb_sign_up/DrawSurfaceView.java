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

    // デバック用タグ
    private static final String TAG = "DrawSurfaceView";

    // 定数
    private static final int TOOL_ERASER = 0; //消しゴム
    private static final int TOOL_PEN = 1; //ペン
    private static final String DEF_FONT_COLOR = "#000000";
    private static final float DEF_FONT_SIZE = 20.0f;

    // 画面比率設定値
    private final float VIEW_WIDTH = 400;
    private final float VIEW_HEIGHT = 600;

    private Utils mUtils;

    // 描画関連
    private float mCanvasScale;
    private Handler mHandler;
    private DrawData mDrawSendData = null;
    private Canvas mLastDrawCanvas = null;
    private Bitmap mLastDrawBitmap = null;

    private SurfaceHolder mHolder;
    private String mDrawFontColor = DEF_FONT_COLOR;
    private float mDrawFontSize = DEF_FONT_SIZE;
    private int mDrawToolCategory = TOOL_PEN;
    private Paint mDrawPaint;
    private Path mDrawPath;

    //private float mScale = 0.5f; // 描画する倍率
    //private ScaleGestureDetector mScaleDetector;


    //    Resources res = this.getContext().getResources();
//    Bitmap bmp = BitmapFactory.decodeResource(res, R.drawable.tatenaga);
//    Bitmap reBmp;

    /**
     * コンストラクター
     *
     * @param context
     */
    public DrawSurfaceView(Context context) {
        super(context);
        mUtils = new Utils(context);

        // 描画データの初期化
        init();
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

        // 透過します。
        setZOrderOnTop(true);
        mHolder.setFormat(PixelFormat.TRANSPARENT);

        // コールバックを設定します。
        mHolder.addCallback(this);

        // ツールをペンに変更
        setToolPen();

        // 透過します。
        setZOrderOnTop(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        // 画面比率からCanvasのスケールを指定
        float scaleX = getWidth() / VIEW_WIDTH;
        float scaleY = getHeight() / VIEW_HEIGHT;
        mCanvasScale = scaleX > scaleY ? scaleY : scaleX;

        // 描画状態を保持するBitmapを生成します。
        clearLastDrawBitmap();

        // 最新の描画データを取得して描画
        getRemoteData();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
//        int src_width = bmp.getWidth();
//        int src_height = bmp.getHeight();
//        float xScale = (float) width / scale / src_width;
//        float yScale = (float) height / scale / src_height;
//        if (xScale < yScale) {
//            xScale = (float) width / scale / src_width;
//            yScale = (float) (src_height * xScale) / src_height;
//        } else {
//            xScale = (float) (src_width * yScale) / src_width;
//            yScale = (float) height / scale / src_height;
//        }
//        Matrix matrix = new Matrix();
//        matrix.postScale(xScale, yScale);
//        reBmp = Bitmap.createBitmap(bmp, 0, 0, src_width, src_height, matrix, true);
        //TODO　変化があるごと画像をセット
        // drawImage();
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

//        // ロックしてキャンバスを取得します。
//        mCanvas = mHolder.lockCanvas();
//        //比率に応じてキャンパスサイズを指定
//        mCanvas.scale(mCanvasScale, mCanvasScale);
//        // キャンバスをクリアします。
//        mCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
//        //mCanvas.drawBitmap(reBmp,0,0,null);
//        // 前回描画したビットマップをキャンバスに描画します。
//        mCanvas.drawBitmap(mLastDrawBitmap, 0, 0, null);
//        // ロックを外します。
//        mHolder.unlockCanvasAndPost(mCanvas);


        // タッチ座標をCanvasに設定したScaleに合わせて修正する
        float touchedX = event.getX() / mCanvasScale;
        float touchedY = event.getY() / mCanvasScale;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                // 新たに描画する線の設定
                mDrawPaint.setStrokeWidth(mDrawFontSize);
                mDrawPaint.setColor(Color.parseColor(mDrawFontColor));

                mDrawPath = new Path();
                mDrawPath.moveTo(touchedX, touchedY);

                // サーバに送る描画データの準備
                mDrawSendData = new DrawData();
                mDrawSendData.setFontSize(mDrawFontSize);
                mDrawSendData.setFontColor(mDrawFontColor);
                mDrawSendData.setPath(touchedX, touchedY);
                break;

            case MotionEvent.ACTION_MOVE:
                mDrawPath.lineTo(touchedX, touchedY);
                drawLine(mDrawPath, mDrawPaint);

                // サーバに送る描画データの準備
                mDrawSendData.setPath(touchedX, touchedY);
                break;

            case MotionEvent.ACTION_UP:
                mDrawPath.lineTo(touchedX, touchedY);
                drawLine(mDrawPath, mDrawPaint);

                // 描画データをサーバに送信する
                mDrawSendData.setPath(touchedX, touchedY);
                sendDrawing(mDrawSendData);

                // 描画データを保存する
                mLastDrawCanvas.drawPath(mDrawPath, mDrawPaint);
                break;

            default:
                break;
        }
        return true;
    }


    /**
     * 描画データをサーバに送信
     *
     * @param drawData
     */

    private void sendDrawing(DrawData drawData) {

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
        obj.put("toolCategory", mDrawToolCategory);
        obj.saveInBackground(new DoneCallback() {
            @Override
            public void done(NCMBException e) {
                if (e != null) {
                    Log.e(TAG, "sendDrawing:保存失敗");
                } else {
                    Log.i(TAG, "sendDrawing:保存成功");
                }
            }
        });
    }

    /**
     * サーバの描画データを取得
     */
    private void getRemoteData() {

        //ダイアログ表示
        mUtils.progressShow("通信中", "描画データを読み込み中です");

        // サーバの描画データを取得する
        NCMBQuery<NCMBObject> query = new NCMBQuery<>("DrawingClass");
        query.findInBackground(new FindCallback<NCMBObject>() {
            @Override
            public void done(List<NCMBObject> results, NCMBException e) {
                if (e != null) {
                    Log.e(TAG, "sendDrawing：保存失敗");
                } else {
                    Log.i(TAG, "sendDrawing：保存成功");

                    NCMBObject data;
                    int resultsSize = results.size();
                    for (int i = 0; i < resultsSize; i++) {
                        // 取得した描画データをキャンバスに描画する
                        data = results.get(i);
                        drawRemoteData(data.getInt("toolCategory"), data.getString("fontColor"), data.getInt("fontSize"), data.getJSONArray("path"), data.getInt("state"));
                    }
                    mUtils.progressDismiss();
                }
            }
        });
    }

    /**
     * サーバの描画データをCanvasへ描き込む
     *
     * @param category
     * @param fontColor
     * @param fontSize
     * @param pathArray
     * @param state
     */

    private void drawRemoteData(int category, String fontColor, int fontSize, JSONArray pathArray, int state) {

        Paint paint = new Paint();

        //ツールの切り替え
        switch (category) {

            case TOOL_ERASER:
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                paint.setARGB(0, 0, 0, 0);
                paint.setAntiAlias(true);
                paint.setStrokeWidth(fontSize);
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeCap(Paint.Cap.ROUND);
                break;

            case TOOL_PEN:
                paint = new Paint();
                paint.setAntiAlias(true);
                paint.setColor(Color.parseColor(fontColor));
                paint.setStrokeWidth(fontSize);
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeCap(Paint.Cap.ROUND);
                break;

            default:
                break;
        }

        Path path = new Path();
        int pathArrayLength = pathArray.length();
        for (int i = 0; i < pathArrayLength; i++) {
            try {

                JSONObject data = pathArray.getJSONObject(i);
                float x = Float.parseFloat(data.getString("x"));
                float y = Float.parseFloat(data.getString("y"));
                if (i == 0) path.moveTo(x, y);
                path.lineTo(x, y);

            } catch (JSONException e) {
                Log.e(TAG, e.toString());
            }
        }
        drawLine(path, paint);
    }

    /**
     * 描画処理
     *
     * @param path
     */
    private void drawLine(Path path, Paint paint) {
        // ロックしてキャンバスを取得します。
        Canvas canvas = mHolder.lockCanvas();

        //比率に応じてキャンパスサイズを指定
        canvas.scale(mCanvasScale, mCanvasScale);

        // キャンバスをクリアします。
        canvas.drawColor(0, PorterDuff.Mode.CLEAR);

        // 前回描画したビットマップをキャンバスに描画します。
        canvas.drawBitmap(mLastDrawBitmap, 0, 0, null);

        // 画像を描画
        //mCanvas.drawBitmap(reBmp,0,0,null);

        // パスを描画します。
        if (path != null && paint != null) canvas.drawPath(path, paint);

        // ロックを外します。
        mHolder.unlockCanvasAndPost(canvas);
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

        //TODO 調整中
        //描画データを初期化
        //mCanvas = mHolder.lockCanvas();
        //mLastDrawBitmap = null;
        clearLastDrawBitmap();
        //mCanvas.drawColor(0, PorterDuff.Mode.CLEAR);

        // 描画データを取得
        getRemoteData();
    }

    /**
     * 消しゴム（OekakiActivityから呼び出し用）
     */

    public void setToolEraser() {
        mDrawToolCategory = TOOL_ERASER;
        mDrawPaint = new Paint();
        mDrawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        mDrawPaint.setARGB(0, 0, 0, 0);
        mDrawPaint.setAntiAlias(true);
        mDrawPaint.setStrokeWidth(mDrawFontSize);
        mDrawPaint.setStyle(Paint.Style.STROKE);
        mDrawPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    /**
     * ペンOekakiActivityから呼び出し用）
     */
    public void setToolPen() {
        mDrawToolCategory = TOOL_PEN;
        mDrawPaint = new Paint();
        mDrawPaint.setAntiAlias(true);
        mDrawPaint.setStrokeWidth(mDrawFontSize);
        mDrawPaint.setColor(Color.parseColor(mDrawFontColor));
        mDrawPaint.setStyle(Paint.Style.STROKE);
        mDrawPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    /**
     * TODO 描画データを全て削除して同期する
     */
    public void deleteAll() {

        // ダイアログ表示
        mUtils.progressShow("通信中", "削除リクエストを送信中です");

        // サーバの描画データを全て削除する
        final NCMBObject obj = new NCMBObject("DrawingClass");
        NCMBQuery<NCMBObject> query = new NCMBQuery<>("DrawingClass");
        query.findInBackground(new FindCallback<NCMBObject>() {
            @Override
            public void done(List<NCMBObject> results, NCMBException e) {
                if (e != null) {
                    Log.e(TAG, "deleteAll:削除失敗");
                } else {
                    Log.i(TAG, "deleteAll:削除成功");
                }
            }
        });

        // 1秒待ってから描画データを更新する
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                    e.printStackTrace();
                }

                // ダイアログを閉じる
                mUtils.progressDismiss();

                // 描画データを更新
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        // TODO 調整中
//                        mCanvas = null;
//                        // ロックしてキャンバスを取得します。
//                        mCanvas = mHolder.lockCanvas();
//                        // キャンバスをクリアします。
//                        init();
//                        mCanvas.scale(mCanvasScale, mCanvasScale);
//                        mCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
//                        //mCanvas.drawBitmap(reBmp,0,0,null);
//                        // ロックを外します。
//                        mHolder.unlockCanvasAndPost(mCanvas);
//                        getRemoteData();
//                        sync();
                    }
                });
            }
        }).start();
    }

    public void setDrawFontSize(float drawFontSize) {
        mDrawFontSize = drawFontSize;
        mDrawPaint.setStrokeWidth(drawFontSize);
    }

    public void setDrawFontColor(String drawFontColor) {
        mDrawFontColor = drawFontColor;
        mDrawPaint.setColor(Color.parseColor(drawFontColor));
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