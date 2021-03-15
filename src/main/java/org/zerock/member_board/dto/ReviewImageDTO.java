package org.zerock.member_board.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

@Builder
@Setter
@Getter
public class ReviewImageDTO {

    private Long inum;

    private String uuid;

    private String imgName;

    private String path;


    public String getOriImgURL() throws UnsupportedEncodingException {
        return URLEncoder.encode(path +uuid + "_"+imgName,"UTF-8");
    }

    public String getThumImgURL() throws UnsupportedEncodingException {
        String encoding = URLEncoder.encode(path+"s_" +uuid  + "_"+imgName,"UTF-8");

        System.out.println(path+"s_" +uuid  + "_"+imgName);
        return encoding;

    }



}