package org.zerock.member_board.service;

import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnailator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.member_board.dto.*;
import org.zerock.member_board.entity.*;
import org.zerock.member_board.entity.redis.Like;
import org.zerock.member_board.repository.*;
import org.zerock.member_board.service.util.MemberHandler;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;


@RequiredArgsConstructor
@Transactional
@Service
public class ReviewServiceImpl implements ReviewService{

    @Autowired
    final ReviewRepository reviewRepository;
    final LikeRepository likeRepository;
    final ParticipationRepository participationRepository;
    final ReviewImageRepository reviewImageRepository;

    Logger logger = LoggerFactory.getLogger(ReviewServiceImpl.class);

    @Value("${org.zerock.upload.path}")
    private String uploadPath;

    @Override
    public Long register(ReviewDTO dto , MultipartFile[] photos) {

        List<Map<String,String>> reviewImgValues = null;
        if(photos != null){
            if(photos.length > 0 && !photos[0].isEmpty()) {//아무 파일도 안보내도 기본적으로 0인덱스가 들어가있음...
                reviewImgValues = makeImageFile(photos); //파일 저장
            }
        }

        Review review = reviewRepository.save(dtoToEntity(dto));

        if(photos != null)
        {
            if(photos.length > 0 && !photos[0].isEmpty()) {

            saveReviewImage(review.getRro(), reviewImgValues);
            }
        }


        //좋아요 레디스 부분 (레디스는 현재 트랙잭션 처리가 안되므로 안정적인 가장 마지막 단계에서 처리..

        Like like = Like.builder()
                .id(review.getRro().toString())
                .likeCnt(String.valueOf(0))
                .likeMembers(new ArrayList<>())
                .build();

        likeRepository.save(like);

        return review.getRro();
    }

    @Override
    public PageResultDTO<ReviewDTO, Object[]> getList(PageRequestDTO pageRequestDTO) {


        Page<Object[]> result = reviewRepository.getReviewListWithReviewImageMemberBoard(pageRequestDTO.getPageable(Sort.by("rro").descending()));

        List<ReviewDTO> list = new ArrayList<>();


        for(Object[] object : result)
        {
            List<ReviewImage> imageList = new ArrayList<>();
            imageList.add((ReviewImage) object[1]); //실제 연관된 ReviewImage 데이터가 없어도 빈 Object가 넘어옴
            Review review = (Review) object[0];
            Like like = likeRepository.findById(review.getRro().toString()).get();
            list.add(entityToDTO(review, imageList, (Member) object[2] , (Board) object[3], like));
        }

        return new PageResultDTO<>(list, result.getTotalPages(), result.getPageable());
    }

    @Override
    public ReviewDTO get(Long rro) {

        List<Object[]> result = reviewRepository.getReviewWithReviewImageMemberBoard(rro);

        if(result == null)
        {
            return null;
        }

        Review review = (Review) result.get(0)[0];
        Member member = (Member) result.get(0)[2];
        Board board = (Board) result.get(0)[3];

        List<ReviewImage> reviewImgList = new ArrayList<>();

        for(Object[] objects : result)
        {
            ReviewImage reviewImage = (ReviewImage) objects[1];
            reviewImgList.add(reviewImage);
        }


        Like like = likeRepository.findById(rro.toString()).get();
        ReviewDTO reviewDTO = entityToDTO(review,reviewImgList, member,board,like);

        return reviewDTO;
    }

    @Override
    public List<ParticipationDTO> getList(String email) {
        List<Object[]> result = participationRepository.getBoardByEmail(email);

        List<ParticipationDTO> list = new ArrayList<>();

        for(Object[] objects : result)
        {
            ParticipationDTO participationDTO = partiEntityToDTO((Board)objects[1]);
            list.add(participationDTO);
        }

        return list;
    }

