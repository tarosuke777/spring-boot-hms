package com.tarosuke777.hms.domain;

import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.tarosuke777.hms.entity.UserEntity;
import com.tarosuke777.hms.form.UserForm;
import com.tarosuke777.hms.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public List<UserForm> getUserList() {
        return userRepository.findAll().stream()
                .map(entity -> modelMapper.map(entity, UserForm.class)).toList();
    }

    public UserForm getUser(Integer userId) {
        UserEntity entity = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return modelMapper.map(entity, UserForm.class);
    }

    @Transactional
    public void registerUser(UserForm form, String role) {
        UserEntity entity = modelMapper.map(form, UserEntity.class);
        entity.setPassword(passwordEncoder.encode(form.getPassword()));
        entity.setRole(role);
        userRepository.save(entity);
    }

    @Transactional
    public void updateUser(UserForm form) {
        UserEntity entity = userRepository.findById(form.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        entity.setUserName(form.getUserName());

        // パスワードが入力されている場合のみ更新（MyBatisの<if>に相当）
        if (form.getPassword() != null && !form.getPassword().isEmpty()) {
            entity.setPassword(passwordEncoder.encode(form.getPassword()));
        }

        userRepository.save(entity);
    }

    @Transactional
    public void deleteUser(Integer userId) {
        userRepository.deleteById(userId);
    }

}
