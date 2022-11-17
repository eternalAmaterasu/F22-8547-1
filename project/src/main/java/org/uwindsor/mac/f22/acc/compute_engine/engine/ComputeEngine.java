package org.uwindsor.mac.f22.acc.compute_engine.engine;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.uwindsor.mac.f22.acc.compute_engine.model.EditDistanceNode;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Vivek
 * @since 17/11/22
 */
@Slf4j
@Component
public class ComputeEngine {

    /**
     * Kindly note, this is using the same function as is available in the Sequences.editDistance() class which is a part of the 'Lab5-Advanced Design and Analysis.zip'
     *
     * @param word1
     * @param word2
     * @return
     */
    private int editDistance(String word1, String word2) {
        int len1 = word1.length();
        int len2 = word2.length();
        int[][] dp = new int[len1 + 1][len2 + 1];
        for (int i = 0; i <= len1; i++) dp[i][0] = i;
        for (int j = 0; j <= len2; j++) dp[0][j] = j;
        //iterate though, and check last char
        for (int i = 0; i < len1; i++) {
            char c1 = word1.charAt(i);
            for (int j = 0; j < len2; j++) {
                char c2 = word2.charAt(j);
                //if last two chars equal
                if (c1 == c2) {
                    //update dp value for +1 length
                    dp[i + 1][j + 1] = dp[i][j];
                } else {
                    int replace = dp[i][j] + 1;
                    int insert = dp[i][j + 1] + 1;
                    int delete = dp[i + 1][j] + 1;
                    int min = Math.min(replace, insert);
                    min = Math.min(delete, min);
                    dp[i + 1][j + 1] = min;
                }
            }
        }
        return dp[len1][len2];
    }

    public List<String> getTopNNearestStrings(String keyword, Set<String> searchBase, int topN) {
        List<EditDistanceNode> editDistanceNodes = new ArrayList<>(searchBase.size());
        searchBase.forEach(search -> editDistanceNodes.add(new EditDistanceNode(editDistance(keyword, search), search)));
        editDistanceNodes.sort(Comparator.comparingInt(EditDistanceNode::getDistance));
        log.debug(searchBase.toString()); //TODO : remove later
        return editDistanceNodes.subList(0, Math.min(editDistanceNodes.size(), topN))
                .stream().map(EditDistanceNode::getWord)
                .collect(Collectors.toList());
    }
}