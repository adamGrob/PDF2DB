package com.PDF2DB.controller;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SegmentCreator {

    private String stringCreateSegmentsFrom;

    private Map<String, List<List<String>>> rollCallMap = new HashMap<>();

    public SegmentCreator(String stringCreateSegmentsFrom) {
        this.stringCreateSegmentsFrom = stringCreateSegmentsFrom;
    }

    public void createVoteSegments() {
        // (?s)(EN\s)(.{1,300})(\s\n\n\d+\.\d+\.\sFinal\svote\s\n\n)(\d+\s[\+\-0]\s\n{2})(.*?)(Corrections to vote)  this is the greater pattern
        // this pattern can cut down all the voting segments (?s)(\d+\s[\+\-0]\s\n{2})(.*?)(\s\n\n\s\n\n)
        // pattern to extract the case the vote happens on from the votesegments: (?s)(?<=(EN)\s\n\n(\d)\.\s)(.*?)(?=\d+\.\d+\.\sFinal vote)
        // pattern for separating party voters from each other(works well except for the last row/party)   (?s)(([A-Z]+([&\\\/])*[A-Z]+))(.*?)(?=([A-Z]+([&\\\/]*)[A-Z]+))
        // code for removing all the party names: voteSegment.replaceAll("(([A-Z]+([&\\\\\\/])*[A-Z]+))", "")
        String patternString = "(?s)(EN\\s)(.{1,300})(\\s\\n\\n\\d+\\.\\d+\\.\\sFinal\\svote\\s\\n\\n)(\\d+\\s[\\+\\-0]\\s\\n{2})(.*?)(Corrections to vote)";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(stringCreateSegmentsFrom);

        while(matcher.find()) {
            String voteSegment = stringCreateSegmentsFrom.substring(matcher.start(), matcher.end());
            String nameOfTheCaseToVoteOn = getTheNameOfTHeCaseToVoteOn(voteSegment, "(?s)(?<=(EN)\\s\\n\\n(\\d)\\.\\s)(.*?)(?=\\d+\\.\\d+\\.\\sFinal vote)");
            List<List<String>> voteSegmentsOFACase = getVoteSegmentsOFACase(voteSegment, "(?s)(\\d+\\s[\\+\\-0]\\s\\n{2})(.*?)(\\s\\n\\n\\s\\n\\n)");
            rollCallMap.put(nameOfTheCaseToVoteOn, voteSegmentsOFACase);
        }
    }

    public void printSegments () {
        for (Map.Entry<String, List<List<String>>> entry : rollCallMap.entrySet()) {
            System.out.println("----------------------------------------------");
            String key = entry.getKey();
            List<List<String>> value = entry.getValue();
            System.out.println(key);
            for (List<String> voters: value) {
                System.out.println(voters);
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

    private List<List<String>> getVoteSegmentsOFACase(String stringToCreateSegmentsFrom, String regex) {
        List<List<String>> voteSegmentList = new ArrayList<>();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(stringToCreateSegmentsFrom);
        while(matcher.find()) {
            String voteSegment = stringToCreateSegmentsFrom.substring(matcher.start(), matcher.end());
            voteSegment = voteSegment.replaceAll("(([A-Z]+([&\\\\\\/])*[A-Z]+))", ""); // remove the party names
            //voteSegmentList.add(voteSegment);
            String[] voterList = voteSegment.split(",|\\n\\n\\s");
            List<String> cleanVoterList = cleanVoterList(voterList);
            System.out.println(cleanVoterList);
            voteSegmentList.add(cleanVoterList);
        }
        return voteSegmentList;
    }

    private List<String> cleanVoterList(String[] voterList) {
        List<String> cleanedVoterList = new ArrayList<>();
        for (String voter: voterList) {
            if (!voter.equals("") && !voter.equals(" ")) {
                String trimmedVoter = voter.trim();
                trimmedVoter = trimmedVoter.replaceAll("\n\n", "");
                cleanedVoterList.add(trimmedVoter);
            }
        }
        return cleanedVoterList;
    }
}
