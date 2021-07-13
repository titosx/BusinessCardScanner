package com.example.businesscardscanner;

import androidx.appcompat.app.AppCompatActivity;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.googlecode.tesseract.android.TessBaseAPI;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OpenOcrActivity extends AppCompatActivity {

    private TessBaseAPI mTess;
    String datapath = "";

    EditText displayName;
    EditText displayPhone;
    EditText displayAddress;
    EditText displayCity;
    EditText displayEmail;
    Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Uri file = (Uri) this.getIntent().getParcelableExtra("File");
        setContentView(R.layout.activity_open_ocr);

        displayName = (EditText) findViewById(R.id.cardName);
        displayPhone = (EditText) findViewById(R.id.cardPhone);
        displayAddress = (EditText) findViewById(R.id.cardAddress);
        displayCity = (EditText) findViewById(R.id.cardCity);
        displayEmail = (EditText) findViewById(R.id.cardEmail);
        save = (Button) findViewById(R.id.saveToDB);
        displayName.addTextChangedListener(textWatcher);
        displayPhone.addTextChangedListener(textWatcher);
        displayAddress.addTextChangedListener(textWatcher);
        displayCity.addTextChangedListener(textWatcher);
        displayEmail.addTextChangedListener(textWatcher);

        //initialize Tesseract API
        String language = "eng";
        datapath = getFilesDir()+ "/tesseract/";
        mTess = new TessBaseAPI();

        checkFile(new File(datapath + "tessdata/"));

        mTess.init(datapath, language);

        ImageView imageView = (ImageView)findViewById(R.id.imageView);

        //get bitmap from uri
        Bitmap image = null;
        try {
            image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), file);


            String path = file.toString().substring(7);
            image = modifyOrientation(image, path);
            imageView.setImageBitmap(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mTess.setImage(image);
        String OCRresult = null;
        OCRresult = mTess.getUTF8Text();
        System.out.println(OCRresult);
        extractName(OCRresult);
        extractPhone(OCRresult);
        extractAddress(OCRresult);
        extractCity(OCRresult);
        extractEmail(OCRresult);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToDatabase();
            }
        });
    }

    public void saveToDatabase() {
        EditText cardName = (EditText)findViewById(R.id.cardName);
        String name = cardName.getText().toString().trim();
        EditText cardPhone = (EditText)findViewById(R.id.cardPhone);
        String phone = cardPhone.getText().toString().trim();
        EditText cardAddress = (EditText)findViewById(R.id.cardAddress);
        String address = cardAddress.getText().toString().trim();
        EditText cardCity = (EditText)findViewById(R.id.cardCity);
        String city = cardCity.getText().toString().trim();
        EditText cardEmail = (EditText)findViewById(R.id.cardEmail);
        String email = cardEmail.getText().toString().trim();
        DBSaveBackground DBSaveBackground = new DBSaveBackground(getApplicationContext());
        DBSaveBackground.execute(name,phone,address,city,email);
        this.finish();
    }



    public static Bitmap modifyOrientation(Bitmap bitmap, String image_absolute_path) throws IOException {
        ExifInterface ei = new ExifInterface(image_absolute_path);
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotate(bitmap, 90);

            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotate(bitmap, 180);

            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotate(bitmap, 270);

            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                return flip(bitmap, true, false);

            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                return flip(bitmap, false, true);

            default:
                return bitmap;
        }
    }

    public static Bitmap rotate(Bitmap bitmap, float degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static Bitmap flip(Bitmap bitmap, boolean horizontal, boolean vertical) {
        Matrix matrix = new Matrix();
        matrix.preScale(horizontal ? -1 : 1, vertical ? -1 : 1);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public void extractName(String str){
        System.out.println("Getting the Name");
        final String NAME_REGEX = "^([A-Z]([a-z]*|\\.) *){1,2}([A-Z][a-z]+-?)+$";
        Pattern p = Pattern.compile(NAME_REGEX, Pattern.MULTILINE);
        Matcher m =  p.matcher(str);
        if(m.find()){
            System.out.println(m.group());
            displayName.setText(m.group(), TextView.BufferType.EDITABLE);
        }
    }

    public void extractEmail(String str) {
        System.out.println("Getting the email");
        //final String EMAIL_REGEX = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
        final String EMAIL_REGEX = "[!-~]+@[!-~]+";
        Pattern p = Pattern.compile(EMAIL_REGEX, Pattern.MULTILINE);
        Matcher m = p.matcher(str);   // get a matcher object
        if(m.find()){
            System.out.println(m.group());
            displayEmail.setText(m.group(), TextView.BufferType.EDITABLE);
        }
    }

    public void extractPhone(String str){
        System.out.println("Getting Phone Number");
        final String PHONE_REGEX="(?:^|\\D)(\\d{3})[)\\-. ]*?(\\d{3})[\\-. ]*?(\\d{4})(?:$|\\D)";
        Pattern p = Pattern.compile(PHONE_REGEX, Pattern.MULTILINE);
        Matcher m = p.matcher(str);   // get a matcher object
        if(m.find()){
            System.out.println(m.group());
            displayPhone.setText(m.group().trim(), TextView.BufferType.EDITABLE);
        }
    }

    public void extractAddress(String str) {                    //must be "Address AddressNumber, City"
        System.out.println("Getting Address");
        final String ADDR_REGEX="^[A-z]+\\s([A-z]+\\s)*\\d+,\\s[A-z]+(\\s[A-z]+)*";
        Pattern p = Pattern.compile(ADDR_REGEX, Pattern.MULTILINE);
        Matcher m = p.matcher(str);   // get a matcher object
        if(m.find()){
            System.out.println(m.group().split(", ", 2)[0]);
            displayAddress.setText(m.group().split(", ", 2)[0].trim(), TextView.BufferType.EDITABLE);
        }
    }

    public void extractCity(String str) {
        System.out.println("Getting City");
        final String ADDR_REGEX="^[A-z]+\\s([A-z]+\\s)*\\d+,\\s[A-z]+(\\s[A-z]+)*";
        Pattern p = Pattern.compile(ADDR_REGEX, Pattern.MULTILINE);
        Matcher m = p.matcher(str);   // get a matcher object
        if(m.find()){
            System.out.println(m.group().split(", ", 2)[1]);
            displayCity.setText(m.group().split(", ", 2)[1].trim(), TextView.BufferType.EDITABLE);
        }
    }


    private void checkFile(File dir) {
        //directory does not exist, but we can successfully create it
        if (!dir.exists()&& dir.mkdirs()){
            copyFiles();
        }
        //The directory exists, but there is no data file in it
        if(dir.exists()) {
            String datafilepath = datapath+ "/tessdata/eng.traineddata";
            File datafile = new File(datafilepath);
            if (!datafile.exists()) {
                copyFiles();
            }
        }
    }

    private void copyFiles() {
        try {
            //location we want the file to be at
            String filepath = datapath + "/tessdata/eng.traineddata";

            //get access to AssetManager
            AssetManager assetManager = getAssets();
            //open byte streams for reading/writing
            InputStream instream = assetManager.open("tessdata/eng.traineddata");
            OutputStream outstream = new FileOutputStream(filepath);

            //copy the file to the location specified by filepath
            byte[] buffer = new byte[1024];
            int read;
            while ((read = instream.read(buffer)) != -1) {
                outstream.write(buffer, 0, read);
            }
            outstream.flush();
            outstream.close();
            instream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String name = displayName.getText().toString().trim();
            String phone = displayPhone.getText().toString().trim();
            String address = displayAddress.getText().toString().trim();
            String city = displayCity.getText().toString().trim();
            String email = displayEmail.getText().toString().trim();

            save.setEnabled(!name.isEmpty() && !phone.isEmpty() && !address.isEmpty()  && !city.isEmpty()  && !email.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };


}