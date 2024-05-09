package com.maksympanov.hneu.mjt.sbcrud.sbcrud.dto.genre;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateGenreInfoDto {

    @Size(min = 4, max = 255)
    @NotEmpty
    private String genreName;

    private String description;

}