    @Override
    public void modify(ReviewDTO reviewDTO, MultipartFile[] photos) {

        List<ReviewImage> result = reviewImageRepository.findByReviewRro(reviewDTO.getRro());

        if(result.size() > 0) //애초에 기존 사진이 없다면 삭제할것도 없음.. //하나도 없을때 테스트해볼것
        {
            boolean[] existImage = new boolean[result.size()]; //result와 같은 사이즈로 생성!
            Arrays.fill(existImage, false);

            List<ReviewImageDTO> dtoList = reviewDTO.getImageList();
            if(dtoList != null)
            {
                for(int i = 0; i < dtoList.size(); i++)
                {
                    Long inum = dtoList.get(i).getInum(); //안 지워진 이미지들의 inum

                    for(int j = 0; j < result.size(); j++)
                    {
                        ReviewImage ri = result.get(j);
                        if(ri.getInum() == inum)
                        {
                            existImage[j] = true;
                        }
                    }
                }
            }
            for(int i = result.size()-1; i >= 0; i--)
            {
                if(existImage[i] == false)
                {
                    ReviewImage ri = result.get(i);
                    reviewImageRepository.delete(ri);
                    removeFile(ri.getPath(),ri.getUuid(),ri.getImgName());
                }
            }
        }
        //ReivewImage 삭제 완료

        if(photos != null )
        {

            if(photos.length > 0 && !photos[0].isEmpty())
            {
                List<Map<String,String>> reviewImgValues = makeImageFile(photos); //파일 저장

                saveReviewImage(reviewDTO.getRro(), reviewImgValues);
                //새로 첨부한 이미지 추가 완료
            }
        }



        reviewRepository.updateReviewTitleContent(reviewDTO.getTitle(), reviewDTO.getContent(), reviewDTO.getRro());

    }


    @Override
    public void removeReviewWithReviewImage(Long rro) {
        Review review = Review.builder().
                rro(rro)
                .build();

        List<ReviewImage> reviewImageList = reviewImageRepository.findByReviewRro(rro);

        for(ReviewImage ri : reviewImageList)
        {
            removeFile(ri.getPath(), ri.getUuid(), ri.getImgName());
            reviewImageRepository.deleteReviewImageByReview(review);
        }

        reviewRepository.delete(review);
    }

    @Override
    public LikeDTO getReviewLikeData(Long rro) {

        Optional<Like> result = likeRepository.findById(rro.toString());

        if(result.isPresent())
        {
            Like like = result.get();

            //현재 세션 인증 정보 가져오기
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            boolean contain = false;
            if(authentication.getName() != "anonymousUser")
            {
                String memeberEmail = MemberHandler.GetMemberEmail();

                if(like.getLikeMembers() != null)
                {
                    if(like.getLikeMembers().contains(memeberEmail))
                    {
                        contain = true;

                    }
                }

            }

            LikeDTO likeDTO = LikeDTO
                    .builder()
                    .likeCnt(like.getLikeCnt())
                    .contains(contain)
                    .build();

            return likeDTO;
        }
        else
        {
            return null;
        }
    }

