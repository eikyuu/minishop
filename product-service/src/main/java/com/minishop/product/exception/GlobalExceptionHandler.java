package com.minishop.product.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.time.Instant;

// @RestControllerAdvice = @ControllerAdvice + @ResponseBody
// S'applique à tous les @RestController du contexte Spring
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private ProblemDetail buildProblem(HttpStatus status, String detail, String typePath, String title) {
        ProblemDetail p = ProblemDetail.forStatusAndDetail(status, detail);
        p.setType(URI.create("/problems/" + typePath));
        p.setTitle(title);
        p.setProperty("timestamp", Instant.now());
        return p;
    }

    // ── 1. Produit introuvable ────────────────────────────────────────────
    @ExceptionHandler(ProductNotFoundException.class)
    public ProblemDetail handleProductNotFound(ProductNotFoundException ex) {
        log.warn("Product not found: {}", ex.getProductId());
        return buildProblem(HttpStatus.CONFLICT,ex.getMessage(),"urn:problem-type:product-not-found","Product not found");
    }

    // ── 2. Conflit métier ────────────────────────────────────────────────
    @ExceptionHandler(ProductAlreadyExistsException.class)
    public ProblemDetail handleProductAlreadyExists(ProductAlreadyExistsException ex) {
        log.warn("Product conflict: {}", ex.getMessage());
      return buildProblem(HttpStatus.CONFLICT,ex.getMessage(),"urn:problem-type:product-not-found","Product already exists");
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail handleDataIntegrity(DataIntegrityViolationException ex) {
        log.warn("Data integrity violation: {}", ex.getMostSpecificCause().getMessage());
        return buildProblem(HttpStatus.CONFLICT,
                "Resource conflict",
                "data-conflict",
                "Data conflict");
    }

}