package com.fr.design.fun;

/**
 * @author richie
 * @date 2015-03-31
 * @since 8.0
 */
public interface Feedback {
    String MARK_STRING = "FineReport_Feedback";

    void didFeedback();

    Feedback EMPTY = new Feedback() {
        @Override
        public void didFeedback() {

        }
    };
}