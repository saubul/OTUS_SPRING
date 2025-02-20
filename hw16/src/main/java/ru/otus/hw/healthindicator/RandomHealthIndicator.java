package ru.otus.hw.healthindicator;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;

import java.util.Random;

// Ничего умного так и не придумал, поэтому просто копия :(
@Component
public class RandomHealthIndicator implements HealthIndicator {

    private final Random random = new Random();

    @Override
    public Health health() {
        boolean serverIsDown = random.nextBoolean();
        if (serverIsDown) {
            return Health.down()
                    .status(Status.DOWN)
                    .withDetail("message", "ERROR")
                    .build();
        } else {
            return Health.up().withDetail("message", "OK").build();
        }
    }
}
