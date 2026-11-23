package com.ducvm.mayday.security;

import com.ducvm.mayday.entity.Account;
import com.ducvm.mayday.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	private final AccountRepository accountRepository;

	@Override
	public UserDetails loadUserByUsername(String phone)
			throws UsernameNotFoundException {

		Account account = accountRepository.findByUsername(phone)
				.orElseThrow(() ->
						new UsernameNotFoundException("Account not found")
				);

		return new CustomUserDetails(account);
	}
}