    @Override
    public void clickLikeReview(Long rro) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getName() == "anonymousUser") //들어올일 없지만..
        {
            return;
        }
        Optional<Like> result = likeRepository.findById(rro.toString());

        if(!result.isPresent())
        {
            return;
        }

        Like like = result.get();

        String email = MemberHandler.GetMemberEmail();

        if(like.getLikeMembers() == null)
        {
            //증가
            Long likeCount = Long.valueOf(like.getLikeCnt());
            like.setLikeCnt((++likeCount).toString());

            //리스트 생성
            List<String> newList = new ArrayList<>();
            newList.add(email);
            like.setLikeMembers(newList);

            likeRepository.save(like);
            return;
        }

        if(like.getLikeMembers().contains(email))
        {
            //좋아요 취소처리
            Long likeCount = Long.valueOf(like.getLikeCnt());
            like.setLikeCnt((--likeCount).toString());

            like.getLikeMembers().remove(email);
        }
        else
        {
            //좋아요 처리
            Long likeCount = Long.valueOf(like.getLikeCnt());
            like.setLikeCnt((++likeCount).toString());

            like.getLikeMembers().add(email);
        }
        likeRepository.save(like);
    }

    public ParticipationDTO partiEntityToDTO(Board board)
    {
        ParticipationDTO participationDTO = ParticipationDTO.builder()
                .bno(board.getBno())
                .Title(board.getTitle())
                .build();

        return participationDTO;

    }

    private String makeFolder() {
        String str = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));

        String folderPath = str.replace("\\", File.separator);

        File uploadPathFolder = new File(uploadPath, folderPath);

        if(uploadPathFolder.exists() == false){
            uploadPathFolder.mkdirs();
        }

        return folderPath;
    }

    private List<Map<String,String>> makeImageFile(MultipartFile[] photos)
    {
        List<Map<String,String>> list = new ArrayList<>();

        if(photos != null)
        {
            if(photos.length > 0)
            {
                for(MultipartFile photo : photos)
                {

                    if(photo.getContentType().startsWith("image") == false)
                    {
                        logger.debug("not image type..");
                        return null;
                    }

                    HashMap<String,String> values = new HashMap<>();

                    String originalName = photo.getOriginalFilename();
                    String fileName = originalName.substring(originalName.lastIndexOf("\\") + 1); //마지막 경로 다음 인덱스 구해서 해당 인덱스에서부터 서브스트링


                    System.out.println("originalName: " + originalName);
                    System.out.println("fileName: " + fileName);


                    //폴더 생성
                    String folderPath = makeFolder();


                    //UUID 발생
                    String uuid = UUID.randomUUID().toString();


                    String path = uploadPath + File.separator + folderPath + File.separator;
                    String saveName = path + uuid
                            + "_" + fileName;

                    Path savePath = Paths.get(saveName);



                    try{

                        photo.transferTo(savePath);

                        String thumbnailName = uploadPath + File.separator + folderPath + File.separator
                                + "s_" + uuid + "_" + fileName;

                        File thumbnailFile = new File(thumbnailName);

                        Thumbnailator.createThumbnail(savePath.toFile(), thumbnailFile, 200, 200);

                        values.put("uuid",uuid);
                        values.put("imgName",fileName);
                        values.put("path",path);

                        list.add(values);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }



        }
        return list;
    }

    public boolean removeFile(String path, String uuid, String imgName){

        File file = new File(path + uuid + "_" + imgName);
        boolean result = file.delete();

        File thumbnail = new File(file.getParent(), "s_" + file.getName());

        result = thumbnail.delete();

        return true;
    }

    public boolean saveReviewImage(Long rro, List<Map<String,String>> attr)
    {
        for(Map<String,String> values : attr){

            Review tempReview = Review.builder()
                    .rro(rro)
                    .build();

            ReviewImage reviewImage = ReviewImage.builder()
                    .uuid(values.get("uuid"))
                    .imgName(values.get("imgName"))
                    .path(values.get("path"))
                    .review(tempReview)
                    .build();

            reviewImageRepository.save(reviewImage);
        }

        return true;
    }


    @Override
    public ResponseEntity<byte[]> getPhotoFile(String fileName) {
        ResponseEntity<byte[]> result = null;

        try{

            String srcFileName = URLDecoder.decode(fileName, "UTF-8");

            File file = new File( srcFileName);





            HttpHeaders headers = new HttpHeaders();
            result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), headers, HttpStatus.OK);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return result;
    }

    @Override
    public List<ReviewDTO> getRecentReview() {

        //페이징 처리까진 필요 없지만 Spring Data JPA 에서 limit 이 안되서..
        Pageable pageable = PageRequest.of(0, 3, Sort.by("rro").descending());
        Page<Object[]> result = reviewRepository.getReviewListWithReviewImageMemberBoard(pageable);


        List<ReviewDTO> dtoList = new ArrayList<>();
        for(Object[] objects : result)
        {
            Review review = (Review) objects[0];
            ReviewImage reviewImg = (ReviewImage) objects[1];
            Member member = (Member) objects[2];
            Board board = (Board) objects[3];



            Optional<Like> likeResult = likeRepository.findById(review.getRro().toString());

            if(!likeResult.isPresent())
            {
                //예외처리
                continue;
            }

            Like like = likeResult.get();

            List<ReviewImage> reviewImageList = new ArrayList<>();

            reviewImageList.add(reviewImg);
            ReviewDTO reviewDTO = entityToDTO(review, reviewImageList, member, board, like);

            dtoList.add(reviewDTO);
        }

        return dtoList;
    }
}
