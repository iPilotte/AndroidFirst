package kku03.chai.waya.training;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.jibble.simpleftp.SimpleFTP;

import java.io.File;

public class SignUpActivity extends AppCompatActivity {

    //Exelicit
    private EditText nameEditText, phoneEditText, userEditText, passwordEditText;
    private ImageView imageView;
    private Button button;
    private String nameString, phoneString, userString, passwordString, imagePathString, imageNameString;
    private Uri uri;
    private Boolean aBoolean = true;
    private String urlAddUser = "http://swiftcodingthai.com/kku/add_user_master.php";
    private String urlImage = "http://swiftcodingthai.com/kku/Image";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //Bind Widget
        nameEditText = (EditText) findViewById(R.id.editText5);
        phoneEditText = (EditText) findViewById(R.id.editText2);
        userEditText = (EditText) findViewById(R.id.editText3);
        passwordEditText = (EditText) findViewById(R.id.editText4);
        imageView = (ImageView) findViewById(R.id.imageView);
        button = (Button) findViewById(R.id.button3);


        //SignUp Controller
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Get Value From EditText
                nameString = nameEditText.getText().toString().trim();
                phoneString = phoneEditText.getText().toString().trim();
                userString = userEditText.getText().toString().trim();
                passwordString = passwordEditText.getText().toString().trim();

                //CheckSpace
                if (nameString.equals("") || phoneString.equals("") || userString.equals("") || passwordString.equals("")) {
                    //Have Space
                    Log.d("12novV1", "Have Space");
                    MyAlert myAlert = new MyAlert(SignUpActivity.this, R.drawable.bird48, "ERROR", "NO SPACE");
                    myAlert.myDialog();
                } else if(aBoolean) {

                    MyAlert myAlert = new MyAlert(SignUpActivity.this, R.drawable.kon48, "ERROR", "NO IMAGE");
                    myAlert.myDialog();

                } else {

                    //Choose image done

                    uploadImageToServer();
                    uploadStringToServer();

                }

            } //Onclick
        });

        //Image Controller
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT); //Process on other program and call back
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "CHOOSE APPLICATION"), 0);


            } //onClick
        });


    } //Main Method

    private void uploadStringToServer() {

        AddNewUser addNewUser = new AddNewUser(SignUpActivity.this);
        addNewUser.execute(urlAddUser);

    }   //UplaodString

    //Create Inner class
    private class AddNewUser extends AsyncTask<String, Void, String> {

        //Explicit
        private Context context;

        public AddNewUser(Context context) {
            this.context = context;
        }


        @Override
        protected String doInBackground(String... params) {

            try {

                OkHttpClient okHttpClient = new OkHttpClient();
                RequestBody requestBody = new FormEncodingBuilder().add("isAdd", "true").add("Name", nameString)
                        .add("Phone", phoneString).add("User", userString)
                        .add("Password", passwordString).add("Image", urlImage + imageNameString).build();
                Request.Builder builder = new Request.Builder();
                Request request = builder.url(params[0]).post(requestBody).build();
                Response response = okHttpClient.newCall(request).execute();
                //Log.d("Error : ", "RegistUser // doInBack try : " + response.body().toString());
                return response.body().string();

            } catch (Exception ex) {
                Log.d("13novV1", "e data : " + ex.toString());
            }

            return null;
        } //DoInBack

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.d("13novV1", "Result : " + s);
            if (Boolean.parseBoolean(s)) {
                Toast.makeText(context, "Upload success", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(context, "Upload fail", Toast.LENGTH_SHORT).show();
            }

        }   //onPost
    } //AddNewUser Class


    private void uploadImageToServer() {

        //CheckPolicy
        StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(threadPolicy);
        try {

            SimpleFTP simpleFTP = new SimpleFTP();
            simpleFTP.connect("ftp.swiftcodingthai.com", 21, "kku@swiftcodingthai.com", "Abc12345");
            simpleFTP.bin();
            simpleFTP.cwd("Image");
            simpleFTP.stor(new File(imagePathString));
            simpleFTP.disconnect();

        } catch (Exception ex) {
            Log.d("12novV1", "Cant upload" + ex.toString());
        }

    } //Upload

    //After Choose image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == 0) && (resultCode == RESULT_OK)) {
            Log.d("12NovV1", "Result ok");
            aBoolean = false;
            //Show image
            uri = data.getData();
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                imageView.setImageBitmap(bitmap);


            } catch (Exception ex) {
                ex.printStackTrace();
            }


            //Find path of image
            imagePathString = myFindPath(uri);
            Log.d("12novV1" , "Path " + imagePathString);

            //Find image name
            imageNameString = imagePathString.substring(imagePathString.lastIndexOf("/")+1);
            Log.d("12novV1", "Name " + imageNameString);

        }   //if

    }

    private String myFindPath(Uri uri) {

        String result = null;
        String[] strings = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, strings, null, null, null);

        if (cursor != null) {

            cursor.moveToFirst();
            int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            result = cursor.getString(index);

        } else {

            result = uri.getPath();
        }

        return result;
    }
} //Main Class
