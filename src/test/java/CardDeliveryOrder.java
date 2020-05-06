import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.LocalDate;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class CardDeliveryOrder {
    private String url = "http://localhost:9999/";
    private int minNumberOfDaysBeforeDelivery = 3;
    private LocalDate theNearestDate = LocalDate.now().plusDays(minNumberOfDaysBeforeDelivery);

    @Test
    void shouldBeSuccessUseEarliestValidDate() {
        open(url);

        $("[data-test-id=city] input").setValue("Москва");
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
        $("[data-test-id=date] input").sendKeys(getTheNearestDate());
        $("[data-test-id=name] input").setValue("Попов");
        $("[data-test-id=phone] input").setValue("+72145695212");
        $("[data-test-id=agreement]").click();
        $$("button").find(Condition.exactText("Забронировать")).click();

        SelenideElement notification = $("[data-test-id=notification]");
        $("[data-test-id=notification]").waitUntil(Condition.visible, 15000);
        $("[data-test-id=notification] .notification__title").should(Condition.exactText("Успешно!"));
        $("[data-test-id=notification] .notification__content").should(Condition.exactText("Встреча успешно забронирована на " + getTheNearestDate()));
    }


    public String getTheNearestDate() {
        return String.format("%02d.%02d.%d", theNearestDate.getDayOfMonth(), theNearestDate.getMonthValue(), theNearestDate.getYear());
    }

    public String getUrl() {
        return url;
    }

}