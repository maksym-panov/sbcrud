package com.maksympanov.hneu.mjt.sbcrud.sbcrud.dto.favourite;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateFavouriteDto {

    @NotEmpty
    private String bookId;

}
