package com.airevents.dto.request;

import com.airevents.dto.Validate;
import com.airevents.error.ErrorCode;
import com.airevents.error.RcnException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RaceRequest implements Validate {
    private String title;
    private String date;
    private String distances;

    @Override
    public void validate() {
        if (StringUtils.isBlank(this.title)) {
            throw new RcnException(ErrorCode.BAD_REQUEST, "Ime trke je obavezno");
        }
        if (StringUtils.isBlank(this.date)) {
            throw new RcnException(ErrorCode.BAD_REQUEST, "Datum trke je obavezan");
        }
        if (StringUtils.isBlank(this.distances)) {
            throw new RcnException(ErrorCode.BAD_REQUEST, "Distance su obavezne");
        }
    }
}
