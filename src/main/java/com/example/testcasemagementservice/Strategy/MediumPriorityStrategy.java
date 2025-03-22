package com.example.testcasemagementservice.Strategy;


import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MediumPriorityStrategy implements PriorityStrategy{
    @Override
    public void applyPriority() {
        log.info("Applying Medium Priority Strategy......");
    }
}
