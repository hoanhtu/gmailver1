package anhtu.com.vn.gmailAPI;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

public class EditMail extends AppCompatActivity {
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_mail);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id= item.getItemId();

        switch (id){
            case R.id.action_delete:{
                AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.AlertDialogTheme);
                builder.setTitle("Delete");
                builder.setMessage("Are you sure to delete these mail?");
                builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),"You deleted emails",Toast.LENGTH_SHORT).show();
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
//            case R.id.action_move:{
//                Toast.makeText(getApplicationContext(),"Move these mails",Toast.LENGTH_SHORT).show();
//            }
//                break;
//            case R.id.action_new_tag:{
//                AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.AlertDialogTheme);
//                builder.setTitle(R.string.dialog_setTag_title);
//                View v= getLayoutInflater().inflate(R.layout.dialog_set_tag,null);
//                builder.setView(v);
//
//                final EditText edtText = v.findViewById(R.id.setTag);
//
//                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Toast.makeText(getApplicationContext(),"Name tag is: "+edtText.getText(),Toast.LENGTH_SHORT).show();
//                        HideKeyBoard();
//                    }
//                });
//                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//
//
//
//                AlertDialog alertDialog = builder.create();
//
//                alertDialog.show();
//
//
//            }
//                break;
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

