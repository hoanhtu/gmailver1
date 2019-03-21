package anhtu.com.vn.gmailAPI;




import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MailAdapter extends BaseAdapter {

    LayoutInflater inflater;
    Context context;
    ArrayList<Mail> mails = new ArrayList<Mail>();

    public MailAdapter(Context context, ArrayList<Mail> mails) {
        this.context = context;
        this.mails = mails;
    }

    @Override
    public int getCount() {
        return mails.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        return null;
//    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.listview_email_item, parent, false);
        TextView tvFrom = itemView.findViewById(R.id.tv_from);
        TextView tvDate = itemView.findViewById(R.id.tv_date);
        TextView tvSubject = itemView.findViewById(R.id.tv_subject);
        TextView tvContent = itemView.findViewById(R.id.tv_content);
        try {
            Mail mail = mails.get(position);
            tvFrom.setText(mail.getFrom());
            tvDate.setText(mail.getDate());
            tvSubject.setText(mail.getSubject());
            tvContent.setText(mail.getContent());
        } catch (Exception e) {

        }
        return itemView;
    }
}
