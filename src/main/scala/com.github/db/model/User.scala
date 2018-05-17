package com.github.db.model

import slick.lifted.Tag
import slick.jdbc.H2Profile.api._

/**
  * User represents one row in `users` table
  *
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