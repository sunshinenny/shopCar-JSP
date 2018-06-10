package com.domain;

public class userInfo {
	/**
	 * username=用户名 password=密码 email=电子邮件账号 sex=性别
	 */
	String username;
	String password;
	static String email;
	String sex;

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

	public static String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		userInfo.email = email;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

}
