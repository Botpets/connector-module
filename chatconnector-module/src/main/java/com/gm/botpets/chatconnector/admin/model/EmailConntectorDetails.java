package com.gm.botpets.chatconnector.admin.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@JsonInclude(Include.NON_NULL)
public class EmailConntectorDetails implements Serializable {

	public ImapInfo getImapInfo() {
		return imapInfo;
	}
	public void setImapInfo(ImapInfo imapInfo) {
		this.imapInfo = imapInfo;
	}
	public SmtpInfo getSmtpInfo() {
		return smtpInfo;
	}
	public void setSmtpInfo(SmtpInfo smtpInfo) {
		this.smtpInfo = smtpInfo;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	private static final long serialVersionUID = 1L;

	private ImapInfo imapInfo;
	private SmtpInfo smtpInfo;
	private String username;
	private String password;

}
