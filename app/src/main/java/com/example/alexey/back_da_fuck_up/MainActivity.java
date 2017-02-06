package com.example.alexey.back_da_fuck_up;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class MainActivity extends Activity implements View.OnClickListener{

    ProgressDialog dialog;
    String WWW;
    String userLOGIN;
    String userPASS;


    EditText etLogin;
    EditText etPassword;
    Button buttonStart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etLogin = (EditText) findViewById(R.id.userLogin);
        etPassword = (EditText) findViewById(R.id.userPassword);





        buttonStart = (Button) findViewById(R.id.btnStart);
        buttonStart.setOnClickListener(this);
    }

    public String getJSON(String login, String pass) // получаем json объект в виде строки
    {
        JSONObject bot = new JSONObject();
        try {
            bot.put("Login", login);
            bot.put("Password", pass);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return bot.toString();
    }

    @Override
    public void onClick(View v) {


        WWW  = "http://192.168.0.183/index.php";
        userLOGIN = etLogin.getText().toString();
        userPASS = etPassword.getText().toString();

        Log.d("BOT","STARTED");
        Log.d("BOT",WWW);
        Log.d("BOT",userLOGIN);
        Log.d("BOT",userPASS);

        new RequestTask().execute(WWW,userLOGIN,userPASS); // скрипт, на который посылаем запрос
    }


    public class RequestTask extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(MainActivity.this);
            dialog.setMessage("Ожидание");
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            dialog.show();
            super.onPreExecute();
        }


        @Override
        protected String doInBackground(String... params) {

            try {
                DefaultHttpClient hc = new DefaultHttpClient();
                ResponseHandler<String> res = new BasicResponseHandler();
                HttpPost postMethod = new HttpPost(params[0]);
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                // ключ - "json", параметр - json в виде строки
                nameValuePairs.add(new BasicNameValuePair("json", getJSON(userLOGIN, userPASS)));
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(nameValuePairs, "UTF-8");
                postMethod.setEntity(entity);
                TimeUnit.SECONDS.sleep(2); //делем паузу 2 сек
                return hc.execute(postMethod, res);
            }
            catch (Exception e)
            {
                System.out.println("Exp=" + e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String res) {
            dialog.dismiss();
            // res - ответ сервера TODO
            super.onPostExecute(res);
        }


    }
}