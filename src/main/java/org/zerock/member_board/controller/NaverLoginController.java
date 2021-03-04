package org.zerock.member_board.controller;

import lombok.RequiredArgsConstructor;
import net.bytebuddy.implementation.bytecode.Throw;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.HttpURLConnection;

import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.text.ParseException;
import java.util.Map;
import java.net.URL;





//oauth2를 안 쓰고 구현한 네이버 로그인
//현재는 쓰이지 않음







@Controller
public class NaverLoginController {

    private final String CLIENT_ID;
    private final String CLI_SECRET;

    public NaverLoginController(Environment environment) {

        CLIENT_ID = environment.getProperty("Naver.ClientID"); //애플리케이션 클라이언트 아이디값";
        CLI_SECRET = environment.getProperty("Naver.ClientSecret"); //애플리케이션 클라이언트 시크릿값";
    }

    //상태 토큰 만들기
    public String generateState()
    {
        SecureRandom random = new SecureRandom();
        return new BigInteger(130, random).toString(32);
    }

    @RequestMapping("/naver")
    public String testNaver(HttpSession session, Model model) throws IOException {

        System.out.println("===="+session.getId()+"====");
        String redirectURI = URLEncoder.encode("http://localhost:8080/naver/callback", "UTF-8");

        String state = generateState();

        String apiURL = "https://nid.naver.com/oauth2.0/authorize?response_type=code";
        apiURL += String.format("&client_id=%s&redirect_uri=%s&state=%s",
                CLIENT_ID, redirectURI, state);
        session.setAttribute("state",state);
        System.out.println("+ "+apiURL+ " + ");
        return "redirect:" + apiURL;
    }

    @GetMapping("/naver/callback")
    public String naverCallback(HttpSession session, HttpServletRequest request, Model model, RedirectAttributes redi) throws IOException, ParseException, org.apache.tomcat.util.json.ParseException {

        System.out.println("=============================콜백==================================");

        String code = request.getParameter("code");
        String state = request.getParameter("state");
        String redirectURI = URLEncoder.encode("http://localhost:8080/naver/callback1", "UTF-8");

        Object storedStateObj = session.getAttribute("state");

        if(state == null || storedStateObj == null)
        {
            //예외처리

        }

        String storedState = storedStateObj.toString();

        if(state != storedState)
        {
            //예외처리
        }


        String apiURL;
        apiURL = "https://nid.naver.com/oauth2.0/token?grant_type=authorization_code&";
        apiURL += "client_id=" + CLIENT_ID;
        apiURL += "&client_secret=" + CLI_SECRET;
        apiURL += "&code=" + code;
        apiURL += "&state=" + state;
        System.out.println("apiURL=" + apiURL);
        String res = requestToServer(apiURL);


        //삭제
        String email = "";
        redi.addAttribute("useremail","tester@yahoo.co.kr");
        redi.addAttribute("userpass","1234");


        if(res != null && !res.equals("")) {
//            model.addAttribute("res", res);
            Map<String, Object> parsedJson = new JSONParser(res).parseObject();
            System.out.println(parsedJson);
            session.setAttribute("currentUser", res);
            session.setAttribute("currentAT", parsedJson.get("access_token"));
            session.setAttribute("currentRT", parsedJson.get("refresh_token"));

            //삭제
            getProfileFromNaver(parsedJson.get("access_token").toString());



        } else {
//            model.addAttribute("res", "Login failed!");
        }
        System.out.println("=============================콜백==================================");

        return "redirect:/member/login";
    }


    private void saveContext(UserDetails userDetails, HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = new PreAuthenticatedAuthenticationToken(userDetails, "[PROTECTED]");

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);

        // 세션에 SecurityContext 저장 & 세션 쿠키 발급
//        securityContextRepository.saveContext(securityContext, request, response);
    }

