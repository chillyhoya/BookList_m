package com.example.booklist.data;

import android.os.Handler;
import android.util.Log;

import com.example.booklist.data.model.Shop;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class ShopLoader {
    public ArrayList<Shop> getShops() {
        return shops;
    }

    private ArrayList<Shop>shops=new ArrayList<>();//初始化商店数组

    public String download(String urlString)//接受一个字符串的网址参数
    {
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);//设置超时
            conn.setUseCaches(false);//初始设置为false方便更新

            conn.connect();//真正获取到数据连接
            InputStream inputStream = conn.getInputStream();
            InputStreamReader input = new InputStreamReader(inputStream);
            BufferedReader buffer = new BufferedReader(input);
            if (conn.getResponseCode() == 200) {//200意味着返回的是"OK"
                String inputLine;
                StringBuffer resultData = new StringBuffer();//StringBuffer字符串拼接很快
                while ((inputLine = buffer.readLine()) != null) {
                    resultData.append(inputLine);
                }
                String text = resultData.toString();
                Log.v("out---------------->", text);
                return (text);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";//若读取出错返回空字符
        }
        public void parseJson(String text)//text是上面获得的文本
        {
            try{
                JSONObject jsonObject = new JSONObject(text);//获得Json对象
                JSONArray jsonDatas = jsonObject.getJSONArray("shops");//获得商店数组对象
                int length = jsonDatas.length();
                String test;
                for (int i = 0; i < length; i++) {//按照数组长度循环，获得每一个商店对象
                    JSONObject shopJson = jsonDatas.getJSONObject(i);
                    Shop shop = new Shop();
                    shop.setName(shopJson.getString("name"));
                    shop.setLatitude(shopJson.getDouble("latitude"));
                    shop.setLongitude(shopJson.getDouble("longitude"));
                    shop.setMemo(shopJson.getString("memo"));
                    shops.add(shop);//将对象添加到商店数组
                }
                }catch(Exception e){
                e.printStackTrace();
            }
        }
        public void load(final Handler handler, final String url)
        {
            new Thread(new Runnable() {
                @Override
                public void run() {
                String content = download(url);
                parseJson(content);
                handler.sendEmptyMessage(1);
                }
            }).start();
        }
}
