package de.juanah.trackingformybullentime.io;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

/**
 * Schreibt daten in den Speicher oder liest ihn aus
 * @author jonas
 *
 */
public class FileHelper {

	//Class Variables
	 private static final String TAG = "FileHelper";
	
	
	public static boolean WriteData(Context context,DataObject data)
	{
		try {
			
			String oldString = readFromFile(context,data.getFilename());
			
			String dataString = oldString + data.getKey() + "=" + data.getValue() + ";" ;
			
			return writeToFile(context, dataString, data.getFilename());
			
		} catch (Exception e) {
			Log.e(TAG, "File write failed: " + e.toString());
			return false;
		}
		
	}
	
	public static ArrayList<DataObject> GetDataObjects(Context con,String filename)
	{
		ArrayList<DataObject> objects = new ArrayList<DataObject>();
		try {
			String dataString = readFromFile(con, filename);
			
			String[] dataEntries = dataString.split(";");
			
			for (String entry : dataEntries) {
				
				String[] values = entry.split("=");
				
				objects.add(new DataObject(values[0], values[1], filename));
				
			}
			return objects;
			
		} catch (Exception e) {
			Log.e(TAG, "File read failed: " + e.toString());
			return objects;
		}
	}
	
	//getApplicationContext.openFileOutput(
    //filename, getActivity().MODE_PRIVATE);
	
    private static boolean writeToFile(Context con ,String data,String filename) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(con.openFileOutput(filename, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
            return true;
        }
        catch (IOException e) {
            Log.e(TAG, "File write failed: " + e.toString());
            return false;
        }
         
    }
	
    private static String readFromFile(Context con,String filename) {

        String ret = "";

        try {
            InputStream inputStream = con.openFileInput(filename);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e(TAG, "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e(TAG, "Can not read file: " + e.toString());
        }

        return ret;
    }
	
	public static boolean isExternalStorageWritable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state)) {
	        return true;
	    }
	    return false;
	}

	public static boolean isExternalStorageReadable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state) ||
	        Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
	        return true;
	    }
	    return false;
	}
	
	
}
