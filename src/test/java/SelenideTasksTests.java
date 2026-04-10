import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.AllureId;

import static com.codeborne.selenide.Selectors.*;
import io.qameta.allure.Description;
import com.codeborne.selenide.ex.UIAssertionError;
import io.qameta.allure.Owner;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static utils.Constants.*;
import utils.Constants;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Execution(ExecutionMode.CONCURRENT)
public class SelenideTasksTests extends BaseUITest {

    @AllureId("001")
    @Test
    @Owner("Kolkov")
    @Tag("smoke")
    @DisplayName("Возможность нажатия на кнопку с динамическим 'id'")
    public void dynamicIdTest() {
        open(DYNAMIC_ID_URL);
        clickButton("Button with Dynamic ID");

        $(byText("Button with Dynamic ID")).shouldBe(visible);
    }

    @AllureId("003")
    @Test
    @Owner("Kolkov")
    @Tag("smoke")
    @DisplayName("Невозможность нажатия на кнопку, если она перекрыта другим элементом")
    @Description("Скриншот перекрытия будет в отчете Allure")
    public void hiddenLayersTest() {
        open(HIDDEN_LAYERS_URL);
        click("button.btn-success");
        Assertions.assertThrows(UIAssertionError.class, () ->
                $("button.btn-success").click());
        // Не прокатило, так как Selenide смотрит по DOM :(
        // $("button.btn-success").shouldNotBe(Condition.clickable);
    }

    @AllureId("005")
    @Test
    @Order(1)
    @Owner("Kolkov")
    @Tag("smoke")
    @DisplayName("Возможность ожидания загрузки элемента на странице (AJAX)")
    public void ajaxDataTest() {
        open(AJAX_URL);
        clickButton("Button Triggering AJAX Request");

        $(byText("Data loaded with AJAX get request."))
                .shouldBe(visible, Duration.ofSeconds(20))
                .click();
    }

    @AllureId("007")
    @Test
    @Owner("Kolkov")
    @Tag("smoke")
    @DisplayName("Возможность клика по элементу на странице")
    public void clickTest() {
        open(CLICK_URL);
        clickButton("Button That Ignores DOM Click Event");

        $("button.btn-success").shouldBe(visible);
    }

    @AllureId("009")
    @Test
    @Owner("Kolkov")
    @Tag("smoke")
    @DisplayName("Возможность скролла до отображения элемента")
    public void scrollbarsTest() {
        open(SCROLLBARS_URL);

        $(byText("Hiding Button"))
                .scrollTo()
                .shouldBe(visible)
                .click();
    }

    @AllureId("011")
    @Test
    @Owner("Kolkov")
    @Tag("smoke")
    @DisplayName("Возможность поиска элемента с очисткой оформления текста")
    public void verifyTextTest() {
        open(VERIFY_TEXT_URL);

        $(withText("Welcome UserName!")).shouldBe(visible);
    }

    @AllureId("013")
    @Test
    @Owner("Kolkov")
    @Tag("smoke")
    @DisplayName("Отображение скрытых кнопок на странице")
    public void visibilityButtonsTest() {
        open(VISIBILITY_URL);
        SelenideElement hideButton = $("#hideButton");
        SelenideElement removedButton = $("#removedButton");
        SelenideElement zeroWidthButton = $("#zeroWidthButton");
        SelenideElement overlappedButton = $("#overlappedButton");
        SelenideElement zeroOpacityButton = $("#transparentButton");
        SelenideElement visibilityHiddenButton = $("#invisibleButton");
        SelenideElement displayNoneButton = $("#notdisplayedButton");
        SelenideElement offscreenButton = $("#offscreenButton");

        hideButton.click();

        hideButton.shouldBe(visible);
        removedButton.shouldBe(hidden);
        zeroWidthButton.shouldBe(hidden);
        overlappedButton.shouldBe(visible);
        zeroOpacityButton.shouldBe(exist);
        visibilityHiddenButton.shouldBe(hidden);
        displayNoneButton.shouldBe(hidden);
        offscreenButton.shouldBe(exist);
    }

