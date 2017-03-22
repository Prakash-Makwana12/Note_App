package com.example.tejasphatakwala.advjavaassign_note;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;


public class ViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        Intent intent = getIntent();
        String NoteID =  intent.getStringExtra("NoteID");

        final DBHealper d = new DBHealper(this);

        final Note[] ViewNote = d.getNotesByID(NoteID);
        System.out.println(ViewNote.length + " retrived");

        TextView sub = (TextView) findViewById(R.id.txtsubject);
        TextView tit = (TextView) findViewById(R.id.txtTitle);
        TextView des = (TextView) findViewById(R.id.txtdesc);
        ImageView im = (ImageView) findViewById(R.id.imageView2);
        Button bt = (Button) findViewById(R.id.btnPlayAudio);
        TextView crea = (TextView) findViewById(R.id.txtCreatedon);


        sub.setText(ViewNote[0].Subject);
        tit.setText(ViewNote[0].Title);
        des.setText(ViewNote[0].Description);
        crea.setText(ViewNote[0].CreatedDate);

        try {
            String photoPath = Environment.getExternalStorageDirectory() + "/" + ViewNote[0].NoteId.replace("-", "") +".jpg";

            System.out.println(photoPath);

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(photoPath, options);

            im.setImageBitmap(bitmap);

        } catch (Exception e) {
            e.printStackTrace();
        }

        final String outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + ViewNote[0].NoteId.replace("-", "") + ".3gp";

        bt.setOnClickListener(new  View.OnClickListener() {
            @Override
            public void onClick(View v) throws IllegalArgumentException, SecurityException, IllegalStateException {
                MediaPlayer m = new MediaPlayer();

                try {
                    m.setDataSource(outputFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    m.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                m.start();
                Toast.makeText(getApplicationContext(), "Playing audio", Toast.LENGTH_LONG).show();
            }
        });




    }
}
