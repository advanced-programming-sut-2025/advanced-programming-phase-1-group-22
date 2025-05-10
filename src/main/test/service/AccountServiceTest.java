package service;

import model.User;
import model.dto.UserDto;
import model.enums.Gender;
import model.records.Response;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import repository.UserRepository;
import utils.PasswordHasher;
import view.ViewRender;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@PrepareForTest(ViewRender.class)
public class AccountServiceTest {
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
        // Arrange: Mock existing username and user declining
        String existingUsername = "existingUser";
        when(userDto.username()).thenReturn(existingUsername);
        when(userDto.password()).thenReturn("StrongPass1!");
        when(userDto.passwordConfirmation()).thenReturn("StrongPass1!");
        when(userDto.email()).thenReturn("user@example.com");
        when(userDto.nickName()).thenReturn("nick");
        when(userDto.gender()).thenReturn("MALE");

        User existingUser = new User(existingUsername, "hashedPassword", "user@example.com", "nick", Gender.MALE);
        when(userRepository.findByUsername(existingUsername)).thenReturn(Optional.of(existingUser));

        // Simulate user input "no"
        Response userResponse = new Response("no");
        when(viewRender.getResponse()).thenReturn(userResponse);

        // Act: Call the registerUser method
        Response result = accountService.registerUser(userDto);

