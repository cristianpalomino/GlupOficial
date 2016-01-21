package pe.com.glup.utils;

import android.util.Log;

import org.apache.http.util.ByteArrayBuffer;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Glup on 6/01/16.
 */
public class ImageManager {
    private String PATH = "/mnt/sdcard/GlupFiles/textures/";
    public ImageManager(){
    }

    public String DownloadFromUrl(String imageURL,String filename,boolean isTextura){
        String miTiempo=":-1";
        try{
            if (isTextura){
                PATH="/mnt/sdcard/GlupFiles/textures/";
            }else{
                PATH= "/mnt/sdcard/GlupFiles/objFiles/";
            }
            File directory=new File(PATH);
            directory.mkdirs();
            URL url=new URL(imageURL);
            File file=new File(PATH+filename);
            long startTime = System.currentTimeMillis();
            Log.e("ImageManager", "download begining");
            Log.e("ImageManager", "download url:" + url);
            Log.e("ImageManager", "downloaded file name:" + filename);

            URLConnection ucon= url.openConnection();

            InputStream is = ucon.getInputStream();
            BufferedInputStream bis=new BufferedInputStream(is);

            ByteArrayBuffer baf= new ByteArrayBuffer(50);
            int current = 0;
            while ((current = bis.read()) != -1){
                baf.append((byte)current);
            }

            FileOutputStream fos= new FileOutputStream(file);
            fos.write(baf.toByteArray());
            fos.close();
            miTiempo=":"+ String.valueOf(System.currentTimeMillis() - startTime);
            Log.e("ImageManager", "download ready in" +
                    ((System.currentTimeMillis() - startTime) / 1000) + " sec");

        }catch (IOException e){

            Log.e("ImageManager", "Error" + e);
        }

        return PATH+filename+miTiempo;
    }



}
