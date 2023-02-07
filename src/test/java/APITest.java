
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static ru.netology.testmode.data.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.testmode.data.DataGenerator.Registration.getUser;
import static ru.netology.testmode.data.DataGenerator.getRandomLogin;
import static ru.netology.testmode.data.DataGenerator.getRandomPassword;

class APITest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successfully login with active registered user")
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        Configuration.holdBrowserOpen = true;
        Configuration.timeout = 15;
        var registeredUser = getRegisteredUser("active");
        $("[data-test-id = 'login'] input").setValue(registeredUser.getLogin());
        $("[data-test-id  = 'password'] input").setValue(registeredUser.getPassword());
        $("[data-test-id = 'action-login']").click();
        $("h2").shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.exactText("Личный кабинет"));


    }

    @Test
    @DisplayName("Should get error message if login with not registered user")
    void shouldGetErrorIfNotRegisteredUser() {
        Configuration.holdBrowserOpen = true;
        Configuration.timeout = 15;
        var notRegisteredUser = getUser("active");
        $("[data-test-id = 'login'] input").setValue(notRegisteredUser.getLogin());
        //System.out.println(registeredUser.getLogin());
        $("[data-test-id  = 'password'] input").setValue(notRegisteredUser.getPassword());
        //System.out.println(registeredUser.getPassword());
        $("[data-test-id = 'action-login']").click();
        $("[data-test-id = 'error-notification'] .notification__title")
                .shouldBe(Condition.visible, Duration.ofSeconds(15)).shouldHave(Condition.exactText("Ошибка"));
        $("[data-test-id = 'error-notification'] .notification__content")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.exactText("Ошибка! " + "Неверно указан логин или пароль"));

    }

    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorIfBlockedUser() {
        Configuration.holdBrowserOpen = true;
        Configuration.timeout = 15;
        var blockedUser = getRegisteredUser("blocked");
        $("[data-test-id = 'login'] input").setValue(blockedUser.getLogin());
        $("[data-test-id  = 'password'] input").setValue(blockedUser.getPassword());
        $("[data-test-id = 'action-login']").click();
        $("[data-test-id = 'error-notification'] .notification__title")
                .shouldBe(Condition.visible, Duration.ofSeconds(15)).shouldHave(Condition.exactText("Ошибка"));
        $("[data-test-id = 'error-notification'] .notification__content")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.exactText("Ошибка! " + "Пользователь заблокирован"));
    }

    @Test
    @DisplayName("Should get error message if login with wrong login")
    void shouldGetErrorIfWrongLogin() {
        Configuration.holdBrowserOpen = true;
        Configuration.timeout = 15;
        var registeredUser = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();
        $("[data-test-id = 'login'] input").setValue(wrongLogin);
        $("[data-test-id  = 'password'] input").setValue(registeredUser.getPassword());
        $("[data-test-id = 'action-login']").click();
        $("[data-test-id = 'error-notification'] .notification__title")
                .shouldBe(Condition.visible, Duration.ofSeconds(15)).shouldHave(Condition.exactText("Ошибка"));
        $("[data-test-id = 'error-notification'] .notification__content")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.exactText("Ошибка! " + "Неверно указан логин или пароль"));
    }

    @Test
    @DisplayName("Should get error message if login with wrong password")
    void shouldGetErrorIfWrongPassword() {
        Configuration.holdBrowserOpen = true;
        Configuration.timeout = 15;
        var registeredUser = getRegisteredUser("active");
        var wrongPassword = getRandomPassword();
        $("[data-test-id = 'login'] input").setValue(registeredUser.getLogin());
        $("[data-test-id  = 'password'] input").setValue(wrongPassword);
        $("[data-test-id = 'action-login']").click();
        $("[data-test-id = 'error-notification'] .notification__title")
                .shouldBe(Condition.visible, Duration.ofSeconds(15)).shouldHave(Condition.exactText("Ошибка"));
        $("[data-test-id = 'error-notification'] .notification__content")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.exactText("Ошибка! " + "Неверно указан логин или пароль"));
    }
}