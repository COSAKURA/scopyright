package lltw.scopyright.controller;

import lltw.scopyright.VO.ResultVO;
import lltw.scopyright.entity.Works;
import lltw.scopyright.service.WorksService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/check")
@Slf4j
public class PlagiarismCheckController {

    private static final double SIMILARITY_THRESHOLD = 0.9;

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

        // 获取所有 `copyrightNumber` 不为空的作品
        List<Works> allWorks = worksService.getAllWorks().stream()
                .filter(w -> w.getCopyrightNumber() != null && !w.getCopyrightNumber().isEmpty())
                .collect(Collectors.toList());

        for (Works otherWork : allWorks) {
            // 避免与自身比较
            if (otherWork.getId().equals(id)) continue;

            // 获取其他作品内容
            String otherFileContent = decodeBlobToString(otherWork.getFileContent());
            if (otherFileContent == null || otherFileContent.isEmpty()) continue;

            // 计算相似度
            double similarityScore = calculateLevenshteinSimilarity(fileContent, otherFileContent);
            log.info("相似度: {}", similarityScore);

            // 转换为百分数
            double similarityPercentage = similarityScore * 100;
            if (similarityPercentage > SIMILARITY_THRESHOLD * 100) {
                return ResultVO.error(-3, "查重未通过，相似度：" + String.format("%.2f", similarityPercentage) + "%");
            }
        }
        return ResultVO.success("查重通过，可以申请版权");
    }

    private String decodeBlobToString(byte[] blob) {
        if (blob == null) return null;
        return new String(blob, StandardCharsets.UTF_8);
    }

    private double calculateLevenshteinSimilarity(String text1, String text2) {
        LevenshteinDistance levenshteinDistance = new LevenshteinDistance();
        int distance = levenshteinDistance.apply(text1, text2);
        int maxLength = Math.max(text1.length(), text2.length());
        // 计算相似度
        return 1.0 - (double) distance / maxLength;
    }
}
