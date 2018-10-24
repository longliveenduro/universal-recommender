package com.actionml

import org.apache.http.{Header, HttpEntity}
import org.elasticsearch.client.{Response, ResponseListener, RestClient}
import scala.collection.JavaConverters._
import scala.concurrent.{Future, Promise}

object ScalaRestClient {

  implicit class ExtendedScalaRestClient(restClient: RestClient) {

    def performRequestFuture(method: String, endpoint: String, params: Map[String, String],
                             entity: HttpEntity, headers: Header*): Future[Response] = {
      val promise: Promise[Response] = Promise()
      val responseListener = new ResponseListener {
        override def onSuccess(response: Response): Unit = promise.success(response)
        override def onFailure(exception: Exception): Unit = promise.failure(exception)
      }
      restClient.performRequestAsync(method, endpoint, params.asJava, entity, responseListener, headers: _*)
      promise.future
    }
  }
}
