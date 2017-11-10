package to.msn.wings.ncmb_sign_up.api;

import android.app.Activity;

import org.json.JSONObject;

public class ApiDrawingManager {

    public interface REQUEST_API_PRODUCT {
        String URL = ApiProductConfig.BASE_URL + "/product/";
        String INDEX = URL + "index.json";
        String FIND = URL + "find.json";
        String ADD = URL + "add.json";
        String DELETE = URL + "delete.json";
    }

    public interface PRODUCT_COLUMNS {
        String ID = "id";
        String NAME = "name";
        String PRICE = "price";
    }

    private Activity parent = null;

    // コンストラクタ
    public ApiDrawingManager(Activity activity) {
        parent = activity;
    }

    // 描画情報の送信
    public void send(JSONObject json) {
        ApiDrawingSend apiDrawingSend = new ApiDrawingSend();
        String[] params = new String[]{
                REQUEST_API_PRODUCT.ADD,
                "?",
                PRODUCT_COLUMNS.PRICE + "=" + json.toString()
        };
        apiDrawingSend.execute(params);
    }
}
