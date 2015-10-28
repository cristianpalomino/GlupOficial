package pe.com.glup.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Random;

/**
 * Created by usuario on 23/04/15.
 */
public class ImageUtils {
    private static int ntomas=0;

    public static int getNtomas() {
        return ntomas;
    }

    public static void setNtomas(int ntomas) {
        ImageUtils.ntomas = ntomas;
    }

    public static byte[] toBytes(Bitmap bitmap) {
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bs);
        return bs.toByteArray();
    }

    public static Bitmap toBitmap(byte[] bytes) {
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int degress) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degress);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
        Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
        return rotatedBitmap;
    }

    public static String SaveImage(Bitmap finalBitmap) {
        Bitmap bitmap = finalBitmap.createScaledBitmap(finalBitmap, 850, 850, false);
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/GlupFotos");
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        /*setNtomas(getNtomas()+1);
        Log.e("contTomas", getNtomas() + "");*/
        String fname = "GlupImage" + n + ".PNG";

        File file = new File(myDir, fname);
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,out);
            out.flush();
            out.close();
            return file.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return "NONE";
        }
    }
    public static String renameFile(){

        return null;
    }

    public static String saveFile(String log) {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/GlupFotos");
        myDir.mkdirs();
        String fname = "LOG.json";
        File file = new File(myDir, fname);
        if (file.exists()) file.delete();
        try {
            PrintWriter out = new PrintWriter(file);
            out.println(log);
            out.close();
            return file.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return "NONE";
        }
    }

}
