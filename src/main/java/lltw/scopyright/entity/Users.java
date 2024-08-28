package lltw.scopyright.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
    public class Users implements Serializable {

    private static final long serialVersionUID=1L;

      /**
     * 主键，自增
     */
        @TableId(value = "id", type = IdType.AUTO)
      private Long id;

      /**
     * 用户名，唯一且不能为空
     */
      private String username;

      /**
       * role
       */
      private String role;

  /**
   * 密码（加密存储）
     */
      private String password;

      /**
     * 密码盐值
     */
      private String salt;

      /**
     * 电子邮件地址，唯一且不能为空
     */
      private String email;

      /**
     * 创建时间
     */
      private LocalDateTime createdAt;

      /**
     * 更新时间
     */
      private LocalDateTime updatedAt;


}
