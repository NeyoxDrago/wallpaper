package app.sample.app.wallpaper;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Uploadfile extends AppCompatActivity {

    ImageView image;
    Button browse , upload;
    public static final int GALLERY_CODE =100;
    StorageReference mstorage;
    DatabaseReference mdef;

    Uri filepath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploadfile);

        image = findViewById(R.id.uploadimage);
        browse = findViewById(R.id.browse);
        upload = findViewById(R.id.upload);
        upload.setEnabled(false);

        mstorage = FirebaseStorage.getInstance().getReference();
        mdef = FirebaseDatabase.getInstance().getReference().child("Category");


        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent();
                gallery.setAction(Intent.ACTION_GET_CONTENT);
                gallery.setType("image/");
                startActivityForResult(Intent.createChooser(gallery, "Choose an Image "), GALLERY_CODE);
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog p = new ProgressDialog(Uploadfile.this);
                p.setMessage("Uploading");


                if (filepath != null) {

                    final Dialog d = new Dialog(Uploadfile.this);
                    d.setContentView(R.layout.uploadialog);
                    d.setCancelable(false);
                    d.show();

                    final String[] orient = new String[]{"potrait","landscape"};
                    ListView list = d.findViewById(R.id.orientationlist);
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(d.getContext() , android.R.layout.simple_list_item_1 , orient);
                    list.setAdapter(adapter);

                    final String[] Orientation = {""};
                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            d.dismiss();
                            p.show();

                            Orientation[0] = orient[position];

                            final StorageReference file = mstorage.child(FirebaseAuth.getInstance().getCurrentUser().getEmail())
                                    .child(UUID.randomUUID().toString());

                            file.putFile(filepath)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            final String[] s = {""};
                                            file.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    s[0] = uri.toString();
                                                    final Map<String, Object> map = new HashMap<>();
                                                    map.put("ImageUrl", s[0]);
                                                    map.put("Name", "wallPaper");
                                                    map.put("count", "1");
                                                    map.put("orientation", Orientation[0]);

                                                    mdef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                            String key = "";
                                                            for(DataSnapshot i :dataSnapshot.getChildren())
                                                            {
                                                                key = i.getKey();
                                                            }

                                                            mdef.child((Integer.parseInt(key)+1)+"").setValue(map);
                                                            p.dismiss();
                                                            Intent a = new Intent(Uploadfile.this , MainActivity.class);
                                                            startActivity(a);
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                            Toast.makeText(Uploadfile.this, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                                            p.dismiss();


                                                        }
                                                    });


                                                }
                                            });


                                        }
                                    })
                                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                            p.setMessage(taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount() + "% content Uploaded");
                                        }
                                    });
                        }
                    });

                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == GALLERY_CODE && resultCode == RESULT_OK && data!=null && data.getData()!=null)
        {
            filepath = data.getData();
            try{

               Bitmap b =  MediaStore.Images.Media.getBitmap(getContentResolver() , filepath);
               image.setImageBitmap(b);
               upload.setEnabled(true);

            }catch(Exception e){}
        }

    }
}

