package com.kelsonthony.demofoliointegrationmodeusers.infrastructure.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kelsonthony.demofoliointegrationmodeusers.app.dto.UserDTO;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.SkipListener;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class StepSkipListener implements SkipListener<UserDTO, Number> {
    Logger logger = LoggerFactory.getLogger(StepSkipListener.class);

    @Override //item Reader
    public void onSkipInRead(Throwable throwable) {
        logger.info("A failure on read {} ", throwable.getMessage());
    }

    @Override
    public void onSkipInWrite(@NonNull Number item, @NonNull Throwable throwable) {
        if (throwable instanceof DuplicateKeyException) {
            logger.warn("Duplicate entry found: {}", throwable.getMessage());
        } else {
            logger.info("A failure on write {} , {} ", throwable.getMessage(), item);
        }
    }

    @SneakyThrows
    @Override
    public void onSkipInProcess(@NonNull UserDTO userDTO, @NonNull Throwable throwable) {
        logger.info("Process Item {} was skipped to the exception {}",
                new ObjectMapper().writeValueAsString(userDTO), throwable.getMessage());
    }
}
