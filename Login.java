package com.darkbears.webservice.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.darkbears.webservice.R;

import org.json.JSONObject;

import java.io.IOException;

import javax.security.auth.login.LoginException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Login extends ActionBarActivity {
    EditText email,password;
    Button login,signupnow;
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initialize();
        initonclicklistner();
    }

    private void initonclicklistner() {

        login.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_DOWN){
                    login.setBackgroundResource(R.drawable.greenbt);

                }else if (event.getAction()==MotionEvent.ACTION_UP){
                    login.setBackgroundResource(R.drawable.smallbt);
                    Asyncforlogin async=new Asyncforlogin();
                    async.execute();
                }
                return true;
            }
        });

        signupnow.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_DOWN){
                    signupnow.setBackgroundResource(R.drawable.greenbt);

                }else if (event.getAction()==MotionEvent.ACTION_UP){
                    signupnow.setBackgroundResource(R.drawable.smallbt);
                    Intent in = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(in);
                    finish();
                }
                return true;
            }
        });
    }

    private void initialize() {
        email=(EditText)findViewById(R.id.editText);
        password=(EditText)findViewById(R.id.editText2);
        login=(Button)findViewById(R.id.button);
        signupnow=(Button)findViewById(R.id.button2);

    }

    public class Asyncforlogin extends AsyncTask<Void,Void,String>
    {

        String api_response;
        String semail,spassword;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Login.this);
            pDialog.setMessage("Please Wait...");
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            semail=email.getText().toString();
            spassword=password.getText().toString();
            OkHttpClient client = new OkHttpClient();

            RequestBody formBody = new FormBody.Builder()
                    .add("email", semail)
                    .add("password",spassword)
                    .build();
            Request request = new Request.Builder()
                    .url("http://dbwsweb.com/projectdemo/conwash_test/web_apis/Ws_controller/login")
                    .post(formBody)
                    .build();

            try {
                Response response = client.newCall(request).execute();

                api_response=response.body().string();
                Log.e("clickingbody", api_response);

                // Do something with the response.
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jobj=new JSONObject(api_response);

                String message=jobj.getString("message");
                new AlertDialog.Builder(Login.this)
                        .setMessage(message)
                        .show();
                pDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
}
