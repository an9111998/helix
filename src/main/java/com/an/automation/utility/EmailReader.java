/**
 * 
 */
package com.an.automation.utility;

import java.io.IOException;
import java.util.Properties;

import javax.mail.Flags;
import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.FlagTerm;

import org.apache.commons.lang3.StringUtils;

import com.an.automation.report.HtmlReporter;

public class EmailReader {
	
	private String host;
	private String storeType; 
	private String user;
	private String password;
	
	public EmailReader(String username, String password) {
		this.host = "imap.gmail.com";
		this.storeType = "imaps";
		this.user = username;
		this.password = password;
		System.out.println(user);
		System.out.println(password);
	}


	public static void check(String host, String storeType, String user, String password) {
		try {

			// create properties
			Properties properties = new Properties();

			properties.put("mail.imap.host", host);
			properties.put("mail.imap.port", "993");
			properties.put("mail.imap.starttls.enable", "true");
			properties.put("mail.imap.ssl.trust", host);

			Session emailSession = Session.getDefaultInstance(properties);

			// create the imap store object and connect to the imap server
			Store store = emailSession.getStore(storeType);

			store.connect(host, user, password);

			// create the inbox object and open it
			Folder inbox = store.getFolder("Inbox");
			inbox.open(Folder.READ_WRITE);

			// retrieve the messages from the folder in an array and print it
			Message[] messages = inbox.search(new FlagTerm(new Flags(Flag.SEEN), false));
			System.out.println("messages.length---" + messages.length);

			for (int i = 0, n = messages.length; i < n; i++) {
				Message message = messages[i];
				message.setFlag(Flag.SEEN, true);
				System.out.println("---------------------------------");
				System.out.println("Email Number " + (i + 1));
				System.out.println("Subject: " + message.getSubject());
				System.out.println("From: " + message.getFrom()[0]);
				System.out.println("To: " + message.getRecipients(Message.RecipientType.TO)[0]);
				System.out.println("Folder: " + message.getFolder());
				System.out.println("Text: " + message.getContent().toString());

			}

			inbox.close(false);
			store.close();

		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getLatestEmail() throws MessagingException, IOException {
		Store store = null;
		Folder inbox = null;
		try {

			// create properties
			Properties properties = new Properties();

			properties.put("mail.imap.host", host);
			properties.put("mail.imap.port", "993");
			properties.put("mail.imap.starttls.enable", "true");
			properties.put("mail.imap.ssl.trust", host);

			Session emailSession = Session.getDefaultInstance(properties);

			// create the imap store object and connect to the imap server
			store = emailSession.getStore(storeType);

			store.connect(host, user, password);

			// create the inbox object and open it
			inbox = store.getFolder("Inbox");
			inbox.open(Folder.READ_WRITE);

			// retrieve the messages from the folder in an array and print it
			Message[] messages = inbox.search(new FlagTerm(new Flags(Flag.SEEN), false));
			System.out.println("messages.length---" + messages.length);
			Message latestMessage = messages[messages.length-1];
			//latestMessage.setFlag(Flag.SEEN, true);
			//HtmlReporter.pass("Got latest message from Inbox: [" + latestMessage.getContent().toString() + "]");
			return latestMessage.getContent().toString();
		} catch (NoSuchProviderException e) {
			HtmlReporter.fail("Cannot read latest email",e);
			throw e;
		} catch(Exception e){
			HtmlReporter.fail("Cannot read latest email",e);
			throw e;
		} finally {
			if(inbox != null)
				inbox.close(false);
			if(store != null)
				store.close();
		}
	}

	public static void main(String[] args) throws MessagingException, IOException {

		String host = "imap.gmail.com";
		String mailStoreType = "imaps";
		String username = "l0vehd931@gmail.com";
		String password = "smileangel";
		EmailReader registerEmails = new EmailReader(username, password);
		String message = registerEmails.getLatestEmail();
		System.out.println("here:" + message);
		String a = StringUtils.substringBetween(message, "<strong>", "</strong>");
		System.out.println(a);
	}
}