package com.example.aggregator.service;

import com.example.aggregator.client.AggregatorRestClient;
import com.example.aggregator.model.Entry;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class AggregatorService {

    private AggregatorRestClient restClient;

    public AggregatorService(AggregatorRestClient restClient) {
        this.restClient = restClient;
    }

    public Entry getDefinitionFor(String word) {

        return restClient.getDefinitionFor(word);
    }

    public List<Entry> getWordsStartingWith(String chars) {

        return restClient.getWordsStartingWith(chars);
    }

    public List<Entry> getWordsThatContainSuccessiveLettersAndStartsWith(String chars) {

        List<Entry> wordsThatStartWith = restClient.getWordsStartingWith(chars);
        List<Entry> wordsThatContainSuccessiveLetters = restClient.getWordsThatContainConsecutiveLetters();

        List<Entry> common = new ArrayList<>(wordsThatStartWith);
        common.retainAll(wordsThatContainSuccessiveLetters);

        return common;
    }

    public List<Entry> getAllPalindromes() {

        final List<Entry> candidates = new ArrayList<>();

        for (char alphabet = 'a'; alphabet <= 'z'; alphabet++) {
            // get words starting and ending with character
            List<Entry> startsWith = restClient.getWordsStartingWith(String.valueOf(alphabet));
            List<Entry> endsWith = restClient.getWordsEndingWith(String.valueOf(alphabet));

            // keep entries that exist in both lists
            List<Entry> startsAndEndsWith = new ArrayList<>(startsWith);
            startsAndEndsWith.retainAll(endsWith);

            // store list with existing entries
            candidates.addAll(startsAndEndsWith);
        }

        final List<Entry> results = new ArrayList<>();

        for (int i = 0; i < candidates.size(); i++) {
            String word = candidates.get(i).getWord();
            String reverse = new StringBuilder(word).reverse()
                    .toString();
            if (word.equals(reverse)) {
                results.add(restClient.getDefinitionFor(word));
            }
        }

        return results;

    }
}
