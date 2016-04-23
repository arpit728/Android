package com.arpit.android.fileupload;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends ActionBarActivity {
    EditText editFile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editFile=(EditText)findViewById(R.id.editFile);
        Log.i("onCreate"," method over");

    }

    public void onButtonChooseFile(View v) {
        Log.i("click","1");
        //Create FileOpenDialog and register a callback
        SimpleFileDialog fileOpenDialog =  new SimpleFileDialog(MainActivity.this,"FileOpen..",
                new SimpleFileDialog.SimpleFileDialogListener()
                {
                    @Override
                    public void onChosenDir(String chosenDir)
                    {
                        // The code in this function will be executed when the dialog OK button is pushed
                        editFile.setText(chosenDir);
                    }
                }
        );
        Log.i("click","2");
        Log.i("click",editFile.getText().toString()+"appended");
        //You can change the default filename using the public variable "Default_File_Name"
        fileOpenDialog.default_file_name = editFile.getText().toString();
        Log.i("click","3");
        fileOpenDialog.chooseFile_or_Dir(fileOpenDialog.default_file_name);
        Log.i("click","4");


    }

    public void upload(View view)
    {
        new ExecuteTask().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    class ExecuteTask extends AsyncTask<JSONObject,Integer,JSONObject> {
        String result;
        JSONObject jsonObject;

        @Override
        protected JSONObject doInBackground(JSONObject... params) {
            String s = editFile.getText().toString();
            jsonObject = new JSONObject();
            JSONObject responseObject=null;
            try {
                jsonObject.put("tid", 1);
                jsonObject.put("cls", 8);
                jsonObject.put("sid", 1);
                jsonObject.put("content", "android");
                Log.i("jsonObject", jsonObject.toString());
                result = CommomUtilites.post("http://192.168.140.174:8080/Megabizz/webapi/notes", params[0]);
                Log.i("response", result + "blank inputstream");
                jsonObject = new JSONObject(result);
                if ((jsonObject.get("status")) == 1) {
                     responseObject=CommomUtilites.postFile("http://192.168.146.22:8080/Megabizz/webapi/notes/upload",s,jsonObject.getInt("id"));

                   /* String url = "http://192.168.146.22:8080/Megabizz/webapi/notes/upload";
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(url);
                    File file = new File(editFile.getText().toString());
                    MultipartEntity mpEntity = new MultipartEntity();
                    ContentBody cbFile = new FileBody(file, "image/jpeg");
                    StringBody stringBody=new StringBody(jsonObject.get("id")+"");
                    mpEntity.addPart("file", cbFile);
                    mpEntity.addPart("id",stringBody);
                    httpPost.setEntity(mpEntity);
                    System.out.println("executing request " + httpPost.getRequestLine());
                    HttpResponse response = httpClient.execute(httpPost);
                    HttpEntity resEntity = response.getEntity();*/

                } else {
                    Toast.makeText(getApplicationContext(), "File not uploaded", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return responseObject;
        }

        @Override
        protected void onPostExecute(JSONObject responseObject) {
            try {
                if(responseObject.getInt("status")==1)Toast.makeText(getApplicationContext(), "File Successfully uploaded", Toast.LENGTH_LONG).show();
                else Toast.makeText(getApplicationContext(), "File not uploaded", Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                e.printStackTrace();

            }
        }
    }
}
