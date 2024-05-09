package com.maksympanov.hneu.mjt.sbcrud.sbcrud.dto.favourite;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FavouriteDto {

    private String id;

    private String bookId;

    private String bookName;

}
