package ui.tests;

import ui.item.Item;
import org.junit.jupiter.api.Test;
import ui.pages.Cart;
import ui.pages.ItemCardPage;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.*;

public class CheckGoodInTheBasketTest {

    @Test
    public void test01() {
        open("https://navisale.ru/");

        goToFirstItemInCategory("Куртки");
        Item firstItem = new Item();
        ItemCardPage itemCardPage = new ItemCardPage(firstItem);
        itemCardPage.saveItemInfo().addToCart();
        checkCharacteristics(firstItem);

        goToFirstItemInCategory("Трусы");
        Item secondItem = new Item();
        itemCardPage = new ItemCardPage(secondItem);
        itemCardPage.saveItemInfo().addToCart(2);
        checkCharacteristics(secondItem);

        goToFirstItemInCategory("Джоггеры");
        Item thirdItem = new Item();
        itemCardPage = new ItemCardPage(thirdItem);
        itemCardPage.saveItemInfo().addToCart();
        checkCharacteristics(thirdItem);

        Cart cart = itemCardPage.goToCart();

        checkItemsInCart(cart, firstItem);
        checkItemsInCart(cart, secondItem);
        checkItemsInCart(cart, thirdItem);

        cart.checkOrUncheckItem(thirdItem);
        assertEquals(cart.getCountAndWeight(), cart.getCountOfAllCheckedItems());
    }

    public void goToFirstItemInCategory(String categoryName) {
        $("#rubrics-toggle").scrollTo().click();
        $("[title=\"" + categoryName + "\"]").scrollTo().click();
        $(".product-listing-card:first-child .product-listing-card__preview").click();
    }

    public void checkCharacteristics(Item item) {
        assertAll("Проверки соответствия полей в карточке товара", () -> {
            if (item.getBrandInCharacteristics() != null) {
                assertTrue(item.getFullName().contains(item.getBrandInCharacteristics()),
                        "Название бренда в полном названии товара не найдено.");
            }
            if (item.getColorInName() != null) {
                assertTrue(item.getColorInCharacteristics().contains(item.getColorInName()),
                        "Цвет в названии товара не найден в характеристиках");
            }
            if (item.getSizeInName() != null) {
                assertTrue(item.getSizeInCharacteristics().contains(item.getSizeInName()),
                        "Размер в названии товара не найден в характеристиках");
                assertEquals(item.getSizeUnderPrice(), item.getCheckedSizeInBar(),
                        "Размер под ценой товара не совпадает с размером выбранным в доступных размерах.");
                assertEquals(item.getSizeUnderPrice(), item.getSizeInName(),
                        "Размер под ценой товара не совпадает с размером в названии.");
            }
        });
    }

    public void checkItemsInCart(Cart cart, Item item) {
        assertAll("Проверки полей карточек товаров в корзине", () -> {
            String dataId = cart.getDataIdForItemAndCheckItemName(item.getNameWithoutCharacteristics());
            if (item.getColorInName() != null) {
                assertEquals(item.getColorInName(), cart.getColorInCart(dataId),
                        "Цвет товара в карточке и цвет товара в корзине не совпадают!");
            }
            if (item.getSizeInName() != null) {
                assertEquals(item.getSizeInName(), cart.getSizeInCart(dataId),
                        "Размер товара в карточке и размер товара в корзине не совпадают!");
            }
            assertEquals(item.getSalePrice(), cart.getSalePrice(dataId),
                    "Цена товара в карточке и цена товара в корзине не совпадают!");
            if (item.getPriceDiscount() != null) {
                assertEquals(item.getPriceDiscount(), cart.getPriceDiscountInCart(dataId),
                        "Цена товара до скидки в карточке и цена товара до скидки в корзине не совпадают!");
            }
        });
    }
}