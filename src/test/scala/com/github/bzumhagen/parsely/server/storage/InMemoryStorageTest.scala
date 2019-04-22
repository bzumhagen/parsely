package com.github.bzumhagen.parsely.server.storage

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import akka.Done
import com.github.bzumhagen.parsely.{Female, Male, Record}
import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.ExecutionContext.Implicits.global

class InMemoryStorageTest extends FlatSpec with Matchers {
  val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")

  "InMemoryStorage" should "store and retrieve records" in {
    val storage = new InMemoryStorage
    val expectedRecords = Set(
      Record("John", "Doe", Male, "Blue", LocalDate.parse("01/01/1990", formatter)),
      Record("Jane", "Doe", Female, "Red", LocalDate.parse("10/11/1989", formatter))
    )

    storage.write(expectedRecords.head).map { result =>
      result shouldBe Done
    }
    storage.write(expectedRecords.last).map { result =>
      result shouldBe Done
    }
    storage.readAll().map { result =>
      result shouldBe expectedRecords
    }
  }

}
