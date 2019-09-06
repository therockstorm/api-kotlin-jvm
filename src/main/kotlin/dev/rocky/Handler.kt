package dev.rocky

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2ProxyResponseEvent
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.logging.log4j.LogManager

open class Response
data class HelloResponse(val message: String, val input: Map<String, Any>) : Response()

@Suppress("unused")
class Handler : RequestHandler<Map<String, Any>, APIGatewayV2ProxyResponseEvent> {
    private val log = LogManager.getLogger(Handler::class.java)
    private val mapper = ObjectMapper()

    override fun handleRequest(input: Map<String, Any>, context: Context): APIGatewayV2ProxyResponseEvent {
        log.info("received: " + input.keys.toString())

        return res(
                statusCode = 200,
                body = HelloResponse("Go Serverless v1.x! Your Kotlin function executed successfully!", input),
                headers = mapOf("X-Powered-By" to "AWS Lambda & serverless")
        )
    }

    private fun res(statusCode: Int, body: Response, headers: Map<String, String> = mapOf()): APIGatewayV2ProxyResponseEvent {
        val res = APIGatewayV2ProxyResponseEvent()
        res.statusCode = statusCode
        try {
            res.body = mapper.writeValueAsString(body)
        } catch (e: JsonProcessingException) {
            log.error("Failed to serialize object", e)
        }
        res.headers = headers
        return res
    }
}
