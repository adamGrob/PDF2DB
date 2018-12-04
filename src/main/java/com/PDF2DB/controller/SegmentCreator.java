package com.PDF2DB.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SegmentCreator {

    private String stringCreateSegmentsFrom;

    private Map<String, List<String>> rollCallMap = new HashMap<>();

    public SegmentCreator(String stringCreateSegmentsFrom) {
        this.stringCreateSegmentsFrom = stringCreateSegmentsFrom;
    }

    public void createVoteSegments() {
        // (?s)(EN\s)(.{1,300})(\s\n\n\d+\.\d+\.\sFinal\svote\s\n\n)(\d+\s[\+\-0]\s\n{2})(.*?)(Corrections to vote)  this is the greater pattern
        // this pattern can cut down all the voting segments (?s)(\d+\s[\+\-0]\s\n{2})(.*?)(\s\n\n\s\n\n)
        // pattern to extract the case the vote happens on from the votesegments: (?s)(?<=(EN)\s\n\n(\d)\.\s)(.*?)(?=\d+\.\d+\.\sFinal vote)

        String patternString = "(?s)(EN\\s)(.{1,300})(\\s\\n\\n\\d+\\.\\d+\\.\\sFinal\\svote\\s\\n\\n)(\\d+\\s[\\+\\-0]\\s\\n{2})(.*?)(Corrections to vote)";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(stringCreateSegmentsFrom);

        while(matcher.find()) {
            String voteSegment = stringCreateSegmentsFrom.substring(matcher.start(), matcher.end());
            String nameOfTheCaseToVoteOn = getTheNameOfTHeCaseToVoteOn(voteSegment, "(?s)(?<=(EN)\\s\\n\\n(\\d)\\.\\s)(.*?)(?=\\d+\\.\\d+\\.\\sFinal vote)");
            List<String> voteSegmentsOFACase = getVoteSegmentsOFACase(voteSegment, "(?s)(\\d+\\s[\\+\\-0]\\s\\n{2})(.*?)(\\s\\n\\n\\s\\n\\n)");
            rollCallMap.put(nameOfTheCaseToVoteOn, voteSegmentsOFACase);
        }
    }

    public void printSegments () {
        for (Map.Entry<String, List<String>> entry : rollCallMap.entrySet()) {
            System.out.println("----------------------------------------------");
            String key = entry.getKey();
            List<String> value = entry.getValue();
            System.out.println(key);
            for (String vote: value) {
                System.out.println(vote);
            }
            System.out.println("----------------------------------------------");
        }
    }

    private String getTheNameOfTHeCaseToVoteOn (String stringToExtractTheCaseNameFrom, String regex) {
        Pattern voteNamePatter = Pattern.compile(regex);
        Matcher voteNameMatcher = voteNamePatter.matcher(stringToExtractTheCaseNameFrom);
        if (voteNameMatcher.find()) {
            return stringToExtractTheCaseNameFrom.substring(voteNameMatcher.start(), voteNameMatcher.end());
        }
        System.out.println("No matches found");
        return "Pattern not found";
    }

    private List<String> getVoteSegmentsOFACase(String stringToCreateSegmentsFrom, String regex) {
        List<String> voteSegmentList = new ArrayList<>();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(stringToCreateSegmentsFrom);
        while(matcher.find()) {
            String voteSegment = stringToCreateSegmentsFrom.substring(matcher.start(), matcher.end());
            voteSegmentList.add(voteSegment);
        }
        return voteSegmentList;
    }
}
