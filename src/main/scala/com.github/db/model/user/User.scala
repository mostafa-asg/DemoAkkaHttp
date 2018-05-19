package com.github.db.model.user

import slick.jdbc.H2Profile.api._
import slick.lifted.Tag

/**
  * Row represents one row in `users` table
  *
  * @param id the row identifier
  * @param username the user's username
  * @param password the user's password
  * @param balance the amount of money in user's account
  */
case class Row(id: Long, username: String, password: String, balance:Double) {
  def toRowView = RowView(id,username,balance)
}

/**
  * Same as `Row` without `password` column
  */
case class RowView(id: Long, username: String, balance:Double)

/**
  * UsersTable defines the structure and schema of the `users` table
  * @param tag
  */
class UsersTable(tag: Tag) extends Table[Row](tag,"users") {
  def id = column[Long]("id",O.PrimaryKey,O.AutoInc)
  def username = column[String]("username",O.Unique,O.Length(30,true))
  def password = column[String]("password")
  def balance = column[Double]("balance",O.Default[Double](0))

  def * = (id ,username,password,balance) <> (Row.tupled , Row.unapply)
}