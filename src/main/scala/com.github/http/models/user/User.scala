package com.github.http.models.user

import com.github.http.models.Response

case class RegisterUserRequest(username: String,
                               password: String,
                               balance: Double) extends Response
case class RegisterUserResponse(userId: Long, success:Boolean=true) extends Response