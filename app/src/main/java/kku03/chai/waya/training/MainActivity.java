package kku03.chai.waya.training;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

public class MainActivity extends AppCompatActivity {

    //Explicit
    private Button signInButton, signUpButton;
    private EditText userEditText, passwordEditText;
    private String userString, passwordString;
    private MyConstant myConstant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myConstant = new MyConstant();

        //Bind Widget
        signInButton = (Button) findViewById(R.id.button);
        signUpButton = (Button) findViewById(R.id.button2);
        userEditText = (EditText) findViewById(R.id.editText5);
        passwordEditText = (EditText) findViewById(R.id.editText6);

        //SignInController
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userString = userEditText.getText().toString().trim();
                passwordString = passwordEditText.getText().toString().trim();

                if (userString.equals("") || passwordString.equals("")) {

                    //HaveSpace
                    MyAlert myAlert = new MyAlert(MainActivity.this, R.drawable.doremon48
                            , getResources().getString(R.string.title_HaveSpace)
                            , getResources().getString(R.string.message_HaveSpace));
                    myAlert.myDialog();

                } else {
                    //NoSpace
                    SynUser synUser = new SynUser(MainActivity.this);
                    synUser.execute(myConstant.getUrlGetJSON());


                }
            }    //onClick
        });

        //SignUp Controller
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SignUpActivity.class));
            }
        });

    } //Main Method

    private class SynUser extends AsyncTask<String, Void, String> {

        private Context context;

        private SynUser(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {

            try {

                OkHttpClient okHttpClient = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                Request request = builder.url(params[0]).build();
                Response response = okHttpClient.newCall(request).execute();
                return response.body().string();

            } catch (Exception ex) {
                Log.d("13novV2", "e : doIN :: " + ex.toString());
                return null;
            }

        }   //doInBack

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.d("13noV2", "JSON : "+ s);

        }   //onPost
    } //synUser


} //Main Class
