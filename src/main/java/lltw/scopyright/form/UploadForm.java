package lltw.scopyright.form;


import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Sakura
 */
@Data
public class UploadForm {
    private Long createId;
    private String title;
    private String description;
}
