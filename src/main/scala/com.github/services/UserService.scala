package com.github.services

import com.github.db.model.user.{User, UserView}
import com.github.db.repositories.UserRepository
import com.github.http.models.{ErrorResponse, Response, SuccessResponse}
import com.github.http.models.user._
import com.github.services.validation.Validate
import com.github.services.validation.UserValidation._

import scala.concurrent.Future
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
  def getAll(pageNumber: Int , pageSize: Int): Future[Seq[UserView]] = {
    val page = if (pageNumber < 1) 1 else 0
    UserRepository.page(page-1,pageSize)
  }

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
    * @param request
    * @return
    */
  def update(id:Long , request: UpdateRequest) = {
    Validate(request) >
      usernameEmptiness >
      usernameUniqueness andThen
      { req =>
        UserRepository.update(id,request.username,request.balance).map { result =>
          if (result>0) SuccessResponse("Updated") else ErrorResponse("Error has occured")
        }
      }
  }

  /**
    * Change user password
    * @param id
    * @param request
    * @return
    */
  def changePassword(id:Long , request:ChangePassRequest) : Future[Response] = {
    val newPassword = request.copy(id=Some(id))

    Validate(newPassword) >
      passwordLength >
      newPasswordIsNotSameAsOldOne andThen
      updatePassword
  }

  /**
    * Register new user
    * @param user
    * @return
    */
  def registerNewUser(user: RegisterRequest): Future[Response] = {
    Validate(user) >
      usernameEmptiness >
      passwordLength >
      usernameUniqueness andThen
      saveUserToDb
  }

  private def updatePassword(request: ChangePassRequest) : Future[Response] = {
    UserRepository.updatePassword(request.id.get,request.oldPass,request.newPass).map( _ =>
      SuccessResponse("Password has been changed")
    )
  }

  private def saveUserToDb(user: RegisterRequest): Future[RegisterResponse] = {
    UserRepository.insert(User(0,user.username,user.password,user.balance)).map(userId =>
      RegisterResponse(userId)
    )
  }



}

