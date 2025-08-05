package io.github.mrspock182.resource.advice;

import io.github.mrspock182.exception.InternalServerException;
import io.github.mrspock182.exception.NotFoundException;
import io.github.mrspock182.resource.dto.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
public class RestExceptionAdvice {
    private static final Logger LOGGER = LoggerFactory.getLogger(RestExceptionAdvice.class);

    @ResponseBody
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler({InternalServerException.class, Exception.class})
    public ErrorResponse handleInternalServerError(
            final HttpServletRequest request,
            final Exception exception) {
        LOGGER.error("Erro interno na aplicação: {}", exception.getMessage());

        return new ErrorResponse(
                LocalDateTime.now(),
                request.getServletPath(),
                INTERNAL_SERVER_ERROR.value(),
                INTERNAL_SERVER_ERROR.getReasonPhrase(),
                exception.getMessage());
    }

    @ResponseBody
    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ErrorResponse handleNotFound(
            final HttpServletRequest request,
            final NotFoundException exception) {
        LOGGER.info("Item não encontrado: {}", exception.getMessage());

        return new ErrorResponse(
                LocalDateTime.now(),
                request.getServletPath(),
                NOT_FOUND.value(),
                NOT_FOUND.getReasonPhrase(),
                exception.getMessage());
    }
}