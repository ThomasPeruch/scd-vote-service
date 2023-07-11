package com.tperuch.voteservice.endpoint.handler;

import java.time.LocalDateTime;
import java.util.List;

public class ErrorDto {
    private String time;
    private String exceptionClass;
    private int httpStatus;
    private List<String> erros;

    public ErrorDto(String exceptionClass, String time, int httpStatus, List<String> erros) {
        this.time = time;
        this.exceptionClass = exceptionClass;
        this.httpStatus = httpStatus;
        this.erros = erros;
    }

    public String getTime() {
        return time;
    }

    public String getExceptionClass() {
        return exceptionClass;
    }

    public void setExceptionClass(String exceptionClass) {
        this.exceptionClass = exceptionClass;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(int httpStatus) {
        this.httpStatus = httpStatus;
    }

    public List<String> getErros() {
        return erros;
    }

    public void setErros(List<String> erros) {
        this.erros = erros;
    }
}