    @AllureId("015")
    @Test
    @Owner("Kolkov")
    @Tag("smoke")
    @DisplayName("Возможность нажатия на элемент при изменении этого элемента в DOM при наведении курсора")
    public void mouseOverTest() {
        open(MOUSE_OVER_URL);

        $(byText("Click me")).hover().click();
        $(byText("Link Button")).shouldBe(visible).click();

        $("#clickCount").shouldHave(text("1"));
        $("#clickButtonCount").shouldHave(text("1"));
    }

    @AllureId("017")
    @Test
    @Owner("Kolkov")
    @Tag("smoke")
    @DisplayName("Возможность взаимодействия с перекрытым элементом")
    public void overlappedElementTest() {
        open(OVERLAPPED_URL);
        $("#name").scrollTo().setValue("Nikita");
    }

    @AllureId("019")
    @Test
    @Owner("Kolkov")
    @Tag("smoke")
    @DisplayName("Взаимодействие со всплывающими окнами")
    public void alertsTest() {
        open(ALERTS_URL);

        $("#alertButton").click();
        switchTo().alert().accept();

        $("#confirmButton").click();
        String confirmMessage = switchTo().alert().getText();
        System.out.println("Сообщение во всплывающем окне (Confirm): " + confirmMessage);
        switchTo().alert().accept();

        $("#promptButton").click();
        switchTo().alert().sendKeys("asd");
        switchTo().alert().accept();
    }

    @AllureId("021")
    @Test
    @Owner("Kolkov")
    @Tag("smoke")
    @DisplayName("Взаимодействие с движущимся элементом")
    public void animatedButtonTest() {
        open(ANIMATION_URL);
        $("#animationButton").click();

        $(Constants.PRIMARY_BUTTON_CSS).shouldNotHave(cssClass("spin"), Duration.ofSeconds(12));

        $(Constants.PRIMARY_BUTTON_CSS).click();
        $("#opstatus").shouldHave(text("Moving Target clicked. It's class name is 'btn btn-primary'"));
    }

    @AllureId("023")
    @Test
    @Owner("Kolkov")
    @Tag("smoke")
    @DisplayName("Возможность взаимодействия с чекбоксами, выпадающим списком")
    public void autoWaitTest() {
        String target = "#target";
        String opStatus = "#opstatus";
        String elementType = "#element-type";
        String applyButton = "Apply 3s";

        open(AUTO_WAIT_URL);

        // Чекбокс 'Visible' и элемент 'Button'
        setCheckbox("Visible");
        clickButton(applyButton);
        $(target).shouldBe(hidden);

        // Чекбокс 'Enabled' и элемент 'Textarea'
        $(elementType).selectOption("Textarea");
        setCheckbox("Enabled");
        clickButton(applyButton);
        $(target).setValue("test");
        $(elementType).click();
        $(opStatus).shouldHave(text("Text: test"));

        // Чекбокс 'Editable' и элемент 'Input'
        $(elementType).selectOption("Input");
        setCheckbox("Editable");
        clickButton(applyButton);
        $(target).setValue("test").pressEnter();
        $(opStatus).shouldHave(text("Text: test"));

        // Чекбокс 'On Top' и элемент 'Select'
        $(elementType).selectOption("Select");
        setCheckbox("On Top");
        clickButton(applyButton);
        $(target).selectOption("Item 2");
        $(opStatus).shouldHave(text("Selected: Item 2"));

        // Чекбокс 'Non Zero Size' и элемент 'Label'
        $(elementType).selectOption("Label");
        setCheckbox("Non Zero Size");
        clickButton(applyButton);
        $(target).shouldHave(text("This is a Label"));
    }

    @AllureId("025")
    @Test
    @Owner("Kolkov")
    @Tag("smoke")
    @DisplayName("Возможность работы с геолокацией")
    @Description("Геолокация не работает на сайте, поэтому проверяем только отклонение доступа к геолокации")
    public void geoLocationTest() {
        open(GEOLOCATION_URL);
        $("#requestLocation").click();

        $("#location").shouldHave(text("unavailable"));
    }
}
