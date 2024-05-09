package com.maksympanov.hneu.mjt.sbcrud.sbcrud.dto.book;

import com.maksympanov.hneu.mjt.sbcrud.sbcrud.dto.genre.ProductGenreDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {

    private String id;

    private String isbn;

    private String bookName;

    private BigDecimal price;

    private Integer quantity;

    private String description;

    private String imageUri;

    private List<ProductGenreDto> genres;

}
