package org.zerock.member_board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.member_board.dto.ReplyDTO;
import org.zerock.member_board.entity.Board;
import org.zerock.member_board.entity.Reply;
import org.zerock.member_board.repository.ReplyRepository;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Transactional
@Service
@RequiredArgsConstructor
@Log4j2
public class ReplyServiceImpl implements ReplyService{

    private final ReplyRepository replyRepository;

    @Override
    public Long register(ReplyDTO replyDTO) {

        Reply reply = dtoToEntity(replyDTO);
        replyRepository.save(reply);

        return reply.getRno();
    }

    @Override
    public List<ReplyDTO> getList(Long bno) {

        Board board = Board.builder().bno(bno).build();
        List<Reply> list = replyRepository.getRepliesByBoardOrderByRno(board);

        Function<Reply,ReplyDTO> fn = (reply -> entityToDTO(reply));
        return list.stream().map(fn).collect(Collectors.toList());
    }

    @Override
    public void modify(ReplyDTO replyDTO) {
        Optional<Reply> result = replyRepository.findById(replyDTO.getRno());

        if(result.isPresent())
        {
            Reply reply = result.get();
            reply.changeContent(replyDTO.getText());
        }
    }

    @Override
    public void remove(Long rno) {
        replyRepository.deleteById(rno);

    }
}

