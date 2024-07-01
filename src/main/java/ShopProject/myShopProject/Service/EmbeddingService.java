package ShopProject.myShopProject.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service @Slf4j
public class EmbeddingService{

    //상품명의 임베딩 값을 스트링으로 변환하여 반환한다. [... ... ... ... ...] 형식

    public String getEmbeddingScore(String itemName) throws IOException{
        String pythonInterpreter = "python"; // 또는 파이썬 설치 경로 예: "C:\\Python39\\python.exe"

        // Python 스크립트 경로 설정
        String scriptPath = "C:\\Users\\mldhc\\OneDrive\\바탕 화면\\Project\\myShopProject-master\\myShopProject-master\\src\\main\\java\\ShopProject\\myShopProject\\Service\\Embedding.py";

        // 인수 설정
        List<String> command = new ArrayList<>();
        command.add(pythonInterpreter);
        command.add(scriptPath);
        command.add("arg1");  // 첫 번째 인수

        // ProcessBuilder 생성 및 명령어 설정
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.redirectErrorStream(true);

        // 프로세스 시작
        Process process = processBuilder.start();

        // 프로세스 출력 읽기
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder output = new StringBuilder();
        String line;

        String a = reader.readLine();
        String b = reader.readLine();

        while ((line = reader.readLine()) != null) {
            output.append(line);
            System.out.println(line+"DD");
        }
        System.out.println(output.toString());
        String result = output.toString();
        return result;
    }
}
