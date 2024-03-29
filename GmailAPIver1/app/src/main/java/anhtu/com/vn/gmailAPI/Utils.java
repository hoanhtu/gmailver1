package anhtu.com.vn.gmailAPI;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.widget.EditText;

import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;

public class Utils {

    public static final int REQUEST_ACCOUNT_PICKER = 1000;
    public static final int REQUEST_AUTHORIZATION = 1001;
    public static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    public static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;

    public static boolean isNotEmpty(EditText editText) {
        return editText.getText().toString().trim().length() > 0;
    }

    @NonNull
    public static String getString(EditText editText) {
        return editText.getText().toString().trim();
    }

    public static boolean checkPermission(Context context, String permission) {
        if (isMarshmallow()) {
            int result = ContextCompat.checkSelfPermission(context, permission);
            return result == PackageManager.PERMISSION_GRANTED;
        } else {
            return true;
        }
    }

    public static boolean isMarshmallow() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    public static String base64UrlDecode(String input) {
        if(input != null && !input.isEmpty()) {
            String result = null;
            Base64 decoder = new Base64(true);
            byte[] decodedBytes = decoder.decode(input);
            result = new String(decodedBytes);
            return result;
        }
        return null;
    }



}
