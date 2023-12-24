package com.programmers.springboard.member.service;

import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.programmers.springboard.global.config.jwt.JwtAuthentication;
import com.programmers.springboard.global.config.jwt.JwtAuthenticationToken;
import com.programmers.springboard.member.domain.Member;
import com.programmers.springboard.member.exception.DuplicateIdException;
import com.programmers.springboard.member.exception.MemberNotFoundException;
import com.programmers.springboard.member.repository.MemberRepository;
import com.programmers.springboard.member.dto.MemberCreateRequest;
import com.programmers.springboard.member.dto.MemberLoginRequest;
import com.programmers.springboard.member.dto.MemberUpdateRequest;
import com.programmers.springboard.member.dto.MemberLoginResponse;
import com.programmers.springboard.member.dto.MemberResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MemberService {

	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;

	public MemberLoginResponse login(MemberLoginRequest request) {
		JwtAuthenticationToken authenticationToken = new JwtAuthenticationToken(request.loginId(), request.password());
		Authentication authenticated = authenticationManager.authenticate(authenticationToken);
		JwtAuthentication authentication = (JwtAuthentication) authenticated.getPrincipal();
		Member member = (Member) authenticated.getDetails();
		member.updateLastLoginDate();
		return new MemberLoginResponse(authentication.getToken() , member.getId(), member.getRole().name());
	}

	@Transactional(readOnly = true)
	public MemberResponse getMemberById(Long id) {
		return memberRepository.findById(id)
			.map(MemberResponse::of)
			.orElseThrow(MemberNotFoundException::new);
	}

	public MemberResponse createMember(MemberCreateRequest request) {
		validateDuplicateId(request);
		String encodedPassword = passwordEncoder.encode(request.password());
		Member member = request.toEntity(encodedPassword);
		Member savedMember = memberRepository.save(member);
		return MemberResponse.of(savedMember);
	}

	private void validateDuplicateId(MemberCreateRequest request) {
		if (memberRepository.findByLoginId(request.loginId()).isPresent()) {
			throw new DuplicateIdException();
		}
	}

	public MemberResponse updateMember(Long id, MemberUpdateRequest request) {
		Member member = memberRepository.findById(id)
			.orElseThrow(MemberNotFoundException::new);
		String encodedPassword = passwordEncoder.encode(request.password());
		member.changePassword(encodedPassword);
		return MemberResponse.of(member);
	}

	public void deleteMembers(List<Long> ids) {
		List<Member> members = memberRepository.findAllById(ids);
		memberRepository.deleteAll(members);
	}

}
