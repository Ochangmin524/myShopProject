package ShopProject.myShopProject.Service;

import ShopProject.myShopProject.Domain.Member;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class KakaoLoginService {

    public void withdrawKakaoMemberByAdminKey(Member member)throws IOException {

        String reqURL = "https://kapi.kakao.com/v1/user/unlink";
        URL url = new URL(reqURL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();//URL을 사용해서 커넥션을 얻기
        conn.setRequestMethod("POST");  //PSOT 요청 보내기
        conn.setRequestProperty("Authorization", "KakaoAK 7b30c95c0f1dfcb0c02dd095fcece882");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        String bodyData = "target_id_type=user_id&target_id=" + member.getLoginId();

        conn.setDoOutput(true);
        try(OutputStream os = conn.getOutputStream()) {
            byte[] input = bodyData.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }






        String line = "";
        String result = "";


        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

        while ((line = br.readLine()) != null) {
            result += line;
        }

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> jsonMap = objectMapper.readValue(result, new TypeReference<Map<String, Object>>() {
        });

        log.info("Response Body : " + result);


    }

    //로그인 성공후 받은 code를 이용하여 토큰을 받아오는 메소드
    public String getAccessTokenFromKakao(String client_id, String code) throws IOException {

        log.info("getAccessTokenFromKakao, kakao post 요청");
        //------kakao POST 요청------
        String reqURL = "https://kauth.kakao.com/oauth/token?grant_type=authorization_code&client_id=" + client_id + "&code=" + code;
        URL url = new URL(reqURL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();//URL을 사용해서 커넥션을 얻기
        conn.setRequestMethod("POST");  //PSOT 요청 보내기

        log.info("BufferedReader, br 요청");

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        //getInputStream()으로 요청 응답을 읽기 위한 입력 스트림을 반환
        //InputStreamReader  로 바이트 기반의 입력 스프림을 문자 기반의 입력 스트림을 변환
        //BufferedReader 입력 스트림을 버퍼링하여 효율적으로 읽을 수 있있. 한줄씩, 한번에 읽기 가능


        String line = "";
        String result = "";

        while ((line = br.readLine()) != null) {
            result += line;
        }

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> jsonMap = objectMapper.readValue(result, new TypeReference<Map<String, Object>>() {
        });

        log.info("Response Body : " + result);

        String accessToken = (String) jsonMap.get("access_token");
        String refreshToken = (String) jsonMap.get("refresh_token");
        String scope = (String) jsonMap.get("scope");


        log.info("Access Token : " + accessToken);
        log.info("Refresh Token : " + refreshToken);
        log.info("Scope : " + scope);


        return accessToken;
            }


    //getAccessTokenFromKakao로 받아온 토큰을 이용하여 유저의 정보를 HashMap에 담아 반납하는 메소드
    public HashMap<String, Object> getUserInfo(String access_Token) throws IOException {
        //클라이언트 요청 정보 HashMap 초기화
        log.info("getUserInfo 시작");
        HashMap<String,Object> userinfo = new HashMap<String,Object>();

        String reqURL = "https://kapi.kakao.com/v2/user/me"; // 카카오 가이드에 있는 경로
        URL url = new URL(reqURL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection(); //URL을 사용해서 커넥션을 얻기
        conn.setRequestMethod("GET"); // get 요청 보내기
        conn.setRequestProperty("Authorization", "Bearer " + access_Token);
        //authorization 헤더로 토큰을 넣어준다. 일반적으로 authorization은 토큰을 담는 필드이다.
        //Bearer은 JWT와 OAuth를 나타내는 인증 타입이다.



        int responseCode = conn.getResponseCode();
        System.out.println("responseCode : " + responseCode);

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

        String line = "";
        String result = "";

        while ((line = br.readLine()) != null) {
            result += line;
        }


        // jackson objectmapper 객체 생성
        ObjectMapper objectMapper = new ObjectMapper();
        // JSON String -> Map
        Map<String, Object> jsonMap = objectMapper.readValue(result, new TypeReference<Map<String, Object>>() {
        });

        for(String key : jsonMap.keySet()) {

            log.info(key + " : " + jsonMap.get(key));
        }

        //사용자 정보 추출
        Map<String, Object> properties = (Map<String, Object>) jsonMap.get("properties");

        Long id = (Long) jsonMap.get("id");
        String nickname = properties.get("nickname").toString();
        String profileImage = properties.get("profile_image").toString();

        //userInfo에 넣기
        userinfo.put("id", id);
        userinfo.put("nickname", nickname);
        userinfo.put("profileImage", profileImage);


        return userinfo;
    }

    }



