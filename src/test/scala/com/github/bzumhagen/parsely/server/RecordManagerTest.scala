package com.github.bzumhagen.parsely.server

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import akka.Done
import com.github.bzumhagen.parsely.{Female, Male, Parser, Record, Records}
import com.github.bzumhagen.parsely.server.storage.RecordStorage
import org.scalamock.scalatest.AsyncMockFactory
import org.scalatest.{AsyncFlatSpec, Matchers}

import scala.concurrent.Future

class RecordManagerTest extends AsyncFlatSpec with Matchers with AsyncMockFactory {
  val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")

  "RecordManager" should "record a valid line of input with pipe delineation" in {
    val storage = mock[RecordStorage]
    val parser = mock[Parser]
    val manager = new RecordManager(storage, parser)
    val record = Record("Jane", "Doe", Female, "Blue", LocalDate.parse("01/02/1990", formatter))
    val validLine = s"${record.lastName} | ${record.firstName} | ${record.gender} | ${record.favoriteColor} | ${formatter.format(record.dob)}"

    (parser.parse(_: String)).expects(validLine).returns(record)
    (storage.write _).expects(record).returns(Future.successful(Done))

    manager.record(validLine).map { result =>
      result shouldBe Done
    }
  }

  it should "record a valid line of input with comma delineation" in {
    val storage = mock[RecordStorage]
    val parser = mock[Parser]
    val manager = new RecordManager(storage, parser)
    val record = Record("Jane", "Doe", Female, "Blue", LocalDate.parse("01/02/1990", formatter))
    val validLine = s"${record.lastName}, ${record.firstName}, ${record.gender}, ${record.favoriteColor}, ${formatter.format(record.dob)}"

    (parser.parse(_: String)).expects(validLine).returns(record)
    (storage.write _).expects(record).returns(Future.successful(Done))

    manager.record(validLine).map { result =>
      result shouldBe Done
    }
  }

  it should "record a valid line of input with space delineation" in {
    val storage = mock[RecordStorage]
    val parser = mock[Parser]
    val manager = new RecordManager(storage, parser)
    val record = Record("Jane", "Doe", Female, "Blue", LocalDate.parse("01/02/1990", formatter))
    val validLine = s"${record.lastName} ${record.firstName} ${record.gender} ${record.favoriteColor} ${formatter.format(record.dob)}"

    (parser.parse(_: String)).expects(validLine).returns(record)
    (storage.write _).expects(record).returns(Future.successful(Done))

    manager.record(validLine).map { result =>
      result shouldBe Done
    }
  }

  it should "fail to record an invalid line of input" in {
    val storage = mock[RecordStorage]
    val parser = mock[Parser]
    val manager = new RecordManager(storage, parser)
    val invalidLine = "Doe<>Jane<>Female<>Blue<>01/02/1990"

    (parser.parse(_: String)).expects(invalidLine).throws(new UnsupportedOperationException)

    recoverToSucceededIf[UnsupportedOperationException] {
      manager.record(invalidLine)
    }
  }

  it should "retrieve records" in {
    val storage = mock[RecordStorage]
    val parser = mock[Parser]
    val manager = new RecordManager(storage, parser)
    val expectedRecords = Records(Set(
      Record("John", "Doe", Male, "Blue", LocalDate.parse("01/02/1990", formatter))
    ))

    (storage.readAll _).expects().returns(Future.successful(expectedRecords))

    manager.get().map { result =>
      result shouldBe expectedRecords
    }
  }
}
