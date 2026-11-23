package com.ducvm.mayday.service;

import com.ducvm.mayday.exceptions.OtpTooFrequentException;
import com.ducvm.mayday.util.Constant;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OtpService {

	private static final Duration TTL = Duration.ofSeconds(Constant.OTP_EXPIRY_SECONDS);
	private static final Duration RESEND_COOLDOWN = Duration.ofSeconds(Constant.OTP_RESEND_COOLDOWN_SECONDS);

	private record OtpEntry(String code, Instant issuedAt, Instant expiresAt) {}

	private final Map<String, OtpEntry> store = new ConcurrentHashMap<>();
	private final SecureRandom random = new SecureRandom();

	public String generate(String phone) {
		Instant now = Instant.now();

		OtpEntry existing = store.get(phone);
		if (existing != null && existing.issuedAt().plus(RESEND_COOLDOWN).isAfter(now)) {
			throw new OtpTooFrequentException();
		}

		String code = String.format("%06d", random.nextInt(1_000_000));
		store.put(phone, new OtpEntry(code, now, now.plus(TTL)));
		return code;
	}
}
