package com.example.myapp;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

public class ErrorController {

	@Controller
	public class Error implements org.springframework.boot.web.servlet.error.ErrorController {
		private String ERROR_TEMPLATES_PATH = "/error/";

		@RequestMapping(value = "/error")
		public String handleError(HttpServletRequest request) {
			Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
			if (status != null) {
				int statusCode = Integer.valueOf(status.toString());
				if (statusCode == HttpStatus.BAD_REQUEST.value()) {
					return ERROR_TEMPLATES_PATH + "400";
				}
				if (statusCode == HttpStatus.FORBIDDEN.value()) {
					return ERROR_TEMPLATES_PATH + "403";
				}
				if (statusCode == HttpStatus.NOT_FOUND.value()) {
					return ERROR_TEMPLATES_PATH + "404";
				}
				if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
					return ERROR_TEMPLATES_PATH + "500";
				}
			}
			return "error";
		}
	}
}
