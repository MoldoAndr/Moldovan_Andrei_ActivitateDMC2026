package labs.mta.lab9;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PhotosActivity extends AppCompatActivity {

    private List<ImageItem> imageItems;
    private ImageAdapter adapter;
    private final ExecutorService executorService = Executors.newFixedThreadPool(4);
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_photos);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initData();
        configureList();
        downloadImages();
    }

    private void initData() {
        imageItems = new ArrayList<>();
        imageItems.add(new ImageItem("Marea Neagră", "NASA view of Black Sea", 
                "https://upload.wikimedia.org/wikipedia/commons/thumb/4/48/Black-Sea-NASA.jpg/500px-Black-Sea-NASA.jpg", 
                "https://ro.wikipedia.org/wiki/Pe%C8%99tii_din_Marea_Neagr%C4%83"));
        imageItems.add(new ImageItem("Marea Baltică", "Beach in Svetlogorsk", 
                "https://upload.wikimedia.org/wikipedia/commons/thumb/9/95/SvetlogorskRauschen_05-2017_img11_beach.jpg/500px-SvetlogorskRauschen_05-2017_img11_beach.jpg", 
                "https://ro.wikipedia.org/wiki/Marea_Baltic%C4%83"));
        imageItems.add(new ImageItem("Marea Moartă", "Dead Sea view",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/2/21/Dead_Sea_by_David_Shankbone.jpg/500px-Dead_Sea_by_David_Shankbone.jpg",
                "https://ro.wikipedia.org/wiki/Marea_Moart%C4%83"));
        imageItems.add(new ImageItem("Oceanul Pacific", "Sunset Marina",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/9/90/Sunset_Marina.JPG/960px-Sunset_Marina.JPG", 
                "https://ro.wikipedia.org/wiki/Oceanul_Pacific"));
        imageItems.add(new ImageItem("Marea Banda", "Drone view",
                "https://encrypted-tbn0.gstatic.com/licensed-image?q=tbn:ANd9GcRlvNhRmKy-3KErVOK-HL9i_K2rCn6ev7HNSCNv4WiAercc4Gjv9m2DXvlRoN44YiI2Yc3c4AYFpHmYPAXswgKE8pAd&s=19",
                "https://ro.wikipedia.org/wiki/Marea_Banda"));
    }

    private void configureList() {
        ListView photosListView = findViewById(R.id.photosList);
        adapter = new ImageAdapter(this, imageItems);
        photosListView.setAdapter(adapter);

        photosListView.setOnItemClickListener((parent, view, position, id) -> {
            ImageItem clickedItem = imageItems.get(position);
            Intent intent = new Intent(PhotosActivity.this, DetailWebViewActivity.class);
            intent.putExtra("url", clickedItem.getDetailUrl());
            startActivity(intent);
        });
    }

    private void downloadImages() {
        for (ImageItem item : imageItems) {
            executorService.execute(() -> {
                Bitmap bitmap = downloadBitmap(item.getImageUrl());
                if (bitmap != null) {
                    mainHandler.post(() -> {
                        item.setBitmap(bitmap);
                        adapter.notifyDataSetChanged();
                    });
                } else {
                    Log.e("PhotosActivity", "Failed to download image: " + item.getImageUrl());
                }
            });
        }
    }

    private static final int TARGET_WIDTH = 500;
    private static final int TARGET_HEIGHT = 500;

    private Bitmap downloadBitmap(String urlString) {
        HttpURLConnection connection = null;
        InputStream input = null;
        byte[] bytes;
        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setInstanceFollowRedirects(true);
            connection.connect();

            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                Log.e("PhotosActivity", "HTTP error " + responseCode + " for URL: " + urlString);
                return null;
            }

            input = connection.getInputStream();
            bytes = readBytes(input);
            input.close();
            input = null;
        } catch (Exception e) {
            Log.e("PhotosActivity", "Error downloading " + urlString, e);
            return null;
        } finally {
            if (input != null) { try { input.close(); } catch (Exception ignored) {} }
            if (connection != null) { connection.disconnect(); }
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
        options.inSampleSize = calculateInSampleSize(options, TARGET_WIDTH, TARGET_HEIGHT);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
    }

    private byte[] readBytes(InputStream input) throws java.io.IOException {
        java.io.ByteArrayOutputStream buffer = new java.io.ByteArrayOutputStream();
        byte[] data = new byte[8192];
        int n;
        while ((n = input.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, n);
        }
        return buffer.toByteArray();
    }

    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            int halfHeight = height / 2;
            int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}
