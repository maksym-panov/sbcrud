package com.maksympanov.hneu.mjt.sbcrud.sbcrud.controller;

import com.maksympanov.hneu.mjt.sbcrud.sbcrud.config.APIPrefixes;
import com.maksympanov.hneu.mjt.sbcrud.sbcrud.dto.AbstractResponseDto;
import com.maksympanov.hneu.mjt.sbcrud.sbcrud.dto.book.BookCreationDto;
import com.maksympanov.hneu.mjt.sbcrud.sbcrud.dto.book.BookDto;
import com.maksympanov.hneu.mjt.sbcrud.sbcrud.dto.book.BooksDto;
import com.maksympanov.hneu.mjt.sbcrud.sbcrud.dto.book.UpdateBookInfoDto;
import com.maksympanov.hneu.mjt.sbcrud.sbcrud.exception.FormValidationException;
import com.maksympanov.hneu.mjt.sbcrud.sbcrud.model.Book;
import com.maksympanov.hneu.mjt.sbcrud.sbcrud.service.BookService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping
@AllArgsConstructor
public class BookController {

    private BookService bookService;

    private DataMapper dataMapper;

    @GetMapping(APIPrefixes.PUBLIC + "/books")
    public AbstractResponseDto<BooksDto> getBooksPageable(
            @RequestParam(required = false) Integer pageNumber,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) String partialName
    ) {
        if (pageNumber == null || pageSize == null || pageNumber <= 0 || pageSize <= 0) {
            pageNumber = 1;
            pageSize = 20;
        }

        List<Book> books;
        if (StringUtils.isNotEmpty(partialName)) {
            books = bookService.getBooksByPartialNamePageable(partialName, pageNumber, pageSize);
        } else {
            books = bookService.getBooksPageable(pageNumber, pageSize);
        }

        var rv = dataMapper.mapBooksDto(books);
        return new AbstractResponseDto<>(rv);
    }

    @GetMapping(APIPrefixes.PUBLIC + "/books/{bookId}")
    public AbstractResponseDto<BookDto> getBookInfoById(@PathVariable String bookId) {
        var id = UUID.fromString(bookId);
        var book = bookService.getBookById(id);
        var rv = dataMapper.mapBookDto(book);
        return new AbstractResponseDto<>(rv);
    }

    @PostMapping(APIPrefixes.VENDOR + "/books")
    public AbstractResponseDto<BookDto> createNewBookByVendor(
            @Valid @RequestBody BookCreationDto dto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            log.warn("createNewBookByVendor: found validation errors in request body: {}", bindingResult.getAllErrors());
            throw new FormValidationException(bindingResult.getAllErrors());
        }

        var newBook = bookService.createNewBook(
                dto.getIsbn(),
                dto.getBookName(),
                dto.getPrice(),
                dto.getQuantity(),
                dto.getDescription(),
                dto.getImageUri(),
                dto.getGenresIds()
        );

        var rv = dataMapper.mapBookDto(newBook);

        return new AbstractResponseDto<>(rv);
    }

    @PatchMapping(APIPrefixes.VENDOR + "/books/{bookId}")
    public AbstractResponseDto<BookDto> updateBookDataByVendor(
            @PathVariable String bookId,
            @Valid @RequestBody UpdateBookInfoDto dto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            log.warn(
                    "updateBookDataByVendor: id: {}, found validation errors in request body: {}",
                    bookId,
                    bindingResult.getAllErrors()
            );
            throw new FormValidationException(bindingResult.getAllErrors());
        }

        var id = UUID.fromString(bookId);

        var updatedBook = bookService.patchBookInfo(
                id,
                dto.getIsbn(),
                dto.getBookName(),
                dto.getPrice(),
                dto.getQuantity(),
                dto.getDescription(),
                dto.getImageUri(),
                dto.getGenresIds()
        );

        var rv = dataMapper.mapBookDto(updatedBook);

        return new AbstractResponseDto<>(rv);
    }

}
