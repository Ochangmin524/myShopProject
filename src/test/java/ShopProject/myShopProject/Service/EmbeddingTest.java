package ShopProject.myShopProject.Service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EmbeddingTest {

    @Autowired
    EmbeddingService embeddingService;

    @Test
    public void getembeddingscoretest() throws IOException {
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
            System.out.println(line+"D");
        }
        System.out.println(output.toString());
        String result = output.toString();

    }
}
