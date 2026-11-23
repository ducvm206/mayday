package com.ducvm.mayday.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtService {

	@Value("${jwt.secret}")
	private String secret;

	@Value("${jwt.expiration}")
	private long expiration;

	/**
	 * Get the signing key.
	 */
	private SecretKey getSigningKey() {
		byte[] keyBytes = Decoders.BASE64.decode(secret);

		return Keys.hmacShaKeyFor(keyBytes);
	}

	/**
	 * Generate a JWT for an authenticated user.
	 */
	public String generateToken(UserDetails userDetails) {

		Date now = new Date();

		return Jwts.builder()
				.id(UUID.randomUUID().toString())
				.subject(userDetails.getUsername())
				.claim("roleId", ((CustomUserDetails) userDetails)
						.getAccount()
						.getRole()
						.getId())
				.issuedAt(now)
				.expiration(new Date(now.getTime() + expiration))
				.signWith(getSigningKey())
				.compact();
	}

	/**
	 * Parse and verify a JWT, returning its claims.
	 * Throws a JwtException if the token is malformed, tampered with or expired.
	 */
	private Claims extractAllClaims(String token) {

		return Jwts.parser()
				.verifyWith(getSigningKey())
				.build()
				.parseSignedClaims(token)
				.getPayload();
	}

	/**
	 * Extract the phone number (subject) from a JWT.
	 */
	public String extractUsername(String token) {

		return Jwts.parser()
				.verifyWith(getSigningKey())
				.build()
				.parseSignedClaims(token)
				.getPayload()
				.getSubject();
	}

	/**
	 * Extract the Role ID from a JWT.
	 */
	public Long extractRoleId(String token) {

		Claims claims = Jwts.parser()
				.verifyWith(getSigningKey())
				.build()
				.parseSignedClaims(token)
				.getPayload();

		return claims.get("roleId", Long.class);
	}

	/**
	 * Extract the JWT ID.
	 */
	public String extractJti(String token) {

		return extractAllClaims(token).getId();
	}

	/**
	 * Extract the expiry time.
	 */
	public Instant extractExpiration(String token) {

		return extractAllClaims(token).getExpiration().toInstant();
	}

	/**
	 * Validate a JWT against a user.
	 */
	public boolean isTokenValid(
			String token,
			UserDetails userDetails) {

		String username = extractUsername(token);

		return username.equals(userDetails.getUsername());
	}
}
