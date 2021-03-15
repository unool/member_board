package org.zerock.member_board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.member_board.dto.AttendDTO;
import org.zerock.member_board.dto.BoardDTO;
import org.zerock.member_board.entity.redis.Attend;
import org.zerock.member_board.repository.AttendRepository;

import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Transactional
@Service
public class AttendService {

    private final AttendRepository attendRepository;


    public AttendDTO getAttend(Long bno){

        Optional<Attend> result = attendRepository.findById(bno.toString());

        AttendDTO attendDTO = null;
        if(result.isPresent())
        {
            try {
                Attend attend = result.get();

                //삭제
                for(String mem : attend.getMembers())
                {
                    System.out.println(mem);
                }

                attendDTO = entityToDTO(attend);

                return attendDTO;
            }
            catch (Exception e)
            {
                attendDTO = AttendDTO.builder().build();
                System.out.println(e.toString());
            }
        }

        return attendDTO;

    }

    public AttendDTO attendMember(Long bno, String attendEmail){
        Optional<Attend> result = attendRepository.findById(bno.toString());

        if(!result.isPresent())
        {
            return null;
        }

        if(attendEmail == "null")
        {
            return null;
        }
        if(attendEmail == "annoymous")
        {
            return null;

        }
        Attend attend = result.get();




        List<String> curMemberList = attend.getMembers();

        Long curCnt = Long.valueOf(attend.getCurrentCnt());
        if(attend.getMembers().contains(attendEmail))
        {
            curMemberList.remove(attendEmail);
            curCnt--;
        }
        else
        {
            if(Long.valueOf(attend.getCurrentCnt()) >= Long.valueOf(attend.getRequiredCnt()))
            {
                return null;
            }

            curMemberList.add(attendEmail);
            curCnt++;
        }

        attend.setMembers(curMemberList);
        attend.setCurrentCnt(curCnt.toString());
        attendRepository.save(attend);

        AttendDTO attendDTO = entityToDTO(attend);
        return attendDTO;
    }

    public AttendDTO entityToDTO(Attend attend)
    {
        AttendDTO attendDTO = AttendDTO.builder()
                .id(attend.getId())
                .currentCnt(attend.getCurrentCnt())
                .requiredCnt(attend.getRequiredCnt())
                .members(attend.getMembers())
                .build();

        return attendDTO;
    }
}
