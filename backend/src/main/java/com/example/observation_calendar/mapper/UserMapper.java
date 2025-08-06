package com.example.observation_calendar.mapper;

import com.example.observation_calendar.dto.UserDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface UserMapper {

    /**
     * ユーザーIDでユーザーを取得
     */
    Optional<UserDto> findById(@Param("id") Long id);

    /**
     * メールアドレスでユーザーを取得
     */
    Optional<UserDto> findByEmail(@Param("email") String email);

    /**
     * ユーザー名でユーザーを取得
     */
    Optional<UserDto> findByUsername(@Param("username") String username);

    /**
     * 全ユーザーを取得
     */
    List<UserDto> findAll();

    /**
     * ユーザーを新規作成
     */
    int insert(UserDto user);

    /**
     * ユーザー情報を更新
     */
    int update(UserDto user);

    /**
     * ユーザーを論理削除
     */
    int deleteById(@Param("id") Long id);

    /**
     * メールアドレスの存在確認
     */
    boolean existsByEmail(@Param("email") String email);

    /**
     * ユーザー名の存在確認
     */
    boolean existsByUsername(@Param("username") String username);

}
