package com.virtuslab.slick.xor.config

import com.typesafe.config.ConfigFactory
import org.flywaydb.core.Flyway
import slick.jdbc.H2Profile.api._

object DbInstance {
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
