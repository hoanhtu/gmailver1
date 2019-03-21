package anhtu.com.vn.gmailAPI;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePartHeader;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import anhtu.com.vn.gmailAPI.helper.GmailUtil;
import anhtu.com.vn.gmailAPI.helper.Mail;
import anhtu.com.vn.gmailAPI.helper.MailAdapter;
import anhtu.com.vn.gmailAPI.helper.RoundedTransformation;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static anhtu.com.vn.gmailAPI.helper.GmailAPI.*;

public class HomePage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, EasyPermissions.PermissionCallbacks {
    private ListView lstMail;
    private FloatingActionButton fabNewMail;
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private TextView txtSearch;

    private TextView tvUserName;
    private TextView tvEmail;
    private ImageView ivPhoto;

    ProgressDialog mProgress;
    ProgressDialog progressDialog;

    MailAdapter adapter;
    static String FROM = "";
    static String TO = "";
    static String SUBJECT = "";
    static String CONTENT = "";
    static String DATE = "";
    static String html = "";

    Boolean isFirst = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_bar);
        findView();

        //region init
        toolbar.setTitle("Inbox");
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                HideKeyBoard();
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                HideKeyBoard();
                super.onDrawerOpened(drawerView);
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);

        fabNewMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePage.this, NewMailActivity.class));
                //startActivity(new Intent(HomePage.this, ViewMail.class));
            }
        });

        txtSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SearchActivity.class));
            }
        });

        //endregion

        getUserInfo();

        //load mail về
        lstMail = findViewById(R.id.listView);
        adapter = new MailAdapter(HomePage.this, mails);
        lstMail.setAdapter(adapter);
        // Event Click
        lstMail.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(HomePage.this, "Reply Mail", Toast.LENGTH_SHORT).show();
                Mail mail = mails.get(position);
                FROM = mail.getTo();
                DATE = mail.getDate();
                SUBJECT = mail.getSubject();
                CONTENT = mail.getContent();
                openSendActivity(mail);
            }

        });

        mProgress = new ProgressDialog(this);
        mProgress.setMessage("Calling Gmail API ...");

        // Initialize credentials and service object.
