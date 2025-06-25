package org.baseball.domain.fairy;

import org.apache.ibatis.annotations.Mapper;
import org.baseball.dto.FairyDTO;

import java.util.List;

@Mapper
public interface FairyMapper {
    List<FairyDTO> showFairyRank();
}
