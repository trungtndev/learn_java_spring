package com.learnweb.learnweb.exceptoion;


import com.learnweb.learnweb.dto.request.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalException {
    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse> handlingException(RuntimeException exception){
        ApiResponse apiResponse = new ApiResponse();

        apiResponse.setCode(ErrorCode.UNCATEGORIZED_EXCEOTION.getCode());
        apiResponse.setMessage(ErrorCode.UNCATEGORIZED_EXCEOTION.getMessage());

        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> handlingException(AppException exception){
        ApiResponse apiResponse = new ApiResponse();

        apiResponse.setCode(exception.getErrorCode().getCode());
        apiResponse.setMessage(exception.getErrorCode().getMessage());

        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handlingValidation(MethodArgumentNotValidException exception){
        String enumKey = exception.getFieldError().getDefaultMessage();
        ErrorCode errorCode = ErrorCode.INVALID_ENUM_KEY;

        try{
            errorCode = ErrorCode.valueOf(enumKey);
        } catch (IllegalArgumentException e){

        }


        ApiResponse apiResponse = new ApiResponse();

        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());

        return ResponseEntity.badRequest().body(apiResponse);
    }

}
