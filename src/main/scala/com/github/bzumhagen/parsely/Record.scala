package com.github.bzumhagen.parsely

import java.time.LocalDate

sealed trait Gender
case object Gender {
  def apply(value: String): Gender = value.toLowerCase match {
    case "male"   => Male
    case "female" => Female
    case _        => Other
  }
}
case object Male extends Gender
case object Female extends Gender
case object Other extends Gender

case class Record(
  firstName: String,
  lastName: String,
  gender: Gender,
  favoriteColor: String,
  dob: LocalDate
)
