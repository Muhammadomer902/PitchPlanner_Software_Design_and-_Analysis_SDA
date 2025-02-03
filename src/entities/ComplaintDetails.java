package entities;

public class ComplaintDetails {
    private int id;
    private String complainantName;
    private String complainantRole; // e.g., Player, Ground Owner
    private String issue;
    private String status; // OPEN or RESOLVED
    private String groundName;
    private Integer otherUserId1; // Nullable
    private Integer otherUserId2; // Nullable

    public ComplaintDetails(int id, String complainantName, String complainantRole, String issue, String status, 
                            String groundName, Integer otherUserId1, Integer otherUserId2) {
        this.id = id;
        this.complainantName = complainantName;
        this.complainantRole = complainantRole;
        this.issue = issue;
        this.status = status;
        this.groundName = groundName;
        this.otherUserId1 = otherUserId1;
        this.otherUserId2 = otherUserId2;
    }

    public int getId() {
        return id;
    }

    public String getComplainantName() {
        return complainantName;
    }

    public String getComplainantRole() {
        return complainantRole;
    }

    public String getIssue() {
        return issue;
    }

    public String getStatus() {
        return status;
    }

    public String getGroundName() {
        return groundName;
    }

    public Integer getOtherUserId1() {
        return otherUserId1;
    }

    public Integer getOtherUserId2() {
        return otherUserId2;
    }
}
