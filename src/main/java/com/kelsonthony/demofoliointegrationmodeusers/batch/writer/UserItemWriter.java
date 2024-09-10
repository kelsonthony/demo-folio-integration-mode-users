package com.kelsonthony.demofoliointegrationmodeusers.batch.writer;

import com.kelsonthony.demofoliointegrationmodeusers.app.dto.UserDTO;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class UserItemWriter implements ItemWriter<UserDTO> {
    @Override
    public void write(@NonNull Chunk<? extends UserDTO> users) {
        System.out.println("my users writer: " + users);
    }
}
