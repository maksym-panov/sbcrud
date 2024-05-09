package com.maksympanov.hneu.mjt.sbcrud.sbcrud.dto.book;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BooksDto {

    private List<BookDto> books;

}
