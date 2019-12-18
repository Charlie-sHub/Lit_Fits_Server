package miscellaneous;

import java.util.Properties;
import java.util.regex.Pattern;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * Builds an Email Service capable of sending normal email to a given SMTP Host. Currently <b>send()</b> can only works
 * with text.
 */
public class EmailService {
    private String user = "lit_fits_no_reply@outlook.com";
    private String pass = "litfits69";
    private String smtp_host = null;
    private int smtp_port = 0;
    private static final String DEFAULT_SMTP_HOST = "outlook.com";
    private static final int DEFAULT_SMTP_PORT = 25;

    /**
     * Empty constructor
     */
    public EmailService() {
    }

    /**
     * Builds the EmailService. If the Server DNS and/or Port are not provided, default values will be loaded
     *
     * @param user User account login
     * @param pass User account password
     * @param host The Server DNS
     * @param port The Port
     */
    public EmailService(String user, String pass, String host, String port) {
        this.user = user;
        this.pass = pass;
        this.smtp_host = (host == null ? DEFAULT_SMTP_HOST : host);
        this.smtp_port = (port == null ? DEFAULT_SMTP_PORT : new Integer(port).intValue());
    }

    /**
     * Sends the given <b>text</b> from the <b>sender</b> to the <b>receiver</b>. In any case, both the <b>sender</b>
     * and <b>receiver</b> must exist and be valid mail addresses. <br/>
     * <br/>
     *
     * Note the <b>user</b> and <b>pass</b> for the authentication is provided in the class constructor. Ideally, the
     * <b>sender</b> and the <b>user</b>
     * coincide.
     *
     * @param sender The mail's FROM part
     * @param receiver The mail's TO part
     * @param subject The mail's SUBJECT
     * @param text The proper MESSAGE
     * @throws MessagingException Is something awry happens
     *
     */
    public void sendMail(String receiver, String subject, String text) throws MessagingException {
        // Mail properties
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", true);
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", smtp_host);
        properties.put("mail.smtp.port", smtp_port);
        properties.put("mail.smtp.ssl.trust", smtp_host);
        properties.put("mail.imap.partialfetch", false);
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, pass);
            }
        });
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(user));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiver));
        message.setSubject(subject);
        Multipart multipart = new MimeMultipart();
        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(text, "text/html");
        multipart.addBodyPart(mimeBodyPart);
        /*MimeBodyPart messageBodyPart = new MimeBodyPart();
        String filename = "Lit Fits Logo";
        DataSource source = new FileDataSource(filename);
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(filename);
        multipart.addBodyPart(messageBodyPart);*/
        message.setContent(multipart);
        Transport.send(message);
    }

    /**
     * True if the mail is not null and a valid email address, false otherwise.
     *
     * @param mail The email address
     * @return True or False
     */
    public static boolean isValid(String mail) {
        Pattern pattern = Pattern.compile(
                "^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z" + "A-Z]{2,7}$");
        if (mail == null) {
            return false;
        }
        return pattern.matcher(mail).matches();
    }
}