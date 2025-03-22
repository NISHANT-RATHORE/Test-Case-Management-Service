package com.example.testcasemagementservice.Strategy;


import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LowPriorityStrategy implements PriorityStrategy{

    @Override
    public void applyPriority() {
        log.info("Applying Low Priority Strategy........");
    }
}
