package lltw.scopyright.VO;

import lombok.Data;

/**
 * @author Sakura
 */
@Data
public class WorksDTO {
    private Long id;
    private String title;
    private String description;
    private String status;
    private String creatorName;
    private String fileContent;

    // 构造函数
    public WorksDTO(Long id, String title, String description, String status, String creatorName ,String fileContent) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.creatorName = creatorName;
        this.fileContent = fileContent;
    }

}
