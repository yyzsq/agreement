
package com.gemframework.model.request;

import com.gemframework.model.common.BaseRequest;
import lombok.Data;

@Data
public class UserLoginRequest extends BaseRequest {

    private String username;
    private String password;
    private boolean rememberMe;
}
