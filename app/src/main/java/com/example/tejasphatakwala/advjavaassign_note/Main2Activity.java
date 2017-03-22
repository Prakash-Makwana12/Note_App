package com.example.tejasphatakwala.advjavaassign_note;

import android.app.AlertDialog;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main2Activity extends AppCompatActivity {

    public static int spinnerSelect;
    boolean spinnerActive = false;
    String[] category;
    String  Note_Id = UUID.randomUUID().toString();

    Button play, stop, record, pick;
    private MediaRecorder myAudioRecorder;
    private String outputFile = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        final Spinner spinnerList = (Spinner) findViewById(R.id.spinner);

        DBHealper db = new DBHealper(this);

        category = db.getAllcategory();

        if(category.length > 0) {
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, category);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerList.setAdapter(spinnerArrayAdapter);
        }
        else
        {
            db.insertCategory("Study");
            db.insertCategory("Work");
            db.insertCategory("Sports");
            db.insertCategory("Travel");

            category = db.getAllcategory();

            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, category);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerList.setAdapter(spinnerArrayAdapter);
        }

        spinnerList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerSelect = position;
                spinnerActive = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                spinnerActive = false;
            }
        });

        Button addProduct = (Button) findViewById(R.id.AddProduct);

        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spinnerActive) {
                    EditText title = (EditText) findViewById(R.id.Title);
                    EditText description = (EditText) findViewById(R.id.Description);
                    String sub = category[spinnerSelect];

                    if(title.getText().toString().length() > 0 && description.getText().toString().length() > 0 &&
                            sub.toString().length() > 0 )
                    {
                        Date dt = new Date();
                        int hours = dt.getHours();
                        int minutes = dt.getMinutes();
                        int seconds = dt.getSeconds();

                        Calendar c = Calendar.getInstance();
                        System.out.println("Current time => "+c.getTime());

                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String curTime = df.format(c.getTime());

                        Note p = new Note(Note_Id,sub, title.getText().toString(), description.getText().toString(),curTime);
                        DBHealper db = new DBHealper(Main2Activity.this);
                        db.insertData(p);

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                        startActivity(intent);
                    }
                    else
                    {
                        AlertDialog.Builder alertObj = new AlertDialog.Builder(Main2Activity.this);
                        alertObj.setMessage("All fields are mandatory");
                        alertObj.setNegativeButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        alertObj.show();
                    }
                }
                else
                {
                    AlertDialog.Builder alertObj = new AlertDialog.Builder(Main2Activity.this);
                    alertObj.setMessage("Select Product Image");
                    alertObj.setNegativeButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    alertObj.show();
                }
            }
        });

        Button b1=(Button)findViewById(R.id.buttoncamere);
        Button b3=(Button)findViewById(R.id.BtnGallery);
        ImageView iv=(ImageView)findViewById(R.id.imageView);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 0);
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });

        play = (Button) findViewById(R.id.btnPlay);
        stop = (Button) findViewById(R.id.btnstop);
        record = (Button) findViewById(R.id.btnRecord);
        pick=(Button)findViewById(R.id.PickAudio);

        stop.setEnabled(false);
        play.setEnabled(false);
        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + Note_Id.replace("-", "") + ".3gp";

        myAudioRecorder = new MediaRecorder();
        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        myAudioRecorder.setOutputFile(outputFile);

        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    myAudioRecorder.prepare();
                    myAudioRecorder.start();
                } catch (IllegalStateException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                record.setEnabled(false);
                stop.setEnabled(true);

                Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_LONG).show();
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myAudioRecorder.stop();
                myAudioRecorder.release();
                myAudioRecorder = null;

                stop.setEnabled(false);
                play.setEnabled(true);

                Toast.makeText(getApplicationContext(), "Audio recorded successfully", Toast.LENGTH_LONG).show();
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
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

        pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) throws IllegalArgumentException, SecurityException, IllegalStateException {
                Intent intent_upload = new Intent();
                intent_upload.setType("audio/*");
                intent_upload.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent_upload,2);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            ImageView iv = (ImageView) findViewById(R.id.imageView);
            if (requestCode == 1) {
                //Bitmap bp = (Bitmap) data.getExtras().get("data");

                //iv.setImageBitmap(bp);
                try {
                    InputStream inputStream = this.getApplicationContext().getContentResolver().openInputStream(data.getData());

                    Bitmap bp = BitmapFactory.decodeStream(inputStream);

                    String path = Environment.getExternalStorageDirectory().toString();
                    OutputStream fOut = null;
                    File file = new File(path, Note_Id.replace("-", "") + ".jpg"); // the File to save to
                    System.out.println(file.getName());
                    fOut = new FileOutputStream(file);

                    Bitmap pictureBitmap = bp; // obtaining the Bitmap
                    pictureBitmap.compress(Bitmap.CompressFormat.JPEG, 85, fOut); // saving the Bitmap to a file compressed as a JPEG with 85% compression rate
                    fOut.flush();
                    fOut.close(); // do not forget to close the stream

                    MediaStore.Images.Media.insertImage(getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    String photoPath = Environment.getExternalStorageDirectory() + "/" + Note_Id.replace("-", "") + ".jpg";

                    System.out.println(photoPath);

                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                    Bitmap bitmap = BitmapFactory.decodeFile(photoPath, options);

                    iv.setImageBitmap(bitmap);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 0) {

                Bitmap bp = (Bitmap) data.getExtras().get("data");
                //iv.setImageBitmap(bp);
                try {
                    String path = Environment.getExternalStorageDirectory().toString();
                    OutputStream fOut = null;
                    File file = new File(path, Note_Id.replace("-", "") + ".jpg"); // the File to save to
                    System.out.println(file.getName());
                    fOut = new FileOutputStream(file);

                    Bitmap pictureBitmap = bp; // obtaining the Bitmap
                    pictureBitmap.compress(Bitmap.CompressFormat.JPEG, 85, fOut); // saving the Bitmap to a file compressed as a JPEG with 85% compression rate
                    fOut.flush();
                    fOut.close(); // do not forget to close the stream

                    MediaStore.Images.Media.insertImage(getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    String photoPath = Environment.getExternalStorageDirectory() + "/" + Note_Id.replace("-", "") + ".jpg";

                    System.out.println(photoPath);

                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                    Bitmap bitmap = BitmapFactory.decodeFile(photoPath, options);

                    iv.setImageBitmap(bitmap);


                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (requestCode == 2) {
                //iv.setImageBitmap(bp);
                try {

                    InputStream in = this.getApplicationContext().getContentResolver().openInputStream(data.getData());


                    OutputStream out = new FileOutputStream(outputFile);

                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = in.read(buf)) > 0)
                        out.write(buf, 0, len);
                    in.close();
                    out.close();


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
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(Main2Activity.this, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }
}
