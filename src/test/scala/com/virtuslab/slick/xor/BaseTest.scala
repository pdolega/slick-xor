package com.virtuslab.slick.xor

import com.typesafe.scalalogging.Logger
import com.virtuslab.slick.xor.config.DbInstance
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Millis, Seconds, Span}
import org.scalatest.{BeforeAndAfterAll, Matchers, TestSuite, WordSpecLike}
import slick.jdbc.H2Profile.api._
import slick.lifted.Query

import scala.concurrent.duration.DurationLong
import scala.concurrent.{Await, Future}
import scala.language.higherKinds

trait BaseTest extends TestSuite with BeforeAndAfterAll with WordSpecLike with Matchers with ScalaFutures {

  implicit val config = generateTimeoutConfig

  val log = Logger(getClass)

  lazy val db = DbInstance.db

  protected def blockingWait[T](f: Future[T]) = Await.result(f, config.timeout.totalNanos.nanos)

  protected def querySync[E, U, C[_]](q: Query[E, U, C]) = Await.result(db.run(q.result), 2.seconds)

  private def generateTimeoutConfig =
    PatienceConfig(timeout = scaled(Span(5, Seconds)), interval = scaled(Span(100, Millis)))
}
