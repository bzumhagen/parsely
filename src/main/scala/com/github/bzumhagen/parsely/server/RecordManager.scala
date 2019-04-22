package com.github.bzumhagen.parsely.server

import akka.Done
import com.github.bzumhagen.parsely.{Parser, Records}
import com.github.bzumhagen.parsely.server.storage.RecordStorage

import scala.concurrent.{ExecutionContext, Future}

class RecordManager(storage: RecordStorage, parser: Parser)(implicit ec: ExecutionContext) {
  def record(input: String): Future[Done] = {
    try {
      storage.write(parser.parse(input))
    } catch {
      case e: Exception => Future.failed(e)
    }
  }

  def get(): Future[Records] =
    storage.readAll()
}
