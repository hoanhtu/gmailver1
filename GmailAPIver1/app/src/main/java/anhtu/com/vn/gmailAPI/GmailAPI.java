package anhtu.com.vn.gmailAPI;

import android.Manifest;
import android.content.Context;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.gmail.GmailScopes;

import java.util.ArrayList;
import java.util.Arrays;


public class GmailAPI {
    public static GoogleSignInClient mGoogleSignInClient = null;
    public static GoogleApiClient mGoogleApiClient = null;
    public static GoogleAccountCredential mCredential = null;
    public static GoogleSignInAccount mGoogleSignInAccount = null;
    public static ArrayList<Mail> mails = new ArrayList<Mail>();
    public static GoogleSignInOptions gso;

    public static final String SHARED_PREFERENCES_NAME = "Account";
    public static final String PREF_ACCOUNT_NAME = "accountName";
    public static final int REQUEST_ACCOUNT_PICKER = 1000;
    public static final int REQUEST_AUTHORIZATION = 1001;
    public static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    public static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;

    public static final String[] SCOPES = {
            GmailScopes.MAIL_GOOGLE_COM,
            "https://www.googleapis.com/auth/plus.me"
    };

    public static String[] perms = {Manifest.permission.GET_ACCOUNTS, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    public static HttpTransport transport;
    public static JsonFactory jsonFactory;
    public static com.google.api.services.gmail.Gmail mService = null;
    public static Boolean isFirst = true;

    public static void Instance(Context applicationContext) {
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .build();
        //Initialize credentials and service object.
        mCredential = GoogleAccountCredential.usingOAuth2(
                applicationContext, Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());
        mGoogleApiClient = new GoogleApiClient.Builder(applicationContext)
                .addApi(Auth.GOOGLE_SIGN_IN_API, GmailAPI.gso)
                .build();

        transport = AndroidHttp.newCompatibleTransport();
        jsonFactory = JacksonFactory.getDefaultInstance();
        mService = new com.google.api.services.gmail.Gmail.Builder(
                transport, jsonFactory, mCredential)
                .setApplicationName(applicationContext.getResources().getString(R.string.app_name))
                .build();
    }
}
