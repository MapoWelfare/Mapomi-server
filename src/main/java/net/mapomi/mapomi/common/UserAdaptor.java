package net.mapomi.mapomi.common;

import lombok.RequiredArgsConstructor;
import net.mapomi.mapomi.common.error.UserNotFoundException;
import net.mapomi.mapomi.domain.Role;
import net.mapomi.mapomi.domain.user.User;
import net.mapomi.mapomi.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserAdaptor {
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public User queryUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
    }
    @Transactional(readOnly = true)
    public User queryUser(Long userId, String role) {
        if(role.equals("disabled"))
            return userRepository.findDisabledById(userId).orElseThrow(UserNotFoundException::new);
        else if(role.equals("abled"))
            return userRepository.findAbledById(userId).orElseThrow(UserNotFoundException::new);
        else
            return userRepository.findObserverById(userId).orElseThrow(UserNotFoundException::new);
    }
}