//    /**
//     * 토큰 갱신 요청 페이지 컨트롤러
//     * @param session
//     * @param request
//     * @param model
//     * @param refreshToken
//     * @return
//     * @throws IOException
//     * @throws ParseException
//     */
//    @RequestMapping("/naver/refreshToken")
//    public String refreshToken(HttpSession session, HttpServletRequest request, Model model, String refreshToken) throws IOException, ParseException {
//        String apiURL;
//        apiURL = "https://nid.naver.com/oauth2.0/token?grant_type=refresh_token&";
//        apiURL += "client_id=" + CLIENT_ID;
//        apiURL += "&client_secret=" + CLI_SECRET;
//        apiURL += "&refresh_token=" + refreshToken;
//        System.out.println("apiURL=" + apiURL);
//        String res = requestToServer(apiURL);
//        model.addAttribute("res", res);
//        session.invalidate();
//        return "test-naver-callback";
//    }
//    /**
//     * 토큰 삭제 컨트롤러
//     * @param session
//     * @param request
//     * @param model
//     * @param accessToken
//     * @return
//     * @throws IOException
//     */
//    @RequestMapping("/naver/deleteToken")
//    public String deleteToken(HttpSession session, HttpServletRequest request, Model model, String accessToken) throws IOException {
//        String apiURL;
//        apiURL = "https://nid.naver.com/oauth2.0/token?grant_type=delete&";
//        apiURL += "client_id=" + CLIENT_ID;
//        apiURL += "&client_secret=" + CLI_SECRET;
//        apiURL += "&access_token=" + accessToken;
//        apiURL += "&service_provider=NAVER";
//        System.out.println("apiURL=" + apiURL);
//        String res = requestToServer(apiURL);
//        model.addAttribute("res", res);
//        session.invalidate();
//        return "test-naver-callback";
//    }
//    /**
//     * 액세스 토큰으로 네이버에서 프로필 받기
//     * @param accessToken
//     * @return
//     * @throws IOException
//     */
//    @ResponseBody
//    @RequestMapping("/naver/getProfile")
    public String getProfileFromNaver(String accessToken) throws IOException {
        // 네이버 로그인 접근 토큰;
        String apiURL = "https://openapi.naver.com/v1/nid/me";
        String headerStr = "Bearer " + accessToken; // Bearer 다음에 공백 추가
        String res = requestToServer(apiURL, headerStr);
        return res;
    }
//    /**
//     * 세션 무효화(로그아웃)
//     * @param session
//     * @return
//     */
//    @RequestMapping("/naver/invalidate")
//    public String invalidateSession(HttpSession session) {
//        session.invalidate();
//        return "redirect:/naver";
//    }
//    /**
//     * 서버 통신 메소드
//     * @param apiURL
//     * @return
//     * @throws IOException
//     */
    private String requestToServer(String apiURL) throws IOException {
        return requestToServer(apiURL, "");
    }
//    /**
//     * 서버 통신 메소드
//     * @param apiURL
//     * @param headerStr
//     * @return
//     * @throws IOException
//     */
    private String requestToServer(String apiURL, String headerStr) throws IOException {
        URL url = new URL(apiURL);
        HttpURLConnection con = (HttpURLConnection)url.openConnection();
        con.setRequestMethod("GET");
        System.out.println("header Str: " + headerStr);
        if(headerStr != null && !headerStr.equals("") ) {
            con.setRequestProperty("Authorization", headerStr);
        }
        int responseCode = con.getResponseCode();
        BufferedReader br;
        System.out.println("responseCode="+responseCode);
        if(responseCode == 200) { // 정상 호출
            br = new BufferedReader(new InputStreamReader(con.getInputStream()));
        } else {  // 에러 발생
            br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
        }
        String inputLine;
        StringBuffer res = new StringBuffer();
        while ((inputLine = br.readLine()) != null) {
            res.append(inputLine);
        }
        br.close();
        if(responseCode==200) {




            System.out.println("============= res " +res +"==============================");
            return res.toString();
        } else {
            return null;
        }
    }
}
