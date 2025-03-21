package com.example.farmily;

import static android.Manifest.permission.READ_MEDIA_IMAGES;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import androidx.core.app.ActivityCompat;

import android.content.pm.PackageManager;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.util.ArrayList;

import model.GitHubUtilities;

public class ListingActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener{

    private EditText editTextDescription;
    private EditText editTextProductId;
    private TextView textViewPhoto;
    private ImageView imageViewProduct;
    private EditText editTextPrice;
    private EditText editTextStock;
    private EditText editTextTitle;
    private Button buttonSubmit;

//    private String[] fileNames;

    private Spinner spPhoto;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> photoList;
    private ArrayList<String> photoPath;
    private ImageView imPhoto;
    DatabaseReference listings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_listing);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_listing), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initialize();
    }

    private void initialize() {
        imPhoto = findViewById(R.id.imageViewProduct);
        spPhoto = findViewById(R.id.spPhoto);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextProductId = findViewById(R.id.editTextProductId);
        textViewPhoto = findViewById(R.id.textViewPhoto);
        imageViewProduct = findViewById(R.id.imageViewProduct);
        editTextPrice = findViewById(R.id.editTextPrice);
        editTextStock = findViewById(R.id.editTextStock);
        editTextTitle = findViewById(R.id.editTextTitle);

        spPhoto.setOnItemSelectedListener(this);
        photoList = new ArrayList<String>();
        photoPath = new ArrayList<String>();

        listings = FirebaseDatabase.getInstance().getReference("Listings");

        ActivityCompat.requestPermissions(this,new String[]{READ_MEDIA_IMAGES}
                , PackageManager.PERMISSION_GRANTED);

        getFilesInfoFromDownloads();

        buttonSubmit = findViewById(R.id.buttonSubmit);
        buttonSubmit.setOnClickListener(this);

        this.textViewPhoto.setOnClickListener(v -> getFilesInfoFromDownloads());
    }

    @Override
    public void onClick(View v) {

    }

    public void getFilesInfoFromDownloads() {
        File downloadDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        if (downloadDirectory.exists() && downloadDirectory.isDirectory()) {
            File[] files = downloadDirectory.listFiles();
            if (files != null && files.length > 0) {
                populateSpinner(files);
            } else {
                Log.d("FILES", "No files found in the Download Folder");
            }
        } else {
            Log.d("FILES", "Download directory does not exist");
        }
    }

    private void populateSpinner(File[] files) {
        for (File file:files) {
            photoList.add(file.getName());
            photoPath.add(file.getAbsoluteFile().getAbsolutePath());
        }

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item,photoList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPhoto.setAdapter(adapter);
    }

    private void uploadPhoto(View view) {

        String photo = spPhoto.getSelectedItem().toString();
        GitHubUtilities.uploadFileToGithub(this, photo);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String photo = spPhoto.getSelectedItem().toString();
        String photoFile = photoPath.get(position);
        Bitmap bitmapPhoto = BitmapFactory.decodeFile(photoFile);
        imPhoto.setImageBitmap(bitmapPhoto);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}