package litfitsserver.miscellaneous;

import java.time.LocalDate;
import java.util.Properties;
import java.util.regex.Pattern;
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
import litfitsserver.entities.Company;
import litfitsserver.entities.User;

/**
 * Builds an Email Service capable of sending normal email to a given SMTP Host. Currently <b>send()</b> can only works
 * with text.
 *
 * @author Asier Vila & Carlos Mendez
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

    /**
     * Sends a confirmation email to the address associated with the company
     *
     * @param company
     * @throws MessagingException
     */
    public void sendCompanyPasswordChangeComfirmationEmail(Company company) throws MessagingException, Exception {
        String text = "The password for the company: " + company.getNif() + " was changed the " + LocalDate.now();
        sendMail(company.getEmail(), "Your Lit Fits password has been changed", text);
    }

    /**
     * Sends an email notifying the password has been changed to a new random one, used when users forget their
     * passwords
     *
     * @param company
     * @throws MessagingException
     */
    public void sendCompanyPasswordReestablishmentEmail(Company company) throws MessagingException, Exception {
        String text = "The password for the company: " + company.getNif() + " was changed the " + LocalDate.now() + ", to " + company.getPassword();
        sendMail(company.getEmail(), "Your Lit Fits password has been changed", text);
    }
    
    /**
     * Send an email to the received user with the new password.
     *
     * @param user The User that is going to receive the new password.
     * @throws MessagingException
     * 
     * @author Asier
     */
    public void sendUserPasswordReestablishmentEmail(User user) throws MessagingException, Exception {
        String text = "The password for the user: " + user.getUsername() + " was changed the " + LocalDate.now() + ", to " + user.getPassword();
        sendMail(user.getEmail(), "Your Lit Fits password has been changed", text);
    }
}