        // Assert: The result should be empty since the user declined
        assertEquals(Response.empty(), result);
    }

    @Test
    public void shouldReturnSuccess_whenUsernameExistsAndUserAccepts() {
        // Arrange: Mock existing username and user accepting
        String existingUsername = "existingUser";
        when(userDto.username()).thenReturn(existingUsername);
        when(userDto.password()).thenReturn("StrongPass1!");
        when(userDto.passwordConfirmation()).thenReturn("StrongPass1!");
        when(userDto.email()).thenReturn("user@example.com");
        when(userDto.nickName()).thenReturn("nick");
        when(userDto.gender()).thenReturn("MALE");

        User existingUser = new User(existingUsername, "hashedPassword", "user@example.com", "nick", Gender.MALE);
        when(userRepository.findByUsername(existingUsername)).thenReturn(Optional.of(existingUser));

        // Simulate user input "yes"
        Response userResponse = new Response("yes");
        when(viewRender.getResponse()).thenReturn(userResponse);

        // Mock user registration success
        User newUser = new User(existingUsername, "StrongPass1!", "user@example.com", "nick", Gender.MALE);
        when(userRepository.save(any(User.class))).thenReturn(Optional.of(newUser));

        // Act: Call the registerUser method
        Response result = accountService.registerUser(userDto);

        // Assert: The result should be success
        assertEquals("success", result.message());
    }

    @Test
    public void shouldReturnInvalidUsername_whenUsernameIsInvalid() {
        // Arrange: Mock invalid username
        when(userDto.username()).thenReturn("bad username");  // Invalid username
        when(userDto.password()).thenReturn("StrongPass1!");
        when(userDto.passwordConfirmation()).thenReturn("StrongPass1!");
        when(userDto.email()).thenReturn("user@example.com");
        when(userDto.nickName()).thenReturn("nick");
        when(userDto.gender()).thenReturn("MALE");

        // Act: Call the registerUser method
        Response result = accountService.registerUser(userDto);

        // Assert: The result should be "invalid username"
        assertEquals("invalid username", result.message());
    }

    @Test
    public void shouldReturnInvalidPassword_whenPasswordIsInvalid() {
        // Arrange: Mock invalid password
        when(userDto.username()).thenReturn("validUser");
        when(userDto.password()).thenReturn("@");  // Invalid password
        when(userDto.passwordConfirmation()).thenReturn("@");
        when(userDto.email()).thenReturn("user@example.com");
        when(userDto.nickName()).thenReturn("nick");
        when(userDto.gender()).thenReturn("MALE");

        // Act: Call the registerUser method
        Response result = accountService.registerUser(userDto);

        // Assert: The result should be "invalid password"
        assertEquals("invalid password", result.message());
    }

    @Test
    public void shouldReturnWeakPassword_whenPasswordIsWeak() {
        // Arrange: Mock weak password
        when(userDto.username()).thenReturn("validUser");
        when(userDto.password()).thenReturn("-");  // Weak password
        when(userDto.passwordConfirmation()).thenReturn("-");
        when(userDto.email()).thenReturn("user@example.com");
        when(userDto.nickName()).thenReturn("nick");
        when(userDto.gender()).thenReturn("MALE");

        // Act: Call the registerUser method
        Response result = accountService.registerUser(userDto);

        // Assert: The result should be "weak password"
        assertEquals("weak password", result.message());
    }


    @Test
    public void shouldReturnInvalidEmail_whenEmailIsInvalid() {
        // Arrange: Mock invalid email
        when(userDto.username()).thenReturn("validUser");
        when(userDto.password()).thenReturn("StrongPass1!");
        when(userDto.passwordConfirmation()).thenReturn("StrongPass1!");
        when(userDto.email()).thenReturn("invalid-email");  // Invalid email
        when(userDto.nickName()).thenReturn("nick");
        when(userDto.gender()).thenReturn("MALE");

        // Act: Call the registerUser method
        Response result = accountService.registerUser(userDto);

        // Assert: The result should be "invalid email"
        assertEquals("invalid email", result.message());
    }

    @Test
    public void shouldReturnSuccess_whenAllValid() {
        // Arrange: Mock all valid inputs
        when(userDto.username()).thenReturn("validUser");
        when(userDto.password()).thenReturn("StrongPass1!");
        when(userDto.passwordConfirmation()).thenReturn("StrongPass1!");
        when(userDto.email()).thenReturn("user@example.com");
        when(userDto.nickName()).thenReturn("nick");
        when(userDto.gender()).thenReturn("MALE");

        User newUser = new User("validUser", "hashedPassword", "user@example.com", "nick", Gender.MALE);
        when(userRepository.save(any(User.class))).thenReturn(Optional.of(newUser));

        // Act: Call the registerUser method
        Response result = accountService.registerUser(userDto);

        // Assert: The result should be "success"
        assertEquals("success", result.message());
    }

    @Test
    public void shouldReturnInvalidPassword_whenPasswordDoesNotMatchPattern() {
        // Arrange: Use a password that fails the PASSWORD regex pattern
        when(userDto.username()).thenReturn("validUser");
        when(userDto.password()).thenReturn("short"); // invalid basic pattern
        when(userDto.passwordConfirmation()).thenReturn("short");
        when(userDto.email()).thenReturn("user@example.com");
        when(userDto.nickName()).thenReturn("nick");
        when(userDto.gender()).thenReturn("MALE");

        // No need to simulate viewRender or userRepository for this test
        when(userRepository.findByUsername("validUser")).thenReturn(Optional.empty());

        // Act
        Response result = accountService.registerUser(userDto);

        // Assert
        assertEquals("weak password", result.message());
    }


    @Test
    public void shouldReturnEmpty_whenPasswordDoesNotMatchAndUserEntersInvalidPasswordOnReEntry() {
        // Arrange: Use a password and confirmation that don't match
        when(userDto.username()).thenReturn("validUser");
        when(userDto.password()).thenReturn("tU>(13sEeO2");  // Valid password
        when(userDto.passwordConfirmation()).thenReturn("tU>(13sEeO3");  // Different password
        when(userDto.email()).thenReturn("invalid-email");
        when(userDto.nickName()).thenReturn("nick");
        when(userDto.gender()).thenReturn("MALE");

        // Simulate an empty repository (no user with this username)
        when(userRepository.findByUsername("validUser")).thenReturn(Optional.empty());

        // Mock the viewRender interactions (getResponse should return an invalid password response)
        Response invalidPasswordResponse = new Response("sdf");  // Invalid password response
        when(viewRender.getResponse()).thenReturn(invalidPasswordResponse);
        Response result = accountService.registerUser(userDto);
        assertEquals("invalid email", result.message());
    }

    @Test
    public void shouldNotdReturnEmpty_whenPasswordDoesNotMatchAndUserEntersInvalidPasswordOnReEntry() {
        // Arrange: Use a password and confirmation that don't match
        when(userDto.username()).thenReturn("validUser");
        when(userDto.password()).thenReturn("tU>(13sEeO2");  // Valid password
        when(userDto.passwordConfirmation()).thenReturn("tU>(13sEeO3");  // Different password
        when(userDto.email()).thenReturn("invalid-email");
        when(userDto.nickName()).thenReturn("nick");
        when(userDto.gender()).thenReturn("MALE");

        // Simulate an empty repository (no user with this username)
        when(userRepository.findByUsername("validUser")).thenReturn(Optional.empty());

        // Mock the viewRender interactions (getResponse should return an invalid password response)
        Response invalidPasswordResponse = new Response("@");  // Invalid password response
        when(viewRender.getResponse()).thenReturn(invalidPasswordResponse);
        Response result = accountService.registerUser(userDto);
        assertEquals("", result.message());
    }

    @Test
    public void shouldReturnEmpty_whenUsernameExistsAndUserAccepts() throws NoSuchAlgorithmException {
        // Arrange: Mock existing username and user accepting
        String existingUsername = "existingUser";
        when(userDto.username()).thenReturn(existingUsername);
        when(userDto.password()).thenReturn("StrongPass1!");
        when(userDto.passwordConfirmation()).thenReturn("StrongPass1!");
        when(userDto.email()).thenReturn("user@example.com");
        when(userDto.nickName()).thenReturn("nick");
        when(userDto.gender()).thenReturn("MALE");

        User existingUser = new User(existingUsername, "hashedPassword", "user@example.com", "nick", Gender.MALE);
        when(userRepository.findByUsername(existingUsername)).thenReturn(Optional.of(existingUser));

        // Simulate user input "yes"
        Response userResponse = new Response("yes");
        when(viewRender.getResponse()).thenReturn(userResponse);
        // Mock user registration success
        User newUser = new User(existingUsername, "StrongPass1!", "user@example.com", "nick", Gender.MALE);
        when(userRepository.save(any(User.class))).thenReturn(Optional.of(newUser));

        when(userRepository.findByUsername("validUser")).thenReturn(Optional.empty());
        // Act: Call the registerUser method
        Response result = accountService.registerUser(userDto);
        PasswordHasher passwordHasher = Mockito.mock(PasswordHasher.class);
        when(passwordHasher.hashPassword(anyString())).thenReturn("sdf");
        assertEquals("success", result.message());
    }

    @Test
    public void userNotIsPresent() {
        // Arrange: Mock all valid inputs
        when(userDto.username()).thenReturn("validUser");
        when(userDto.password()).thenReturn("StrongPass1!");
        when(userDto.passwordConfirmation()).thenReturn("StrongPass1!");
        when(userDto.email()).thenReturn("user@example.com");
        when(userDto.nickName()).thenReturn("nick");
        when(userDto.gender()).thenReturn("MALE");

        User newUser = new User("validUser", "hashedPassword", "user@example.com", "nick", Gender.MALE);
        when(userRepository.save(any(User.class))).thenReturn(Optional.empty());

        // Act: Call the registerUser method
        Response result = accountService.registerUser(userDto);

        // Assert: The result should be "success"
        assertNull(result);
    }

    @Test
    public void notNullHashedPass() throws NoSuchAlgorithmException {
        // Arrange: Mock existing username and user accepting
        String existingUsername = "existingUser";
        when(userDto.username()).thenReturn(existingUsername);
        when(userDto.password()).thenReturn("StrongPass1!");
        when(userDto.passwordConfirmation()).thenReturn("StrongPass1!");
        when(userDto.email()).thenReturn("user@example.com");
        when(userDto.nickName()).thenReturn("nick");
        when(userDto.gender()).thenReturn("MALE");

        User existingUser = new User(existingUsername, "hashedPassword", "user@example.com", "nick", Gender.MALE);
        when(userRepository.findByUsername(existingUsername)).thenReturn(Optional.of(existingUser));

        // Simulate user input "yes"
        Response userResponse = new Response("yes");
        when(viewRender.getResponse()).thenReturn(userResponse);

        // Mock user registration success
        when(passwordHasher.hashPassword(anyString())).thenReturn("hashed-pass");
        User newUser = new User(existingUsername, "StrongPass1!", "user@example.com", "nick", Gender.MALE);
        when(userRepository.save(any(User.class))).thenReturn(Optional.of(newUser));
        // Act: Call the registerUser method
        Response result = accountService.registerUser(userDto);

        // Assert: The result should be success
        assertEquals("success", result.message());
    }

    @Test
    public void shouldThrowRuntimeException_whenPasswordHasherThrowsNoSuchAlgorithmException() throws NoSuchAlgorithmException {
        // Arrange: Mock the scenario where PasswordHasher throws NoSuchAlgorithmException
        String username = "validUser";
        String password = "StrongPass1!";
        String passwordConfirmation = "StrongPass1!";
        String email = "user@example.com";
        String nickName = "nick";
        String gender = "MALE";

        // Mock the userDto behavior
        when(userDto.username()).thenReturn(username);
        when(userDto.password()).thenReturn(password);
        when(userDto.passwordConfirmation()).thenReturn(passwordConfirmation);
        when(userDto.email()).thenReturn(email);
        when(userDto.nickName()).thenReturn(nickName);
        when(userDto.gender()).thenReturn(gender);

        // Mock the PasswordHasher to throw NoSuchAlgorithmException when hashPassword is called
        PasswordHasher passwordHasher = Mockito.mock(PasswordHasher.class);
        doThrow(new NoSuchAlgorithmException("Algorithm not found"))
                .when(passwordHasher)
                .hashPassword(anyString());

        // Inject the mock PasswordHasher into AccountService
        accountService = new AccountService(userRepository, viewRender, passwordHasher);

        // Act: Call the registerUser method and expect a RuntimeException
        try {
            accountService.registerUser(userDto);
            fail("Expected RuntimeException to be thrown");
        } catch (RuntimeException e) {
            // Assert: Verify that RuntimeException is thrown and the cause is NoSuchAlgorithmException
            assertTrue(e.getCause() instanceof NoSuchAlgorithmException);
            assertEquals("Algorithm not found", e.getCause().getMessage());
        }
    }

}
