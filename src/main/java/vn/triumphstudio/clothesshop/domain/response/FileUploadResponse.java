package vn.triumphstudio.clothesshop.domain.response;

public class FileUploadResponse {
    private String id;

    private String fileName;

    public FileUploadResponse(String id, String fileName) {
        this.id = id;
        this.fileName = fileName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
