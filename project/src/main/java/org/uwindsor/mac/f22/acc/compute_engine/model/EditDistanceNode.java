package org.uwindsor.mac.f22.acc.compute_engine.model;

/**
 * @author Vivek
 * @since 17/11/22
 * <p>
 * Class to store the distance of the word being compared for edit distance along with the target word
 * distance - the variable which stores the edit distance of the query word with target word
 * word - the target word against which the query word was measured for edit distance
 */

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class EditDistanceNode {
    private final int distance;
    private final String word;

    public EditDistanceNode(int distance, String word) {
        this.distance = distance;
        this.word = word;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("distance", distance)
                .append("word", word)
                .toString();
    }

    public int getDistance() {
        return distance;
    }

    public String getWord() {
        return word;
    }
}