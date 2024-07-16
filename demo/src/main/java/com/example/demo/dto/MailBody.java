package com.example.demo.dto;

import lombok.Builder;

@Builder
public record MailBody(String to,String subject,String text) {

}
