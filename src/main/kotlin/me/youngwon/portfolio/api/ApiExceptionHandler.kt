package me.youngwon.portfolio.api

import jakarta.validation.ConstraintViolationException
import me.youngwon.portfolio.generated.model.ApiErrorResponse
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ApiExceptionHandler {
    @ExceptionHandler(
        MethodArgumentNotValidException::class,
        ConstraintViolationException::class,
        HttpMessageNotReadableException::class,
    )
    fun handleBadRequest(): ResponseEntity<ApiErrorResponse> =
        ResponseEntity.badRequest()
            .body(
                ApiErrorResponse(
                    code = "INVALID_REQUEST",
                    message = "Request validation failed.",
                ),
            )
}
