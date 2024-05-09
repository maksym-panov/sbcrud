package com.maksympanov.hneu.mjt.sbcrud.sbcrud.dto.book;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookCreationDto {

    private String isbn;

    @Size(min = 5, max = 255)
    @NotEmpty
    private String bookName;


    @Positive
    @NotNull
    private BigDecimal price;

    @PositiveOrZero
    @NotNull
    private Integer quantity;

    private String description;

    private String imageUri;

    @NotNull
    private List<String> genresIds;

}
