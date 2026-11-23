package com.ducvm.mayday.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LogSmsSender implements SmsSender {
	private static final Logger log = LoggerFactory.getLogger(LogSmsSender.class);

	@Override
	public void send(String phone, String message) {
		log.info("SMS to {}: {}", phone, message);   // DEV ONLY: replace with a real provider
	}
}
