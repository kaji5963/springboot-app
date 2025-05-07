package com.example.demo.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SignupMessage {

	SUCCEED(MessageConst.SIGNUP_RESIST_SUCCEED, false),
	
	EXISTED_LOGIN_ID(MessageConst.SIGNUP_EXISTED_LOGINID, true);
	
	private String messageId;
	
	private boolean isError;
}
