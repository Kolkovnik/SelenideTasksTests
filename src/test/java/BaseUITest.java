import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

public class BaseUITest {

    @BeforeAll
    static void setupAllure() {
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide()
                .screenshots(true)
                .savePageSource(false));
    }

    @BeforeEach
    void setup() {
        Configuration.browser = "chrome";
        Configuration.headless = false;
        Configuration.timeout = 10000;
    }

    /**
     * Клик по кнопке через текст
     */
    protected void clickButton(String name) {
        $(byText(name)).shouldBe(visible).click();
    }

    /**
     * Универсальный клик по селектору
     */
    protected void click(String selector) {
        $(selector).click();
    }

    /**
     * Выбрать чекбокс
     */
    protected void setCheckbox(String label) {
        $(byText(label)).click();
    }

    /**
     * Принять всплывающее окно
     */
    protected void acceptAlert() {
        switchTo().alert().accept();
    }

    /**
     * Проверить, что элемент содержит конкретный текст
     */
    protected void verifyText(String selector, String expectedText) {
        $(selector).shouldHave(text(expectedText));
    }
}
