package org.zerock.member_board.repository.querydsl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
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

        QBoard board = QBoard.board;
        QReply reply = QReply.reply;
        QMember member = QMember.member;

        JPQLQuery<Board> jpqlQuery = from(board);
        jpqlQuery.leftJoin(member).on(board.writer.eq(member));
        jpqlQuery.leftJoin(reply).on(reply.board.eq(board));

        JPQLQuery<Tuple> tuple = jpqlQuery.select(board, member, reply.count());

        BooleanBuilder booleanBuilder = new BooleanBuilder(); //조건을 추가 변수

        booleanBuilder.and(board.bno.gt(0L)); // ID 0이상 조건 추가

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

                booleanBuilder.and(basicBuilder);
            }
        }


        if(region == true)
        {
            if(regionKeyword != "")
            {
                //지역 조건
                BooleanBuilder regionBuilder = new BooleanBuilder();
                regionBuilder.or(board.place.contains(regionKeyword));
                booleanBuilder.and(regionBuilder); //AND 처리
            }
        }

        if(minCost != null && maxCost != null)
        {
            //최소, 최대 모임금액 조건
            BooleanBuilder costBuilder = new BooleanBuilder();
            costBuilder.or(board.costs.between(minCost,maxCost));
            booleanBuilder.and(costBuilder);
        }

        tuple.where(booleanBuilder);

        //정렬
        Sort sort = pageable.getSort();

        sort.stream().forEach(order -> {
            Order direction = order.isAscending()? Order.ASC: Order.DESC;
            String prop = order.getProperty();

            PathBuilder orderByExpression = new PathBuilder(Board.class, "board");
            tuple.orderBy(new OrderSpecifier(direction, orderByExpression.get(prop)));
        });



        tuple.groupBy(board);

        tuple.offset(pageable.getOffset());
        tuple.limit(pageable.getPageSize());

        List<Tuple> result = tuple.fetch();

        long count = tuple.fetchCount();

        return new PageImpl<> (
                result.stream().map(t->t.toArray()).collect(Collectors.toList()), pageable, count);


//        BooleanBuilder
//        if(type != null)
//        {
//            String[] types = type.split("");
//
//            for(String type : types)
//            {
//                if(type == "")
//            }
//        }
//
    }
}
