package tw.nekomimi.nekogram.helpers;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import org.telegram.messenger.ApplicationLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PhotoPickerHelper {

    public static final int REQUEST_CODE_SYSTEM_PICKER = 25;

    public static boolean isSystemPhotoPickerAvailable() {
        Context context = ApplicationLoader.applicationContext;
        if (context == null) return false;
        if (Build.VERSION.SDK_INT >= 33) {
            return true;
        }
        if (Build.VERSION.SDK_INT >= 30) {
            Intent intent = new Intent(MediaStore.ACTION_PICK_IMAGES);
            PackageManager pm = context.getPackageManager();
            return pm != null && intent.resolveActivity(pm) != null;
        }
        return false;
    }

    public static Intent createVisualMediaIntent(boolean allowVideo, int maxItems) {
        Context context = ApplicationLoader.applicationContext;
        PackageManager pm = context != null ? context.getPackageManager() : null;

        if (isSystemPhotoPickerAvailable()) {
            Intent intent = new Intent(MediaStore.ACTION_PICK_IMAGES);
            if (!allowVideo) {
                intent.setType("image/*");
            }
            if (maxItems > 1) {
                int maxLimit = 100;
                try {
                    maxLimit = Math.min(maxItems, MediaStore.getPickImagesMaxLimit());
                } catch (Throwable ignore) {
                }
                intent.putExtra(MediaStore.EXTRA_PICK_IMAGES_MAX, maxLimit);
            }
            if (pm != null && intent.resolveActivity(pm) != null) {
                return intent;
            }
        }

        Intent fallback = new Intent(Intent.ACTION_GET_CONTENT);
        if (!allowVideo) {
            fallback.setType("image/*");
        } else {
            fallback.setType("*/*");
            fallback.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"image/*", "video/*"});
        }
        if (maxItems > 1) {
            fallback.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        }
        return fallback;
    }

    public static List<Uri> getPickedUris(Intent data) {
        List<Uri> uris = new ArrayList<>();
        if (data == null) {
            return uris;
        }
        if (data.getClipData() != null) {
            ClipData clipData = data.getClipData();
            int count = clipData.getItemCount();
            for (int i = 0; i < count; i++) {
                ClipData.Item item = clipData.getItemAt(i);
                if (item != null && item.getUri() != null) {
                    uris.add(item.getUri());
                }
            }
        } else if (data.getData() != null) {
            uris.add(data.getData());
        }
        return uris;
    }

    public static boolean isVideoUri(Uri uri) {
        if (uri == null) return false;
        try {
            Context context = ApplicationLoader.applicationContext;
            if (context != null) {
                String type = context.getContentResolver().getType(uri);
                if (type != null && type.startsWith("video/")) {
                    return true;
                }
            }
        } catch (Throwable ignore) {
        }
        String path = uri.getPath();
        if (path != null) {
            String lower = path.toLowerCase(Locale.ROOT);
            return lower.endsWith(".mp4") || lower.endsWith(".mkv") || lower.endsWith(".mov") || lower.endsWith(".avi") || lower.endsWith(".webm") || lower.endsWith(".3gp");
        }
        return false;
    }
}
