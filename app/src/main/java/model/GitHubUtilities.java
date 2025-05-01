package model;

import static android.content.Context.STORAGE_SERVICE;

import android.content.Context;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.util.Log;

import org.kohsuke.github.GHContentBuilder;
import org.kohsuke.github.GHContentUpdateResponse;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;


public class GitHubUtilities {

    private static final String GITHUB_TOKEN = "";
    private static final String GITHUB_OWNER = "Andres94b";
    private static final String GITHUB_REPO = "FarmilyImages";
    private static final String GITHUB_PHOTO_FOLDER = "Images";


    public static void uploadFileToGithub(Context context,String photo){

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    StorageManager storageManager = (StorageManager)context.getSystemService(STORAGE_SERVICE);
                    StorageVolume storageVolume = storageManager.getStorageVolumes().get(0);
                    File file = new File(storageVolume.getDirectory().getPath()+"/Download/"+photo);
                    GitHub gitHub = new GitHubBuilder().withOAuthToken(GITHUB_TOKEN).build();
                    GHRepository ghRepository = null;
                    ghRepository = gitHub.getRepository(GITHUB_OWNER+"/"+GITHUB_REPO);
                    GHContentBuilder ghContentBuilder = ghRepository.createContent();
                    byte[] bytes= Files.readAllBytes(file.toPath());
                    String path = file.toPath().getFileName().toString();
                    ghContentBuilder.content(bytes);
                    ghContentBuilder.branch("main");
                    ghContentBuilder.message("commit 1");
                    ghContentBuilder.path(GITHUB_PHOTO_FOLDER+"/"+photo);
                    GHContentUpdateResponse ghContentUpdateResponse = ghContentBuilder.commit();
                    Log.d("RESULT",ghContentUpdateResponse.toString());
                } catch (IOException e) {
                    Log.d("RESULT",e.getMessage());

                }
            }
        });
        thread.start();
    }
}