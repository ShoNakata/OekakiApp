package to.msn.wings.ncmb_sign_up.api;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiDrawingSend extends AsyncTask<String, Integer, String> {

    public ApiDrawingSend() {
        super();
    }

    @Override
    protected String doInBackground(String... params) {
        //publishProgress(30);

        //SystemClock.sleep(ApiProductConfig.REQUEST_INTERVAL);

        //publishProgress(60);

        StringBuffer list = new StringBuffer();
        try {

            String strUrl = "";
            for (String param : params) {
                strUrl += param;
            }

            URL url = new URL(strUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            new BufferedReader(new InputStreamReader(con.getInputStream(), "Shift-JIS"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //publishProgress(100);
        return list.toString();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //mProgressBar.setVisibility(ProgressBar.VISIBLE);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        //mProgressBar.setProgress(values[0]);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        //txtResult.setText(s);
        //mProgressBar.setVisibility(ProgressBar.GONE);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        //mProgressBar.setVisibility(ProgressBar.GONE);
    }
}