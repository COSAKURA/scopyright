package lltw.scopyright.mapper;

import lltw.scopyright.entity.Works;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author sakura
 * @since 2024-08-08
 */
@Mapper
public interface WorksMapper extends BaseMapper<Works> {

    @Select("SELECT * FROM works WHERE status ='approved'")
    List<Works> selectFileContent();
}
