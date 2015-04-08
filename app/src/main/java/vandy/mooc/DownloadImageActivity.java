package vandy.mooc;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;


/**
 * An Activity that downloads an image, stores it in a local file on
 * the local device, and returns a Uri to the image file.
 */
public class DownloadImageActivity extends Activity {
    /**
     * Debugging tag used by the Android logger.
     */
    private final String TAG = getClass().getSimpleName();

    /**
     * Hook method called when a new instance of Activity is created.
     * One time initialization code goes here, e.g., UI layout and
     * some class scope variable initialization.
     *
     * @param savedInstanceState object that contains saved state information.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Always call super class for necessary
        // initialization/implementation.
        super.onCreate(savedInstanceState);

        // Get the URL associated with the Intent data.
        Intent intent = getIntent();
        final Uri url = Uri.parse(intent.getStringExtra("url"));

        // Download the image in the background, create an Intent that
        // contains the path to the image file, and set this as the
        // result of the Activity.

        Thread background = new Thread(new Runnable() {
            @Override
            public void run() {
                final Uri downloadImg = DownloadUtils.downloadImage(getBaseContext(), url);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = DownloadImageActivity.this.getIntent();
                        if(downloadImg!= null) {
                            intent.putExtra("uri", downloadImg.toString());
                            DownloadImageActivity.this.setResult(RESULT_OK, intent);
                        }
                        else{
                            DownloadImageActivity.this.setResult(RESULT_CANCELED, intent);
                        }
                        finish();
                    }
                });
            }
        });
        background.start();
    }
}
