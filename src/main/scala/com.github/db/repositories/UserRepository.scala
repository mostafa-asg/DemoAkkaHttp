package com.github.db.repositories

import com.github.db.model.user.{User, UserView, UsersTable}
import slick.jdbc.H2Profile.api._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object UserRepository {

  val db = Database.forConfig("db")
  val users = TableQuery[UsersTable]

  /**
    * Create `users` table in database
    * @return
    */
  def createTable = db.run(
    users.schema.create
  )

  /**
    * GetAll returns all `users` row
    * @return Returns all users
    */
  def getAll = db.run(users.result).map(_.map(_.toUserView))

  /**
    * Get rows on the specific page number
    * @param pageNumber page number
    * @param pageSize page size
    * @return Returns only the subsets of `users` table
    */
  def page(pageNumber: Int , pageSize: Int) = db.run(
    users.drop( pageNumber*pageSize ).take(pageSize).result
  ).map(_.map(_.toUserView))

  /**
    * Get specific user by id
    * @param id user's id
    * @return Returns user if id found
    */
  def getById(id: Long) : Future[Option[UserView]] = {
    val result = db.run(
      users.filter( users => users.id === id ).take(1).result
    )
    result.map { rows =>
      if (rows.isEmpty) None else Some(rows.head.toUserView)
    }
  }

  /**
    * Get specific user by username
    * @param username user's username
    * @return Returns user if username found
    */
  def getByUsername(username: String) : Future[Option[UserView]] = {
    val result = db.run(
      users.filter( users => users.username === username ).take(1).result
    )
    result.map { rows =>
      if (rows.isEmpty) None else Some(rows.head.toUserView)
    }
  }

  /**
    * Insert a user to the `users` table
    * @param newUser the new user to add
    * @return
    */
  def insert(newUser: User) = db.run(
    (users returning users.map(_.id)) += newUser
  )

  /**
    * Update the user information
    * @param id the user's id
    * @param username the updated username
    * @param balance the updated balance
    * @return
    */
  def update(id: Long, username:String , balance: Double) = {
    val oldUser = users.filter(users => users.id === id).map( x=>
      (x.username,x.balance)
    )
    db.run(oldUser.update((username,balance)))
  }

  /**
    * Update user's password
    * @param id the user's id
    * @param oldPass old password
    * @param newPass new password
    * @return
    */
  def updatePassword(id: Long, oldPass: String, newPass: String) = {
    val user = users.filter(users => users.id === id && users.password === oldPass).map( x =>
      x.password
    )
    db.run(user.update(newPass))
  }

  /**
    * Delete a user by `id`
    * @param id the user's id
    * @return
    */
  def deleteById(id: Long) = db.run(
    users.filter( users => users.id === id ).delete
  )

}
