package com.gm.botpets.chatconnector.listener;

import java.io.IOException;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.event.MessageCountAdapter;
import javax.mail.event.MessageCountEvent;
import javax.mail.internet.InternetAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.gm.botpets.chatconnector.admin.model.EmailConntectorDetails;
import com.gm.botpets.chatconnector.admin.model.ImapInfo;
import com.gm.botpets.chatconnector.commons.ConnectorConfig;
import com.gm.botpets.chatconnector.model.ChannelType;
import com.gm.botpets.chatconnector.model.ChatRequest;
import com.gm.botpets.chatconnector.model.EmailDTO;
import com.gm.botpets.chatconnector.model.NLPRequest;
import com.gm.botpets.chatconnector.model.User;
import com.gm.botpets.chatconnector.service.IAdminService;
import com.gm.botpets.chatconnector.service.IChatService;
import com.gm.botpets.chatconnector.service.IMessageProcessor;
import com.gm.botpets.chatconnector.util.ChatConnectorUtil;
import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPStore;

@Component
public class EmailListener {

	private Logger logger = LoggerFactory.getLogger(EmailListener.class);

	private static String username;
	private static String password;

	/*@Autowired
	private Environment env;*/

	@Autowired
	private IMessageProcessor messageProcessor;

	@Autowired
	private ChatConnectorUtil chatConnectorUtil;

	@Autowired
	private IAdminService adminService;

	@Autowired
	private IChatService chatService;

	@Autowired
	private ConnectorConfig connectorConfig;

	@Scheduled(fixedDelay = 15000)
	public void listen() {

		EmailConntectorDetails connectorDetails = ConnectorConfig.getEmailConntectorDetails();

		if (connectorDetails != null) {
			String username = connectorDetails.getUsername();
			String password = connectorDetails.getPassword();
			ImapInfo imapInfo = connectorDetails.getImapInfo();

			// loadEmailConnectorDetails();
			Properties properties = new Properties();
			properties.put("mail.store.protocol", "imaps");
			properties.put("mail.imaps.host", imapInfo.getHost());
			properties.put("mail.imaps.port", imapInfo.getPort());
			properties.put("mail.imaps.timeout", "10000");
			Session session = Session.getInstance(properties);

			IMAPStore store = null;
			Folder inbox = null;

			try {
				store = (IMAPStore) session.getStore("imaps");
				// setUsername(env.getProperty("email.server.username"));
				// setPassword(env.getProperty("email.server.password"));
				setUsername(username);
				setPassword(password);
				store.connect(username, password);
				logger.info("connected to IMAPStore()");
				if (!store.hasCapability("IDLE")) {
					throw new RuntimeException("IDLE not supported");
				}

				inbox = (IMAPFolder) store.getFolder("INBOX");
				inbox.addMessageCountListener(new MessageCountAdapter() {

					@Override
					public void messagesAdded(MessageCountEvent event) {
						Message[] messages = event.getMessages();

						for (Message message : messages) {
							try {
								long starttime = System.currentTimeMillis();
								ChatRequest chatRequest = new ChatRequest();
								InternetAddress address = (InternetAddress) message.getFrom()[0];
								String sender = address.getAddress();
								String subject = message.getSubject();
								EmailDTO emailDTO = new EmailDTO();
								emailDTO.setRecipient(sender);
								emailDTO.setSubject(subject);
								String sessionId = chatConnectorUtil.base64EncodedSessionId(ChannelType.EMAIL + sender);
								String[] msgId = message.getHeader("Message-ID");
								if (msgId != null && msgId.length > 0) {
									emailDTO.setMessageId(msgId[0]);
								}
								Object response = null;
								// if (toEmail.equals(address.getAddress())) {
								logger.info("Valid sender");
								String contentType = message.getContentType();
								logger.info("Content Type" + contentType);
								Object content = null;
								NLPRequest nlpRequest = null;
								if (contentType.contains("text/plain") || contentType.contains("text/html")) {
									String messageContent = null;
									try {
										content = message.getContent();
										nlpRequest = new NLPRequest();
										nlpRequest.setQuestion(content.toString().trim());
										nlpRequest.setSessionId(sessionId);
										User user = new User();
										user.setEmail(sender);
										nlpRequest.setUser(user);
										if (content != null) {
											messageContent = content.toString();
											response = messageProcessor.processMessageRequest(nlpRequest);
											chatRequest.setChannelType(ChannelType.EMAIL);
											chatRequest.setChannelDTO(emailDTO);
											chatRequest.setMessage(response);
											long endtime = System.currentTimeMillis();
											long difftime = endtime - starttime;
											logger.info("Time taken to process in Sec's:" + difftime / 1000);
											chatService.processChatRequest(chatRequest);
											logger.info("Message " + messageContent);

										}
									} catch (Exception ex) {
										messageContent = "Error downloading content";
										logger.error("Error downloading content", ex);
									}
								} else {
									try {
										content = message.getContent();
									} catch (IOException ioe) {
										logger.error("Failed to read the content", ioe);
									}
									if (contentType.contains("multipart")) {
										Multipart multiPart = (Multipart) content;
										int multiPartCount = multiPart.getCount();
										for (int j = 0; j < multiPartCount - 1; j++) {
											BodyPart bodyPart = multiPart.getBodyPart(j);

											try {
												Object emailMsg = bodyPart.getContent();
												logger.info("Message is " + emailMsg.toString());
												nlpRequest = new NLPRequest();
												nlpRequest.setQuestion(emailMsg.toString().trim());
												nlpRequest.setSessionId(sessionId);
												User user = new User();
												user.setEmail(sender);
												nlpRequest.setUser(user);
												response = messageProcessor.processMessageRequest(nlpRequest);
												chatRequest.setChannelType(ChannelType.EMAIL);
												chatRequest.setChannelDTO(emailDTO);
												chatRequest.setMessage(response);
												long endtime = System.currentTimeMillis();
												long difftime = endtime - starttime;
												logger.info("Time taken to process in Sec's:" + difftime / 1000);
												chatService.processChatRequest(chatRequest);
												break;
											} catch (IOException e) {
												e.printStackTrace();
											} catch (Exception e) {
												e.printStackTrace();
											}
										}
									}
								}
								// }

							} catch (MessagingException e) {
								e.printStackTrace();
							}
						}
					}
				});

				IdleThread idleThread = new IdleThread(inbox);
				idleThread.setDaemon(false);
				idleThread.start();

				idleThread.join();

			} catch (Exception e) {
				e.printStackTrace();
			} finally {

				close(inbox);
				close(store);
			}
		} else {
			// TODO throw exception
		}
	}

