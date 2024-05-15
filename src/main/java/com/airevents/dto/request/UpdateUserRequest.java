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
public class UpdateUserRequest implements Validate {
    private String fullName;
    private String membershipUntil;
    private String medicalUntil;
    private String stravaId;
    private String email;
    private String shirtSize;
    private String phone;
    private String info;

    @Override
    public void validate() {
        if (StringUtils.isBlank(this.fullName)) {
            throw new RcnException(ErrorCode.BAD_REQUEST, "Ime i prezime je obvezno");
        }
    }
}