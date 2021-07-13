package com.example.businesscardscanner;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class DBSaveBackground extends AsyncTask<String,Void,String> {

    String returnStatus;
    private Context context;

    public DBSaveBackground(Context context){
        this.context=context;
    }

    @Override
    protected String doInBackground(String... strings) {
        String reg_url="http://" + IPAddress.getIPAddress() + "/business_card_scanner/saveDB.php";
        String name = strings[0];
        String phone = strings[1];
        String address = strings[2];
        String city = strings[3];
        String email = strings[4];
        try {
            URL url = new URL(reg_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            OutputStream OS = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
            String data= URLEncoder.encode("name","UTF-8")+"="+URLEncoder.encode(name,"UTF-8")+"&"+URLEncoder.encode("phone","UTF-8")+"="+URLEncoder.encode(phone,"UTF-8")+"&"+URLEncoder.encode("address","UTF-8")+"="+URLEncoder.encode(address,"UTF-8")+"&"+URLEncoder.encode("city","UTF-8")+"="+URLEncoder.encode(city,"UTF-8")+"&"+URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(email,"UTF-8");
            bufferedWriter.write(data);
            bufferedWriter.flush();
            bufferedWriter.close();
            OS.close();
            InputStream IS = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(IS));
            returnStatus = bufferedReader.readLine();
            System.out.println(returnStatus);

            bufferedReader.close();
            IS.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        Toast.makeText(context, returnStatus, Toast.LENGTH_SHORT).show();
    }
}
