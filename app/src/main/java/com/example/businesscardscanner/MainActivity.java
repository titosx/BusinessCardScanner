package com.example.businesscardscanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.TextView;
import android.widget.Toast;
import com.theartofdev.edmodo.cropper.CropImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends AppCompatActivity {
    ImageView imageView;
    Bitmap bitmap;
    Toolbar toolbar;
    private Button takePictureButton;
    private Button dataBaseButton;
    Uri file;
    Uri resultUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final IPAddress ip = new IPAddress("192.168.1.14");

        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Business Card Scanner");
        toolbar.setOverflowIcon(getResources().getDrawable(R.drawable.menu_icon));

        takePictureButton = (Button) findViewById(R.id.btnCamera);
        dataBaseButton = (Button) findViewById(R.id.btnDB);

        imageView = (ImageView) findViewById(R.id.imageView);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            takePictureButton.setEnabled(false);
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
        }
        else {
            takePictureButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    takePicture();
                }
            });
        }

        dataBaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatabase();
            }
        });

        Button btnChangeIP = (Button) findViewById(R.id.btnChangeIP);
        btnChangeIP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ip.setIPAddress(((EditText) findViewById(R.id.dbIP)).getText().toString().trim());
                findViewById(R.id.textIP).setVisibility(View.GONE);
                findViewById(R.id.dbIP).setVisibility(View.GONE);
                findViewById(R.id.btnChangeIP).setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "IP Address changed to " + IPAddress.getIPAddress(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.changeIP:
                findViewById(R.id.textIP).setVisibility(View.VISIBLE);
                EditText dbIP = (EditText) findViewById(R.id.dbIP);
                dbIP.setVisibility(View.VISIBLE);
                dbIP.setText(IPAddress.getIPAddress(), TextView.BufferType.EDITABLE);

                Button btnChangeIP = (Button) findViewById(R.id.btnChangeIP);
                btnChangeIP.setVisibility(View.VISIBLE);
                break;
            case R.id.about:
                Toast.makeText(this, "About Checked", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                CropImage.activity(file)
                        .start(this);
            }
        }
        else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();

                Intent intent = new Intent(this, OpenOcrActivity.class);
                intent.putExtra("File", resultUri);
                startActivity(intent);
            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private static File getOutputMediaFile(){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "CameraDemo");

        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");
    }

    private void takePicture() {

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());


        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = Uri.fromFile(getOutputMediaFile());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, file);

        startActivityForResult(intent, 100);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                takePictureButton.setEnabled(true);
                imageView = (ImageView)findViewById(R.id.imageView);

                takePictureButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        takePicture();
                    }
                });
            }
        }
    }

    public void openDatabase() {
        DBReadBackground DBReadBackground = new DBReadBackground(this);
        DBReadBackground.execute();
    }

}
