package api.models;

import lombok.Data;

import java.util.List;

@Data
public class PetInfo {
    private long id;
    private Category category;
    private String name;
    private List<String> photoUrls;
    private List<Tags> tags;
    private String status;

    @Data
    private static class Category {
        private int id;
        private String name;
    }

    @Data
    private static class Tags{
        private int id;
        private String name;
    }

}
