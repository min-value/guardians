package org.baseball.domain.fairy;

import org.baseball.dto.FairyDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FairyService {
    @Autowired
    FairyMapper fairyMapper;

    public List<FairyDTO> showFairyRank(){
        List<FairyDTO> list = fairyMapper.showFairyRank();
        int place = 1;
        for(FairyDTO l : list){
            l.setPlace(place++);
            while(l.getRatio().length()<5){
                l.setRatio(l.getRatio() + '0');
            }
        }
        return list;
    }
}
