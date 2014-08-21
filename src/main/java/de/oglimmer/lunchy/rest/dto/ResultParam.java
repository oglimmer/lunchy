package de.oglimmer.lunchy.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultParam {

	private boolean success;
	private String errorMsg;

}
