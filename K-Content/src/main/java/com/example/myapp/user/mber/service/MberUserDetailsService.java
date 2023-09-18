package com.example.myapp.user.mber.service;

import java.util.List;

import com.example.myapp.user.mber.model.MberUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.myapp.commoncode.service.ICommonCodeService;
import com.example.myapp.user.mber.model.Mber;


@Service
public class MberUserDetailsService implements UserDetailsService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private IMberService mberService;

	@Autowired
	private ICommonCodeService commonCodeService;

	@Override
	public UserDetails loadUserByUsername(String mberId) throws UsernameNotFoundException {
		// 사용자 이름(여기서는 mberId)을 기반으로 사용자 정보를 데이터베이스에서 가져온다.
		Mber mber = mberService.selectMberbyId(mberId);
		String mberStat = commonCodeService.mberStatByCode(mberId);
		String mberGender = commonCodeService.mberGenderByCode(mberId);
		String mberRole = commonCodeService.mberRoleByCode(mberId);

//

		if (mberStat == "DEACTIVATED") {
			throw new DisabledException("비활성화된 계정입니다. 관리자에게 문의하세요.");
		} else if (mberStat.equals("ACTIVATED")) {
			// 사용자가 활성화 상태인 경우 UserDetails 객체를 생성하고 반환한다.
			List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(mberRole);

			return new MberUserDetails(mber.getMberId(), mber.getMberPwd(), authorities, mber.getMberName(), mber.getMberEmail(), mberGender, mber.getMberBirth(), mber.getMberPhone(),
					mber.getMberRegistDate());

		}

//			return User.builder().username(mber.getMberId()).password(mber.getMberPwd()).roles("ADMIN").build();

		// 다른 상태 코드에 대한 처리를 원하면 여기에 추가하세요.

		throw new BadCredentialsException("올바르지 않은 계정 상태: " + mberStat);

	}
}
