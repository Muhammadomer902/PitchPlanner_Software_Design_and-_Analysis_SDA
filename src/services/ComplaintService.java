package services;

import entities.Complaint;
import entities.ComplaintDetails;
import repositories.ComplaintRepository;

import java.util.List;

public class ComplaintService {
    private final ComplaintRepository complaintRepository;

    public ComplaintService() {
        this.complaintRepository = new ComplaintRepository();
    }

    public boolean fileComplaint(int playerId, Integer bookingId, String issue) {
        return complaintRepository.fileComplaint(playerId, bookingId, issue);
    }

    public boolean hasComplaint(int userId, Integer bookingId) {
        return complaintRepository.hasComplaint(userId, bookingId);
    }
    
    public List<ComplaintDetails> getOpenComplaintDetails() {
        return complaintRepository.fetchOpenComplaintDetails();
    }


    // File a complaint by ground owner
    // public boolean fileComplaintForOwner(int ownerId, int groundId, Integer bookingId, String issue) {
    //     // Validate ground ownership
    //     if (!complaintRepository.groundOwnedByOwner(groundId, ownerId)) {
    //         System.out.println("Invalid ground ID or you do not own this ground.");
    //         return false;
    //     }

    //     // Validate booking association if booking ID is provided
    //     if (bookingId != null && !complaintRepository.bookingBelongsToGround(bookingId, groundId)) {
    //         System.out.println("Invalid booking ID or it does not belong to this ground.");
    //         return false;
    //     }

    //     // File the complaint
    //     Complaint complaint = new Complaint(0, ownerId, groundId, bookingId, issue, "OPEN");
    //     return complaintRepository.fileComplaint(complaint);
    // }


    // Get open complaints
    public List<Complaint> getOpenComplaints() {
        return complaintRepository.getOpenComplaints();
    }

    // Resolve a complaint
    public boolean resolveComplaint(int complaintId) {
        return complaintRepository.resolveComplaint(complaintId);
    }
}
