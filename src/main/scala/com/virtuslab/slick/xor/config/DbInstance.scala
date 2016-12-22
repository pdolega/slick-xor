package com.virtuslab.slick.xor.config

import com.typesafe.config.ConfigFactory
import org.flywaydb.core.Flyway
import slick.ast.Library.SqlOperator
import slick.jdbc.MySQLProfile
import slick.lifted.{BaseExtensionMethods, ExtensionMethods, Rep}

import scala.language.implicitConversions

trait ExtendedMySQLProfile extends MySQLProfile {
  trait ExtApi extends API {
    implicit def extendedIntColumn(c: Rep[Int]): BaseIntExtendedIntMethods[Int] = new BaseIntExtendedIntMethods[Int](c)
    implicit def extendedIntOptionColumn(c: Rep[Option[Int]]): BaseIntExtendedIntMethods[Option[Int]] = new BaseIntExtendedIntMethods[Option[Int]](c)
  }

  override val api: ExtApi = new ExtApi {}

  final class BaseIntExtendedIntMethods[P1](val c: Rep[P1]) extends ExtendedIntColumnMethods[P1, P1] with BaseExtensionMethods[P1]

  trait ExtendedIntColumnMethods[B1, P1] extends Any with ExtensionMethods[B1, P1] {
    def ^[P2, R](e: Rep[P2])(implicit om: o#arg[B1, P2]#to[B1, R]) =
      om.column(Operators.^, n, e.toNode)
  }

  object Operators {
    val ^ = new SqlOperator("^")
  }
}

object ExtendedMySQLProfile extends ExtendedMySQLProfile


object DbInstance {
  import ExtendedMySQLProfile.api._

  lazy val db = dbHandle

  private def dbHandle = {
    val config = ConfigFactory.load("application.conf")
    val dbConfig = config.getConfig("fileDb")

    val flyway = new Flyway
    flyway.setDataSource(dbConfig.getString("url"), dbConfig.getString("user"), dbConfig.getString("password"))
    flyway.migrate()

    Database.forConfig("fileDb")
  }
}
