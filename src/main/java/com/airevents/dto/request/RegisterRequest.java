package com.airevents.dto.request;

import com.airevents.dto.Validate;
import com.airevents.error.AirEventsException;
import com.airevents.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Column;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest implements Validate {
    private String fullName;
    private String email;
    private String password;
    private String vatsimId;
    private String ivaoId;
    private String posconId;

    @Override
    public void validate() {
        if (StringUtils.isBlank(this.fullName)) {
            throw new AirEventsException(ErrorCode.BAD_REQUEST, "Full name must not be empty");
        }
        if (StringUtils.isBlank(this.email)) {
            throw new AirEventsException(ErrorCode.BAD_REQUEST, "Email must not be empty");
        }
        if (StringUtils.isBlank(this.password)) {
            throw new AirEventsException(ErrorCode.BAD_REQUEST, "Password must not be empty");
        }
    }
}
