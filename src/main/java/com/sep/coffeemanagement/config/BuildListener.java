package com.sep.coffeemanagement.config;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class BuildListener implements ApplicationListener<ContextRefreshedEvent> {

  @Override
  public void onApplicationEvent(ContextRefreshedEvent event) {
    // TODO Auto-generated method stub
    // throw new UnsupportedOperationException("Unimplemented method 'onApplicationEvent'");
  }
}
