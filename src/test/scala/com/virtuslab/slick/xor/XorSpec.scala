package com.virtuslab.slick.xor

import com.virtuslab.slick.xor.model.Model.{XorTestTable, _}
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.ExecutionContext.Implicits.global

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

//      db.run(
//        XorTestTable.map(x =>
//          (x.number1, x.number2)
//        ).result
//      ).map { results =>
//        results.length should be >= 5
//      }.futureValue
    }
  }

  private def insert(x: XorTest): DBIO[Long] = XorTestTable.returning(XorTestTable.map(_.id)) += x
//
//    "general query test" in {
//      db.run(
//        StudentTable
//          .result
//      ).map { results =>
//        log.info(s"\n${results.mkString("\n")}")
//        results.length should be > 0
//      }.futureValue
//    }
//  }
}
