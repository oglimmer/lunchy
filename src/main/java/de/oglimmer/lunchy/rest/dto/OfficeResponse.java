package de.oglimmer.lunchy.rest.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class OfficeResponse extends OfficeCreateInput {

	private Integer id;

}
