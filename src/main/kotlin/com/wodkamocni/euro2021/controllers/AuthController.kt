package com.wodkamocni.euro2021.controllers

import com.wodkamocni.euro2021.controllers.dtos.LoginDto
import com.wodkamocni.euro2021.services.LoginService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
@CrossOrigin(origins = ["*"])
class AuthController {

    @Autowired
    lateinit var loginService: LoginService

    @PostMapping("/login")
    fun login(@RequestBody loginDto: LoginDto): ResponseEntity<Any> {
        return try {
            val jwt = loginService.loginUser(loginDto.username, loginDto.password)
            ResponseEntity.ok().body(jwt)
        } catch (e: Exception) {
            print(e)
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        }
    }

    @PostMapping("/register")
    fun register(@RequestBody loginDto: LoginDto): ResponseEntity<Any> {
        return try {
            val jwt = loginService.registerUser(loginDto.username, loginDto.password)
            ResponseEntity.ok().body(jwt)
        } catch (e: Exception) {
            print(e)
            ResponseEntity.badRequest().build()
        }
    }
}
