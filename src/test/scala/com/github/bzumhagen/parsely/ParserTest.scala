package com.github.bzumhagen.parsely

import java.time.LocalDate

import better.files.File
import org.scalatest.{FlatSpec, Matchers}

class ParserTest extends FlatSpec with Matchers {
  private val parser = new Parser

  "Parser" should "successfully parse file with pipe delineation" in {
    val expectedRecords = Set(
      Record("John", "Doe", Male, "Blue", LocalDate.parse("01/01/1990", parser.formatter)),
      Record("Jane", "Doe", Female, "Red", LocalDate.parse("10/11/1989", parser.formatter)),
      Record("Rain", "Doe", Other("fluid"), "Black", LocalDate.parse("12/25/1970", parser.formatter))
    )

    File.usingTemporaryFile() { tempFile =>
      expectedRecords.foreach { case Record(firstName, lastName, gender, favoriteColor, dob) =>
        tempFile.appendLine(s"$lastName | $firstName | $gender | $favoriteColor | ${parser.formatter.format(dob)}")
      }

      parser.parse(tempFile) shouldBe Records(expectedRecords)
    }
  }

  it should "successfully parse file with comma delineation" in {
    val expectedRecords = Set(
      Record("John", "Doe", Male, "Blue", LocalDate.parse("01/01/1990", parser.formatter)),
      Record("Jane", "Doe", Female, "Red", LocalDate.parse("10/11/1989", parser.formatter)),
      Record("Rain", "Doe", Other("fluid"), "Black", LocalDate.parse("12/25/1970", parser.formatter))
    )

    File.usingTemporaryFile() { tempFile =>
      expectedRecords.foreach { case Record(firstName, lastName, gender, favoriteColor, dob) =>
        tempFile.appendLine(s"$lastName, $firstName, $gender, $favoriteColor, ${parser.formatter.format(dob)}")
      }

      parser.parse(tempFile) shouldBe Records(expectedRecords)
    }
  }

  it should "successfully parse file with space delineation" in {
    val expectedRecords = Set(
      Record("John", "Doe", Male, "Blue", LocalDate.parse("01/01/1990", parser.formatter)),
      Record("Jane", "Doe", Female, "Red", LocalDate.parse("10/11/1989", parser.formatter)),
      Record("Rain", "Doe", Other("fluid"), "Black", LocalDate.parse("12/25/1970", parser.formatter))
    )

    File.usingTemporaryFile() { tempFile =>
      expectedRecords.foreach { case Record(firstName, lastName, gender, favoriteColor, dob) =>
        tempFile.appendLine(s"$lastName $firstName $gender $favoriteColor ${parser.formatter.format(dob)}")
      }

      parser.parse(tempFile) shouldBe Records(expectedRecords)
    }
  }

  it should "successfully parse a single line" in {
    val expectedRecord = Record("John", "Doe", Male, "Blue", LocalDate.parse("01/01/1990", parser.formatter))
    val expectedLine =
      s"${expectedRecord.lastName} ${expectedRecord.firstName} ${expectedRecord.gender} ${expectedRecord.favoriteColor} ${parser.formatter.format(expectedRecord.dob)}"

    parser.parse(expectedLine) shouldBe expectedRecord
  }

  it should "successfully parse an empty file" in {
    File.usingTemporaryFile() { tempFile =>
      parser.parse(tempFile).set shouldBe Set.empty
    }
  }

  it should "throw an exception if file has unknown delineation" in {
    val record =  Record("John", "Doe", Male, "Blue", LocalDate.parse("01/01/1990", parser.formatter))

    File.usingTemporaryFile() { tempFile =>
      val lineUnknownDelineation = s"${record.lastName}<>${record.firstName}<>${record.gender}<>${record.favoriteColor}<>${parser.formatter.format(record.dob)}"
      tempFile.appendLine(lineUnknownDelineation)

      the [UnsupportedOperationException] thrownBy {
        parser.parse(tempFile)
      } should have message s"Unable to identify a supported delimiter [$lineUnknownDelineation]"
    }
  }

  it should "throw an exception if a record in the file doesn't have all required fields" in {
    val record =  Record("John", "Doe", Male, "Blue", LocalDate.parse("01/01/1990", parser.formatter))

    File.usingTemporaryFile() { tempFile =>
      val lineMissingField = s"${record.lastName} | ${record.gender} | ${record.favoriteColor} | ${parser.formatter.format(record.dob)}"
      tempFile.appendLine(lineMissingField)

      the [UnsupportedOperationException] thrownBy {
        parser.parse(tempFile)
      } should have message s"Parsed record doesn't have all required fields [$lineMissingField]"
    }
  }
}
