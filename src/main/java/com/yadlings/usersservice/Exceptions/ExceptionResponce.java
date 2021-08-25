package com.yadlings.usersservice.Exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionResponce {
    private Integer statusCode;
    private String status;
    private String message;
}
