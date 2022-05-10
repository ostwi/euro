package com.wodkamocni.euro2021.services

import com.wodkamocni.euro2021.model.User
import com.wodkamocni.euro2021.repositories.UserRepository
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.time.LocalDateTime.now
import java.time.ZoneOffset
import java.util.Date

@Service
class LoginService {

    @Autowired
    lateinit var userRepository: UserRepository

    val key: String = System.getenv("ENCRYPTION_KEY")

    fun loginUser(userName: String, password: String): String {
        val user = userRepository.findAll().find { it.username == userName }
        if (BCryptPasswordEncoder().matches(password, user?.password)) {
            return Jwts.builder()
                .setSubject(userName)
                .setIssuedAt(Date.from(now().toInstant(ZoneOffset.UTC)))
                .setExpiration(Date.from(now().plusWeeks(1).toInstant(ZoneOffset.UTC)))
                .signWith(Keys.hmacShaKeyFor(key.toByteArray()))
                .compact()
        } else throw IllegalArgumentException("unauthorized")
    }

    fun registerUser(userName: String, password: String): String {
        if (userRepository.findAll().find { it.username == userName } != null) {
            throw IllegalArgumentException("not allowed")
        }
        userRepository.save(
            User(
                username = userName,
                password = BCryptPasswordEncoder().encode(password)
            )
        )
        return Jwts.builder()
            .setSubject(userName)
            .setIssuedAt(Date.from(now().toInstant(ZoneOffset.UTC)))
            .setExpiration(Date.from(now().plusWeeks(1).toInstant(ZoneOffset.UTC)))
            .signWith(Keys.hmacShaKeyFor(key.toByteArray()))
            .compact()
    }

    fun getAuthorizedUser(token: String): User? {
        val jwt = getJwtBody(token)
        if (jwt.expiration.toInstant() < now().toInstant(ZoneOffset.UTC)) {
            return null
        }
        val userName = extractUsername(jwt)
        return userRepository.findAll().firstOrNull { it.username == userName }
    }

    private fun extractUsername(jwt: Claims): String = jwt.subject.toString()

    private fun getJwtBody(token: String): Claims {
        return Jwts.parserBuilder()
            .setSigningKey(Keys.hmacShaKeyFor(key.toByteArray()))
            .build()
            .parse(token).body as io.jsonwebtoken.Claims
    }
}
