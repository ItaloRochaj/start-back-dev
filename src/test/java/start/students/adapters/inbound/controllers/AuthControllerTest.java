package start.students.adapters.inbound.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import start.students.adapters.inbound.http.ApiResponse;
import start.students.core.application.dtos.LoginInputDTO;
import start.students.core.application.dtos.TokenResponseDTO;
import start.students.core.application.usecases.AuthenticationUseCase;
import start.students.core.domain.exceptions.DomainException;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthController Tests")
class AuthControllerTest {

    @Mock
    private AuthenticationUseCase authenticationUseCase;

    @InjectMocks
    private AuthController controller;

    @Test
    @DisplayName("Should login successfully")
    void shouldLoginSuccessfully() {
        // ARRANGE
        LoginInputDTO input = new LoginInputDTO();
        input.setUsername("user123");
        input.setPassword("pass456");

        TokenResponseDTO tokenResponse = TokenResponseDTO.builder()
                .token("jwt-token-123")
                .username("user123")
                .build();

        when(authenticationUseCase.authenticate(any(LoginInputDTO.class))).thenReturn(tokenResponse);

        // ACT
        ResponseEntity<ApiResponse<TokenResponseDTO>> response = controller.login(input);

        // ASSERT
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isSuccess()).isTrue();
        assertThat(response.getBody().getData().getToken()).isEqualTo("jwt-token-123");

        verify(authenticationUseCase, times(1)).authenticate(any(LoginInputDTO.class));
    }

    @Test
    @DisplayName("Should return error when credentials are invalid")
    void shouldReturnErrorWhenCredentialsAreInvalid() {
        // ARRANGE
        LoginInputDTO input = new LoginInputDTO();
        input.setUsername("user123");
        input.setPassword("wrongpass");

        when(authenticationUseCase.authenticate(any(LoginInputDTO.class)))
                .thenThrow(new DomainException("Credenciais inválidas"));

        // ACT
        ResponseEntity<ApiResponse<TokenResponseDTO>> response = controller.login(input);

        // ASSERT
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isSuccess()).isFalse();

        verify(authenticationUseCase, times(1)).authenticate(any(LoginInputDTO.class));
    }

    @Test
    @DisplayName("Should handle DomainException in login")
    void shouldHandleDomainExceptionInLogin() {
        // ARRANGE
        LoginInputDTO input = new LoginInputDTO();
        input.setUsername("user123");
        input.setPassword("pass456");

        when(authenticationUseCase.authenticate(any(LoginInputDTO.class)))
                .thenThrow(new DomainException("User not found"));

        // ACT
        ResponseEntity<ApiResponse<TokenResponseDTO>> response = controller.login(input);

        // ASSERT
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().isSuccess()).isFalse();
    }
}

