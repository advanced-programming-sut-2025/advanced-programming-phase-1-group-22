package service;

import model.User;
import model.dto.UserDto;
import model.enums.Gender;
import model.records.Response;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import repository.UserRepository;
import utils.PasswordHasher;
import view.ViewRender;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class RegistrationTest {
    private AccountService accountService;
    private UserRepository<User> userRepository;
    private ViewRender viewRender;
    private PasswordHasher passwordHasher;
    private UserDto userDto;

    @Before
    public void setUp() {
        // Mock dependencies
        userRepository = Mockito.mock(UserRepository.class);
        viewRender = Mockito.mock(ViewRender.class);
        userDto = Mockito.mock(UserDto.class);
        passwordHasher = Mockito.mock(PasswordHasher.class);

        // Inject mocks into AccountService
        accountService = new AccountService(userRepository, viewRender, passwordHasher);
    }

    @Test
    public void shouldReturnEmpty_whenUsernameExistsAndUserDeclines() {
        String existingUsername = "existingUser";
        when(userDto.username()).thenReturn(existingUsername);
        when(userDto.password()).thenReturn("StrongPass1!");
        when(userDto.passwordConfirmation()).thenReturn("StrongPass1!");
        when(userDto.email()).thenReturn("user@example.com");
        when(userDto.nickName()).thenReturn("nick");
        when(userDto.gender()).thenReturn("MALE");

        User existingUser = new User(existingUsername, "hashedPassword", "user@example.com", "nick", Gender.MALE);
        when(userRepository.findByUsername(existingUsername)).thenReturn(Optional.of(existingUser));

        Response userResponse = new Response("no");
        when(viewRender.getResponse()).thenReturn(userResponse);

        Response result = accountService.registerUser(userDto);

        assertEquals(Response.empty(), result);
    }

    @Test
    public void shouldReturnSuccess_whenUsernameExistsAndUserAccepts() {
        String existingUsername = "existingUser";
        when(userDto.username()).thenReturn(existingUsername);
        when(userDto.password()).thenReturn("StrongPass1!");
        when(userDto.passwordConfirmation()).thenReturn("StrongPass1!");
        when(userDto.email()).thenReturn("user@example.com");
        when(userDto.nickName()).thenReturn("nick");
        when(userDto.gender()).thenReturn("MALE");

        User existingUser = new User(existingUsername, "hashedPassword", "user@example.com", "nick", Gender.MALE);
        when(userRepository.findByUsername(existingUsername)).thenReturn(Optional.of(existingUser));

        Response userResponse = new Response("yes");
        when(viewRender.getResponse()).thenReturn(userResponse);

        User newUser = new User(existingUsername, "StrongPass1!", "user@example.com", "nick", Gender.MALE);
        when(userRepository.save(any(User.class))).thenReturn(Optional.of(newUser));

        Response result = accountService.registerUser(userDto);

        assertEquals("success", result.message());
    }

    @Test
    public void shouldReturnInvalidUsername_whenUsernameIsInvalid() {
        when(userDto.username()).thenReturn("bad username");  // Invalid username
        when(userDto.password()).thenReturn("StrongPass1!");
        when(userDto.passwordConfirmation()).thenReturn("StrongPass1!");
        when(userDto.email()).thenReturn("user@example.com");
        when(userDto.nickName()).thenReturn("nick");
        when(userDto.gender()).thenReturn("MALE");

        Response result = accountService.registerUser(userDto);

        assertEquals("invalid username", result.message());
    }

    @Test
    public void shouldReturnInvalidPassword_whenPasswordIsInvalid() {
        when(userDto.username()).thenReturn("validUser");
        when(userDto.password()).thenReturn("@");  // Invalid password
        when(userDto.passwordConfirmation()).thenReturn("@");
        when(userDto.email()).thenReturn("user@example.com");
        when(userDto.nickName()).thenReturn("nick");
        when(userDto.gender()).thenReturn("MALE");

        Response result = accountService.registerUser(userDto);

        assertEquals("invalid password", result.message());
    }

    @Test
    public void shouldReturnWeakPassword_whenPasswordIsWeak() {
        when(userDto.username()).thenReturn("validUser");
        when(userDto.password()).thenReturn("-");  // Weak password
        when(userDto.passwordConfirmation()).thenReturn("-");
        when(userDto.email()).thenReturn("user@example.com");
        when(userDto.nickName()).thenReturn("nick");
        when(userDto.gender()).thenReturn("MALE");

        Response result = accountService.registerUser(userDto);

        assertEquals("weak password", result.message());
    }


    @Test
    public void shouldReturnInvalidEmail_whenEmailIsInvalid() {
        when(userDto.username()).thenReturn("validUser");
        when(userDto.password()).thenReturn("StrongPass1!");
        when(userDto.passwordConfirmation()).thenReturn("StrongPass1!");
        when(userDto.email()).thenReturn("invalid-email");  // Invalid email
        when(userDto.nickName()).thenReturn("nick");
        when(userDto.gender()).thenReturn("MALE");

        Response result = accountService.registerUser(userDto);

        assertEquals("invalid email", result.message());
    }

    @Test
    public void shouldReturnSuccess_whenAllValid() {
        when(userDto.username()).thenReturn("validUser");
        when(userDto.password()).thenReturn("StrongPass1!");
        when(userDto.passwordConfirmation()).thenReturn("StrongPass1!");
        when(userDto.email()).thenReturn("user@example.com");
        when(userDto.nickName()).thenReturn("nick");
        when(userDto.gender()).thenReturn("MALE");

        User newUser = new User("validUser", "hashedPassword", "user@example.com", "nick", Gender.MALE);
        when(userRepository.save(any(User.class))).thenReturn(Optional.of(newUser));

        Response result = accountService.registerUser(userDto);

        assertEquals("success", result.message());
    }

    @Test
    public void shouldReturnInvalidPassword_whenPasswordDoesNotMatchPattern() {
        when(userDto.username()).thenReturn("validUser");
        when(userDto.password()).thenReturn("short"); // invalid basic pattern
        when(userDto.passwordConfirmation()).thenReturn("short");
        when(userDto.email()).thenReturn("user@example.com");
        when(userDto.nickName()).thenReturn("nick");
        when(userDto.gender()).thenReturn("MALE");

        when(userRepository.findByUsername("validUser")).thenReturn(Optional.empty());

        Response result = accountService.registerUser(userDto);

        assertEquals("weak password", result.message());
    }


    @Test
    public void shouldReturnEmpty_whenPasswordDoesNotMatchAndUserEntersInvalidPasswordOnReEntry() {
        when(userDto.username()).thenReturn("validUser");
        when(userDto.password()).thenReturn("tU>(13sEeO2");
        when(userDto.passwordConfirmation()).thenReturn("tU>(13sEeO3");
        when(userDto.email()).thenReturn("invalid-email");
        when(userDto.nickName()).thenReturn("nick");
        when(userDto.gender()).thenReturn("MALE");

        when(userRepository.findByUsername("validUser")).thenReturn(Optional.empty());

        Response invalidPasswordResponse = new Response("sdf");
        when(viewRender.getResponse()).thenReturn(invalidPasswordResponse);
        Response result = accountService.registerUser(userDto);
        assertEquals("invalid email", result.message());
    }

    @Test
    public void shouldNotdReturnEmpty_whenPasswordDoesNotMatchAndUserEntersInvalidPasswordOnReEntry() {
        when(userDto.username()).thenReturn("validUser");
        when(userDto.password()).thenReturn("tU>(13sEeO2");
        when(userDto.passwordConfirmation()).thenReturn("tU>(13sEeO3");
        when(userDto.email()).thenReturn("invalid-email");
        when(userDto.nickName()).thenReturn("nick");
        when(userDto.gender()).thenReturn("MALE");

        when(userRepository.findByUsername("validUser")).thenReturn(Optional.empty());

        Response invalidPasswordResponse = new Response("@");
        when(viewRender.getResponse()).thenReturn(invalidPasswordResponse);
        Response result = accountService.registerUser(userDto);
        assertEquals("", result.message());
    }

    @Test
    public void shouldReturnEmpty_whenUsernameExistsAndUserAccepts() throws NoSuchAlgorithmException {
        String existingUsername = "existingUser";
        when(userDto.username()).thenReturn(existingUsername);
        when(userDto.password()).thenReturn("StrongPass1!");
        when(userDto.passwordConfirmation()).thenReturn("StrongPass1!");
        when(userDto.email()).thenReturn("user@example.com");
        when(userDto.nickName()).thenReturn("nick");
        when(userDto.gender()).thenReturn("MALE");

        User existingUser = new User(existingUsername, "hashedPassword", "user@example.com", "nick", Gender.MALE);
        when(userRepository.findByUsername(existingUsername)).thenReturn(Optional.of(existingUser));

        Response userResponse = new Response("yes");
        when(viewRender.getResponse()).thenReturn(userResponse);
        User newUser = new User(existingUsername, "StrongPass1!", "user@example.com", "nick", Gender.MALE);
        when(userRepository.save(any(User.class))).thenReturn(Optional.of(newUser));

        when(userRepository.findByUsername("validUser")).thenReturn(Optional.empty());
        Response result = accountService.registerUser(userDto);
        PasswordHasher passwordHasher = Mockito.mock(PasswordHasher.class);
        when(passwordHasher.hashPassword(anyString())).thenReturn("sdf");
        assertEquals("success", result.message());
    }

    @Test
    public void userNotIsPresent() {
        when(userDto.username()).thenReturn("validUser");
        when(userDto.password()).thenReturn("StrongPass1!");
        when(userDto.passwordConfirmation()).thenReturn("StrongPass1!");
        when(userDto.email()).thenReturn("user@example.com");
        when(userDto.nickName()).thenReturn("nick");
        when(userDto.gender()).thenReturn("MALE");

        User newUser = new User("validUser", "hashedPassword", "user@example.com", "nick", Gender.MALE);
        when(userRepository.save(any(User.class))).thenReturn(Optional.empty());

        Response result = accountService.registerUser(userDto);

        assertNull(result);
    }

    @Test
    public void notNullHashedPass() throws NoSuchAlgorithmException {
        String existingUsername = "existingUser";
        when(userDto.username()).thenReturn(existingUsername);
        when(userDto.password()).thenReturn("StrongPass1!");
        when(userDto.passwordConfirmation()).thenReturn("StrongPass1!");
        when(userDto.email()).thenReturn("user@example.com");
        when(userDto.nickName()).thenReturn("nick");
        when(userDto.gender()).thenReturn("MALE");

        User existingUser = new User(existingUsername, "hashedPassword", "user@example.com", "nick", Gender.MALE);
        when(userRepository.findByUsername(existingUsername)).thenReturn(Optional.of(existingUser));

        Response userResponse = new Response("yes");
        when(viewRender.getResponse()).thenReturn(userResponse);

        when(passwordHasher.hashPassword(anyString())).thenReturn("hashed-pass");
        User newUser = new User(existingUsername, "StrongPass1!", "user@example.com", "nick", Gender.MALE);
        when(userRepository.save(any(User.class))).thenReturn(Optional.of(newUser));
        Response result = accountService.registerUser(userDto);

        assertEquals("success", result.message());
    }

    @Test
    public void shouldThrowRuntimeException_whenPasswordHasherThrowsNoSuchAlgorithmException() throws NoSuchAlgorithmException {
        String username = "validUser";
        String password = "StrongPass1!";
        String passwordConfirmation = "StrongPass1!";
        String email = "user@example.com";
        String nickName = "nick";
        String gender = "MALE";

        when(userDto.username()).thenReturn(username);
        when(userDto.password()).thenReturn(password);
        when(userDto.passwordConfirmation()).thenReturn(passwordConfirmation);
        when(userDto.email()).thenReturn(email);
        when(userDto.nickName()).thenReturn(nickName);
        when(userDto.gender()).thenReturn(gender);

        PasswordHasher passwordHasher = Mockito.mock(PasswordHasher.class);
        doThrow(new NoSuchAlgorithmException("Algorithm not found"))
                .when(passwordHasher)
                .hashPassword(anyString());

        accountService = new AccountService(userRepository, viewRender, passwordHasher);

        try {
            accountService.registerUser(userDto);
            fail("Expected RuntimeException to be thrown");
        } catch (RuntimeException e) {
            assertTrue(e.getCause() instanceof NoSuchAlgorithmException);
            assertEquals("Algorithm not found", e.getCause().getMessage());
        }
    }

}
