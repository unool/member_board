package org.zerock.member_board.repository.querydsl;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import org.zerock.member_board.entity.Board;
import org.zerock.member_board.entity.QBoard;
import org.zerock.member_board.entity.QMember;
import org.zerock.member_board.entity.QReply;
import java.util.List;
import java.util.stream.Collectors;


@Repository
public class SearchBoardRepositoryImpl extends QuerydslRepositorySupport implements SearchBoardRepository{
    public SearchBoardRepositoryImpl() {
        super(Board.class);
    }

    @Override
    public Page<Object[]> searchBoard(boolean[] type, String typeKeyword, boolean region,
                                      String regionKeyword, Long minCost,
                                      Long maxCost, Pageable pageable) {
        //select 해올 테이블
        QBoard board = QBoard.board;
        QReply reply = QReply.reply;
        QMember member = QMember.member;

        JPQLQuery<Board> jpqlQuery = from(board); //from절 테이블 설정
        jpqlQuery.leftJoin(member).on(board.writer.eq(member)); //Member 테이블 left join
        jpqlQuery.leftJoin(reply).on(reply.board.eq(board)); //Board 테이블 left join

        JPQLQuery<Tuple> tuple = jpqlQuery.select(board, member, reply.count()); //기본 튜플 쿼리 생성

        BooleanBuilder booleanBuilder = new BooleanBuilder(); //조건을 추가 변수 (모든 조건 담음)

        booleanBuilder.and(board.bno.gt(0L)); // ID 0이상 조건 추가 AND

        //제목, 내용, 작성자 조건
        if(type != null)
        {
            if(type.length == 3)
            {
                BooleanBuilder basicBuilder = new BooleanBuilder();

                if(type[0] == true)
                {
                    basicBuilder.or(board.title.contains(typeKeyword));
                }

                if(type[1] == true)
                {
                    basicBuilder.or(board.content.contains(typeKeyword));
                }

                if(type[2] == true)
                {
                    basicBuilder.or(board.writer.name.contains(typeKeyword));
                    basicBuilder.or(board.writer.email.contains(typeKeyword));
                }

                booleanBuilder.and(basicBuilder); //제목, 내용, 작성자 조건 AND (하나의 검색 INPUT)
            }
        }


        if(region == true)
        {
            if(regionKeyword != "")
            {
                //지역 조건
                BooleanBuilder regionBuilder = new BooleanBuilder();
                regionBuilder.or(board.place.contains(regionKeyword));
                booleanBuilder.and(regionBuilder); //지역 조건 AND 처리
            }
        }

        if(minCost != null && maxCost != null)
        {
            //최소, 최대 모임금액 조건
            BooleanBuilder costBuilder = new BooleanBuilder();
            costBuilder.or(board.costs.between(minCost,maxCost));
            booleanBuilder.and(costBuilder); //금액 조건 AND 처리
        }

        tuple.where(booleanBuilder); //조건 설정

        //정렬
        Sort sort = pageable.getSort(); //정렬 객체

        sort.stream().forEach(order -> {
            Order direction = order.isAscending()? Order.ASC: Order.DESC; //파라미터 Pageable의 현재 sort 방향
            String prop = order.getProperty(); //현재 sort 기준 속성이 무엇인지
            PathBuilder orderByExpression = new PathBuilder(Board.class, "board"); //prop(bno)이 Board의 컬럼 이므로
            tuple.orderBy(new OrderSpecifier(direction, orderByExpression.get(prop))); //최종적으로 정렬 적용
        });

        tuple.groupBy(board); //Board 기준으로 Group By
        tuple.offset(pageable.getOffset()); //현재 페이지
        tuple.limit(pageable.getPageSize()); //최대 페이지
        List<Tuple> result = tuple.fetch(); //실제 select
        long count = tuple.fetchCount(); //총 tuple 갯수

        return new PageImpl<> (
                result.stream().map(t->t.toArray()).collect(Collectors.toList()), pageable, count);

    }
}
