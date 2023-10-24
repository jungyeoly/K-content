package com.example.myapp.user.mber.service;

import java.util.List;

import com.example.myapp.user.mber.model.MberUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
		  Mber mber = mberService.selectMberbyId(mberId);

		    if (mber == null) {
		        throw new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + mberId);
		    }
		    String mberStat = mber.getMberStat();
		    boolean enabled = !"비활성화".equals(mberStat);
		    String mberGender = commonCodeService.mberGenderByCode(mberId);
		    String mberRole = mber.getMberRole();

		    List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(mberRole);
		    return new MberUserDetails(mber.getMberId(), mber.getMberPwd(), authorities, mberGender, enabled);
		}
}
