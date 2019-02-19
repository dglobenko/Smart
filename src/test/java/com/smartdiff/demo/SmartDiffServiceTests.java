package com.smartdiff.demo;

import com.smartdiff.services.SmartDiffService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SmartDiffServiceTests {

	@Value("classpath://sample-0.html")
	Resource sample0;
	@Value("classpath://sample-1.html")
	Resource sample1;
	@Value("classpath://sample-2.html")
	Resource sample2;
	@Value("classpath://sample-3.html")
	Resource sample3;
	@Value("classpath://sample-4.html")
	Resource sample4;

	@Autowired
	SmartDiffService smartDiffService;


	@Test
	public void checkXPath() throws IOException {
        final Document parse = Jsoup.parse(sample0.getFile(), "UTF-8");
        final Element elementById = parse.getElementById("make-everything-ok-button");
        assertNotNull(SmartDiffService.printXPath(elementById));

        final String xPath1Found = smartDiffService
            .loadFilesAndStartDiff(sample0.getFile().getPath(), sample1.getFile().getPath());
        assertNotNull(xPath1Found);
        final String xPath2Found = smartDiffService
            .loadFilesAndStartDiff(sample0.getFile().getPath(), sample2.getFile().getPath());
        assertNotNull(xPath2Found);
        final String xPath3Found = smartDiffService
            .loadFilesAndStartDiff(sample0.getFile().getPath(), sample3.getFile().getPath());
        assertNotNull(xPath3Found);
        final String xPath4Found = smartDiffService
            .loadFilesAndStartDiff(sample0.getFile().getPath(), sample4.getFile().getPath());
        assertNotNull(xPath4Found);
    }

}
