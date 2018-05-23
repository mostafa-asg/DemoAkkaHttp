package com.github.http.models.user

import com.github.http.models.Response

case class RegisterRequest(username: String,
                           password: String,
                           balance: Double)
case class RegisterResponse(userId: Long, success:Boolean=true) extends Response

case class UpdateRequest(username:String, balance: Double)
case class ChangePassRequest(id: Option[Long], oldPass:String, newPass:String)