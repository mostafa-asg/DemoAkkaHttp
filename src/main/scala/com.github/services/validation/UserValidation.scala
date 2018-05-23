package com.github.services.validation

import com.github.db.repositories.UserRepository
import com.github.http.models.ErrorResponse
import com.github.http.models.user.{ChangePassRequest, RegisterRequest, UpdateRequest}

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object UserValidation {

  def passwordLength(request: RegisterRequest): Option[ErrorResponse] = {
    passwordShouldNotBeShort(request.password)
  }

  def passwordLength(request: ChangePassRequest): Option[ErrorResponse] = {
    passwordShouldNotBeShort(request.newPass)
  }

  def newPasswordIsNotSameAsOldOne(request: ChangePassRequest): Option[ErrorResponse] = {
    if (request.newPass == request.oldPass) {
      Some(ErrorResponse("New password must not be the same as old password"))
    } else {
      None
    }
  }

  def usernameEmptiness(request: RegisterRequest): Option[ErrorResponse] = {
    usernameShouldNotBeEmpty(request.username)
  }

  def usernameEmptiness(request: UpdateRequest): Option[ErrorResponse] = {
    usernameShouldNotBeEmpty(request.username)
  }

  def usernameUniqueness(request: RegisterRequest): Option[ErrorResponse] = {
    usernameShouldNotBeTaken(request.username)
  }

  def usernameUniqueness(request: UpdateRequest): Option[ErrorResponse] = {
    usernameShouldNotBeTaken(request.username)
  }

  private def usernameShouldNotBeTaken(username: String): Option[ErrorResponse] = {
    // TODO think about this blocking code
    // Blocking code is not a good idea, especially waiting for ever
    val result = Await.result(UserRepository.getByUsername(username),Duration.Inf)
    result match {
      case Some(_) => Some(ErrorResponse("Username has already taken"))
      case None => None
    }
  }

  private def usernameShouldNotBeEmpty(username: String): Option[ErrorResponse] = {
    if (username.isEmpty) {
      Some(ErrorResponse("Please provide a username"))
    } else {
      None
    }
  }

  private def passwordShouldNotBeShort(pass: String): Option[ErrorResponse] = {
    if (pass.length < 6) {
      Some(ErrorResponse("Password is too short"))
    } else {
      None
    }
  }

}
