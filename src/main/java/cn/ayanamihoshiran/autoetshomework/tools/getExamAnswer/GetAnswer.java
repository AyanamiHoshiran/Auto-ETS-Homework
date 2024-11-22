package cn.ayanamihoshiran.autoetshomework.tools.getExamAnswer;

import cn.ayanamihoshiran.autoetshomework.entity.ChooseAnswer;
import cn.ayanamihoshiran.autoetshomework.entity.ListenAnswer;
import cn.ayanamihoshiran.autoetshomework.entity.ReproduceAnswer;
import cn.ayanamihoshiran.autoetshomework.globalUtils.loggerUtil.Log;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import static cn.ayanamihoshiran.autoetshomework.tools.getExamAnswer.LatestModifiedFolder.getLatestModifiedFolder;

public class GetAnswer {
    public static void use(String resourcePath) throws Exception{
        Path path = Path.of(resourcePath);
        String latestFolder = getLatestModifiedFolder(path);
        Log.info("Latest modified folder: " + latestFolder);

        path = path.resolve(latestFolder);

        ArrayList<ChooseAnswer> chooseAnswers = new ArrayList<>();
        ArrayList<ListenAnswer> listenAnswers = new ArrayList<>();
        ArrayList<ReproduceAnswer> reproduceAnswers = new ArrayList<>();


        // 获取每个content里面的content.json，并打印文件内部的json内容
        Arrays.stream(Objects.requireNonNull(path.toFile().listFiles()))
                .filter(file -> file.isDirectory() && file.getName().startsWith("content"))
                .forEach(file -> {
                    try {
                        String jsonContent = Files.readString(file.toPath().resolve("content.json"));
                        parseAndAddAnswer(jsonContent, chooseAnswers, listenAnswers, reproduceAnswers);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });

        Log.info("chooseAnswers: " + chooseAnswers);
        Log.info("listenAnswers: " + listenAnswers);
        Log.info("reproduceAnswers: " + reproduceAnswers);

        AtomicInteger index = new AtomicInteger();
        if (!chooseAnswers.isEmpty()) {
            chooseAnswers.forEach(chooseAnswer -> Log.info(index.incrementAndGet() + ": " + chooseAnswer.getAllAnswers()));
            Log.info("\n====================");
            Log.info("");
        }
        if (!listenAnswers.isEmpty()) {
            listenAnswers.forEach(listenAnswer -> Log.info(index.incrementAndGet() + ": " + listenAnswer.getTheBestAnswer(index)));
            Log.info("\n====================");
            Log.info("");
        }
        if (!reproduceAnswers.isEmpty()) {
            reproduceAnswers.forEach(reproduceAnswer -> Log.info(index.incrementAndGet() + ": " + reproduceAnswer.getTheBestAnswer()));
        }

        try {
            Path answerFiles = Path.of("./answer.txt");
            if (Files.exists(answerFiles)) {
                Log.warn("答案文件已存在，将覆盖原文件");
            }
            Files.writeString(answerFiles, generateAnswerContent(chooseAnswers, listenAnswers, reproduceAnswers));

            Desktop.getDesktop().open(answerFiles.toFile());
        } catch (IOException e) {
            throw new IOException("Failed to write answers to file", e);
        }
    }

    private static String generateAnswerContent(ArrayList<ChooseAnswer> chooseAnswers, ArrayList<ListenAnswer> listenAnswers, ArrayList<ReproduceAnswer> reproduceAnswers) {
        StringBuilder content = new StringBuilder();
        AtomicInteger index = new AtomicInteger();

        if (!chooseAnswers.isEmpty()) {
            chooseAnswers.forEach(chooseAnswer -> content.append(index.incrementAndGet()).append(": ").append(chooseAnswer.getAllAnswers()).append("\n\n"));
            content.append("\n====================\n\n");
        }
        if (!listenAnswers.isEmpty()) {
            listenAnswers.forEach(listenAnswer -> content.append(index.incrementAndGet()).append(": ").append(listenAnswer.getTheBestAnswer(index)).append("\n\n"));
            content.append("\n====================\n\n");
        }
        if (!reproduceAnswers.isEmpty()) {
            reproduceAnswers.forEach(reproduceAnswer -> content.append(index.incrementAndGet()).append(": ").append(reproduceAnswer.getTheBestAnswer()).append("\n\n"));
        }

        return content.toString();
    }



    private static void parseAndAddAnswer(String jsonContent, ArrayList<ChooseAnswer> chooseAnswers , ArrayList<ListenAnswer> listenAnswers, ArrayList<ReproduceAnswer> reproduceAnswers) {
        try {
            Log.info("jsonContent: " + jsonContent);
            ChooseAnswer chooseAnswer = ChooseAnswer.Companion.parse(jsonContent);
            chooseAnswers.add(chooseAnswer);
            return;
        } catch (Exception ignored) {
        }

        try {
            ListenAnswer listenAnswer = ListenAnswer.Companion.parse(jsonContent);
            listenAnswers.add(listenAnswer);
            return;
        } catch (Exception ignored) {
        }

        try {
            ReproduceAnswer reproduceAnswer = ReproduceAnswer.Companion.parse(jsonContent);
            reproduceAnswers.add(reproduceAnswer);
        } catch (Exception ignored) {
        }
    }
}
