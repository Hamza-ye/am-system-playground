package org.nmcpye.activitiesmanagement.cucumber;

import io.cucumber.spring.CucumberContextConfiguration;
import org.nmcpye.activitiesmanagement.AmSystemPlaygroundApp;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

@CucumberContextConfiguration
@SpringBootTest(classes = AmSystemPlaygroundApp.class)
@WebAppConfiguration
public class CucumberTestContextConfiguration {}
