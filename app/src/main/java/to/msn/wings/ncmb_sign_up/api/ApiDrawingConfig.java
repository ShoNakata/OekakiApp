package to.msn.wings.ncmb_sign_up.api;

public class ApiDrawingConfig {
    public static String DOMAIN = "morijyobi.sakura.ne.jp";
    // APIプロジェクト名
    public static String PROJECT_NAME = "SampleApi";
    public static String BASE_URL = "http://" + DOMAIN + "/" + PROJECT_NAME;

    public static String API_APP_KEY = "b18d561e7aa78c63abe4cd7a0bab2693b84cb975fe627e805281aaf9a2cfd82b";
    public static String API_CLIENT_KEY = "80c9d743a613b53e9c09f104371d32cfdf7d79449ca1ed2b8b2e89dd31de5f72";

    // API通信の間隔（連打防止）
    public static int REQUEST_INTERVAL = 500;
}
