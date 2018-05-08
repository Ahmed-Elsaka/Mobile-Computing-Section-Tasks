package com.example.killua.accelerometer_app;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by killua on 4/28/18.
 */

public class WriteInMemory {



    public static void Save(String fileName, String data, Context ctx )
    {

       // Toast.makeText(ctx, "hi iam in writeFile", Toast.LENGTH_LONG).show();
          String path = Environment.getExternalStorageDirectory().getAbsolutePath()+
                  "/ElsakaSensors/"
                  + fileName+".txt";

         // Toast.makeText(ctx, path, Toast.LENGTH_LONG).show();

          File file = new File(path);
        FileOutputStream fos = null;
        try
        {
            fos = new FileOutputStream(file , true);
        }
        catch (FileNotFoundException e) {e.printStackTrace();}
        try
        {
            try
            {

                fos.write(data.getBytes());
                fos.write("\n".getBytes());


            }
            catch (IOException e) {e.printStackTrace();}
        }
        finally
        {
            try
            {
                fos.close();
            }
            catch (IOException e) {e.printStackTrace();}
        }
    }

}
