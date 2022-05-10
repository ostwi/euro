package com.wodkamocni.euro2021.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import java.time.Instant.now
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.Date
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtCredentialsAuthenticationFilter(authenticationManager: AuthenticationManager) :
    UsernamePasswordAuthenticationFilter() {
    override fun attemptAuthentication(request: HttpServletRequest?, response: HttpServletResponse?): Authentication {

        return authenticationManager.authenticate(UsernamePasswordAuthenticationToken("", ""))
    }

    override fun successfulAuthentication(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        chain: FilterChain?,
        authResult: Authentication?
    ) {
        val key = "secure"
        val token = Jwts.builder()
            .setSubject(authResult!!.name)
            .claim("authorities", authResult.authorities)
            .setIssuedAt(Date.from(now()))
            .setExpiration(Date.from(LocalDateTime.now().plusWeeks(2).toInstant(ZoneOffset.UTC)))
            .signWith(Keys.hmacShaKeyFor(key.toByteArray()))
            .compact()
        response!!.addHeader("Authorization", "Bearer $token")
    }
}
