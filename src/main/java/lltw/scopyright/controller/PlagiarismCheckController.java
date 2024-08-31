package lltw.scopyright.controller;

import lltw.scopyright.VO.ResultVO;
import lltw.scopyright.entity.Works;
import lltw.scopyright.service.WorksService;
import lltw.scopyright.utils.TfIdfVectorizer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.similarity.CosineSimilarity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

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

        // 初始化TF-IDF向量器
        TfIdfVectorizer vectorizer = new TfIdfVectorizer();
        Map<CharSequence, Integer> vector1 = vectorizer.vectorize(fileContent);

        for (Works otherWork : allWorks) {
            log.info("-------------------------{}", otherWork);

            // 避免与自身比较
            if (otherWork.getId().equals(id)) continue;

            // 获取其他作品内容
            String otherFileContent = decodeBlobToString(otherWork.getFileContent());
            if (otherFileContent == null || otherFileContent.isEmpty()) continue;

            log.info("Comparing with work id: {}, title: {}", otherWork.getId(), otherWork.getTitle());

            // 对其他作品进行向量化
            Map<CharSequence, Integer> vector2 = vectorizer.vectorize(otherFileContent);

            // 计算余弦相似度
            CosineSimilarity cosineSimilarity = new CosineSimilarity();
            double similarityScore = cosineSimilarity.cosineSimilarity(vector1, vector2);

            log.info("相似度: {}", similarityScore);

            // 如果相似度超过阈值，判定为抄袭
            if (similarityScore > SIMILARITY_THRESHOLD) {
                double similarityPercentage = similarityScore * 100;
                return ResultVO.error(-3, "查重未通过，相似度：" + String.format("%.2f", similarityPercentage) + "%");
            }
        }

        return ResultVO.success("查重通过，可以申请版权");
    }

    // 将Blob转换为String
    private String decodeBlobToString(byte[] blob) {
        if (blob == null) return null;
        return new String(blob, StandardCharsets.UTF_8);
    }
}
