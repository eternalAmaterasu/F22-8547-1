package org.uwindsor.mac.f22.acc.compute_engine.model;

import java.util.Arrays;

/**
 * @author Vivek
 * @since 24/11/22
 */
public class TrieNode {

    private final TrieNode[] children;

    public TrieNode() {
        children = new TrieNode[128];
        Arrays.fill(children, null);
    }

    public TrieNode getChildAt(char ch) {
        return children[ch];
    }

    public void putChildAt(char ch, TrieNode child) {
        children[ch] = child;
    }

    public TrieNode[] getChildren() {
        return children;
    }
}