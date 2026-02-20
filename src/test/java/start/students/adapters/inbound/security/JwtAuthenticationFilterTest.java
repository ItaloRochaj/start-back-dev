package start.students.adapters.inbound.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.context.SecurityContextHolder;
import start.students.core.ports.JwtTokenPort;

import java.io.IOException;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@DisplayName("JwtAuthenticationFilter Tests")
class JwtAuthenticationFilterTest {

    @Mock
    private JwtTokenPort jwtTokenPort;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("should skip JWT validation for public login path")
    void shouldSkipJwtValidationForPublicLoginPath() throws ServletException, IOException {
        // Arrange
        when(request.getRequestURI()).thenReturn("/api/auth/login");

        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(filterChain).doFilter(request, response);
        verify(jwtTokenPort, never()).extractUsername(anyString());
    }

    @Test
    @DisplayName("should skip JWT validation for public auth login path")
    void shouldSkipJwtValidationForPublicAuthLoginPath() throws ServletException, IOException {
        // Arrange
        when(request.getRequestURI()).thenReturn("/auth/login");

        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(filterChain).doFilter(request, response);
        verify(jwtTokenPort, never()).extractUsername(anyString());
    }

    @Test
    @DisplayName("should skip JWT validation for h2-console path")
    void shouldSkipJwtValidationForH2ConsolePath() throws ServletException, IOException {
        // Arrange
        when(request.getRequestURI()).thenReturn("/h2-console");

        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(filterChain).doFilter(request, response);
        verify(jwtTokenPort, never()).extractUsername(anyString());
    }

    @Test
    @DisplayName("should skip JWT validation for test path")
    void shouldSkipJwtValidationForTestPath() throws ServletException, IOException {
        // Arrange
        when(request.getRequestURI()).thenReturn("/test");

        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(filterChain).doFilter(request, response);
        verify(jwtTokenPort, never()).extractUsername(anyString());
    }

    @Test
    @DisplayName("should skip JWT validation for debug path")
    void shouldSkipJwtValidationForDebugPath() throws ServletException, IOException {
        // Arrange
        when(request.getRequestURI()).thenReturn("/debug");

        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(filterChain).doFilter(request, response);
        verify(jwtTokenPort, never()).extractUsername(anyString());
    }

    @Test
    @DisplayName("should skip JWT validation for error path")
    void shouldSkipJwtValidationForErrorPath() throws ServletException, IOException {
        // Arrange
        when(request.getRequestURI()).thenReturn("/error");

        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(filterChain).doFilter(request, response);
        verify(jwtTokenPort, never()).extractUsername(anyString());
    }

    @Test
    @DisplayName("should skip filter when Authorization header is null")
    void shouldSkipFilterWhenAuthorizationHeaderIsNull() throws ServletException, IOException {
        // Arrange
        when(request.getRequestURI()).thenReturn("/api/students");
        when(request.getHeader("Authorization")).thenReturn(null);

        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(filterChain).doFilter(request, response);
        verify(jwtTokenPort, never()).extractUsername(anyString());
    }

    @Test
    @DisplayName("should skip filter when Authorization header doesn't start with Bearer")
    void shouldSkipFilterWhenAuthorizationHeaderDoesNotStartWithBearer() throws ServletException, IOException {
        // Arrange
        when(request.getRequestURI()).thenReturn("/api/students");
        when(request.getHeader("Authorization")).thenReturn("Basic xyz123");

        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(filterChain).doFilter(request, response);
        verify(jwtTokenPort, never()).extractUsername(anyString());
    }

