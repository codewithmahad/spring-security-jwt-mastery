package dev.shaikhmahad.security.exceptions;

import dev.shaikhmahad.security.dto.response.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // --- 1. CUSTOM BUSINESS EXCEPTIONS ---

    // Catches errors when a specific database record isn't found (e.g., searching for User ID 99 that doesn't exist)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> resourceNotFoundExceptionHandler(ResourceNotFoundException ex) {
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .message(ex.getMessage())
                .success(false)
                .build();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    // Catches errors when a user tries to register/update with data that must be unique but is already taken (e.g., duplicate email)
    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Void>> handleResourceAlreadyExistsException(ResourceAlreadyExistsException ex) {
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(false)
                .message(ex.getMessage())
                .build();

        // 409 Conflict is the industry standard HTTP status for duplicate data
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    // Catches general business logic failures that we manually trigger (e.g., "Insufficient balance" or "Account locked")
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse<Void>> handleApiException(ApiException ex) {
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .message(ex.getMessage())
                .success(false)
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    // --- 2. VALIDATION & DATA INTEGRITY EXCEPTIONS ---

    // Catches DTO validation failures from @Valid (e.g., a user leaving an @NotBlank email field empty)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ApiResponse<Map<String, String>> response = ApiResponse.<Map<String, String>>builder()
                .message("Validation Failed")
                .success(false)
                .data(errors)
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Catches entity-level validation failures that happen right before the database tries to save the object
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleConstraintViolationException(ConstraintViolationException ex) {
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .message("Database constraint violation: " + ex.getMessage())
                .success(false)
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Catches database collisions, like trying to save a duplicate email into a @Column(unique=true) field
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .message("Data integrity violation. This usually happens when trying to save a duplicate record for a unique field.")
                .success(false)
                .build();
        return new ResponseEntity<>(response, HttpStatus.CONFLICT); // 409 Conflict
    }


    // --- 3. HTTP PROTOCOL & ROUTING EXCEPTIONS ---

    // Catches 404 URL typos (e.g., the client requests /api/usrs instead of /api/users)
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNoResourceFoundException(NoResourceFoundException ex) {
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .message("The URL you are trying to reach does not exist: /" + ex.getResourcePath())
                .success(false)
                .build();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    // Catches requests made with the wrong HTTP verb (e.g., client sends a POST request to a GET-only endpoint)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<Void>> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .message("HTTP Method '" + ex.getMethod() + "' is not supported for this endpoint. Supported methods are: " + ex.getSupportedHttpMethods())
                .success(false)
                .build();
        return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
    }

    // Catches requests where the client sends the wrong data format (e.g., sending plain text instead of JSON)
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ApiResponse<Void>> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException ex) {
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .message("Unsupported Media Type. Please ensure you are sending the correct format (e.g., application/json).")
                .success(false)
                .build();
        return new ResponseEntity<>(response, HttpStatus.UNSUPPORTED_MEDIA_TYPE); // 415 Status Code
    }

    // Catches requests that are missing a mandatory query parameter (e.g., forgetting the ?status= query in the URL)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<Void>> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .message("Missing required parameter: '" + ex.getParameterName() + "' of type " + ex.getParameterType())
                .success(false)
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Catches URL parameters that are the wrong data type (e.g., passing text "abc" into a variable that expects an Integer ID)
    @ExceptionHandler(org.springframework.web.method.annotation.MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentTypeMismatchException(org.springframework.web.method.annotation.MethodArgumentTypeMismatchException ex) {
        String expectedType = ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "Unknown";
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .message("Invalid input for parameter '" + ex.getName() + "'. Expected data type is: " + expectedType)
                .success(false)
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Catches Malformed JSON payloads (e.g., a missing comma or an unclosed bracket in the Postman request body)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .message("Malformed JSON request. Please check your syntax and ensure all fields are correctly formatted.")
                .success(false)
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    // --- 4. FILE UPLOAD EXCEPTIONS ---

    // Catches files that exceed the server's configured maximum upload size limit
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ApiResponse<Void>> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException ex) {
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .message("File size exceeds the maximum allowed limit.")
                .success(false)
                .build();
        return new ResponseEntity<>(response, HttpStatus.PAYLOAD_TOO_LARGE); // 413 Status Code
    }


    // --- 5. THE ULTIMATE FALLBACK ---

    // Catches any massive server crashes or unknown exceptions that slip past the specific handlers above
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGlobalException(Exception ex) {
        // NOTE: In production, you MUST use a logger (like SLF4J) to log `ex.getMessage()` here!
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .message("An unexpected server error occurred. Please contact support.")
                .success(false)
                .build();
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}