package com.virtuslab.slick.xor.model

import com.virtuslab.slick.xor.config.ExtendedMySQLProfile.api._

object Model {
  def xor = SimpleFunction.binary[Int, Int, Int]("xorf")

  def xor(rep1: Rep[Option[Int]], rep2: Rep[Option[Int]]) = {
    val func = SimpleFunction.binary[Option[Int], Option[Int], Option[Int]]("xorf")
    func(rep1, rep2)
  }

  case class XorTest(number1: Int,
                     number2: Int,
                     optNumber3: Option[Int],
                     id: Long = -1)

  // scalastyle:off
  class XorTestTable(tag: Tag) extends Table[XorTest](tag, _schemaName = Option("slick_xor"), "xor_test") {
    def number1 = column[Int]("number1")
    def number2 = column[Int]("number2")
    def maybeNumber3 = column[Option[Int]]("number3")
    def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)

    def * = (number1, number2, maybeNumber3, id) <> (XorTest.tupled, XorTest.unapply)
  }
  // scalastyle:on

  lazy val XorTestTable = TableQuery[XorTestTable]
}