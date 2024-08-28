package lltw.scopyright.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lltw.scopyright.VO.ResultVO;
import lltw.scopyright.VO.WorksDTO;
import lltw.scopyright.entity.Users;
import lltw.scopyright.entity.Works;
import lltw.scopyright.form.UploadForm;
import lltw.scopyright.mapper.UsersMapper;
import lltw.scopyright.mapper.WorksMapper;
import lltw.scopyright.service.BlockchainService;
import lltw.scopyright.service.WorksService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple10;
import org.fisco.bcos.sdk.model.TransactionReceipt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author sakura
 * @since 2024-08-08
 */
@Service
@Slf4j
@Transactional
public class WorksServiceImpl extends ServiceImpl<WorksMapper, Works> implements WorksService {

    private final WorksMapper worksMapper;
    private final UsersMapper usersMapper;
    private final BlockchainService blockchainService;

    @Autowired
    public WorksServiceImpl(WorksMapper worksMapper, UsersMapper usersMapper,BlockchainService blockchainService){
        this.worksMapper = worksMapper;
        this.usersMapper = usersMapper;
        this.blockchainService = blockchainService;
    }

    @Override
    public ResultVO uploadWork(UploadForm uploadForm) {
        Users user = usersMapper.selectById(uploadForm.getId());
        if (user != null) {
            Works works = new Works();
            works.setCreatorId(uploadForm.getId());
            works.setTitle(uploadForm.getTitle());
            works.setDescription(uploadForm.getDescription());

            int rows = worksMapper.insert(works);
            if (rows > 0) {
                // 作品上传成功后，将作品信息上链
                try {
                    // 作品的标题和描述
                    String title = uploadForm.getTitle();
                    String description = uploadForm.getDescription();

                    // 调用区块链服务，将作品信息上链
                    TransactionReceipt receipt = blockchainService.registerWork(title, description, user.getEmail());

                    if (receipt.isStatusOK()) {
                        log.info("作品上传成功，并成功上链。交易哈希: {}", receipt.getTransactionHash());
                        return ResultVO.success("作品上传成功，并成功上链");
                    } else {
                        log.error("作品上传成功，但上链失败。交易哈希: {}，状态码: {}", receipt.getTransactionHash(), receipt.getStatus());
                        return ResultVO.success("作品上传成功，但上链失败");
                    }
                } catch (Exception e) {
                    log.error("作品上传成功，但上链时出现错误: {}", e.getMessage(), e);
                    return ResultVO.error(-1, "作品上传成功，但上链时出现错误：" + e.getMessage());
                }
            }
            return ResultVO.error(-1, "作品上传失败");
        }
        return ResultVO.error(-2, "用户不存在");
    }

    @Override
    public ResultVO showAll(Long creatorId) {
        QueryWrapper<Works> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("creator_id", creatorId);
        List<Works> worksList = worksMapper.selectList(queryWrapper);
        if (worksList != null) {
            return ResultVO.success(worksList);
        }
        return ResultVO.error(-1, "查询作品失败，用户不存在");
    }
    @Override
    public ResultVO getAllWorksWithCreatorName() {
        QueryWrapper<Works> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("copyright_applied", 1);
        // 查询所有作品
        List<Works> worksList = worksMapper.selectList(queryWrapper);

        // 对每个作品查询对应的内容创作者
        List<WorksDTO> worksWithCreatorName = worksList.stream().map(work -> {
            Users creator = usersMapper.selectById(work.getCreatorId());
            return new WorksDTO(work.getId(), work.getTitle(),work.getDescription(), work.getStatus(),
                    creator.getUsername());
        }).collect(Collectors.toList());

        return ResultVO.success(worksWithCreatorName);
    }

