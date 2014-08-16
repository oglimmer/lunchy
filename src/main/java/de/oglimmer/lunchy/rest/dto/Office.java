package de.oglimmer.lunchy.rest.dto;

import lombok.Data;
import de.oglimmer.lunchy.database.generated.tables.records.OfficesRecord;
import de.oglimmer.lunchy.rest.BeanMappingProvider;

@Data
public class Office {

	private Integer id;
	private String name;
	private Double geoLat;
	private Double geoLng;
	private Integer zoomfactor;

	public static Office getInstance(OfficesRecord officeRec) {
		Office officeDto = new Office();
		BeanMappingProvider.INSTANCE.getMapper().map(officeRec, officeDto);
		return officeDto;
	}
}
