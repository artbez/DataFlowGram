package se.iimetra.dataflowgram.utils

import kotlinx.coroutines.await
import org.w3c.fetch.RequestCredentials
import org.w3c.fetch.RequestInit
import org.w3c.fetch.Response
import kotlin.browser.window
import kotlin.js.json

private const val POST = "POST"
private const val GET = "GET"

suspend fun get(url: String, body: dynamic = null, headers: dynamic = defaultHeaders): String {
  val response = request(GET, url, body, headers)
  if (!response.ok) {
    window.alert(response.text().await())
    throw RuntimeException("Exception when get")
  } else {
    return response.text().await()
  }
}

suspend fun post(url: String, body: dynamic = null, headers: dynamic = defaultHeaders): String {
  val response = request(POST, url, body, headers)
  if (!response.ok) {
    window.alert(response.text().await())
    throw RuntimeException("Exception when post")
  } else {
    return response.text().await()
  }
}

suspend fun request(method: String, url: String, body: dynamic, headers: dynamic = defaultHeaders): Response =
  window.fetch(url, object : RequestInit {
    var processData: Boolean? = false
    var data = body
    var contentType = false
    override var method: String? = method
    override var body: dynamic = body
    override var credentials: RequestCredentials? = "same-origin".asDynamic()
    override var headers: dynamic = headers
  }).await()

private val defaultHeaders = json(
  "Accept" to "application/json",
  "Content-Type" to "application/json;charset=UTF-8"
)

val fileHeaders = json(
  // "Accept" to "multipart/form-data",
  "enctype" to "multipart/form-data",
  "processData" to false,
  "contentType" to false
)
