package service;

import model.User;
import model.dto.UserDto;
import model.records.Response;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import repository.UserRepository;
import utils.PasswordHasher;
import variables.Session;
import view.ViewRender;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class LoginTest {
    private AccountService accountService;
    private UserRepository<User> userRepository;
    private PasswordHasher passwordHasher;
    private ViewRender viewRender;


    @Mock
    private UserDto userDto;
    @Before
    public void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        passwordHasher = Mockito.mock(PasswordHasher.class);
        viewRender = Mockito.mock(ViewRender.class);

        accountService = new AccountService(userRepository, viewRender, passwordHasher);
    }

    @Test
    public void shouldReturnUserNotFound_whenUserDoesNotExist() {
        String username = "nonExistentUser";
        String password = "password123";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        Response result = accountService.loginUser(username, password, null);

        assertEquals("User not found", result.message());
    }

    @Test
    public void shouldReturnWrongPassword_whenPasswordDoesNotMatch() throws NoSuchAlgorithmException {
        // Arrange
        String username = "validUser";
        String password = "wrongPassword";
        String correctPassword = "correctPassword";

        User user = new User(username, correctPassword, "user@example.com", "nick", null);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        when(passwordHasher.verifyPassword(password, correctPassword)).thenReturn(false);

        Response result = accountService.loginUser(username, password, null);

        assertEquals("Wrong password", result.message());
    }

    @Test
    public void shouldReturnLoggedInSuccessfully_whenPasswordMatches() throws NoSuchAlgorithmException {
        String username = "validUser";
        String password = "correctPassword";

        User user = new User(username, password, "user@example.com", "nick", null);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        when(passwordHasher.verifyPassword(password, password)).thenReturn(true);

        Response result = accountService.loginUser(username, password, null);

        assertEquals("Logged in successfully", result.message());
    }

    @Test
    public void shouldReturnLoggedInSuccessfullyAndStayLoggedIn_whenStayLoggedInOptionIsSelected() throws NoSuchAlgorithmException {
        String username = "validUser";
        String password = "correctPassword";

        User user = new User(username, password, "user@example.com", "nick", null);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        when(passwordHasher.verifyPassword(password, password)).thenReturn(true);


        Response result = accountService.loginUser(username, password, "stay-logged-in");

        assertEquals("Logged in successfully", result.message());
        assertTrue(Session.isStayedLoggedIn());
    }



    @Test
    public void shouldNotSetStayedLoggedIn_whenStayedLoggedInIsNullOrDoesNotContainStayLoggedIn() throws NoSuchAlgorithmException {
        // Arrange: Prepare the user data
        String username = "validUser";
        String password = "correctPassword";
        User user = new User(username, password, "user@example.com", "nick", null);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(passwordHasher.verifyPassword(password, password)).thenReturn(true);

        // Start mocking static methods using Mockito.mockStatic
        try (var mockedSession = mockStatic(Session.class)) {

            // Act with stayedLoggedIn == null
            Response result1 = accountService.loginUser(username, password, null);

            // Verify that the static method `Session.setStayedLoggedIn(true)` is NOT called
            mockedSession.verify(() -> Session.setStayedLoggedIn(true), times(0)); // Verify not called

            // Act with stayedLoggedIn = "other-string"
            Response result2 = accountService.loginUser(username, password, "other-string");

            // Verify that the static method `Session.setStayedLoggedIn(true)` is NOT called
            mockedSession.verify(() -> Session.setStayedLoggedIn(true), times(0)); // Verify not called

            // Act with stayedLoggedIn = "stay-logged-in"
            Response result3 = accountService.loginUser(username, password, "stay-logged-in");

            // Verify that the static method `Session.setStayedLoggedIn(true)` is called
            mockedSession.verify(() -> Session.setStayedLoggedIn(true), times(1)); // Verify called once
        }
    }
    @Test
    public void shouldThrowRuntimeException_whenPasswordHasherThrowsNoSuchAlgorithmException() throws NoSuchAlgorithmException {
        // Arrange: Prepare the input data
        String username = "validUser";
        String password = "correctPassword";
        String stayedLoggedIn = null;  // Assuming this is part of your login logic

        // Create a mock user with the username and password
        User user = new User(username, password, "user@example.com", "nick", null);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // Mock PasswordHasher to throw NoSuchAlgorithmException during password verification
        PasswordHasher passwordHasher = Mockito.mock(PasswordHasher.class);
        doThrow(new NoSuchAlgorithmException("Algorithm not found"))
                .when(passwordHasher).verifyPassword(anyString(), anyString());

        // Create AccountService with mocked dependencies
        accountService = new AccountService(userRepository, viewRender, passwordHasher);

        // Act & Assert: Expect a RuntimeException to be thrown
        try {
            accountService.loginUser(username, password, stayedLoggedIn);
            fail("Expected RuntimeException to be thrown");
        } catch (RuntimeException e) {
            // Assert: Check that the exception is caused by a NoSuchAlgorithmException
            assertTrue(e.getCause() instanceof NoSuchAlgorithmException);
            assertEquals("Algorithm not found", e.getCause().getMessage());
        }
    }
    @Test
    public void shouldReturnSuccess_whenPasswordMatchesAndVerifyPasswordWorks() throws NoSuchAlgorithmException {
        String username = "validUser";
        String password = "correctPassword";

        User user = new User(username, password, "user@example.com", "nick", null);
        user=Mockito.mock(User.class);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        PasswordHasher passwordHasher = Mockito.mock(PasswordHasher.class);
        when(passwordHasher.verifyPassword(password, password)).thenReturn(true);
        when(user.getPassword()).thenReturn("correctPassword");

        Response result = accountService.loginUser(username, password, null);

        assertEquals("Logged in successfully", result.message());
    }
}

