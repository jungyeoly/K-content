package com.example.myapp.user.mber.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.myapp.user.mber.dao.IMberRepository;
import com.example.myapp.user.mber.model.Mber;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private IMberRepository mberRepository;

	@Autowired
    private PasswordEncoder passwordEncoder;
    
	@Override
	public UserDetails loadUserByUsername(String mberId) throws UsernameNotFoundException {
		// 사용자 이름(여기서는 mberId)을 기반으로 사용자 정보를 데이터베이스에서 가져온다.
		Mber mber = mberRepository.selectMberbyId(mberId);
		
	
		if (mber == null) {
			throw new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + mberId);
		}
		System.out.println(mber.getMberId());
		System.out.println(mber.getMberPwd());
		System.out.println(mber.getMberEmail());
		System.out.println(mber.getMberName());
		System.out.println(mber.getMberBirth());
		System.out.println(mber.getMberPhone());
		System.out.println(mber.getMberRegistDate());
		System.out.println(mber.getMberUpdateDate());
		System.out.println(mber.getMberGenderCode());
		System.out.println(mber.getMberStatCode());
		
		String mberStatCode = mber.getMberStatCode();
		if ("C0201".equals(mberStatCode)) {
			throw new DisabledException("비활성화된 계정입니다. 관리자에게 문의하세요.");
		} else if ("C0202".equals(mberStatCode)) {
			// 사용자가 활성화 상태인 경우 UserDetails 객체를 생성하고 반환한다.
			return User.builder().username(mber.getMberId()).password(passwordEncoder.encode(mber.getMberPwd())).roles("USER").build();
		} else if ("C0203".equals(mberStatCode)) {
			// 사용자가 어드민인 경우 UserDetails 객체를 생성하고 반환한다.
			System.out.println(mber.getMberId());
			System.out.println(passwordEncoder.encode(mber.getMberPwd()));
			
			
			return User.builder().username(mber.getMberId()).password(passwordEncoder.encode(mber.getMberPwd())).roles("ADMIN").build();
		}

		// 다른 상태 코드에 대한 처리를 원하면 여기에 추가하세요.

		// 어떤 경우에도 해당하지 않으면 예외 처리
		throw new BadCredentialsException("올바르지 않은 계정 상태: " + mberStatCode);

	}
}
