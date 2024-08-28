package lltw.scopyright.service;

import lltw.scopyright.VO.ResultVO;
import lltw.scopyright.entity.Works;
import com.baomidou.mybatisplus.extension.service.IService;
import lltw.scopyright.form.UploadForm;

import java.math.BigInteger;
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

    ResultVO uploadWork(UploadForm uploadForm);

    ResultVO showAll(Long creatorId);

    ResultVO applicationTrue(Long workId);

    ResultVO applicationFalse(Long workId);

    ResultVO reviewCopyrightApplication(Long workId, boolean approval);

    ResultVO getAllWorksWithCreatorName();

    ResultVO queryCopyrightByTitle(Long workId);

    ResultVO searchWorks(String title);
}
