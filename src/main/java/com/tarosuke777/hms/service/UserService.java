package com.tarosuke777.hms.service;

import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.tarosuke777.hms.enums.Role;
import com.tarosuke777.hms.entity.UserEntity;
import com.tarosuke777.hms.form.UserForm;
import com.tarosuke777.hms.mapper.UserMapper;
import com.tarosuke777.hms.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public List<UserForm> getUserList() {
        return userRepository.findAll().stream().map(entity -> userMapper.toForm(entity)).toList();
    }

    public UserForm getUser(Integer id) {
        UserEntity entity = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return userMapper.toForm(entity);
    }

    @Transactional
    public void registerUser(UserForm form, Role role) {
        UserEntity entity = userMapper.toEntity(form);
        entity.setPassword(passwordEncoder.encode(form.getPassword()));
        entity.setRole(role);
        userRepository.save(entity);
    }

    @Transactional
    public void updateUser(UserForm form) {
        UserEntity entity = userRepository.findById(form.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        entity.setName(form.getName());

        // ロールは画面から変更可能なのでフォーム値を反映
        if (form.getRole() != null) {
            entity.setRole(form.getRole());
        }

        // パスワードが入力されている場合のみ更新（MyBatisの<if>に相当）
        if (form.getPassword() != null && !form.getPassword().isEmpty()) {
            entity.setPassword(passwordEncoder.encode(form.getPassword()));
        }

        userRepository.save(entity);
    }

    @Transactional
    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }

}
