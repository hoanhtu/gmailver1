package anhtu.com.vn.gmailAPI;

import java.io.Serializable;


public class Mail implements Serializable {

    private String subject;
    private String content;
    private String from;
    private String to;
    private String date;

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    private String html;

    public Mail(String subject, String content, String from, String to, String date, String html) {
        this.subject = subject;
        this.content = content;
        this.from = from;
        this.to = to;
        this.date = date;
        this.html = html;
    }

    public Mail(String subject, String content, String from, String to, String date) {
        this.subject = subject;
        this.content = content;
        this.from = from;
        this.to = to;
        this.date = date;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
