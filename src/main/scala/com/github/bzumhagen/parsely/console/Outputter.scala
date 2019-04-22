package com.github.bzumhagen.parsely.console

import java.io.OutputStreamWriter
import java.time.format.DateTimeFormatter

import com.github.bzumhagen.parsely.Record

class Outputter(writer: OutputStreamWriter) {
  private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")

  def outputTable(title: String, records: Seq[Record]): Unit = {
    def format(table: Seq[Seq[Any]]): String = table match {
      case Seq() => ""
      case _ =>
        val sizes =
          for (row <- table) yield
            for (cell <- row) yield
              if (cell == null) 0 else cell.toString.length
        val colSizes =
          for (col <- sizes.transpose) yield col.max
        val rows =
          for (row <- table) yield formatRow(row, colSizes)
        formatRows(rowSeparator(colSizes), rows)
    }

    def formatRows(rowSeparator: String, rows: Seq[String]): String = (
      rowSeparator ::
        rows.head ::
        rowSeparator ::
        rows.tail.toList :::
        rowSeparator ::
        List()
      ).mkString("\n")

    def formatRow(row: Seq[Any], colSizes: Seq[Int]): String = {
      val cells =
        for ((item, size) <- row.zip(colSizes)) yield
          if (size == 0) "" else ("%" + size + "s").format(item)
      cells.mkString("|", "|", "|")
    }

    def rowSeparator(colSizes: Seq[Int]): String = colSizes map { "-" * _ } mkString("+", "+", "+")

    writer.write('\n')
    writer.write(title)
    writer.write('\n')
    writer.write(
      format(
          Seq("First Name", "Last Name", "Gender", "Favorite Color", "Date of Birth") +:
          records.map(r => Seq(r.firstName, r.lastName, r.gender.toString, r.favoriteColor, r.dob.format(formatter)))
      )
    )
    writer.write('\n')
    writer.flush()
  }
}
