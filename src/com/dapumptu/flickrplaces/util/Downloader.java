
package com.dapumptu.flickrplaces.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.json.JSONException;

import com.dapumptu.flickrplaces.model.DataManager;
import com.dapumptu.flickrplaces.model.PhotoObject;
import com.dapumptu.flickrplaces.model.Place;
import com.dapumptu.flickrplaces.model.PlaceData;

import android.os.AsyncTask;
import android.util.Log;

public class Downloader {

    public static final int RESULT_CONNECTION_ERROR = 100;
    //public static final int RESULT_XML_ERROR = 101;
    public static final int RESULT_SUCCESS = 102;

    public interface DownloadTaskListener {
        void onDownloadingFailed();
        void onTaskCompleted();
    }

    public Downloader() {

    }

    public void download(String url, DownloadTaskListener listener) {
        if (url.startsWith("http")) {
            DownloadJsonTask task = new DownloadJsonTask(listener);
            task.execute(url);
        } 
//        else {
//            // FIXME: hack for local JSON file
//            listener.onTaskCompleted();
//        }
    }

    class DownloadJsonTask extends AsyncTask<String, Void, Integer> {

        private DownloadTaskListener mListener;

        public DownloadJsonTask(DownloadTaskListener listener) {
            mListener = listener;
        }

        @Override
        protected Integer doInBackground(String... urls) {
            Integer resultCode = RESULT_CONNECTION_ERROR;
            InputStream stream = null;
            StringBuilder builder = new StringBuilder();
            try {
                stream = downloadUrl(urls[0]);

                // TODO: improve string reading code
                String jsonStr = null; 
                String line;    
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));       
                while ((line = reader.readLine()) != null) {
                  builder.append(line);
                }
                jsonStr = builder.toString();

                //Log.d("TAG", jsonStr);
                
                if (jsonStr != null) {
                    
                    // TODO: better handling of parsing places and parsing photos JSON files
                    if (urls[0].contains("flickr.places.getTopPlacesList")) {
                        List<Place> list = FlickrJsonParser.parsePlaces(jsonStr);
                        DataManager.getInstance().setPlaceList(list);
                    } else {
                        List<PhotoObject.Photo> list = FlickrJsonParser.parsePhotos(jsonStr);
                        DataManager.getInstance().setPhotoList(list);
                    }
                }

                resultCode = RESULT_SUCCESS;
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return resultCode;
        }
        
        @Override
        protected void onPostExecute(Integer result) {

            switch (result) {
                case RESULT_SUCCESS:
                    mListener.onTaskCompleted();
                    break;
                case RESULT_CONNECTION_ERROR:
                    mListener.onDownloadingFailed();
                    break;
                // TODO: handler unknown result code
                default:
                    break;
            }

        }

    }

//    private void loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException {
//        InputStream stream = null;
//
//        try {
//            stream = downloadUrl(urlString);
//            //channelParser.parse(stream);
//            // Makes sure that the InputStream is closed after the app is
//            // finished using it.
//        } finally {
//            if (stream != null) {
//                stream.close();
//            }
//        }
//    }

    private InputStream downloadUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        
        // Starts the query
        conn.connect();
        
        int returnCode = conn.getResponseCode();  
        if (returnCode == HttpURLConnection.HTTP_OK)  
          Log.d("TAG", "测试成功");
        else  
            Log.d("TAG", "测试失败，returnCode："+returnCode);  
        
        InputStream stream = conn.getInputStream();
        return stream;
    }

}
