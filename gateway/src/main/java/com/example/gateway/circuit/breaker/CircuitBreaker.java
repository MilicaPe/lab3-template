package com.example.gateway.circuit.breaker;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class CircuitBreaker {

    public enum State {
        CLOSED,   // Нормальное состояние, запросы выполняются
        OPEN,     // Запросы заблокированы
        HALF_OPEN // Тестирование работоспособности ресурса
    }

    private int failureCount = 0;
    private final int failureThreshold = 3;
    private Duration timeout = Duration.ofSeconds(3);
    private LocalDateTime lastAttemptTime;
    private State state;

    public Boolean isOpen;
    public Boolean isHalfOpen;


    public CircuitBreaker() {
        this.lastAttemptTime = LocalDateTime.now();
        this.state =State.CLOSED;
        this.isOpen = this.state == State.OPEN;
        this.isHalfOpen = this.state == State.HALF_OPEN;
    }


    public void recordSuccess(){
        failureCount = 0;
        state = State.CLOSED;
    }
    public void recordFailure() {
        failureCount++;
        if (failureCount >= failureThreshold) {
            this.state = State.OPEN;
            lastAttemptTime = LocalDateTime.now();
        }
    }

    public Boolean allowRequest(){
        if(state==State.OPEN){
            if(Duration.between(LocalDateTime.now(), lastAttemptTime).compareTo(timeout) >= 0){
                state = State.HALF_OPEN;
                return true;
            }
            return false;
        }
        return true;
    }

    private void reset() {
        state = State.CLOSED;
        failureCount = 0;
    }

    public State getState() {
        return state;
    }
}
