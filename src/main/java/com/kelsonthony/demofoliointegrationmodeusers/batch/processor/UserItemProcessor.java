package com.kelsonthony.demofoliointegrationmodeusers.batch.processor;

import com.kelsonthony.demofoliointegrationmodeusers.app.dto.UserDTO;
import com.kelsonthony.demofoliointegrationmodeusers.app.port.UserProcessorPort;
import org.springframework.stereotype.Component;

@Component
public class UserItemProcessor implements UserProcessorPort {

    @Override
    public UserDTO process(UserDTO userDTO) {
        System.out.println("User Processor: " + userDTO);
        return userDTO;
    }
}