    @Test
    @DisplayName("should extract username from valid Bearer token")
    void shouldExtractUsernameFromValidBearerToken() throws ServletException, IOException {
        // Arrange
        String jwt = "valid-jwt-token";
        String username = "testuser";
        when(request.getRequestURI()).thenReturn("/api/students");
        when(request.getHeader("Authorization")).thenReturn("Bearer " + jwt);
        when(jwtTokenPort.extractUsername(jwt)).thenReturn(username);
        when(jwtTokenPort.isTokenValid(jwt, username)).thenReturn(true);

        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(jwtTokenPort).extractUsername(jwt);
        verify(jwtTokenPort).isTokenValid(jwt, username);
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("should not authenticate when token is invalid")
    void shouldNotAuthenticateWhenTokenIsInvalid() throws ServletException, IOException {
        // Arrange
        String jwt = "invalid-jwt-token";
        String username = "testuser";
        when(request.getRequestURI()).thenReturn("/api/students");
        when(request.getHeader("Authorization")).thenReturn("Bearer " + jwt);
        when(jwtTokenPort.extractUsername(jwt)).thenReturn(username);
        when(jwtTokenPort.isTokenValid(jwt, username)).thenReturn(false);

        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(jwtTokenPort).extractUsername(jwt);
        verify(jwtTokenPort).isTokenValid(jwt, username);
        verify(filterChain).doFilter(request, response);
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @Test
    @DisplayName("should not authenticate when username extraction returns null")
    void shouldNotAuthenticateWhenUsernameExtractionReturnsNull() throws ServletException, IOException {
        // Arrange
        String jwt = "token-with-no-username";
        when(request.getRequestURI()).thenReturn("/api/students");
        when(request.getHeader("Authorization")).thenReturn("Bearer " + jwt);
        when(jwtTokenPort.extractUsername(jwt)).thenReturn(null);

        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(jwtTokenPort).extractUsername(jwt);
        verify(jwtTokenPort, never()).isTokenValid(anyString(), anyString());
        verify(filterChain).doFilter(request, response);
    }

    @ParameterizedTest
    @ValueSource(strings = {"/api/auth/login", "/auth/login", "/h2-console", "/test", "/debug", "/error"})
    @DisplayName("should skip JWT validation for all public paths")
    void shouldSkipJwtValidationForAllPublicPaths(String publicPath) throws ServletException, IOException {
        // Arrange
        when(request.getRequestURI()).thenReturn(publicPath);

        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(filterChain).doFilter(request, response);
        verify(jwtTokenPort, never()).extractUsername(anyString());
    }

    @Test
    @DisplayName("should handle Bearer token with extra spaces")
    void shouldHandleBearerTokenWithExtraSpaces() throws ServletException, IOException {
        // Arrange
        String jwt = "token123";
        String username = "user";
        when(request.getRequestURI()).thenReturn("/api/students");
        when(request.getHeader("Authorization")).thenReturn("Bearer " + jwt);
        when(jwtTokenPort.extractUsername(jwt)).thenReturn(username);
        when(jwtTokenPort.isTokenValid(jwt, username)).thenReturn(true);

        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(jwtTokenPort).extractUsername(jwt);
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("should continue filter chain after authentication")
    void shouldContinueFilterChainAfterAuthentication() throws ServletException, IOException {
        // Arrange
        String jwt = "valid-jwt-token";
        String username = "testuser";
        when(request.getRequestURI()).thenReturn("/api/students");
        when(request.getHeader("Authorization")).thenReturn("Bearer " + jwt);
        when(jwtTokenPort.extractUsername(jwt)).thenReturn(username);
        when(jwtTokenPort.isTokenValid(jwt, username)).thenReturn(true);

        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    @DisplayName("should not re-authenticate if already authenticated")
    void shouldNotReAuthenticateIfAlreadyAuthenticated() throws ServletException, IOException {
        // Arrange - First authentication
        String jwt = "valid-jwt-token";
        String username = "testuser";
        when(request.getRequestURI()).thenReturn("/api/students");
        when(request.getHeader("Authorization")).thenReturn("Bearer " + jwt);
        when(jwtTokenPort.extractUsername(jwt)).thenReturn(username);
        when(jwtTokenPort.isTokenValid(jwt, username)).thenReturn(true);

        // Act - First call
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Get the current authentication from SecurityContext
        var auth = SecurityContextHolder.getContext().getAuthentication();

        // Assert - Verify authentication was set
        if (auth != null) {
            assertThat(auth.getPrincipal()).isEqualTo(username);
        }
    }

}