    @Override
    public ResultVO queryCopyrightByTitle(Long workId) {
        // 查询作品是否存在
        Works work = worksMapper.selectById(workId);
        if (work == null) {
            return ResultVO.error(-1, "作品不存在");
        }

        // 查询作品的版权信息
        Tuple10<BigInteger, String, String, String, String, Boolean, BigInteger, BigInteger, BigInteger, String> copyrightInfo;
        copyrightInfo = blockchainService.queryCopyrightByTitle(work.getTitle());

        // 构建返回结果
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("chainId", copyrightInfo.getValue1());
        resultMap.put("address", copyrightInfo.getValue2());
        resultMap.put("title", copyrightInfo.getValue3());
        resultMap.put("description", copyrightInfo.getValue4());
        resultMap.put("copyrightNumber", copyrightInfo.getValue5());
        resultMap.put("applied", copyrightInfo.getValue6());
        resultMap.put("createdAt", copyrightInfo.getValue7());
        resultMap.put("updatedAt", copyrightInfo.getValue8());
        resultMap.put("status", copyrightInfo.getValue9());
        resultMap.put("creatorUsername", copyrightInfo.getValue10());

        return ResultVO.success(resultMap);
    }

    @Override
    public ResultVO searchWorks(String title) {
        QueryWrapper<Works> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("title", title);
        List<Works> worksList = worksMapper.selectList(queryWrapper);
        if (worksList!= null) {
            return ResultVO.success(worksList);
        }
        return ResultVO.error(-1, "查询作品失败，作品不存在");

    }

    /**
     * 用户申请作品版权
     * @param workId 作品ID
     * @return 申请结果
     */
    @Override
    public ResultVO applicationTrue(Long workId) {
        Works work = worksMapper.selectById(workId);
        if (work == null) {
            return ResultVO.error(-1, "作品不存在");
        }
        if ("pending".equals(work.getStatus())) {
            return ResultVO.error(-2, "作品正在审核中");
        }

        UpdateWrapper<Works> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", workId)
                .set("status", "pending")
                .set("copyright_applied", true);

        worksMapper.update(null, updateWrapper);
        return ResultVO.success("版权申请已提交，等待审核");
    }

    /**
     * 用户取消申请作品版权
     * @param workId 作品ID
     * @return 申请结果
     */
    @Override
    public ResultVO applicationFalse(Long workId) {
        Works work = worksMapper.selectById(workId);
        if (work == null) {
            return ResultVO.error(-1, "作品不存在");
        }
        UpdateWrapper<Works> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", workId)
                .set("status", "")
                .set("copyright_applied", false);

        worksMapper.update(null, updateWrapper);
        return ResultVO.success("版权申请已取消");
    }

    /**
     * 审核机构审核
     */
    @Override
    public ResultVO reviewCopyrightApplication(Long workId, boolean approval) {
        Works work = worksMapper.selectById(workId);
        if (work == null) {
            return ResultVO.error(-1, "作品不存在");
        }
        String copyrightNumber = null;
        try {
            if (approval) {
                // 生成唯一版权编号
                copyrightNumber = UUID.randomUUID().toString();

                // 更新数据库
                UpdateWrapper<Works> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("id", workId)
                        .set("status", "approved")
                        .set("copyright_applied", true)
                        .set("copyright_number", copyrightNumber)
                        .set("updated_at", LocalDateTime.now());
                worksMapper.update(null, updateWrapper);

                // 将信息上链
                TransactionReceipt receipt = blockchainService.reviewWork(work.getTitle(), true, copyrightNumber);
                if (receipt.isStatusOK()) {
                    log.info("版权申请审核通过，版权编号：{}，交易哈希：{}", copyrightNumber, receipt.getTransactionHash());
                    return ResultVO.success("版权申请审核通过，数据已上链");
                } else {
                    log.error("版权申请审核通过，但上链失败。交易哈希：{}，状态码：{}", receipt.getTransactionHash(), receipt.getStatus());
                    return ResultVO.success("版权申请审核通过，但上链失败");
                }
            } else {
                UpdateWrapper<Works> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("id", workId)
                        .set("status", "rejected")
                        .set("copyright_applied", false)
                        .set("updated_at", LocalDateTime.now());
                worksMapper.update(null, updateWrapper);

                log.info("版权申请审核被拒绝");
                return ResultVO.success("版权申请被拒绝");
            }
        } catch (Exception e) {
            log.error("版权申请审核时出现错误：{}", e.getMessage(), e);
            return ResultVO.error(-3, "版权申请审核时出现错误：" + e.getMessage());
        }
    }

}