//        mCredential = GoogleAccountCredential.usingOAuth2(
//                getApplicationContext(), Arrays.asList(SCOPES))
//                .setBackOff(new ExponentialBackOff());

    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    private void getUserInfo() {
        String personEmail = getPreferences(Context.MODE_PRIVATE).getString(PREF_ACCOUNT_NAME, null);
        //tvUserName.setText(personName);
        tvEmail.setText(personEmail);

        String personPhoto = "";
        Picasso.with(this).load(personEmail)
                .placeholder(R.drawable.email_image_border)
                .error(R.drawable.email_image_border)
                .transform(new RoundedTransformation(100, 4))
                .resizeDimen(R.dimen.list_detail_image_size, R.dimen.list_detail_image_size)
                .centerCrop()
                .into(ivPhoto);


    }

    private void findView() {
        drawer = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        lstMail = findViewById(R.id.listView);
        fabNewMail = findViewById(R.id.fab);
        txtSearch = findViewById(R.id.textView_Search);

        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        tvUserName = headerView.findViewById(R.id.TextView_displayName);
        tvEmail = headerView.findViewById(R.id.textView_email);
        ivPhoto = headerView.findViewById(R.id.imageView_photo);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nav_bar, menu);
        return true;
    }

    private void HideKeyBoard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sort) {
            //tạo dialog

            final String[] sortItem = getResources().getStringArray(R.array.dialog_single_choice_array);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Sort");
            builder.setCancelable(true);
            builder.setItems(sortItem, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(HomePage.this, "You choose " + sortItem[which], Toast.LENGTH_LONG).show();
                }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();

            HideKeyBoard();

            return true;
        }
        if (id == R.id.action_edit) {
            Intent intent = new Intent(HomePage.this, EditMail.class);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_inbox) {
            toolbar.setTitle("Inbox");
        } else if (id == R.id.nav_sent) {
            toolbar.setTitle("Sent");
        } else if (id == R.id.nav_setting) {
            startActivity(new Intent(HomePage.this, SettingActivity.class));
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //Disconnect accounts
    private void revokeAccess() {
        mGoogleSignInClient.revokeAccess()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
    }

    //code load mail
    @Override
    protected void onResume() {
        super.onResume();
        getResultsFromApi();
    }

    private void getResultsFromApi() {
        if (!isGooglePlayServicesAvailable()) {
            acquireGooglePlayServices();
        } else if (mCredential.getSelectedAccountName() == null) {
            chooseAccount();
        } else if (!isDeviceOnline()) {
            //mProgress.setMessage("No Internet");
            //Toast.makeText(getBaseContext(),"No Internet",Toast.LENGTH_SHORT).show();
        } else {
            if (isFirst) {
                isFirst = false;
                new MakeRequestTask().execute();
            }
        }
    }

    void openSendActivity() {
        Intent intent = new Intent(HomePage.this, ViewMail.class);
        startActivity(intent);
    }

    private void openSendActivity(String from, String date, String subject, String content) {
        Intent intent = new Intent(HomePage.this, ViewMail.class);
        intent.putExtra("from", from);
        intent.putExtra("date", date);
        intent.putExtra("subject", subject);
        intent.putExtra("content", content);
        startActivity(intent);
    }

    private void openSendActivity(Mail mail) {
        Intent intent = new Intent(HomePage.this, ViewMail.class);
        intent.putExtra("mail", mail);
        startActivity(intent);
    }

    @AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
    private void chooseAccount() {
        String[] perms = {Manifest.permission.GET_ACCOUNTS, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            String accountName = getPreferences(Context.MODE_PRIVATE).getString(PREF_ACCOUNT_NAME, null);
            if (accountName != null) {
                mCredential.setSelectedAccountName(accountName);
                getResultsFromApi();
            } else {
                // Start a dialog from which the user can choose an account
                startActivityForResult(mCredential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);
            }
        } else {
            EasyPermissions.requestPermissions(this,
                    "We need to access your email account and external storage to get mail and store it in local memory",
                    REQUEST_PERMISSION_GET_ACCOUNTS, perms);
        }
//        String accountName = getPreferences(Context.MODE_PRIVATE)
//                    .getString(PREF_ACCOUNT_NAME, null);
//        mCredential.setSelectedAccountName(accountName);
//        getResultsFromApi();
    }

    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode != RESULT_OK) {
//                    mOutputText.setText(
//                            "This app requires Google Play Services. Please install " +
//                                    "Google Play Services on your device and relaunch this app.");
                } else {
                    getResultsFromApi();
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && data != null &&
                        data.getExtras() != null) {
                    String accountName =
                            data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(PREF_ACCOUNT_NAME, accountName);
                        editor.apply();
                        mCredential.setSelectedAccountName(accountName);
                        getResultsFromApi();
                    }
                }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode == RESULT_OK) {
                    getResultsFromApi();
                }
                break;
        }
    }

    /**
     * Respond to requests for permissions at runtime for API 23 and above.
     *
     * @param requestCode  The request code passed in
     *                     requestPermissions(android.app.Activity, String, int, String[])
     * @param permissions  The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *                     which is either PERMISSION_GRANTED or PERMISSION_DENIED. Never null.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(
                requestCode, permissions, grantResults, this);
    }

    /**
     * Callback for when a permission is granted using the EasyPermissions
     * library.
     *
     * @param requestCode The request code associated with the requested
     *                    permission
     * @param list        The requested permission list. Never null.
     */
    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        // Do nothing.
    }

    /**
     * Callback for when a permission is denied using the EasyPermissions
     * library.
     *
     * @param requestCode The request code associated with the requested
     *                    permission
     * @param list        The requested permission list. Never null.
     */
    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {
        // Do nothing.
    }

    public boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }


    public boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(this);
        return connectionStatusCode == ConnectionResult.SUCCESS;
    }

    public void acquireGooglePlayServices() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(this);
        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
        }
    }

    public void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = apiAvailability.getErrorDialog(
                HomePage.this,
                connectionStatusCode,
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }

    private class MakeRequestTask extends AsyncTask<Void, Void, List<Mail>> {
        private Exception mLastError = null;

        @Override
        protected List<Mail> doInBackground(Void... params) {
            try {
                return getDataFromApi();
            } catch (Exception e) {
                mLastError = e;
                cancel(true);
                return null;
            }
        }

        private String filterEmail(String text) {
            String result = "";
            boolean check = false;
            for (int i = 0; i < text.length(); i++) {
                char c = text.charAt(i);
                if (c == '<') {
                    check = true;
                }
                if (c == '>') {
                    return result;
                }
                if (check && c != '<') {
                    result = result + c;
                }
            }
            return text;
        }

        private List<Mail> getDataFromApi() {
            List<Mail> result = new ArrayList<Mail>();
            try {
                List<Message> messages = GmailUtil.listAllMessages(mService, "me", 20);

                for (int i = 0; i < messages.size(); i++) {
                    Message messageDetail = GmailUtil.getMessage(mService, "me", messages.get(i).getId(), "full");
                    String content = messageDetail.getSnippet();

                    String subject = "";
                    String from = "";
                    String to = "";
                    String date = "";
                    List<MessagePartHeader> messagePartHeader = messageDetail.getPayload().getHeaders();
                    for (int j = 0; j < messagePartHeader.size(); j++) {
                        if (messagePartHeader.get(j).getName().equals("Subject")) {
                            subject = messagePartHeader.get(j).getValue();
                        }
                        if (messagePartHeader.get(j).getName().equals("From")) {
                            from = messagePartHeader.get(j).getValue();
                        }
                        if (messagePartHeader.get(j).getName().equals("To")) {
                            to = messagePartHeader.get(j).getValue();
                        }
                        if (messagePartHeader.get(j).getName().equals("Date")) {
                            date = messagePartHeader.get(j).getValue();
                        }
                    }
//                    for (MessagePart p : messageDetail.getPayload().getParts()) {
//                        if (p.getMimeType() == "text/html") {
//                            html = base64UrlDecode(p.getBody().getData());
//                        }
//                    }

                    if (subject.length() > 0 && content.length() > 0 && from.length() > 0 && to.length() > 0 && date.length() > 0) {
                        //date = formatDate(date);
                        Mail mail = new Mail(subject, content, filterEmail(from), filterEmail(to), date);
                        //Mail mail = new Mail(subject, content, filterEmail(from), filterEmail(to), date, html);

                        result.add(mail);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPreExecute() {

            mProgress.show();
        }

        @Override
        protected void onPostExecute(List<Mail> output) {
            mProgress.hide();
            if (output == null || output.size() == 0) {
                Toast.makeText(HomePage.this, "Error đó", Toast.LENGTH_SHORT).show();
            } else {
                mails.clear();
                mails.addAll(output);
                FROM = mails.get(0).getTo();
                Toast.makeText(HomePage.this, "Done", Toast.LENGTH_SHORT).show();
            }
            adapter.notifyDataSetChanged();
        }

        @Override
        protected void onCancelled() {
            mProgress.hide();
            if (mLastError != null) {
                if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                    showGooglePlayServicesAvailabilityErrorDialog(
                            ((GooglePlayServicesAvailabilityIOException) mLastError)
                                    .getConnectionStatusCode());
                } else if (mLastError instanceof UserRecoverableAuthIOException) {
                    startActivityForResult(
                            ((UserRecoverableAuthIOException) mLastError).getIntent(),
                            REQUEST_AUTHORIZATION);
                } else {
//                    mOutputText.setText("The following error occurred:\n"
//                            + mLastError.getMessage());
                }
            } else {
//                mOutputText.setText("Request cancelled.");
            }
        }
    }

    private String formatDate(String date) {
        String tempDate = date;
        SimpleDateFormat formatter = new SimpleDateFormat(getString(R.string.date_format), Locale.ENGLISH);
        Date temp = new Date();
        try {
            temp = formatter.parse(date);
            int d = temp.getDay();
            int M = temp.getMonth();
            int y = temp.getYear();
            int h = temp.getHours();
            int m = temp.getMinutes();

            if (d > new Date().getDay()) {
                tempDate = h + ":" + m;
            } else {
                tempDate = d + " " + M + " " + y;
            }
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), "Failed to parse date", Toast.LENGTH_SHORT).show();
        }

        return tempDate;
    }


//
//    //region code list message
//
//    /**
//     * List all Messages of the user's mailbox matching the query.
//     *
//     * @param service Authorized Gmail API instance.
//     * @param userId  User's email address. The special value "me"
//     *                can be used to indicate the authenticated user.
//     * @param query   String used to filter the Messages listed.
//     * @throws IOException
//     */
//    public static List<Message> listMessagesMatchingQuery(Gmail service, String userId,
//                                                          String query) throws IOException {
//        ListMessagesResponse response = service.users().messages().list(userId).setQ(query).execute();
//
//        List<Message> messages = new ArrayList<Message>();
//        while (response.getMessages() != null) {
//            messages.addAll(response.getMessages());
//            if (response.getNextPageToken() != null) {
//                String pageToken = response.getNextPageToken();
//                response = service.users().messages().list(userId).setQ(query)
//                        .setPageToken(pageToken).execute();
//            } else {
//                break;
//            }
//        }
//
//        for (Message message : messages) {
//            System.out.println(message.toPrettyString());
//        }
//
//        return messages;
//    }
//
//    /**
//     * List all Messages of the user's mailbox with labelIds applied.
//     *
//     * @param service  Authorized Gmail API instance.
//     * @param userId   User's email address. The special value "me"
//     *                 can be used to indicate the authenticated user.
//     * @param labelIds Only return Messages with these labelIds applied.
//     * @throws IOException
//     */
//    public static List<Message> listMessagesWithLabels(Gmail service, String userId,
//                                                       List<String> labelIds) throws IOException {
//        ListMessagesResponse response = service.users().messages().list(userId)
//                .setLabelIds(labelIds).execute();
//
//        List<Message> messages = new ArrayList<Message>();
//        while (response.getMessages() != null) {
//            messages.addAll(response.getMessages());
//            if (response.getNextPageToken() != null) {
//                String pageToken = response.getNextPageToken();
//                response = service.users().messages().list(userId).setLabelIds(labelIds)
//                        .setPageToken(pageToken).execute();
//            } else {
//                break;
//            }
//        }
//
//        for (Message message : messages) {
//            System.out.println(message.toPrettyString());
//        }
//
//        return messages;
//    }
//
//    //endregion
//


}
