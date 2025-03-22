package com.example.testcasemagementservice.Strategy;

import com.example.testcasemagementservice.Enums.Priority;

public class PriorityContext {
    private PriorityStrategy strategy;

    public void setStrategy(Priority priority) {
        switch (priority) {
            case High:
                strategy = new HighPriorityStrategy();
                break;
            case Medium:
                strategy = new MediumPriorityStrategy();
                break;
            case Low:
                strategy = new LowPriorityStrategy();
                break;
            default:
                throw new IllegalArgumentException("Unknown priority: " + priority);
        }
    }

    public void applyStrategy() {
        if (strategy != null) {
            strategy.applyPriority();
        } else {
            throw new IllegalStateException("Strategy not set");
        }
    }
}