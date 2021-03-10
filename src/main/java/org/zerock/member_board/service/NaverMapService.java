package org.zerock.member_board.service;


import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import java.io.*;
import java.net.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

@Service
public class NaverMapService {

    private final String CLIENT_ID;
    private final String CLI_SECRET;

    public NaverMapService(Environment environment) {
        CLIENT_ID = environment.getProperty("naver.map.clientID"); //애플리케이션 클라이언트 아이디값";
        CLI_SECRET = environment.getProperty("naver.map.clientSecret"); //애플리케이션 클라이언트 시크릿값";
    }

    public String reqMapPostion(String address) throws IOException, ParseException {


        String addr = URLEncoder.encode(address,"utf-8");
        String api = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?query="+addr;
        StringBuffer sb = new StringBuffer();

        try {
            URL url = new URL(api);
            HttpsURLConnection http = (HttpsURLConnection)url.openConnection();
            http.setRequestProperty("Content-Type", "application/json");
            http.setRequestProperty("X-NCP-APIGW-API-KEY-ID", CLIENT_ID);
            http.setRequestProperty("X-NCP-APIGW-API-KEY", CLI_SECRET);
            http.setRequestMethod("GET");
            http.connect();

            InputStreamReader in = new InputStreamReader(http.getInputStream(),"utf-8");
            BufferedReader br = new BufferedReader(in);

            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }

            JSONParser parser = new JSONParser();
            JSONObject jsonObject;
            JSONObject jsonObject2;
            JSONArray jsonArray;
            String x = "";
            String y = "";


            System.out.println("결과 >>>>>>>>>> : " + sb.toString());

            jsonObject = (JSONObject)parser.parse(sb.toString());
            jsonArray = (JSONArray)jsonObject.get("addresses");
            for(int i=0;i<jsonArray.size();i++){
                jsonObject2 = (JSONObject) jsonArray.get(i);
                if(null != jsonObject2.get("x")){
                    x = (String) jsonObject2.get("x").toString();
                }
                if(null != jsonObject2.get("y")){
                    y = (String) jsonObject2.get("y").toString();
                }
            }

            br.close();
            in.close();
            http.disconnect();

            System.out.println("Latitude >> " + y + "Longitude >> " + x);

        } catch (IOException e) {

        }


        return sb.toString();




//
//        String apiURL = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?";
//        StringBuffer sb = new StringBuffer();
//
//
//        apiURL += String.format("query=%s&X-NCP-APIGW-API-KEY-ID=%s&X-NCP-APIGW-API-KEY=%s",
//                address, CLIENT_ID, CLI_SECRET);
//
//        System.out.println("api-=--------- "+apiURL);
//
//

//        return requestToServer(apiURL);
    }

    private String requestToServer(String apiURL) throws IOException, ParseException {
        return requestToServer(apiURL, "");
    }
    private String requestToServer(String apiURL, String headerStr) throws IOException, ParseException {


        InputStream in = null;
        BufferedReader reader = null;
        String line = null;
        StringBuffer res = new StringBuffer();



        URL url = new URL(apiURL);
        HttpsURLConnection con = (HttpsURLConnection)url.openConnection();

//        con.setHostnameVerifier(new HostnameVerifier() {
//            @Override
//            public boolean verify(String hostname, SSLSession session)
//            { // Ignore host name verification. It always returns true.
//                return true;
//            }
//        });



//        con.setRequestProperty("Content-Type","application/json");


//        con.setDoInput(true);
//        //con.setDoOutput(true); // Caches setting
//        con.setUseCaches(false);
//        // Read Timeout Setting
//        con.setReadTimeout(1000);
//        // Connection Timeout setting
//        con.setConnectTimeout(1000);
//        // Method Setting(GET/POST)
        con.setRequestMethod("GET");
        con.getHeaderFields();

        int responseCode = con.getResponseCode();
        System.out.println("응답코드 : " + responseCode);
        System.out.println("응답메세지 : " + con.getResponseMessage());
//
//        SSLContext context = SSLContext.getInstance("TLS");
//        context.init(null, null, null);
//        con.setSSLSocketFactory(context.getSocketFactory());


        con.connect();
//        con.setInstanceFollowRedirects(true);
//
//


        if (responseCode == HttpsURLConnection.HTTP_OK) {
            in = con.getInputStream();

        }
        else{
            in = con.getErrorStream();
        }

        reader = new BufferedReader(new InputStreamReader(in,"utf-8"));

        StringBuffer sb = new StringBuffer();
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }



        JSONParser parser = new JSONParser();
        JSONObject jsonObject;
        JSONObject jsonObject2;
        JSONArray jsonArray;
        String x = "";
        String y = "";

        jsonObject = (JSONObject)parser.parse(sb.toString());
        jsonArray = (JSONArray)jsonObject.get("addresses");
        for(int i=0;i<jsonArray.size();i++) {
            jsonObject2 = (JSONObject) jsonArray.get(i);
            if (null != jsonObject2.get("x")) {
                x = (String) jsonObject2.get("x").toString();
            }
            if (null != jsonObject2.get("y")) {
                y = (String) jsonObject2.get("y").toString();
            }
        }



            return res.toString();
        }

}
