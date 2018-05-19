package com.github.services

import com.github.db.model.user.{Row, RowView}
import com.github.db.repositories.UserRepository
import com.github.http.models.{ErrorResponse, Response}
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

  /**
    * Register new user
    * @param user
    * @return
    */
  def registerNewUser(user: RegisterUserRequest): Future[Response] = {
    Validate(user) >
      usernameIsNotEmpty >
      usernameHasNotTaken >
      passwordIsNotTooShort andThen saveUserToDb
  }

  private def saveUserToDb(user: RegisterUserRequest): Future[RegisterUserResponse] = {
    UserRepository.insert(Row(0,user.username,user.password,user.balance)).map(userId =>
      RegisterUserResponse(userId)
    )
  }

  private def passwordIsNotTooShort(user: RegisterUserRequest): Option[ErrorResponse] = {
    if (user.password.length < 6) {
      Some(ErrorResponse("Password is too short"))
    } else {
      None
    }
  }

  private def usernameIsNotEmpty(user: RegisterUserRequest): Option[ErrorResponse] = {
    if (user.username.isEmpty) {
      Some(ErrorResponse("Please provide a username"))
    } else {
      None
    }
  }

  private def usernameHasNotTaken(user: RegisterUserRequest): Option[ErrorResponse] = {
    val result = Await.result(UserRepository.getByUsername(user.username),Duration.Inf)
    result match {
      case Some(_) => Some(ErrorResponse("Username has already taken"))
      case None => None
    }
  }

}

