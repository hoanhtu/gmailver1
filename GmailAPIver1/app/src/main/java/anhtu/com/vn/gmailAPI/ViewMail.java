package anhtu.com.vn.gmailAPI;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;

import anhtu.com.vn.gmailAPI.helper.Mail;


public class ViewMail extends AppCompatActivity implements Serializable {

    private Toolbar toolbar;
    private ImageView ivReply,ivReplyAll,ivDelete;
    private TextView tvContent,tvSubject,tvDate,tvFrom;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_mail);
        findView();




        toolbar.setTitle("Mail");
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.round_arrow_back_ios_24));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ivReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(),"Reply to",Toast.LENGTH_SHORT).show();
            }
        });
        ivReplyAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(),"Reply All",Toast.LENGTH_SHORT).show();
            }
        });
        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext(),R.style.AlertDialogTheme);
                builder.setTitle(R.string.dialog_setTag_title);
                v= getLayoutInflater().inflate(R.layout.dialog_set_tag,null);
                builder.setView(v);

                final EditText edtText = v.findViewById(R.id.setTag);

                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),"Name tag is: "+edtText.getText(),Toast.LENGTH_SHORT).show();
                        HideKeyBoard();
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        SetTextMail();
    }

    private void SetTextMail() {
        Intent intent = getIntent();
        Mail mail = (Mail) intent.getSerializableExtra("mail");
        tvSubject.setText(mail.getSubject());
        tvDate.setText(mail.getDate());
        tvFrom.setText(mail.getFrom());
        tvContent.setText(mail.getContent());
        //webView.loadData(mail.getHtml(), "text/html; charset=utf-8", "UTF-8");
    }

    private void findView() {
        toolbar = findViewById(R.id.toolbar);
        ivReply=findViewById(R.id.img_reply);
        ivReplyAll=findViewById(R.id.img_reply_all);
        ivDelete=findViewById(R.id.img_delete);

        tvContent = findViewById(R.id.tv_content);
        tvSubject =findViewById(R.id.tv_subject);
        tvDate = findViewById(R.id.tv_date);
        tvFrom = findViewById(R.id.tv_from);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_view_mail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.action_next:
                Toast.makeText(getBaseContext(),"Next mail",Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_prev:
                Toast.makeText(getBaseContext(),"Prev",Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_move:
                Toast.makeText(getBaseContext(),"Move",Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_new_tag:
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.AlertDialogTheme);
                builder.setTitle(R.string.dialog_setTag_title);
                View v= getLayoutInflater().inflate(R.layout.dialog_set_tag,null);
                builder.setView(v);

                final EditText edtText = v.findViewById(R.id.setTag);

                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),"Name tag is: "+edtText.getText(),Toast.LENGTH_SHORT).show();
                        HideKeyBoard();
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
                break;
        }
        return true;
    }
    private void HideKeyBoard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
