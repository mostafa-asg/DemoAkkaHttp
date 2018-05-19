package com.github.services

import com.github.db.model.user.{Row, RowView}
import com.github.db.repositories.UserRepository
import com.github.http.models.{ErrorResponse, Response, SuccessResponse}
import com.github.http.models.user._
import com.github.services.validation.Validate

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global

object UserService {

  /**
    * Get All users
    */
  def getAll = UserRepository.getAll

  /**
    * Get users at the specific page number
    * @param pageNumber page number
    * @param pageSize page size
    * @return
    */
  def getAll(pageNumber: Int , pageSize: Int): Future[Seq[RowView]] = UserRepository.page(pageNumber,pageSize)

  /**
    * Get user by his id
    * @param id
    * @return
    */
  def getById(id: Long) = UserRepository.getById(id)

  /**
    * Get user by his username
    * @param username
    * @return
    */
  def getByUsername(username: String) = UserRepository.getByUsername(username)

  /**
    * Delete user
    * @param id
    * @return
    */
  def delete(id: Long) = UserRepository.deleteById(id)

  /**
    * Update user
    * @param id
    * @param user
    * @return
    */
  def update(id:Long , user: RowView) = UserRepository.update(id,user)

  def changePassword(id:Long , request:ChangePassRequest) : Future[Response] = {

    val changePassword = request.copy(id=Some(id))

    Validate(changePassword) >
      passwordLength >
      newPasswordIsNotSameAsOldOne andThen updatePassword
  }

  private def updatePassword(request: ChangePassRequest) : Future[Response] = {
    UserRepository.updatePassword(request.id.get,request.oldPass,request.newPass).map( _ =>
      SuccessResponse("OK")
    )
  }

  /**
    * Register new user
    * @param user
    * @return
    */
  def registerNewUser(user: RegisterUserRequest): Future[Response] = {
    Validate(user) >
      usernameEmptines >
      usernameUniqueness >
      passwordLength andThen saveUserToDb
  }

  private def saveUserToDb(user: RegisterUserRequest): Future[RegisterUserResponse] = {
    UserRepository.insert(Row(0,user.username,user.password,user.balance)).map(userId =>
      RegisterUserResponse(userId)
    )
  }

  private def passwordLength(user: RegisterUserRequest): Option[ErrorResponse] = {
    passwordShouldNotBeShort(user.password)
  }

  private def passwordLength(request: ChangePassRequest): Option[ErrorResponse] = {
    passwordShouldNotBeShort(request.newPass)
  }

  private def newPasswordIsNotSameAsOldOne(request: ChangePassRequest): Option[ErrorResponse] = {
    if (request.newPass == request.oldPass) {
      Some(ErrorResponse("New password must not be the same as old password"))
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

  private def usernameEmptines(user: RegisterUserRequest): Option[ErrorResponse] = {
    if (user.username.isEmpty) {
      Some(ErrorResponse("Please provide a username"))
    } else {
      None
    }
  }

  private def usernameUniqueness(user: RegisterUserRequest): Option[ErrorResponse] = {
    val result = Await.result(UserRepository.getByUsername(user.username),Duration.Inf)
    result match {
      case Some(_) => Some(ErrorResponse("Username has already taken"))
      case None => None
    }
  }

}

