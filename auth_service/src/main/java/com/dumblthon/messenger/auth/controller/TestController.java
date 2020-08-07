package com.dumblthon.messenger.auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SuppressWarnings("unused")
@RestController
@RequestMapping(path = "/api/v1")
public class TestController {

    @GetMapping(path = "/test/protected")
    public void testProtectedEndpoint() {

    }

}
