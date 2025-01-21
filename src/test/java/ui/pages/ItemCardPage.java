package ui.pages;

import ui.item.Item;
import org.junit.jupiter.api.Assertions;

import java.util.Arrays;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class ItemCardPage {

    String itemName = "#name";
    String salePrice = ".price__regular";
    String priceDiscount = ".price__discount";
    String sizeUnderPrice = ".options-group__value";
    String checkedSizeInBar = "//input[@checked]/../label[@data-selector=\"aspect-item\"]";
    String addToCartButton = "[data-selector=\"add-to-cart-btn\"]";
    String countOfPieces = "[class=\"quantity-group__number\"]";
    String plusOnePiece = "[data-selector=\"quantity-group-plus\"]";
    String colorInCharacteristics = "//span[@class=\"parameters-list__term-text\"][text()=\"Цвет\"]/../../dd[@class=\"parameters-list__detail\"]";
    String sizeInCharacteristics = "//span[@class=\"parameters-list__term-text\"][text()=\"Размер\"]/../../dd[@class=\"parameters-list__detail\"]";
    String brandInCharacteristics = "//span[@class=\"parameters-list__term-text\"][text()=\"Бренд\"]/../../dd[@class=\"parameters-list__detail\"]";
    String goToCart = "[data-selector=\"go-to-cart\"]";

    Item item;

    public ItemCardPage(Item item) {
        this.item = item;
    }

    public ItemCardPage saveItemInfo() {
        item.setFullName($(itemName).getText());
        item.setNameWithoutCharacteristics(getNameWithoutSizeAndColor(item.getFullName()));
        item.setSalePrice($(salePrice).getText());
        if ($(priceDiscount).exists()) {
            item.setPriceDiscount($(priceDiscount).getText());
        }
        if ($x(colorInCharacteristics).exists()) {
            item.setColorInName(getColorOrSizeFromName("Цвет: "));
            item.setColorInCharacteristics($x(colorInCharacteristics).getText());
        }
        if ($x(sizeInCharacteristics).exists()) {
            item.setSizeInName(getColorOrSizeFromName("Размер: "));
            item.setSizeInCharacteristics($x(sizeInCharacteristics).getText());
            item.setSizeUnderPrice($(sizeUnderPrice).getText());
            item.setCheckedSizeInBar($x(checkedSizeInBar).getText());
        }
        if ($x(brandInCharacteristics).exists()) {
            item.setBrandInCharacteristics($x(brandInCharacteristics).getText());
        }
        return this;
    }

    /**
     * Метод отделяет название товара от характеристик "Цвет" и "Размер", которых может и не быть
     *
     * @param fullName полное имя в карточке товара
     * @return название товара без характеристик
     */
    public String getNameWithoutSizeAndColor(String fullName) {
        int colorIndex = fullName.indexOf("Цвет:");
        int sizeIndex = fullName.indexOf("Размер:");
        int[] indexes = new int[2];
        indexes[0] = colorIndex;
        indexes[1] = sizeIndex;
        Arrays.sort(indexes);
        int additionalStringIndex = indexes[0] > 0 ? indexes[0] : indexes[1];
        return additionalStringIndex > 0 ? fullName.substring(0, additionalStringIndex-1) : fullName;
    }

    public void addToCart() {
        addToCart(1);
    }

    public void addToCart(int count) {
        $(addToCartButton).scrollTo().click();
        if (count > 1) {
            for (int i = 1; i < count; i++) {
                $(".btn_progress").shouldNot(exist);
                $(plusOnePiece).scrollTo().click();
            }
        }
        $(goToCart).shouldBe(visible);
        Assertions.assertEquals(Integer.toString(count), $(countOfPieces).getText(),
                "Введенное количество товара не совпадает с переданным");
        item.setCountOfPieces(Integer.toString(count));
    }

    public Cart goToCart() {
        $(goToCart).scrollTo().click();
        return new Cart();
    }

    public String getColorOrSizeFromName(String sizeOrColor) {
        String value = item.getFullName().split(sizeOrColor)[1];
        if (value.contains(";")) {
            value = value.split(";")[0];
        }
        return value;
    }
}

