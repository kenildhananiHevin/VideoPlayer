package videoplayer.mediaplayer.hd.other;

import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class VideoCopy {



    public static void copyVideo(String sourcePath, String destinationPath) {
        try {
            File sourceFile = new File(sourcePath);
            File destinationFile = new File(destinationPath);

            // Create parent directories if they don't exist
            destinationFile.getParentFile().mkdirs();

            FileChannel sourceChannel = new FileInputStream(sourceFile).getChannel();
            FileChannel destinationChannel = new FileOutputStream(destinationFile).getChannel();

            // Transfer data from source to destination
            destinationChannel.transferFrom(sourceChannel, 0, sourceChannel.size());

            // Close channels
            sourceChannel.close();
            destinationChannel.close();

            Log.d("TAGGH", "Video copied successfully. ");

        } catch (IOException e) {
            e.printStackTrace();
            Log.d("TAGGH", "Error while copying video: "+e.getMessage());
        }
    }
}
