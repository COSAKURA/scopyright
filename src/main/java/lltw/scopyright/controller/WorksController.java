package lltw.scopyright.controller;


import lltw.scopyright.VO.ResultVO;
import lltw.scopyright.form.UploadForm;
import lltw.scopyright.service.WorksService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author sakura
 * @since 2024-08-08
 */
@RestController
@RequestMapping("/works")
@Slf4j
public class WorksController {

    private final WorksService worksService;

    @Autowired
    public WorksController(WorksService worksService ){
        this.worksService = worksService;
    }

    // 显示内容创作者所有作品
    @GetMapping("/creator/show")
    public ResultVO showWorks(@RequestParam("creatorId") Long creatorId) {
        return worksService.showAll(creatorId);
    }

    // 查询全部用户作品接口
    @GetMapping("/creator/all")
    public ResultVO getAllWorks() {
        return worksService.getAllWorksWithCreatorName();
    }


    // 搜索作品
    // @GetMapping("/creator/searchTitle")
    // public ResultVO searchWorks(@RequestParam("title") String title) {
    //     return worksService.searchWorks(title);
    // }

    // 内容创作者上传作品接口
    @PostMapping("/creator/upload")
    public ResultVO uploadWork(@RequestParam("createId") Long createId ,@RequestParam("title") String title ,
                               @RequestParam("description") String description , @RequestParam("file") MultipartFile file) {
        log.info("++++++++++++++++++{}",file.getOriginalFilename());
        return worksService.uploadWork(createId,title,
                description,file);
    }

    // 内容创作者提交申请版权接口
    @GetMapping("/creator/appliedTrue")
    public ResultVO applicationTrue(@RequestParam("workId") Long workId) {

        return worksService.applicationTrue(workId);
    }

    // 内容创作者取消申请版权接口
    @GetMapping("/creator/appliedFalse")
    public ResultVO applicationFalse(@RequestParam("workId") Long workId) {
        return worksService.applicationFalse(workId);
    }

    // 审核机构审核版权接口
    @GetMapping("/auditor/reviewCopyright")
    public ResultVO reviewCopyrightApplication(@RequestParam("workId") Long workId,
                                               @RequestParam("approval") boolean approval) {
            return worksService.reviewCopyrightApplication(workId, approval);
    }

    // 查询版权的信息
    @GetMapping("/creator/info")
    public ResultVO queryCopyrightByTitle(@RequestParam("workId") Long workId) {
        return worksService.queryCopyrightByTitle(workId);
    }

}
