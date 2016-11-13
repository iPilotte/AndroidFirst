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
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

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
        private String[] nameStrings, phoneStrings, imageStrings;
        private String truePassword;
        private Boolean aBoolean = true;

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
            try {
                JSONArray jsonArray = new JSONArray(s);
                nameStrings = new String[jsonArray.length()];
                phoneStrings = new String[jsonArray.length()];
                imageStrings = new String[jsonArray.length()];

                for(int i=0; i<jsonArray.length();i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    nameStrings[i] = jsonObject.getString("Name");
                    phoneStrings[i] = jsonObject.getString("Phone");
                    imageStrings[i] = jsonObject.getString("Image");
                    Log.d("13novV3", "Name : " + nameStrings[i] + " @ " + i);

                    //Check user
                    if (userString.equals(jsonObject.getString("User"))) {
                        aBoolean = false;
                        truePassword = jsonObject.getString("Password");
                    }
                }
                if (aBoolean) {
                    //User False
                    MyAlert myAlert = new MyAlert(context, R.drawable.kon48, getResources().getString(R.string.title_user), getResources().getString(R.string.message_user));
                    myAlert.myDialog();


                } else if (passwordString.equals(truePassword)) {
                    //Password true
                    Toast.makeText(context, "Welcome", Toast.LENGTH_SHORT).show();

                    //Intent to Service
                    Intent intent = new Intent(MainActivity.this, ServiceActivity.class);
                    intent.putExtra("Name", nameStrings);
                    intent.putExtra("Phone", phoneStrings);
                    intent.putExtra("Image", imageStrings);
                    startActivity(intent);
                    finish();

                } else {
                    //PasswordFail
                    MyAlert myAlert = new MyAlert(context, R.drawable.rat48, getResources().getString(R.string.title_password), getResources().getString(R.string.message_password));
                    myAlert.myDialog();

                }   //If



            } catch (Exception ex) {

            }

        }   //onPost
    } //synUser


} //Main Class
