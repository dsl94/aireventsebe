package com.airevents.dto.request;

import com.airevents.dto.Validate;
import com.airevents.error.ErrorCode;
import com.airevents.error.RcnException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChallengeRequest implements Validate {
    private String title;
    private String startDate;
    private String endDate;

    @Override
    public void validate() {
        if (StringUtils.isBlank(this.title)) {
            throw new RcnException(ErrorCode.BAD_REQUEST, "Ime trke je obavezno");
        }
        if (StringUtils.isBlank(this.startDate)) {
            throw new RcnException(ErrorCode.BAD_REQUEST, "Od datum je obavezan");
        }
        if (StringUtils.isBlank(this.endDate)) {
            throw new RcnException(ErrorCode.BAD_REQUEST, "Do datum je obavezan");
        }
    }
}
