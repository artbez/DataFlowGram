package se.iimetra.dataflowgram.utils

import kotlinx.coroutines.await
import org.w3c.fetch.RequestCredentials
import org.w3c.fetch.RequestInit
import org.w3c.fetch.Response
import kotlin.browser.window
import kotlin.js.json

private const val POST = "POST"
private const val GET = "GET"

suspend fun get(url: String, body: dynamic = null, headers: dynamic): String {
  val response = request(GET, url, body, headers)
  if (!response.ok) {
    window.alert(response.text().await())
    throw RuntimeException("Exception when get")
  } else {
    return response.text().await()
  }
}

suspend fun post(url: String, body: dynamic = null): String {
  val response = request(POST, url, body)
  if (!response.ok) {
    window.alert(response.text().await())
    throw RuntimeException("Exception when post")
  } else {
    return response.text().await()
  }
}

private suspend fun request(method: String, url: String, body: dynamic, headers: dynamic = defaultHeaders): Response =
  window.fetch(url, object : RequestInit {
    override var method: String? = method
    override var body: dynamic = body
    override var credentials: RequestCredentials? = "same-origin".asDynamic()
    override var headers: dynamic = headers
  }).await()

private val defaultHeaders = json(
  "Accept" to "application/json",
  "Content-Type" to "application/json;charset=UTF-8"
)
