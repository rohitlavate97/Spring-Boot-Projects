package com.alchemist.exception;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ErrorInfo {
	private String code;
	private String msg;
	private LocalDate when;
}
