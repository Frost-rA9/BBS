package org.droplet.mapper;

import org.apache.ibatis.annotations.*;
import org.droplet.Entity.Account;

@Mapper
public interface UserMapper {
    @Select("select * from db_account where username = #{text} or email = #{text}")
    Account findAccountByNameOrEmail(String text);

    @Insert("insert into db_account(username, password, email) values(#{username},#{password},#{email})")
    int createAccount(@Param("username") String username, @Param("password") String password, @Param("email") String email);

    @Update("update db_account set password = #{password} where email = #{email}")
    int resetPasswordByEmail(@Param("email") String email,@Param("password") String password);
}
