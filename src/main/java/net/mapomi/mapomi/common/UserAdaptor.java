package net.mapomi.mapomi.common;

import lombok.RequiredArgsConstructor;
import net.mapomi.mapomi.common.error.UserNotFoundException;
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
}
