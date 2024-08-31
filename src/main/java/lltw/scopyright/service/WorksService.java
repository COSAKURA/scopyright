package lltw.scopyright.service;

import lltw.scopyright.VO.ResultVO;
import lltw.scopyright.entity.Works;
import com.baomidou.mybatisplus.extension.service.IService;
import lltw.scopyright.form.UploadForm;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author sakura
 * @since 2024-08-08
 */
public interface WorksService extends IService<Works> {

    ResultVO uploadWork(Long createId,String title , String description , MultipartFile file);

    ResultVO showAll(Long creatorId);

    ResultVO applicationTrue(Long workId);

    ResultVO applicationFalse(Long workId);

    ResultVO reviewCopyrightApplication(Long workId, boolean approval);

    ResultVO getAllWorksWithCreatorName();

    ResultVO queryCopyrightByTitle(Long workId);

    ResultVO searchWorks(String title);

    List<Works> getAllWorks();

    Works getWorkById(Long id);
}
