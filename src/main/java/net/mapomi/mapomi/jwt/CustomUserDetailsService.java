package net.mapomi.mapomi.jwt;

import lombok.RequiredArgsConstructor;
import net.mapomi.mapomi.domain.user.User;
import net.mapomi.mapomi.repository.UserRepository;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    //    @Cacheable(value = "loginUser", key = "#username", cacheManager = "testCacheManager")
    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findById(Long.valueOf(username))
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        return new CustomUserDetails(user.getId(), user.getRole().getKey());
    }
}
