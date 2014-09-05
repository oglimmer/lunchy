package de.oglimmer.lunchy.rest.dto;

import lombok.Data;

@Data
public class LoginResponse {
	private boolean success;
	private Integer userId;
	private Integer fkOffice;
	private String longTimeToken;
	private String errorMsg;
	private String companyName;
	private Integer permissions;
}