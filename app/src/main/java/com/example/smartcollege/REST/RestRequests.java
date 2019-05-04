package com.example.smartcollege.REST;

import com.example.smartcollege.HTTPMethodsEnum;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class RestRequests {
    HttpURLConnection conn;

    public String HttpRequest(String URL, JSONObject obj, String token, HTTPMethodsEnum method) throws IOException {
        URL url = new URL(URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(20000);
        conn.setConnectTimeout(20000);
        conn.setRequestMethod(method.getMethodName());
        conn.setRequestProperty("Content-Type", "application/json");
        if(token != null){
            String basicAuth = "Basic " + token;
            conn.setRequestProperty("Authorization",basicAuth);
        }
        if(method.getMethodName() != HTTPMethodsEnum.GET.getMethodName()) {
            conn.setRequestProperty("Content-Length", "" + obj.toString().getBytes().length);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            try {
                writer.write(obj.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            writer.flush();
            writer.close();
            os.close();
        }


        int responseCode = conn.getResponseCode(); // To Check for 200
        if (responseCode == HttpsURLConnection.HTTP_OK) {

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuffer sb = new StringBuffer("");
            String line = "";
            while ((line = in.readLine()) != null) {
                sb.append(line);
                break;
            }
            in.close();
            return sb.toString();
        }
        return null;
    }
}
