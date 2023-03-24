package com.airevents.dto.request;

import com.airevents.dto.Validate;
import com.airevents.error.AirEventsException;
import com.airevents.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AircraftMappingRequest implements Validate {
    private String simKey;
    private String icao;

    @Override
    public void validate() {
        if (StringUtils.isBlank(this.simKey)) {
            throw new AirEventsException(ErrorCode.BAD_REQUEST, "Sim key must not be empty");
        }
        if (StringUtils.isBlank(this.icao)) {
            throw new AirEventsException(ErrorCode.BAD_REQUEST, "ICAO code must not be empty");
        }
    }
}
