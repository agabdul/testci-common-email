package org.apache.commons.mail;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class EmailTest {
	
	private static final String[] TEST_EMAILS = {"ab@bc.com", "a.b@c.org",
			"abcdefghijklmnopqrst@abcdefghijklmnopqrst.com.bd" };
	
	private EmailConcrete email;
	
	@Before
	public void setUpEmailTest() throws Exception {
		
		email = new EmailConcrete();
	
	}
	
	@After
	public void tearDownEmailTest() throws Exception{
		
	}
	
	/**
	 * Test addBcc(String email...) function
	 */
	@Test	//Tests adding valid email addresses to the Bcc list
	public void testAddBcc_ValidEmail() throws Exception{
		
		email.addBcc(TEST_EMAILS);
		
		assertEquals(3, email.getBccAddresses().size());
	}
	
	@Test	//Tests adding null Bcc addresses.
	public void testAddBccNULL() throws Exception{
		
		try {
            email.addBcc((String[]) null);
        } catch (EmailException e) {
            assertEquals("Address List provided was invalid", e.getMessage());
        }
	}
	
	@Test	//Tests adding empty Bcc addresses.
	public void testAddBccEmpty() throws Exception{
		
		try {
            email.addBcc();
        } catch (EmailException e) {
            assertEquals("Address List provided was invalid", e.getMessage());
        }
	}
	
	
	/**
	 * Test addCc(String email) function
	 */
	
	@Test	//Tests adding a single valid email address to the Cc list.
    public void testAddCc_ValidEmail() throws Exception {
        email.addCc("test@example.com");
        assertEquals(1, email.getCcAddresses().size());
    }
	
	@Test	//Tests adding multiple valid email addresses to the Cc list.
    public void testAddCc_ValidEmailList() throws Exception {
        email.addCc(TEST_EMAILS);
        assertEquals(3, email.getCcAddresses().size());
    }

    @Test	//Tests adding null Cc addresses.
    public void testAddCc_NULL() {
    	try {
            email.addCc((String[]) null);
        } catch (EmailException e) {
            assertEquals("Address List provided was invalid", e.getMessage());
        }
    }
    
    @Test	//Tests adding empty Cc addresses.
	public void testAddCcEmpty() throws Exception{
		
		try {
            email.addCc();
        } catch (EmailException e) {
            assertEquals("Address List provided was invalid", e.getMessage());
        }
	}
    
    /**
	 * Test addHeader(String name, String value) function
	 */
    
    @Test	//Tests adding a header with an empty name.
	public void testAddHeaderEmptyName() throws Exception{
		try {
            email.addHeader("","1");
        } catch (Exception e) {
            assertEquals("name can not be null or empty", e.getMessage());
        }
	}
    
    @Test	//Tests adding a header with an empty value.
	public void testAddHeaderEmptyValue() throws Exception{
		try {
            email.addHeader("1","");
        } catch (Exception e) {
            assertEquals("value can not be null or empty", e.getMessage());
        }
	}
    @Test	//Tests adding a valid header with a name and value.
	public void testAddHeaderValid() throws Exception{
    	
    		email.addHeader("1","1");
    		Map<String, String> headers2 = new HashMap<String, String>();
    		headers2.put("1", "1");  	
            
            assertEquals(email.getHeaders().get("1"),headers2.get("1"));
            
    }
	    
    /**
	 * Test addReplyTo(String email, String name) function
	 */
    
    @Test	//Tests adding a valid reply-to address with a name.
	public void testAddReplyToValid() throws Exception{

            email.addReplyTo("abc@gmail.com","john");
            
            List<InternetAddress> expected = new ArrayList<>();
            expected.add(new InternetAddress("abc@gmail.com", "john"));
            
            
            assertEquals(email.getReplyToAddresses(),expected);
            
    }
    
    /**
	 * Test buildMimeMessage() function
     * @throws MessagingException 
     * @throws IOException 
	 */
    
    
    @Test	//Tests building a valid MimeMessage with proper attributes.
    public void testBuildMimeMessageValid() throws Exception {
    	email.setHostName("localhost");
		email.setSmtpPort(1234);
		email.setFrom("a@b.com");
		email.addTo("c@d.com");
		email.setSubject("test mail");
		email.setCharset("ISO-8859-1");
		email.setContent("test content", "text/plain");
		
		email.addCc("email1@gmail.com");
		email.addBcc("email2@gmail.com");
		email.addReplyTo("email3@gmail.com");

        email.buildMimeMessage();
        MimeMessage message = email.getMimeMessage();
        
        
        assertNotNull(message);
        assertEquals("test mail", message.getSubject());
        assertEquals("test content", message.getContent().toString().trim());
    }
    
    @Test	//Tests attempting to build a MimeMessage twice.
    public void testbuildMimeMessageBuildTwice() throws Exception {
    	try {
    		email.setHostName("localhost");
    		email.setSmtpPort(1234);
    		email.setFrom("a@b.com");
    		email.addTo("c@d.com");
    		email.setSubject("test mail");
    		email.setCharset("ISO-8859-1");
    		email.setContent("test content", "text/plain");
    		email.buildMimeMessage();
    		email.buildMimeMessage();
    	} catch (IllegalStateException re) {
    		String message = "The MimeMessage is already built.";
    		assertEquals(message, re.getMessage());
    	}
    }
    
    @Test	//Tests building a MimeMessage without a "From" address.
    public void testbuildMimeMessageFromNull() throws Exception {
    	try {
    		
    		email.setHostName("localhost");
    		email.setSmtpPort(1234);
    		email.addTo("c@d.com");
    		email.setSubject("test mail");
    		email.setCharset("ISO-8859-1");
    		email.setContent("test content", "text/plain");
    		email.buildMimeMessage();
    	} catch(EmailException e) {
    		
    		assertEquals("From address required", e.getMessage());
    		
    	}

    }
    
    @Test	//Tests building a MimeMessage without any "To" address.
    public void testbuildMimeMessageToNull() throws Exception {
    	try {
    		
    		email.setHostName("localhost");
    		email.setSmtpPort(1234);
    		email.setFrom("a@b.com");
    		email.setSubject("test mail");
    		email.setCharset("ISO-8859-1");
    		email.setContent("test content", "text/plain");
    		email.buildMimeMessage();
    	} catch(EmailException e) {
    		
    		assertEquals("At least one receiver address required", e.getMessage());
    		
    	}

    }

    
    @Test	//Tests adding Cc recipients to a MimeMessage.
    public void testbuildMimeMessageCcs() throws Exception {

    		email.setHostName("localhost");
    		email.setSmtpPort(1234);
    		email.addTo("c@d.com");
    		email.setFrom("a@b.com");
    		email.setSubject("test mail");
    		email.setCharset("ISO-8859-1");
    		email.setContent("test content", "text/plain");
    		
    		email.addCc("email1@gmail.com");
    		
    		email.buildMimeMessage();
    		
    		MimeMessage message = email.getMimeMessage();
    		
    		Address[] expected = message.getRecipients(Message.RecipientType.CC);

    		assertEquals(expected[0].toString(), "email1@gmail.com");
    		
    }
    
    @Test	//Tests adding Bcc recipients to a MimeMessage.
    public void testbuildMimeMessageBccs() throws Exception {

    		email.setHostName("localhost");
    		email.setSmtpPort(1234);
    		email.addTo("c@d.com");
    		email.setFrom("a@b.com");
    		email.setSubject("test mail");
    		email.setCharset("ISO-8859-1");
    		email.setContent("test content", "text/plain");
    		
    		email.addBcc("email1@gmail.com");
    		
    		email.buildMimeMessage();
    		MimeMessage message = email.getMimeMessage();
    		
    		Address[] expected = message.getRecipients(Message.RecipientType.BCC);

    		assertEquals(expected[0].toString(), "email1@gmail.com");
    		
    }
    
    @Test	//Tests adding a reply-to address to a MimeMessage.
    public void testbuildMimeMessageReplyTo() throws Exception {

    		email.setHostName("localhost");
    		email.setSmtpPort(1234);
    		email.addTo("c@d.com");
    		email.setFrom("a@b.com");
    		email.setSubject("test mail");
    		email.setCharset("ISO-8859-1");
    		email.setContent("test content", "text/plain");
    		
    		email.addReplyTo("email1@gmail.com");
    		
    		email.buildMimeMessage();
    		
    		MimeMessage message = email.getMimeMessage();
    		
    		
    		Address[] expected = message.getReplyTo();

    		assertEquals(expected[0].toString(), "email1@gmail.com");
    }
    
    @Test	//Tests adding headers to a MimeMessage.
    public void testbuildMimeMessageHeaders() throws Exception {

    		email.setHostName("localhost");
    		email.setSmtpPort(1234);
    		email.addTo("c@d.com");
    		email.setFrom("a@b.com");
    		email.setSubject("test mail");
    		email.setCharset("ISO-8859-1");
    		email.setContent("test content", "text/plain");
    		
    		email.addHeader("0", "Test Header");
    		
    		email.buildMimeMessage();
    		
    		MimeMessage message = email.getMimeMessage();
    		
    		Map<String, String> expected = new HashMap<String, String>();
    		expected.put("0", "Test Header");
    		
    		
    		assertEquals(message.getHeader("0",null).toString(), "Test Header");
    		
    }

    @Test	//Tests building a MimeMessage without content.
    public void testbuildMimeMessageContentNull() throws Exception {

    		email.setHostName("localhost");
    		email.setSmtpPort(1234);
    		email.addTo("c@d.com");
    		email.setFrom("a@b.com");
    		email.setSubject("test mail");
    		email.setCharset("ISO-8859-1");
    		
    		email.buildMimeMessage();
    		
    		MimeMessage message = email.getMimeMessage();

    		assertEquals(message.getContent().toString(), "");
    		
    }
    
    @Test	//Tests adding multiple "To" recipients to a MimeMessage.
    public void testbuildMimeMessageMultipleTo() throws Exception {
        
    	email.setHostName("localhost");
		email.setSmtpPort(1234);
		email.addTo("c@d.com");
		email.addTo("a@b.com");
		email.setFrom("a2@b.com");
		email.setSubject("test mail");
		email.setCharset("ISO-8859-1");
    	

        email.buildMimeMessage();
        MimeMessage message = email.getMimeMessage();

        Address[] recipients = message.getRecipients(Message.RecipientType.TO);
        assertEquals(2, recipients.length);
    }
    
    @Test	//Tests building a MimeMessage with a valid subject but no charset.
    public void testbuildMimeMessageSubjectValidCharsetNull() throws Exception {
        
    	email.setHostName("localhost");
		email.setSmtpPort(1234);
		email.addTo("c@d.com");
		email.addTo("a@b.com");
		email.setFrom("a2@b.com");
		email.setSubject("test mail");

        email.buildMimeMessage();
        MimeMessage message = email.getMimeMessage();

        assertEquals(message.getSubject(),"test mail");
    }
    
    @Test	//Tests setting up Pop before SMTP in a MimeMessage.
    public void testbuildMimeMessagePopBeforeSMTP() throws Exception {
    	
    	boolean thrown = false;
    	
        try {
    	email.setHostName("localhost");
		email.setSmtpPort(1234);
		email.addTo("c@d.com");
		email.addTo("a@b.com");
		email.setFrom("a2@b.com");
		email.setSubject("test mail");
		email.setCharset("ISO-8859-1");
		
		email.setPopBeforeSmtp(true, "host", "username", "password");

        email.buildMimeMessage();
        MimeMessage message = email.getMimeMessage();

        }catch(EmailException e) {
        	thrown = true;
        	
        }
        
        
        assertTrue(thrown);
    }
    
    
    /**
	 * Test getHostName() function
	 */
    
    @Test	//Tests getting the valid host name for an email.
	public void testgetHostNameValid() throws Exception{
    	email.setHostName("localhost");
    	assertEquals(email.getHostName(),"localhost");
    	
    }
    
    @Test	//Tests getting the host name when it's set to null.
	public void testgetHostNameNull() throws Exception{
    	email.setHostName(null);
    	assertEquals(email.getHostName(), null);
    	
    }
    
    @Test	//Tests getting the host name when it’s not set explicitly.
	public void testgetHostNameNotNull() throws Exception{
    	Properties props = new Properties();
        Session session = Session.getInstance(props);
        
        email.setMailSession(session);
        
        assertEquals(email.getHostName(), null);
    	
    }
    
    /**
	 * Test getMailSession() function
	 */        
        
    
  @Test	//Tests getting the mail session with valid settings.
  public void testGetMailSessionValid() throws Exception {
  	
  	  Properties props = new Properties();
      Session session = Session.getInstance(props);
      
  	  email.setMailSession(session);
      
      assertEquals(email.getMailSession(),session);
      
  }
    
    
    @Test	//Tests getting the mail session with null values.
    public void testGetMailSessionNull() throws Exception {
        email.setHostName("john");
        email.setSmtpPort(123);

        Session session = email.getMailSession();
        Properties props = session.getProperties();
        
        assertEquals("john", props.getProperty(EmailConstants.MAIL_HOST));
        assertEquals("123", props.getProperty(EmailConstants.MAIL_PORT));
    }
    

    @Test	//Tests getting the mail session when the host name is null.
    public void testGetMailSessionNullHostNameNull() {
        email.setHostName(null); 

        try{
        	email.getMailSession();
        }catch(Exception e) {
        	assertEquals(e.getMessage(), "Cannot find valid hostname for mail session");
        }

    }

    @Test	//Tests getting the mail session with TLS enabled.
    public void testGetMailSessionTLSTrue() throws EmailException {
        email.setHostName("john");
        email.setStartTLSEnabled(true);
        email.setStartTLSRequired(true);

        Session session = email.getMailSession();
        Properties props = session.getProperties();

        assertEquals("true", props.getProperty(EmailConstants.MAIL_TRANSPORT_STARTTLS_ENABLE));
        assertEquals("true", props.getProperty(EmailConstants.MAIL_TRANSPORT_STARTTLS_REQUIRED));
    }
    
    @Test	//Tests getting the mail session with SSL enabled.
    public void testGetMailSessionSSLTrue() throws EmailException {
        email.setHostName("john");
        email.setSslSmtpPort("123");
        email.setSSLOnConnect(true);

        Session session = email.getMailSession();
        Properties props = session.getProperties();

        assertEquals("123", props.getProperty("mail.smtp.port"));
        assertEquals("123", props.getProperty("mail.smtp.socketFactory.port"));
        assertEquals("javax.net.ssl.SSLSocketFactory", props.getProperty("mail.smtp.socketFactory.class"));
        assertEquals("false", props.getProperty("mail.smtp.socketFactory.fallback"));
    }

    @Test	//Tests getting the mail session with partial email sending enabled.
    public void testGetMailSessionPartial() throws EmailException {
        email.setHostName("john");
        email.setSendPartial(true);

        Session session = email.getMailSession();
        Properties props = session.getProperties();

        assertEquals("true", props.getProperty(EmailConstants.MAIL_SMTP_SEND_PARTIAL));
        assertEquals("true", props.getProperty(EmailConstants.MAIL_SMTPS_SEND_PARTIAL));
    }

    @Test	//Tests getting the mail session with SSL server identity check enabled.
    public void testGetMailSessionSSLCheckServerIdentity() throws EmailException {
        email.setHostName("john");
        email.setSSLOnConnect(true);
        email.setSSLCheckServerIdentity(true);

        Session session = email.getMailSession();
        Properties props = session.getProperties();

        assertEquals("true", props.getProperty(EmailConstants.MAIL_SMTP_SSL_CHECKSERVERIDENTITY));
    }

    @Test	//Tests getting the mail session with a bounce address set.
    public void testGetMailSessionBounce() throws EmailException {
        email.setHostName("john");
        email.setBounceAddress("bounce");

        Session session = email.getMailSession();
        Properties props = session.getProperties();

        assertEquals("bounce", props.getProperty(EmailConstants.MAIL_SMTP_FROM));
    }

    @Test	//Tests getting the mail session with a socket timeout >0.
    public void testGetMailSessionSocketTimeoutGT0() throws EmailException {
        email.setHostName("john");
        email.setSocketTimeout(10);

        Session session = email.getMailSession();
        Properties props = session.getProperties();

        assertEquals("10", props.getProperty(EmailConstants.MAIL_SMTP_TIMEOUT));
    }

    @Test	//Tests getting the mail session with a socket connection timeout>0.
    public void testGetMailSessionSocketConnectionTimeoutGT0() throws EmailException {
        email.setHostName("john");
        email.setSocketConnectionTimeout(10);

        Session session = email.getMailSession();
        Properties props = session.getProperties();

        assertEquals("10", props.getProperty(EmailConstants.MAIL_SMTP_CONNECTIONTIMEOUT));
    }
    
    @Test	//Tests getting the mail session with a socket timeout = 0.
    public void testGetMailSessionSocketTimeoutLT0() throws EmailException {
        email.setHostName("john");
        email.setSocketTimeout(0);

        Session session = email.getMailSession();
        Properties props = session.getProperties();

        assertEquals(null, props.getProperty(EmailConstants.MAIL_SMTP_TIMEOUT));
    }

    @Test	// Tests getting the mail session with a socket connection timeout = 0.
    public void testGetMailSessionSocketConnectionTimeoutLT0() throws EmailException {
        email.setHostName("john");
        email.setSocketConnectionTimeout(0);

        Session session = email.getMailSession();
        Properties props = session.getProperties();

        assertEquals(null, props.getProperty(EmailConstants.MAIL_SMTP_CONNECTIONTIMEOUT));
    }
    
    
    
    /**
	 * Test getSentDate() function
	 */
    
    @Test	//Tests getting the sent date when it’s not set.
    public void testgetSentDateNull() throws Exception{

    	email.setSentDate(null);
    	Date date = new Date();
    	
    	assertEquals(email.getSentDate(), date);
    	
    }
    
    @Test	//Tests getting the sent date when it is set.
    public void testgetSentDate() throws Exception{
    	
    	Date date = new Date(3/20/25);
    	email.setSentDate(date);
    	
    	
    	assertEquals(email.getSentDate(), date);
    	
    }
    
    /**
	 * Test getSocketConnectionTimeout() function
	 */
	
    @Test	//Tests getting the socket connection timeout value.
    public void testgetSocketConnectionTimeout() throws Exception{
    	
    	email.setSocketConnectionTimeout(10);
    	
    	assertEquals(email.getSocketConnectionTimeout(),10);
    	
    	email.setSocketConnectionTimeout(0);
    	
    	assertEquals(email.getSocketConnectionTimeout(),0);
    	
    }
    
    /**
	 * Test setFrom(String email) function
	 */
    
    @Test	//Tests setting the "From" address for an email
    public void testSetFrom() throws Exception{
    
    	email.setFrom("abc@gmail.com");
    	InternetAddress a = new InternetAddress("abc@gmail.com");
    	assertEquals(email.getFromAddress(),a);
    	
    }
    
}
