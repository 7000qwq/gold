package gold.mapper;

import gold.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper {

    @Insert("insert into user (username, phone, email, password) values (#{username}, #{phone}, #{email}, #{password})")
    void insert(User user);

    @Select("select * from user where email = #{email}")
    User getByEmail(String email);

    @Select("select * from user where id = #{currentId}")
    User getById(Long currentId);

    @Update("UPDATE user SET username = #{username}, phone = #{phone}, email = #{email}, password = #{password} WHERE id = #{id}")
    void update(User user);
}
