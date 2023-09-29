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
public class ChangePasswordRequest implements Validate {
    private String newPassword;

    @Override
    public void validate() {
        if (StringUtils.isBlank(this.newPassword)) {
            throw new RcnException(ErrorCode.BAD_REQUEST, "Nova lozinka je obavezna");
        }
    }
}
