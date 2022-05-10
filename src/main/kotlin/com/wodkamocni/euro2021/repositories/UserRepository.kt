package com.wodkamocni.euro2021.repositories

import com.wodkamocni.euro2021.model.User
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : CrudRepository <User, Long>
