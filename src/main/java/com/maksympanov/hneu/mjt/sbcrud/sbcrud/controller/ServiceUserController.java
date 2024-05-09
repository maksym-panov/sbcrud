package com.maksympanov.hneu.mjt.sbcrud.sbcrud.controller;

import com.maksympanov.hneu.mjt.sbcrud.sbcrud.auth.AuthContextHolder;
import com.maksympanov.hneu.mjt.sbcrud.sbcrud.config.APIPrefixes;
import com.maksympanov.hneu.mjt.sbcrud.sbcrud.dto.AbstractResponseDto;
import com.maksympanov.hneu.mjt.sbcrud.sbcrud.dto.MessageResponseDto;
import com.maksympanov.hneu.mjt.sbcrud.sbcrud.dto.TokenDto;
import com.maksympanov.hneu.mjt.sbcrud.sbcrud.dto.user.*;
import com.maksympanov.hneu.mjt.sbcrud.sbcrud.exception.FormValidationException;
import com.maksympanov.hneu.mjt.sbcrud.sbcrud.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@AllArgsConstructor
public class ServiceUserController {

    private UserService userService;

    private DataMapper dataMapper;

    private AuthContextHolder authContextHolder;

    @GetMapping(APIPrefixes.USER + "/info")
    public AbstractResponseDto<UserInfoDto> getUserInfo() {
        var authenticatedUser = authContextHolder.getCurrentUser();
        var userId = authenticatedUser.getId();
        log.info("getUserInfo: userId - {}", userId);
        var user = userService.getServiceUserById(userId);
        var rv = dataMapper.mapUserInfoDto(user);
        return new AbstractResponseDto<>(rv);
    }

    @GetMapping(APIPrefixes.ADMIN + "/users/{userId}")
    public AbstractResponseDto<UserInfoDto> getUserInfoByAdmin(@PathVariable String userId) {
        log.info("getUserInfoByAdmin: request for userId: {}", userId);
        var id = UUID.fromString(userId);
        var user = userService.getServiceUserById(id);
        var rv = dataMapper.mapUserInfoDto(user);
        return new AbstractResponseDto<>(rv);
    }

    @GetMapping(APIPrefixes.ADMIN + "/users")
    public AbstractResponseDto<UsersInfoDto> getUsersByAdmin(
            @RequestParam(required = false) Integer pageNumber,
            @RequestParam(required = false) Integer pageSize
    ) {
        log.info("getUsersByAdmin: pageNumber - {}, pageSize - {}", pageNumber, pageSize);

        if (pageNumber == null || pageSize == null || pageNumber <= 0 || pageSize <= 0) {
            pageNumber = 1;
            pageSize = 20;
        }

        var users = userService.getUsersListPageable(pageNumber, pageSize);
        var rv = dataMapper.mapUsersInfoDto(users);
        return new AbstractResponseDto<>(rv);
    }


    @PostMapping(APIPrefixes.PUBLIC + "/users/signup")
    public AbstractResponseDto<TokenDto> signup(
            @Valid @RequestBody SignUpDto dto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            log.warn("signup: found validation errors in request body: {}", bindingResult.getAllErrors());
            throw new FormValidationException(bindingResult.getAllErrors());
        }
        log.info("User signup request - email: {}", dto.getEmail());
        var jwtToken = userService.performSignUp(dto.getEmail(), dto.getPassword(), dto.getPhoneNumber(), dto.getFirstName(), dto.getLastName());
        var rv = new TokenDto(jwtToken);
        return new AbstractResponseDto<>(rv);
    }

    @PatchMapping(APIPrefixes.USER + "/info")
    public AbstractResponseDto<UserInfoDto> updateUserInfoByOwner(
            @Valid @RequestBody ChangeUserInfoDto dto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            log.warn("updateUserInfoByOwner: found validation errors in request body: {}", bindingResult.getAllErrors());
            throw new FormValidationException(bindingResult.getAllErrors());
        }
        var userId = authContextHolder.getCurrentUser().getId();
        var updatedUser = userService.patchUserInfo(
                userId,
                dto.getEmail(),
                dto.getPhoneNumber(),
                dto.getFirstName(),
                dto.getLastName()
        );
        var rv = dataMapper.mapUserInfoDto(updatedUser);
        return new AbstractResponseDto<>(rv);
    }

    @PatchMapping(APIPrefixes.ADMIN + "/users/{userId}")
    public AbstractResponseDto<UserInfoDto> updateUserInfoByAdmin(
            @PathVariable String userId,
            @Valid @RequestBody ChangeUserInfoDto dto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            log.warn("updateUserInfoByAdmin: found validation errors in request body: {}", bindingResult.getAllErrors());
            throw new FormValidationException(bindingResult.getAllErrors());
        }
        var id = UUID.fromString(userId);
        var updatedUser = userService.patchUserInfo(
                id,
                dto.getEmail(),
                dto.getPhoneNumber(),
                dto.getFirstName(),
                dto.getLastName()
        );
        var rv = dataMapper.mapUserInfoDto(updatedUser);
        return new AbstractResponseDto<>(rv);
    }

    @PutMapping(APIPrefixes.ADMIN + "/users/{userId}/role")
    public AbstractResponseDto<UserInfoDto> changeUserRoleByAdmin(
            @PathVariable String userId,
            @Valid @RequestBody ChangeUserRoleDto dto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            log.warn("changeUserRoleByAdmin: found validation errors in request body: {}", bindingResult.getAllErrors());
            throw new FormValidationException(bindingResult.getAllErrors());
        }
        var id = UUID.fromString(userId);
        var newUser = userService.changeUserRole(id, dto.getNewRole());
        var rv = dataMapper.mapUserInfoDto(newUser);
        return new AbstractResponseDto<>(rv);
    }

    @PutMapping(APIPrefixes.USER + "/password")
    public AbstractResponseDto<MessageResponseDto> changePasswordByUser(
            @Valid @RequestBody ChangePasswordDto dto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            log.warn("changePasswordByUser: found validation errors in request body: {}", bindingResult.getAllErrors());
            throw new FormValidationException(bindingResult.getAllErrors());
        }
        var userId = authContextHolder.getCurrentUser().getId();
        userService.changePassword(userId, dto.getOldPassword(), dto.getNewPassword());
        return new AbstractResponseDto<>(MessageResponseDto.success());
    }

    @PutMapping(APIPrefixes.ADMIN + "/users/{userId}/password")
    public AbstractResponseDto<MessageResponseDto> resetPasswordByAdmin(@PathVariable String userId) {
        var id = UUID.fromString(userId);
        userService.resetPassword(id);
        return new AbstractResponseDto<>(MessageResponseDto.success());
    }

}
