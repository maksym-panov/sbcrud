package com.maksympanov.hneu.mjt.sbcrud.sbcrud.dto.book;

import jakarta.validation.constraints.AssertTrue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBookInfoDto {

    private String isbn;

    private String bookName;

    private BigDecimal price;

    private Integer quantity;

    private String description;

    private String imageUri;

    private List<String> genresIds;

    @AssertTrue
    public boolean hasAtLeastOneValue() {
        return StringUtils.isNotEmpty(isbn)
                || StringUtils.isNotEmpty(bookName)
                || (price != null && price.compareTo(BigDecimal.ZERO) >= 0)
                || (quantity != null && quantity >= 0)
                || StringUtils.isNotEmpty(description)
                || StringUtils.isNotEmpty(imageUri)
                || genresIds != null;

    }

}
