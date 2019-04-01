package com.example.storagecp;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExploreActivity extends AppCompatActivity implements MyAdapter.OnItemClickListener {

    List<File> list;
    RecyclerView recyclerView;
    MyAdapter adapter;
    File dir;
    String currentDir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);

        dir = Environment.getExternalStorageDirectory();
        currentDir = dir.getAbsolutePath();

        recyclerView = findViewById(R.id.rv_Explorer);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = Arrays.asList(dir.listFiles());      // Returns an array of abstract pathnames denoting the files in the directory


        adapter = new MyAdapter(list, this);


        recyclerView.setAdapter(adapter);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);      // Set whether home should be displayed as an "up" affordance.
    }


    @Override
    public void onItemClick(String filePath, File file) {
        if (file.isDirectory()) {
            currentDir = filePath;
            list = Arrays.asList(file.listFiles());
            if (list != null) {
                if (list.size() != 0) {
                    adapter = new MyAdapter(list, this);
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(this, file.getName() + " is Empty!", Toast.LENGTH_SHORT).show();
                }
            }
        } else {                                                        // if it is a file
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            String mimeType = getMimeType(currentDir);
            Uri apkURI = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", file);
            intent.setDataAndType(apkURI, mimeType);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, "No handler for this type of file.", Toast.LENGTH_LONG).show();
            }
        }
    }

    public String getMimeType(String url) {
        String mimeType = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return mimeType;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (!currentDir.equals(dir.getAbsolutePath())) {
                    currentDir = new File(currentDir).getParent();
                    File parentFile = new File(currentDir);
                    list = Arrays.asList(parentFile.listFiles());
                    adapter.setFiles(list);
                    return true;
                } else {
                    super.onBackPressed();
                }

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
