package nl.workingtalent.wtacademy.filter;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nl.workingtalent.wtacademy.user.IUserRepository;
import nl.workingtalent.wtacademy.user.User;

@Component
public class CheckTokenFilter extends OncePerRequestFilter {

    @Autowired
    private IUserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = request.getHeader("Authorization");

        if (token != null) {

            token = token.substring(7);

            Optional<User> optionalUser = userRepository.findByToken(token);

            if (optionalUser.isEmpty()) {
                response.sendError(401);
                return;
            } else {

                request.setAttribute("WT_USER", optionalUser.get());

            }
        }

        filterChain.doFilter(request, response);
    }
}
