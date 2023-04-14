package org.apache.commons.mail;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.internet.InternetAddress;

import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

//import org.apache.commons.con
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class EmailTest {
	
	private static final String[] testEmails = { "ab@bc.com", "a.b@c.org", "abcdefghijklmnopqrst@abcdefghijklmnopqrst.com.bd" };
	private String[] testValidChars = { " ", "a", "A", "\uc5ec", "0123456789", "01234567890123456789", "\n"};
	private String auxillary =  "extra Info";
	private Date date;

	private EmailDummy email;
	
	
	
	@Before
	public void setupEmailTest() throws Exception
	{
		email = new EmailDummy();
		date = new Date();
	}
	
	@After
	public void teardownEmailTest() throws Exception
	{
	}

	//Add 3 bcc addresses then test if the correct # of addresses has been added
	@Test
	public void addBccTest()
	{		
		//special comments saved in Cddd java file
		try {
			email.addBcc(testEmails);

			assertEquals(testEmails.length, email.getBccAddresses().size());	
		
		} catch (EmailException e) {

		}
	}
	
	//Add 1 cc address then test if that is the first address in the object
	@Test
	public void addCcTest() {
		try {
			email.addCc(testEmails[0]);
			
			assertEquals(testEmails[0], email.getCcAddresses().get(0).toString());
		} catch (EmailException e) {

		}		
	}
	
	//Add 1 reply address then test if that is the first address in the object
	@Test
	public void addReplyToTest() {
		try {
			email.addReplyTo(testEmails[0], auxillary);
			
			assertEquals(auxillary, email.getReplyToAddresses().get(0).getPersonal());
		} catch (EmailException e) {

		}
	}

	//Throw error from param being blank
	@Test
	public void addHeaderTest_Param1Blank() {
		try {
			email.addHeader("", testEmails[0]);
			
			assertEquals(true, email.getHeaders().containsValue(testEmails[0]));
		} catch (IllegalArgumentException e) {

		}
	}
	
	//Throw error from param being blank
	@Test
	public void addHeaderTest_Param2Blank() {
		try {
			email.addHeader(auxillary, "");
			
			assertEquals(true, email.getHeaders().containsValue(""));
		} catch (IllegalArgumentException e) {

		}
	}

	//Try to get a header, then test whether the value of that header exists in the object
	@Test
	public void addHeaderTest() {
		try {
			email.addHeader(auxillary, testEmails[0]);
			
			assertEquals(true, email.getHeaders().containsValue(testEmails[0]));
		} catch (IllegalArgumentException e) {

		}
	}

	//Succeed in reaching as much email code as possible
	@Test
	public void buildMimeMessageTest() {
		try {
		Properties prop = new Properties();
		prop.setProperty(email.MAIL_HOST, "localhost");
		Session scn=Session.getInstance(prop);
		email.setMailSession(scn);
	
		String hostname=email.getHostName();

		email.addBcc(testEmails);
		email.addCc(testEmails[0]);
		email.addReplyTo(testEmails[0], auxillary);
		email.setFrom(testEmails[1]);
		email.addHeader(testEmails[0], auxillary);
		email.addTo(testEmails[2], auxillary);
			
		email.setCharset("utf-8");
		email.setSubject(auxillary);
		email.setSentDate(date);
		
		MimeMultipart m = new MimeMultipart();
		email.setContent(m);
		
		email.buildMimeMessage();		
		} 
		catch (EmailException e) {{
			e.printStackTrace();
		}}
		catch(IllegalStateException e) { }
	}

	//Remove all params like bcc, cc, etc to fail those check paths
	@Test
	public void buildMimeMessageTest_Missing() {
		try {
		Properties prop = new Properties();
		prop.setProperty(email.MAIL_HOST, "localhost");
		Session scn=Session.getInstance(prop);
		email.setMailSession(scn);

		email.addTo(testEmails[0]);
			
		email.setSentDate(date);
		email.setFrom(testEmails[1]);
		//email.popBeforeSmtp = true;
		
		email.buildMimeMessage();
		
		} catch (EmailException e) {
			e.printStackTrace();
		}
		catch(IllegalStateException e) { }
	}

	//Cause a missing fromAddress exception, since you need that for every email
	@Test
	public void buildMimeMessageTest_MissingFrom() {
		try {
		Properties prop = new Properties();
		prop.setProperty(email.MAIL_HOST, "localhost");
		Session scn=Session.getInstance(prop);
		email.setMailSession(scn);

		email.addTo(testEmails[0]);
			
		email.setSentDate(date);
		//email.popBeforeSmtp = true;
		
		email.buildMimeMessage();
		
		} catch (EmailException e) {
		}
		catch(IllegalStateException e) { }
	}

	//SEE: public static final string MAIL_HOST = EmailConstants
	@Test
	public void  getHostNameTest_WithSession() {
		Properties prop = new Properties();
		prop.setProperty(email.MAIL_HOST, "localhost");
		Session scn=Session.getInstance(prop);
		email.setMailSession(scn);
	
		String hostname=email.getHostName();
		assertEquals("localhost",hostname);	
	}
	
	//Don't properly set a hostname before calling get
	@Test
	public void getHostNameTest_WithoutSession() {
		String hostname=email.getHostName();
		assertEquals(null,hostname);	
	}
	
	//Throw error from missing hostname
	@Test
	public void getMailSessionTest_MissingHostname() {
		try {
			email.getMailSession();
		}
		catch (EmailException e) {

		}
	}
	
	//Reach as much valid code as readily possible
	@Test
	public void getMailSessionTest() {
		try {
			email.setHostName("jimbo baggins");
			email.setSSLOnConnect(true);
			email.getMailSession();
		}
		catch (EmailException e) {
		e.printStackTrace();
		}
	}
	
	//Set a sent date then retrieve it
	@Test
	public void getSentDateTest_Existing() {
		email.setSentDate(date);
		assertEquals(date, email.getSentDate());
	}
	
	//Don't set a sent date, thus activating the code branch that instead returns the current time
	@Test
	public void getSentDateTest_New() {
		email.getSentDate();
	}

	//Test that variable returned from get matches the constant it is meant to
	@Test
	public void getSocketConnectionTimeout() {
		assertEquals(EmailConstants.SOCKET_TIMEOUT_MS, email.getSocketConnectionTimeout());
	}
	
	
	

}
