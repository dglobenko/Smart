package com.smartdiff.demo;

import com.smartdiff.services.SmartDiffService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;

@Slf4j
@SpringBootApplication
@ComponentScan("com.smartdiff.services")
public class AppCli implements CommandLineRunner {

    private final SmartDiffService smartDiffService;

    @Autowired
    public AppCli(SmartDiffService smartDiffService) {
        this.smartDiffService = smartDiffService;
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(AppCli.class)
            .run(args);
	}

    @Override
    public void run(String... args) {

        if(args.length < 2) {
            throw new IllegalArgumentException("Not all params is provided");
        }

        smartDiffService.loadFilesAndStartDiff(args[0], args[1]);

        System.exit(0);
    }
}
