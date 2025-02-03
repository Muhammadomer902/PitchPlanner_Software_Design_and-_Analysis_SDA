package services;

import entities.Feedback;
import repositories.FeedbackRepository;

import java.util.List;

public class FeedbackService {
    private final FeedbackRepository feedbackRepository;

    public FeedbackService() {
        this.feedbackRepository = new FeedbackRepository();
    }

    // Checks that a player has given feedback or not
    public boolean hasFeedback(int bookingId, int PlayerId) {
        return feedbackRepository.feedbackExistsForBooking(bookingId, PlayerId);
    }

    // Submit feedback
    public boolean submitFeedback(int bookingId, int userId, int rating, String comment) {
        return feedbackRepository.saveFeedback(bookingId, userId, rating, comment);
    }

    // Get feedback for a ground
    public List<Feedback> getFeedbackForGround(int groundId) {
        return feedbackRepository.getFeedbackByGroundId(groundId);
    }
}
