package com.example.securitybase.error;

import lombok.Getter;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

@Getter
public class ExceptionPayload {
    private String code;
    private String message;
    private ErrorDetail detail;

    public ExceptionPayload(ExceptionCode code) {
        this.code = code.getCode();
        this.message = code.getMessage();
    }

    public ExceptionPayload(final ExceptionCode code, final BindingResult bindingResult) {
        this.code = code.getCode();
        this.message = code.getMessage();
        this.detail = new ErrorDetail(bindingResult);
    }

    @Getter
    public static class ErrorDetail {
        private String field;
        private String reason;

        public ErrorDetail(final BindingResult bindingResult) {
            this.field = bindingResult.getFieldError().getField();

            StringBuilder builder = new StringBuilder();

            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                builder.append("[");
                builder.append(fieldError.getField());
                builder.append("](은)는 ");
                builder.append(fieldError.getDefaultMessage());
                if (!fieldError.getField().equals("password")) {
                    builder.append(" 입력된 값: [");
                    builder.append(fieldError.getRejectedValue());
                    builder.append("]");
                }
            }
            this.reason = builder.toString();
        }
    }

}
