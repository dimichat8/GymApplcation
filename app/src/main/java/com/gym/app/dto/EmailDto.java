package com.gym.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailDto {
    private String email;
    private String subject;
    private String body;
    private String data;
    private String path;

}
