package lltw.scopyright.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.fisco.bcos.sdk.abi.datatypes.Int;

/**
 * <p>
 *
 * </p>
 *
 * @author sakura
 * @since 2024-08-08
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    public class Works implements Serializable {

    private static final long serialVersionUID=1L;

      /**
     * 主键，自增
     */
        @TableId(value = "id", type = IdType.AUTO)
      private Long id;

      /**
     * 作品标题
     */
      private String title;

      /**
     * 作品描述
     */
      private String description;

      /**
     * 创作者ID，外键
     */
      private Long creatorId;

      /**
     * 状态（pending, approved, rejected）
     */
      private String status;

      /**
     * 创建时间
     */
      private LocalDateTime createdAt;

      /**
     * 更新时间
     */
      private LocalDateTime updatedAt;


    /**
     * 版权编号
     */
    private String copyrightNumber;

    /**
     * 申请是否通过
     */
    private Boolean copyrightApplied;
}
