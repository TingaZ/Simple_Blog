package com.example.android.simpleblog;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class PostActivity extends AppCompatActivity {
    private final int GALLERY_REQUEST = 1;
    private ImageButton mSelect;
    private EditText mTitle;
    private EditText mDescription;
    private ImageButton mPost;
    private Uri mImageUri = null;
    private StorageReference mStorage;

    private DatabaseReference mDatabase;

    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        mStorage = FirebaseStorage.getInstance().getReference();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Blob");

        mProgress = new ProgressDialog(this);

        mSelect = (ImageButton) findViewById(R.id.imageButton_select);
        mTitle = (EditText) findViewById(R.id.editText_title);
        mDescription = (EditText) findViewById(R.id.editText_description);
        mPost = (ImageButton) findViewById(R.id.button_submit);

        mSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);

                galleryIntent.setType("image/*");

                startActivityForResult(galleryIntent, GALLERY_REQUEST);

            }
        });

        mPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startPosting();

            }
        });
    }

    private void startPosting() {

        mProgress.setMessage("Posting to Blog ...");


        final String title_value = mTitle.getText().toString().trim();
        final String desc_value = mDescription.getText().toString().trim();

        if (!TextUtils.isEmpty(title_value) && !TextUtils.isEmpty(desc_value) && mImageUri != null) {

            mProgress.show();

            StorageReference filePath = mStorage.child("Blob_Images").child(mImageUri.getLastPathSegment());

            filePath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Uri downloadUrl = taskSnapshot.getDownloadUrl();

                    DatabaseReference newPost = mDatabase.push();
                    newPost.child("title").setValue(title_value);
                    newPost.child("desc").setValue(desc_value);
                    newPost.child("image").setValue(downloadUrl.toString());

                    mProgress.dismiss();

                    Toast.makeText(PostActivity.this, "Successfully Posted..", Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(PostActivity.this, MainActivity.class));

                }
            });

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {

            mImageUri = data.getData();

            mSelect.setImageURI(mImageUri);
        }
    }
}
