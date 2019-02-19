package com.smartdiff.services;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SmartDiffService {

    private static final String BUTTON_DEFAULT_ID = "make-everything-ok-button";

    public String loadFilesAndStartDiff(String originalFilePath, String otherFilePath) {
        Document originalDoc = Jsoup.parse(originalFilePath);
        Document otherDoc = Jsoup.parse(otherFilePath);

        //on call originalDoc.getElementsByTag("a") i got size = 0;
        Element buttonElement = originalDoc.getElementById(BUTTON_DEFAULT_ID);
        final Set<String> classNames = buttonElement.classNames();

        Map<Integer, Element> matchMap = new TreeMap<>();
        classNames.forEach(
            className -> matchMap.putAll(otherDoc.getElementsByClass(className)
                .stream()
                .collect(
                    Collectors.toMap(
                        element -> getMatchCount(buttonElement, (Element) element),
                        element -> element)
                )));

        Element mostSimilarElement = matchMap
            .get(matchMap.keySet().stream().mapToInt(key -> key).max().orElse(0));
        return printXPath(mostSimilarElement);
    }

    /**
     * @param originalButtonElement
     * @param elementToCheck
     * @return Checking matches on elements values.
     * In matching I used classes match classes, tag, data, href and title
     */
    private int getMatchCount(Element originalButtonElement, Element elementToCheck) {
        int countOfMatches = 0;

        if (originalButtonElement.data() != null &&
            originalButtonElement.data().equals(elementToCheck.data())) {
            countOfMatches++;
        }

        if (originalButtonElement.tagName() != null &&
            originalButtonElement.tagName().equals(elementToCheck.tagName())) {
            countOfMatches++;
        }

        if (originalButtonElement.classNames() != null &&
            originalButtonElement.classNames().containsAll(elementToCheck.classNames())) {
            countOfMatches++;
        }

        if (originalButtonElement.dataset().getOrDefault("title", "")
            .equals(elementToCheck.dataset().getOrDefault("title", ""))) {
            countOfMatches++;
        }

        if (originalButtonElement.dataset().getOrDefault("href", "")
            .equals(elementToCheck.dataset().getOrDefault("href", ""))) {
            countOfMatches++;
        }

        return countOfMatches;
    }

    public static String printXPath(Element element) {
        String xPath = "";
        while (element.parent() != null) {
            if (StringUtils.isEmpty(xPath)) {
                xPath = element.tagName();
            } else {
                xPath = element.tagName() + " < " + xPath;
            }
        }
        return xPath;
    }
}
