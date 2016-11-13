package kku03.chai.waya.training;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class ServiceActivity extends AppCompatActivity {

    private ListView listView;
    private String[] nameStrings, phoneStrings, imageStrings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

        //Bind widget
        listView = (ListView) findViewById(R.id.livFriend);

        //Receive value from intent
        nameStrings = getIntent().getStringArrayExtra("Name");
        phoneStrings = getIntent().getStringArrayExtra("Phone");
        imageStrings = getIntent().getStringArrayExtra("Image");

        //Create ListView
        MyAdapter myAdapter = new MyAdapter(ServiceActivity.this, nameStrings, phoneStrings, imageStrings);
        listView.setAdapter(myAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                confirmCall(nameStrings[position], phoneStrings[position]);

            }   //onItemClick
        });


    }

    private void confirmCall(String nameString, final String phoneString) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ServiceActivity.this);
        builder.setCancelable(false);
        builder.setIcon(R.drawable.bird48);
        builder.setTitle("Please confirm");
        builder.setMessage("คุณต้องการโทรหา " + nameString + "จริงหรือไม่");
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:=" + phoneString));
                if (ActivityCompat.checkSelfPermission(ServiceActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(intent);
            }
        });
        builder.show();
    }   //Confirm
}
