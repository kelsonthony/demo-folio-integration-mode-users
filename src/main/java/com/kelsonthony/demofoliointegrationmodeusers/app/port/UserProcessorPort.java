package com.kelsonthony.demofoliointegrationmodeusers.app.port;

import com.kelsonthony.demofoliointegrationmodeusers.app.dto.UserDTO;

public interface UserProcessorPort {
    UserDTO process(UserDTO userDTO);
}
