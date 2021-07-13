package com.example.businesscardscanner;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;


public class DBSearchBackground extends AsyncTask <String, Void, String> {

    private String returnStatus;
    String[] arrOfStr;
    private DataBaseReturn response;

    private Context context;

    public DBSearchBackground(Context context){
        this.context=context;
    }

    @Override
    protected String doInBackground(String... strings) {
        String reg_url="http://" + IPAddress.getIPAddress() + "/business_card_scanner/searchDB.php";
        String query = strings[0];
        try {
            URL url = new URL(reg_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            OutputStream OS = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
            String data= URLEncoder.encode("query","UTF-8")+"="+URLEncoder.encode(query, "UTF-8");
            bufferedWriter.write(data);
            bufferedWriter.flush();
            bufferedWriter.close();
            OS.close();
            InputStream IS = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(IS));

            ArrayList<String> names = new ArrayList<String>();
            ArrayList<String> phones = new ArrayList<String>();
            ArrayList<String> addresses = new ArrayList<String>();
            ArrayList<String> cities = new ArrayList<String>();
            ArrayList<String> emails = new ArrayList<String>();

            while ((returnStatus = bufferedReader.readLine()) != null) {
                System.out.println(returnStatus);
                int i = 0;
                arrOfStr = returnStatus.split("----", -1);
                arrOfStr[arrOfStr.length-1] = "";

                while (i < arrOfStr.length) {
                    if ((i%5 == 0) && (arrOfStr[i]!=""))
                        names.add(arrOfStr[i]);
                    else if (i%5 == 1)
                        phones.add(arrOfStr[i]);
                    else if (i%5 == 2)
                        addresses.add(arrOfStr[i]);
                    else if (i%5 == 3)
                        cities.add(arrOfStr[i]);
                    else if (i%5 == 4)
                        emails.add(arrOfStr[i]);
                    i++;
                }
            }
            response = new DataBaseReturn();
            response.names = names;
            response.phones = phones;
            response.addresses = addresses;
            response.cities = cities;
            response.emails = emails;
            bufferedReader.close();
            IS.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return returnStatus;
    }

    @Override
    protected void onPostExecute(String s) {
        Intent intent = new Intent(context, DisplaySearchResults.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("File", (Serializable) response);
        context.startActivity(intent);
    }
}
