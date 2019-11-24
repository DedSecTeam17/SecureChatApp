package sample.models;

public  class SustGroup {
    private String groupName;
    private String groupPublic_key;

    public SustGroup(String groupName, String groupPublic_key) {
        this.groupName = groupName;
        this.groupPublic_key = groupPublic_key;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupPublic_key() {
        return groupPublic_key;
    }

    public void setGroupPublic_key(String groupPublic_key) {
        this.groupPublic_key = groupPublic_key;
    }
}
