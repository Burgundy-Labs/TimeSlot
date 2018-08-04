package application_components.mailing;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/* Defines the general structure of email components */
public class EmailModel {
    private String icon = ""; // Icon displayed before title of email
    private String title = ""; // Major heading to email
    private EmailBody emailBody; // Map of sections separated by section title and body
    private List<EmailButton> emailButtons;
    private EmailFooter emailFooter;

    public static class EmailButton {
        private String text;
        private String url;
        private String buttonStyles = "background-color:#0067F4; color: white; border-radius: 4px; padding: 10px; " +
                "margin:0 auto; display:block; width: 90%; text-align: center; font-size: 18px; " +
                "text-decoration:none;";

        public EmailButton(String text, String url) {
            this.text = text;
            this.url = url;
        }
        public EmailButton(String text, String url, String buttonStyles) {
            this.text = text;
            this.url = url;
            this.buttonStyles = buttonStyles;
        }

        public String toHtml() {
            return "<a href=\"" + this.url +"\" style=\"" + buttonStyles + "\" target=\"_blank\">" + this.text + "</a>";
        }
    }

    public static class EmailBody {
        private Map<String, String> sections;
        private String bodyStyle = "background-color:#EFF0F1; border-radius:4px; padding: 10px;";

        public EmailBody(Map<String, String> sections) {
            this.sections = new HashMap<>();
            this.sections.putAll(sections);
        }

        public String toHtml() {
            StringBuilder html = new StringBuilder();
            Iterator it = sections.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                html.append("<h3>").append(pair.getKey()).append("</h3>");
                html.append("<div style=\"").append(bodyStyle).append("\">").append(pair.getValue()).append("</div>");
                html.append("<br/>");
                it.remove();
            }
            return html.toString();
        }
    }

    public static class EmailFooter {
        private String footerText;
        private String footerStyle = "";

        public EmailFooter(String footerText) {
            this.footerText = footerText;
        }

        public String toHtml() {
            return "<div style=\"" + this.footerStyle + "\">" + this.footerText+ "</div>";
        }
    }


    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public EmailBody getEmailBody() {
        return emailBody;
    }

    public void setEmailBody(EmailBody body) {
        this.emailBody = body;
    }

    public List<EmailButton> getEmailButtons() {
        return emailButtons;
    }

    public void setEmailButtons(List<EmailButton> emailButtons) {
        this.emailButtons = emailButtons;
    }

    public EmailFooter getEmailFooter() {
        return emailFooter;
    }

    public void setEmailFooter(EmailFooter emailFooter) {
        this.emailFooter = emailFooter;
    }

}
