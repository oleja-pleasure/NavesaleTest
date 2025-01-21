package ui.item;

import lombok.Data;

@Data
public class Item {
    private String fullName;
    private String nameWithoutCharacteristics;
    private String colorInName;
    private String sizeInName;
    private String sizeUnderPrice;
    private String checkedSizeInBar;
    private String salePrice;
    private String priceDiscount;
    private String sizeInCharacteristics;
    private String colorInCharacteristics;
    private String brandInCharacteristics;
    private String countOfPieces;
}
