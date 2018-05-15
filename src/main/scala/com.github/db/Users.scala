package com.github.db

import com.github.db.Users.users
import slick.jdbc.H2Profile.api._
import slick.lifted.Tag

/**
  * User represents one row in `users` table
  * @param id the row identifier
  * @param username the user's username
  * @param password the user's password
  */
case class User(id: Option[Long], username: String , password: String)

/**
  * UsersTable defines the structure and schema of the `users` table
  * @param tag
  */
class UsersTable(tag: Tag) extends Table[User](tag,"users") {
  def id = column[Long]("id",O.PrimaryKey,O.AutoInc)
  def username = column[String]("username",O.Unique)
  def password = column[String]("password")

  def * = (id ?,username,password) <> (User.tupled , User.unapply)
}

object Users {

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
    * @return Returns all users
    */
  def getAll = db.run( users.result )

  /**
    * Get rows on the specific page number
    * @param pageNumber page number
    * @param pageSize page size
    * @return Returns only the subsets of `users` table
    */
  def page(pageNumber: Int , pageSize: Int) = db.run(
    users.drop( pageNumber*pageSize ).take(pageSize).result
  )

  /**
    * Get specific user by id
    * @param id user's id
    * @return Returns user if id found
    */
  def getById(id: Long) = db.run(
    users.filter( users => users.id === id ).take(1).result
  )

  /**
    * Insert a user to the `users` table
    * @param newUser the new user to add
    * @return
    */
  def insert(newUser: User) = db.run(
    users += newUser
  )

  /**
    * Update the user information
    * @param id the user's id
    * @param updatedUser the new updated information
    * @return
    */
  def update(id: Long , updatedUser: User) = {
    val oldUser = users.filter(users => users.id === id)
    db.run(oldUser.update(updatedUser))
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

