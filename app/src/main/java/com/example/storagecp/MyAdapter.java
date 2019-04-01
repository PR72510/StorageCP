package com.example.storagecp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private List<File> files;
    private OnItemClickListener onItemClickListener;

    public MyAdapter(List<File> files, OnItemClickListener onItemClickListener) {
        this.files = files;
        this.onItemClickListener = onItemClickListener;
    }

    public void setFiles(List<File> files) {
        this.files = files;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_item, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
       final File file = files.get(i);
        myViewHolder.fileName.setText(file.getName());
        if(file.isFile()){
            myViewHolder.fileIcon.setImageResource(R.drawable.file);
        }else if(file.isDirectory()){
            myViewHolder.fileIcon.setImageResource(R.drawable.folder);
        }

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("lol", "clicked");
                onItemClickListener.onItemClick(file.getAbsolutePath(),file);
            }
        });
    }

    @Override
    public int getItemCount() {
        return files.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView fileName;
        ImageView fileIcon;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            fileName = itemView.findViewById(R.id.tv_file);
            fileIcon = itemView.findViewById(R.id.img_Icon);
        }
    }
    public interface OnItemClickListener {
        void onItemClick(String filePath, File file);
    }
}
