package com.maksympanov.hneu.mjt.sbcrud.sbcrud.dto.genre;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductGenreDto {

    private String id;

    private String genreName;

}
