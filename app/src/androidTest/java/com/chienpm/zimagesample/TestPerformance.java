package com.chienpm.zimagesample;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class TestPerformance {
    private static final String TAG = "TestPerformance";

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        assertEquals("com.chienpm.zimagesample", appContext.getPackageName());
    }

    @Test
    public void crawImage(){
        //https://api.imgur.com/3/gallery/search/viral/%7Bpage%7D?q_type=jpg&client_id=546c25a59c58ad7&q=school
        StringBuffer response = null;
        String keywords[] = new String[]{"messi", "ronaldo", "neymar", "rooney", "torres", "kaka", "tom", "jerry"};
        String keywords2[] = new String[]{"lady", "girl", "marie", "lan-ngoc", "ngoc-trinh"};
        String clientId = "546c25a59c58ad7";

        JSONObject json = null;

        int count = 0;
        for(String keyword: keywords2) {
            for (int page = 1; page < 5; page++) {
                try {
                    URL apiURL = new URL("https://api.imgur.com/3/gallery/search/viral/" + page + "?q_type=jpg&client_id=" + clientId + "&q=" + keyword);
                    HttpURLConnection connection = (HttpURLConnection) apiURL.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setReadTimeout(10000); //10 seconds

                    int responseCode = connection.getResponseCode();

                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        String inputLine;
                        response = new StringBuffer();

                        // read data
                        while ((inputLine = bufferedReader.readLine()) != null) {
                            response.append(inputLine);
                        }

                        // process data
                        try {

                            json = new JSONObject(response.toString());

                            JSONArray galleries = json.getJSONArray("data");

                            for (int i = 0; i < galleries.length(); ++i) {

                                if (galleries.getJSONObject(i).has("images")) {

                                    JSONArray images = galleries.getJSONObject(i).getJSONArray("images");

                                    for (int j = 0; j < images.length(); ++j) {

                                        JSONObject img = images.getJSONObject(j);
                                        if (img.has("link")) {
                                            String link = img.getString("link");
                                            if (!link.contains(".mp4")) {
                                                System.out.println(link);
                                            }
                                        }
                                    }
                                }

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            System.out.println("error: " + e.getMessage());
                            json = null;
                        }


                    } else {
                        System.out.println("GET request not worked");
                    }


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    response = null;
                } catch (IOException e) {
                    e.printStackTrace();
                    response = null;
                }
            }
        }

        System.out.printf(json.toString());

        assertNotNull(response);
        assertTrue(response.length() > 0);
    }

    public List<String> loadImages(){
        List<String> images = new ArrayList<>();

        try {

            Context testContext = InstrumentationRegistry.getInstrumentation().getContext();
            InputStream inputStream = testContext.getAssets().open("images.txt");

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String line;

            while ((line = bufferedReader.readLine())!=null){
                images.add(line);
                System.out.println(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
            images.clear();
        }

        assertTrue(images.size()>0);
        return  images;
    }
}