	private static class IdleThread extends Thread {
		private Logger logger = LoggerFactory.getLogger(IdleThread.class);
		private final Folder folder;
		private volatile boolean running = true;

		public IdleThread(Folder folder) {
			super();
			this.folder = folder;
		}

		public synchronized void kill() {

			if (!running)
				return;
			this.running = false;
		}

		@Override
		public void run() {
			logger.debug("IDLE thead started");
			while (running) {
				try {
					ensureOpen(folder);
					logger.info("**************** Listening for the new emails ****************");
					if (folder instanceof IMAPFolder) {
						IMAPFolder f = (IMAPFolder) folder;
						f.idle();
					}
				} catch (Exception e) {
					logger.error("Failed to open the folder");
					try {
						Thread.sleep(100);
					} catch (InterruptedException e1) {
						// ignore
					}
				}

			}
		}
	}

	public static void close(final Folder folder) {
		try {
			if (folder != null && folder.isOpen()) {
				folder.close(false);
			}
		} catch (final Exception e) {
			// ignore
		}

	}

	public static void close(final Store store) {
		try {
			if (store != null && store.isConnected()) {
				store.close();
			}
		} catch (final Exception e) {
			// ignore
		}

	}

	public static void ensureOpen(final Folder folder) throws MessagingException {

		if (folder != null) {
			Store store = folder.getStore();
			if (store != null && !store.isConnected()) {
				store.connect(username, password);
			}
		} else {
			throw new MessagingException("Unable to open a null folder");
		}

		if (folder.exists() && !folder.isOpen() && (folder.getType() & Folder.HOLDS_MESSAGES) != 0) {
			folder.open(Folder.READ_ONLY);
			if (!folder.isOpen())
				throw new MessagingException("Unable to open folder " + folder.getFullName());
		}

	}

	public void setPassword(String password) {
		EmailListener.password = password;
	}

	public void setUsername(String username) {
		EmailListener.username = username;
	}
}