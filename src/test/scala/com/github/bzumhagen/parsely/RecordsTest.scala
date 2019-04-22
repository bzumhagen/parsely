package com.github.bzumhagen.parsely

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import org.scalatest.{FlatSpec, Matchers}

class RecordsTest extends FlatSpec with Matchers {
  val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")

  "Records" should "sort records by gender descending and then last name ascending" in {
    val expectedRecords = Seq(
      Record("Jane", "Doeo", Female, "Red", LocalDate.parse("10/12/1980", formatter)),
      Record("Jane", "Smitho", Female, "Red", LocalDate.parse("10/11/1988", formatter)),
      Record("Rain", "Doee", Other("Fluid"), "Black", LocalDate.parse("12/26/1992", formatter)),
      Record("Rain", "Smithe", Other("Fluid"), "Black", LocalDate.parse("12/25/1974", formatter)),
      Record("John", "Doey", Male, "Blue", LocalDate.parse("01/02/1970", formatter)),
      Record("John", "Smithy", Male, "Blue", LocalDate.parse("01/01/1970", formatter))
    )

    Records(expectedRecords.toSet).byGender shouldBe expectedRecords
  }

  it should "sort records by birth date ascending" in {
    val expectedRecords = Seq(
      Record("John", "Smithy", Male, "Blue", LocalDate.parse("01/01/1970", formatter)),
      Record("John", "Doey", Male, "Blue", LocalDate.parse("01/02/1970", formatter)),
      Record("Rain", "Smithe", Other("Fluid"), "Black", LocalDate.parse("12/25/1974", formatter)),
      Record("Jane", "Doeo", Female, "Red", LocalDate.parse("10/12/1980", formatter)),
      Record("Jane", "Smitho", Female, "Red", LocalDate.parse("10/11/1988", formatter)),
      Record("Rain", "Doee", Other("Fluid"), "Black", LocalDate.parse("12/26/1992", formatter))
    )

    Records(expectedRecords.toSet).byDob shouldBe expectedRecords
  }

  it should "sort records by last name descending" in {
    val expectedRecords = Seq(
      Record("John", "Smithy", Male, "Blue", LocalDate.parse("01/01/1970", formatter)),
      Record("Jane", "Smitho", Female, "Red", LocalDate.parse("10/11/1988", formatter)),
      Record("Rain", "Smithe", Other("Fluid"), "Black", LocalDate.parse("12/25/1974", formatter)),
      Record("John", "Doey", Male, "Blue", LocalDate.parse("01/02/1970", formatter)),
      Record("Jane", "Doeo", Female, "Red", LocalDate.parse("10/12/1980", formatter)),
      Record("Rain", "Doee", Other("Fluid"), "Black", LocalDate.parse("12/26/1992", formatter))
    )

    Records(expectedRecords.toSet).byLastName shouldBe expectedRecords
  }
}
