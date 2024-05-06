package com.maksympanov.hneu.mjt.sbcrud.sbcrud.controller;

import com.maksympanov.hneu.mjt.sbcrud.sbcrud.config.APIPrefixes;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping(APIPrefixes.PUBLIC)
    public String testPublic() {
        return "Hello, Public!";
    }

    @GetMapping(APIPrefixes.VENDOR)
    public String testVendor() {
        return "Hello, Vendor!";
    }

    @GetMapping(APIPrefixes.ADMIN)
    public String testAdmin() {
        return "Hello, Admin!";
    }

}
