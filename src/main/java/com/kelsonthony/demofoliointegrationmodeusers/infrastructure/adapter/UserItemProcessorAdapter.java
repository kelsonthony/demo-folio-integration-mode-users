package com.kelsonthony.demofoliointegrationmodeusers.infrastructure.adapter;

import com.kelsonthony.demofoliointegrationmodeusers.app.dto.UserDTO;
import com.kelsonthony.demofoliointegrationmodeusers.app.port.UserProcessorPort;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class UserItemProcessorAdapter implements ItemProcessor<UserDTO, UserDTO> {

    private final UserProcessorPort processorPort;

    public UserItemProcessorAdapter(UserProcessorPort processorPort) {
        this.processorPort = processorPort;
    }

    @Override
    public UserDTO process(UserDTO userDTO) throws Exception {
        System.out.println("User Processor: " + userDTO);
        return processorPort.process(userDTO);
    }

}
