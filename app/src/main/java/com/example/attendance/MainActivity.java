package com.example.attendance;

import android.Manifest;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
       ListView musiclist;
       String[] items;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        musiclist=(ListView)findViewById(R.id.musiclist);
        runtimepermission();
    }
    public void runtimepermission(){
          Dexter.withActivity(this)
                  .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                  .withListener(new PermissionListener() {
                      @Override
                      public void onPermissionGranted(PermissionGrantedResponse response) {
                            display();
                      }

                      @Override
                      public void onPermissionDenied(PermissionDeniedResponse response) {

                      }

                      @Override
                      public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                          token.continuePermissionRequest();
                      }
                  }).check();
    }
    public ArrayList<File> findsong(File file){
        ArrayList<File> arrayList=new ArrayList<>();
        File[] files=file.listFiles();
        for(File singlefile: files){
            if(singlefile.isDirectory() && !singlefile.isHidden()){
                arrayList.addAll(findsong(singlefile));
            }
            else{
                if(singlefile.getName().endsWith(".mp3")){
                    arrayList.add(singlefile);
                }
            }

        }
        return arrayList;
    }
    void display(){
        final ArrayList<File> mylist = findsong(Environment.getExternalStorageDirectory());
        items=new String[mylist.size()];
        for (int i=0;i<mylist.size();i++){
            items[i]=mylist.get(i).getName().replace(".mp3","");
        }
        ArrayAdapter<String> myAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,items);
        musiclist.setAdapter(myAdapter);
    }

}
