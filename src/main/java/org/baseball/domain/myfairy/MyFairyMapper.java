package org.baseball.domain.myfairy;

import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface MyFairyMapper {
    Map<String, Object> selectFairyGameResults(int userPk);
}
