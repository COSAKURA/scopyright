package lltw.scopyright.controller;

import lltw.scopyright.VO.ResultVO;
import lltw.scopyright.entity.Works;
import lltw.scopyright.service.WorksService;
import lltw.scopyright.utils.JaccardSimilarity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Sakura
 */
@RestController
@RequestMapping("/api/check")
@Slf4j
public class PlagiarismCheckController {

    private static final double SIMILARITY_THRESHOLD = 0.6;

    @Resource
    private WorksService worksService;

    @GetMapping("/plagiarism")
    public ResultVO checkPlagiarism(@RequestParam("id") Long id) {
        // 获取特定作品
        Works work = worksService.getWorkById(id);
        if (work == null) {
            return ResultVO.error(-1, "作品不存在");
        }

        // 获取作品内容
        String fileContent = decodeBlobToString(work.getFileContent());
        if (fileContent == null || fileContent.isEmpty()) {
            return ResultVO.error(-2, "作品内容为空");
        }

        // 获取所有状态为 pending 或 approved 的作品
        List<Works> allWorks = worksService.getAllWorks();

        // 收集所有作品的文本内容
        List<String> allTexts = allWorks.stream()
                .map(w -> decodeBlobToString(w.getFileContent()))
                .filter(content -> content != null && !content.isEmpty())
                .collect(Collectors.toList());

        // 计算 Jaccard 相似度
        for (Works otherWork : allWorks) {
            if (otherWork.getId().equals(id)) continue;

            String otherFileContent = decodeBlobToString(otherWork.getFileContent());
            if (otherFileContent == null || otherFileContent.isEmpty()) continue;

            double similarityScore = JaccardSimilarity.calculateJaccardSimilarity(fileContent, otherFileContent);

            log.info("Comparing with work id: {}, title: {}, Jaccard Similarity: {}",
                    otherWork.getId(), otherWork.getTitle(), similarityScore);

            // 如果相似度超过阈值，判定为抄袭
            if (similarityScore > SIMILARITY_THRESHOLD) {
                double similarityPercentage = similarityScore * 100;
                return ResultVO.error(-3, "查重未通过，相似度：" + String.format("%.2f", similarityPercentage) + "%");
            }
        }

        return ResultVO.success("查重通过，可以申请版权");
    }

    // 将 BLOB 转换为 String
    private String decodeBlobToString(byte[] blob) {
        if (blob == null) return null;
        return new String(blob, StandardCharsets.UTF_8);
    }
}
