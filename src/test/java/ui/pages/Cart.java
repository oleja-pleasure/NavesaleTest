package ui.pages;

import com.codeborne.selenide.SelenideElement;
import ui.item.Item;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.NoSuchElementException;

import java.util.Objects;

import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Selenide.*;
import static java.lang.String.format;

public class Cart {
    String salePrice = "//div[@data-id=\"%s\"]//*[contains(@class,\"cart-item-default__price \")]";
    String priceDiscountInCart = "//div[@data-id=\"%s\"]//*[@class =\"cart-item-default__old-price\"]";
    String itemNameInCart = "//*[@class=\"cart-item-default__title\"]";
    String sizeInCart = "//div[@data-id=\"%s\"]//li[@class =\"cart-item-default__props-item\"][1]";
    String colorInCart = "//div[@data-id=\"%s\"]//li[@class =\"cart-item-default__props-item\"][2]";
    String checkBox = "//label[@for=\"input-select-%s\"]/*[@class=\"checkbox__box\"]";
    String countOfCheckedItem = "//input[@checked=\"true\"]/../..//input[@data-selector=\"quantity-group-input\"]";
    String countAndWeight = "//*[@class=\"basket-summary__btn-more\"]/*[@class=\"js-more\"]";

    public Cart() {
        $x("//*[text()=\"Оформить заказ\"]").shouldBe(enabled);
    }

    /**
     * Возвращает id товара в корзине,
     * который используется для определения принадлежности элементов страницы к товару
     * @param name Название товара без характеристик
     * @return id товара
     */
    public String getDataIdForItemAndCheckItemName(String name) {
        String dataId = "";
        try {
            dataId = $x(itemNameInCart + "[text()=\"" + name + "\"]/../..")
                    .attr("data-id");
        } catch (NoSuchElementException e) {
            Assertions.fail("Товара с названием \"" + name + "\" на странице корзины не найдено");
        }
        return dataId;
    }

    /**
     * Цена продажи
     */
    public String getSalePrice(String dataId) {
        return $x(format(salePrice, dataId)).getText();
    }

    /**
     * Зачеркнутая цена, до скидки
     */
    public String getPriceDiscountInCart(String dataId) {
        return $x(format(priceDiscountInCart, dataId)).getText();
    }

    public String getSizeInCart(String dataId) {
        return $x(format(sizeInCart, dataId)).getText()
                .replaceAll("\"", "")
                .split(": ")[1];
    }

    public String getColorInCart(String dataId) {
        return $x(format(colorInCart, dataId)).getText()
                .split(": ")[1];
    }

    /**
     * Строка с общим количеством и весом товара выбранного в корзине
     */
    public int getCountAndWeight() {
        return Integer.parseInt($x(countAndWeight).getText().split(" шт")[0]);
    }

    public void checkOrUncheckItem(Item item) {
        String dataId = getDataIdForItemAndCheckItemName(item.getNameWithoutCharacteristics());
        $x(format(checkBox, dataId)).click();
    }

    public int getCountOfAllCheckedItems() {
        return $$x(countOfCheckedItem)
                .stream()
                .map(SelenideElement::getValue)
                .filter(Objects::nonNull)
                .mapToInt(Integer::parseInt)
                .sum();
    }
}