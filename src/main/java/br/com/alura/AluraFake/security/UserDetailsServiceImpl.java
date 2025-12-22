package br.com.alura.AluraFake.security;

import br.com.alura.AluraFake.domain.User;
import br.com.alura.AluraFake.exception.ObjectNotFoundException;
import br.com.alura.AluraFake.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

        @Autowired
        private UserRepository userRepository;

        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            User user = userRepository.findByEmailIgnoreCase(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));
            return new UserDetailsImpl(user);
        }

        public User findByToken() {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();

            return userRepository.findById(principal.getId())
                    .orElseThrow(() -> new ObjectNotFoundException("Usuário não encontrado"));
        }
}
