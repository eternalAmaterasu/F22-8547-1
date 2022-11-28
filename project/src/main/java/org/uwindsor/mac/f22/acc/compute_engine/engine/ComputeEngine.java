package org.uwindsor.mac.f22.acc.compute_engine.engine;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.uwindsor.mac.f22.acc.compute_engine.model.EditDistanceNode;
import org.uwindsor.mac.f22.acc.compute_engine.model.TrieNode;

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

    public List<String> getTopNNearestStrings(String keyword, Set<String> searchBase, int topN) { //get the top n nearest matching strings for keyword in searchBase set
        List<EditDistanceNode> editDistanceNodes = new ArrayList<>(searchBase.size()); //initialize the edit distance nodes
        searchBase.forEach(search -> editDistanceNodes.add(new EditDistanceNode(editDistance(keyword, search), search))); //compute the edit distance of the keyword with every string from the searchBase and store the result in a list
        editDistanceNodes.sort(Comparator.comparingInt(EditDistanceNode::getDistance)); //in-place sort the edit distance nodes' list in increasing distance, using Java's inbuilt sort which is actually a mergesort for lists
        log.debug(searchBase.toString()); //TODO : remove later
        return editDistanceNodes.subList(0, Math.min(editDistanceNodes.size(), topN)) //get first topN elements from the list
                .stream().map(EditDistanceNode::getWord) //stream through them and extract the word for which the edit distance is computed against the keyword
                .collect(Collectors.toList()); //store the extraction in a list and return
    }

    public List<String> getAutoCompleteForPrefix(String prefix, TrieNode root) { //function to compute the auto complete or the word completion for a prefix
        List<String> result = new ArrayList<>();

        if (prefix.isEmpty() || root.getChildAt(prefix.charAt(0)) == null) return result;
        TrieNode seekLast = navigateToPrefixEnd(prefix, root); //navigate to the end of the prefix in the trie
        if (seekLast == null) return result; //if the end of the prefix is not found, that means the trie doesn't contain the prefix, thus no scope of word completion
        runAutoComplete(prefix, seekLast, result, ""); //reaching here means that the prefix was found, and there is hope of finding the auto complete for the prefix
        return result;
    }

    private TrieNode navigateToPrefixEnd(String prefix, TrieNode root) { //linear method to navigate the trie for the prefix
        TrieNode current = root;
        for (int i = 0; i < prefix.length(); i++) { //iterate through the characters of the prefix one by one
            TrieNode next = current.getChildAt(prefix.charAt(i)); //check if the current node has any node at the current character index
            if (next == null) return null; //if there is no node at the character, it means that the prefix is not found in the trie
            current = next; //reaching here means that the current character was found in the trie from the prefix and then updated current with the next node
        }
        return current; //reaching here means the prefix was found and the node being returned here is the end of the prefix in the trie
    }

    private void runAutoComplete(String prefix, TrieNode node, List<String> result, String lastWord) { //recursive method to run the auto complete searches on the trie from the node of trie
        TrieNode[] children = node.getChildren(); //get the children[] of the node
        boolean leaf = true; //boolean value to store if the current node is a leaf or not
        for (int i = 0; i < children.length; i++) { //iterate through the children[]
            if (children[i] != null) { //if the child at ith position in the children array is present then execute following
                String newPrefix = prefix + (char) i; //append the character to the newPrefix
                leaf = false; //as there is a child, it means it's not a leaf, thus setting leag as false.
                runAutoComplete(newPrefix, children[i], result, newPrefix); //recursively calling runAutoComplete on the newPrefix, the internal node's child, and the result and the newPrefix
            }
        }
        if (leaf) result.add(lastWord); //if this traversal was at a leaf, it's a complete word, therefore add to the list of strings which we had passed into the function
    }
}