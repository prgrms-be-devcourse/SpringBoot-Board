package com.kdt.boardMission.service;

import com.kdt.boardMission.domain.User;
import com.kdt.boardMission.dto.UserDto;
import com.kdt.boardMission.repository.UserRepository;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.kdt.boardMission.dto.UserDto.convertUser;
import static com.kdt.boardMission.dto.UserDto.convertUserDto;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public long saveUser(UserDto userDto) {
        User user = convertUser(userDto);
        User save = userRepository.save(user);
        return save.getId();
    }

    public void updateUser(UserDto userDto) throws NotFoundException {
        User user = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new NotFoundException("해당 아이디를 가진 유저가 없습니다."));
        user.updateHobby(userDto.getHobby());
        user.updateAge(userDto.getAge());
    }

    public void deleteUser(UserDto userDto) {
        userRepository.deleteById(userDto.getId());
    }

    @Transactional(readOnly = true)
    public UserDto findUserById(long userId) throws NotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("해당 아이디를 가진 유저가 없습니다."));
        return convertUserDto(user);
    }

    @Transactional(readOnly = true)
    public Page<UserDto> findUserByName(String name, Pageable pageable) {
        return userRepository.findByName(name, pageable).map(UserDto::convertUserDto);
    }

    @Transactional(readOnly = true)
    public Page<UserDto> findAll(Pageable pageable) {
        return userRepository.findAll(pageable).map(UserDto::convertUserDto);
    }
}
