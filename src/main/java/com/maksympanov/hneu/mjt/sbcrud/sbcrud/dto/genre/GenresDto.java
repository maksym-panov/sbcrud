package com.maksympanov.hneu.mjt.sbcrud.sbcrud.dto.genre;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GenresDto {

    private List<GenreDto> genres;

}
