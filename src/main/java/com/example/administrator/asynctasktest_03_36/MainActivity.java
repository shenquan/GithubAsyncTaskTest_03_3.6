package com.example.administrator.asynctasktest_03_36;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends ActionBarActivity {

    private TextView show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        show = (TextView) findViewById(R.id.show);
    }

    public void download(View v) throws MalformedURLException {
        DownTask downTask = new DownTask(this);
        downTask.execute(new URL("http://weixin.sogou.com/?p=73141200&kw="));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class DownTask extends AsyncTask<URL, Integer, String> {

        ProgressDialog progressDialog;
        int hasRead = 0;
        Context context;

        public DownTask(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(URL... params) {
            StringBuilder stringBuilder = new StringBuilder();
            try {
                URLConnection conn = params[0].openConnection();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()
                        , "utf-8"));
                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line + "\n");
                    hasRead++;
                    publishProgress(hasRead);
                }
                return stringBuilder.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(context);
            progressDialog.setTitle("任务正在进行中");
            progressDialog.setMessage("任务正在执行中，敬请等待");
            progressDialog.setCancelable(false);
            progressDialog.setMax(2000);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setIndeterminate(false);
            progressDialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            show.setText("已经读取了【" + values[0] + "】行！");
            progressDialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            show.setText(s);
            progressDialog.dismiss();

        }
    }
}
