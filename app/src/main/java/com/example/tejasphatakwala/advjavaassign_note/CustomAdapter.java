package com.example.tejasphatakwala.advjavaassign_note;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class CustomAdapter extends BaseAdapter {

    Context context;
    private static LayoutInflater inflater=null;

    Note[] Notes;


    public CustomAdapter(Context mainActivity, Note[] nn) {
        // TODO Auto-generated constructor stub
        Notes = nn;
        context = mainActivity;
        inflater = (LayoutInflater)context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public class Holder {
        ImageView img;
        TextView Subject;
        TextView Title;
        TextView Description;
        TextView CreatedOn;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return Notes.length;
        //return cours.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View rowView;
        rowView = inflater.inflate(R.layout.table_layout, null);

        Holder holder=new Holder();
        holder.Subject = (TextView) rowView.findViewById(R.id.Subject);
        holder.Title = (TextView) rowView.findViewById(R.id.Title);
        holder.Description = (TextView) rowView.findViewById(R.id.Description);
        holder.CreatedOn = (TextView) rowView.findViewById(R.id.CreatedOn);
        holder.img = (ImageView) rowView.findViewById(R.id.img);


        holder.Subject.setText(Notes[position].Subject);
        holder.Title.setText(Notes[position].Title);
        holder.Description.setText(Notes[position].Description);
        holder.CreatedOn.setText(Notes[position].CreatedDate);

        try {
            String photoPath = Environment.getExternalStorageDirectory() + "/" + Notes[position].NoteId.replace("-", "") +".jpg";

            System.out.println(photoPath);

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(photoPath, options);

            holder.img.setImageBitmap(bitmap);

        } catch (Exception e) {
            e.printStackTrace();
        }
        
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Intent intent = new Intent(context, ViewActivity.class);
                intent.putExtra("NoteID", Notes[position].NoteId);

                v.getContext().startActivity(intent);
            }
        });
        return rowView;
    }
}