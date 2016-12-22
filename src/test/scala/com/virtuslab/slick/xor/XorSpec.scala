package com.virtuslab.slick.xor

import com.virtuslab.slick.xor.model.Model.{XorTestTable, _}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Random

import com.virtuslab.slick.xor.config.ExtendedMySQLProfile.api._

class XorSpec extends BaseTest {

  // tests
  "General operations on table" must {
    "mostly work" in {
      db.run(
        for {
          before <- XorTestTable.result
          id <- insert(XorTest(1, 1, None))
          after <- XorTestTable.result
        } yield(before, id, after)
      ).map { results =>
        results match { case (before, id, after) =>
          before.map(_.id) should not contain(id)
          after.map(_.id) should contain(id)
        }
      }.futureValue
    }
  }

  "Approach with XOR as a function" must {
    "work for constant values" in {
      val (a, b) = (Random.nextInt(500), Random.nextInt(500))

      db.run(
        XorTestTable
          .map(_ => xor(a, b))
          .take(1)
          .result
      ).map { results =>
        results.length should be(1)
        results.head should be(a ^ b)
      }.futureValue
    }

    "work for xor with columns" in {
      val (a, b) = (Random.nextInt(500), Random.nextInt(500))
      val c = a ^ b

      db.run(
        for {
          id <- insert(XorTest(a, b, Option(c)))
          results <- XorTestTable
            .filter(x => x.maybeNumber3 === xor(x.number1, x.number2) && x.id === id)
            .result
        } yield (results, id)
      ).map { _ match { case (results, id) =>
        results.head.id should be (id)
      }}.futureValue
    }

    "xor function with optional value" in {
      db.run(
        for {
          id <- insert(XorTest(Random.nextInt(500), Random.nextInt(500), None))
          results <- XorTestTable.filter(_.id === id).map(x => xor(x.number1.?, x.maybeNumber3)).result
        } yield(results)
      ).map {
        _.head should be(None)
      }.futureValue
    }

  "Approach with actual operator" must {
    "work for constant values" in {
      val (a, b) = (Random.nextInt(500), Random.nextInt(500))

      db.run(
        for {
          id <- insert(XorTest(a, b, None))
          results <- XorTestTable
                      .filter(_.id === id)
                      .map(row => row.number1 ^ row.number2)
                      .take(1)
                      .result
        } yield(results)
      ).map { results =>
        results.head should be(a ^ b)
      }.futureValue
    }
  }

  }

  private def insert(x: XorTest): DBIO[Long] = XorTestTable.returning(XorTestTable.map(_.id)) += x
}
