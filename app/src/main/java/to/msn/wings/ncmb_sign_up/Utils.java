package to.msn.wings.ncmb_sign_up;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.DisplayMetrics;

/**
 * 各クラスで使いまわしたいメソッド
 */

public class Utils {

    // 呼び出し元
    private Context mParent;
    // プログレスバー
    private ProgressDialog mProgressDialog;

    /**
     * コンストラクタ
     *
     * @param parent
     */
    public Utils(Context parent) {
        mParent = parent;
    }

    /**
     * プログレスバーの表示
     *
     * @param title
     * @param description
     */
    public void progressShow(String title, String description) {
        mProgressDialog = new ProgressDialog(mParent);
        mProgressDialog.setTitle(title);
        mProgressDialog.setMessage(description);
        mProgressDialog.show();
    }

    /**
     * プログレスバーを閉じる
     */
    public void progressDismiss() {
        mProgressDialog.dismiss();
    }

    /**
     * dpからpixelへの変換
     *
     * @param dp
     * @param context
     * @return float pixel
     */
    public static float convertDp2Px(float dp, Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return dp * metrics.density;
    }

    /**
     * pixelからdpへの変換
     *
     * @param px
     * @param context
     * @return float dp
     */
    public static float convertPx2Dp(int px, Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return px / metrics.density;
    }
}
