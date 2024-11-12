package gold.mapper;

import gold.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface UserMapper {


    void insert(User user);

    @Select("select * from user where email = #{email}")
    User getByEmail(String email);

    @Select("select * from user where id = #{currentId}")
    User getById(Long currentId);

    @Update("UPDATE user SET username = #{username}, phone = #{phone}, email = #{email}, password = #{password} WHERE id = #{id}")
    void update(User user);

    @Select("select id from user")
    List<Long> getAllId();
}
