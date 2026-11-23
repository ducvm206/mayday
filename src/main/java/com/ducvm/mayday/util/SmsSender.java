package com.ducvm.mayday.util;

public interface SmsSender {
	void send(String phone, String message);
}
