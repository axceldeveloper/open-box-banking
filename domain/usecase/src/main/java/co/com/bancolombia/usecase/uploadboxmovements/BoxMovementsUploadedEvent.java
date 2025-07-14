package co.com.bancolombia.usecase.uploadboxmovements;

public class BoxMovementsUploadedEvent {
    private String boxId;
    private int total;
    private int success;
    private int failed;

    public BoxMovementsUploadedEvent(String boxId, int total, int success, int failed) {
        this.boxId = boxId;
        this.total = total;
        this.success = success;
        this.failed = failed;
    }

    public String getBoxId() {
        return boxId;
    }

    public void setBoxId(String boxId) {
        this.boxId = boxId;
    }

    public int getFailed() {
        return failed;
    }

    public void setFailed(int failed) {
        this.failed = failed;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
