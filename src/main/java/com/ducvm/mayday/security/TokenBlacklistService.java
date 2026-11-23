package com.ducvm.mayday.security;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TokenBlacklistService {
	private final Map<String, Instant> blacklist = new ConcurrentHashMap<>();

	public void blacklist(String jti, Instant expiresAt) {
		blacklist.put(jti, expiresAt);
	}

	public boolean isBlacklisted(String jti) {
		return jti != null && blacklist.containsKey(jti);
	}

	@Scheduled(fixedRate = 600_000)
	public void purgeExpired() {
		blacklist.values().removeIf(exp -> exp.isBefore(Instant.now()));
	}
}
