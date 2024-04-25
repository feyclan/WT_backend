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

	/**
	 * This method is used to filter each HTTP request and perform token validation.
	 * It fetches the 'Authorization' header from the request, which should contain the token.
	 * The token is then used to fetch the corresponding user from the database.
	 * If the user does not exist (i.e., the token is invalid), it sends an HTTP 401 error and stops further processing.
	 * If the user exists, it adds the user to the request attributes and proceeds with the next filter in the chain.
	 *
	 * @param request The HttpServletRequest object that contains the request the client made of the servlet.
	 * @param response The HttpServletResponse object that contains the response the servlet sends to the client.
	 * @param filterChain The FilterChain object provided by the servlet container, used to invoke the next filter in the chain.
	 * @throws ServletException If the request for the GET could not be handled.
	 * @throws IOException If an input or output error is detected when the servlet handles the GET request.
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		// Fetch the 'Authorization' header from the request
		String token = request.getHeader("Authorization");

		// Check if the token is not null
		if (token != null) {

			// Remove the 'Bearer ' prefix from the token
			token = token.substring(7);

			// Fetch the user from the database using the token
			Optional<User> optionalUser = userRepository.findByToken(token);

			// Check if the user does not exist
			if (optionalUser.isEmpty()) {
				// If the user does not exist, send an HTTP 401 error and stop further processing
				response.sendError(401);
				return;
			} else {
				// If the user exists, add the user to the request attributes
				request.setAttribute("WT_USER", optionalUser.get());
			}
		}

		// Proceed with the next filter in the chain
		filterChain.doFilter(request, response);
	}
}
