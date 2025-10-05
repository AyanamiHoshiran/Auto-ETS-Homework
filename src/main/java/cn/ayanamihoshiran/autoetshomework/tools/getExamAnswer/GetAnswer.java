package cn.ayanamihoshiran.autoetshomework.tools.getExamAnswer;

import cn.ayanamihoshiran.autoetshomework.entity.ChooseAnswer;
import cn.ayanamihoshiran.autoetshomework.entity.FillInAnswer;
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

public class GetAnswer {
    public static void use(Path path) throws Exception{

        ArrayList<ChooseAnswer> chooseAnswers = new ArrayList<>();
        ArrayList<ListenAnswer> listenAnswers = new ArrayList<>();
        ArrayList<ReproduceAnswer> reproduceAnswers = new ArrayList<>();
        ArrayList<FillInAnswer> fillInAnswers = new ArrayList<>();


        // 获取每个content里面的content.json，并打印文件内部的json内容
        Arrays.stream(Objects.requireNonNull(path.toFile().listFiles()))
                .filter(file -> file.isDirectory() && file.getName().startsWith("content"))
                .forEach(file -> {
                    try {
                        String jsonContent = Files.readString(file.toPath().resolve("content.json"));
                        parseAndAddAnswer(jsonContent, chooseAnswers, listenAnswers, reproduceAnswers, fillInAnswers);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });

        Log.info("chooseAnswers: " + chooseAnswers);
        Log.info("listenAnswers: " + listenAnswers);
        Log.info("reproduceAnswers: " + reproduceAnswers);
        Log.info("fillInAnswers: " + fillInAnswers);

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
            Log.info("\n====================");
            Log.info("");
        }
        if (!fillInAnswers.isEmpty()) {
            fillInAnswers.forEach(fillInAnswer -> Log.info(index.incrementAndGet() + ": " + fillInAnswer.getAllAnswers()));
        }

        try {
            Path answerFiles = Path.of("./answer_" + path.getFileName() + ".txt");
            if (Files.exists(answerFiles)) {
                Log.warn("答案文件已存在，将覆盖原文件");
            }
            Files.writeString(answerFiles, generateAnswerContent(chooseAnswers, listenAnswers, reproduceAnswers, fillInAnswers));

            Desktop.getDesktop().open(answerFiles.toFile());
        } catch (IOException e) {
            throw new IOException("Failed to write answers to file", e);
        }
    }

    private static String generateAnswerContent(ArrayList<ChooseAnswer> chooseAnswers, ArrayList<ListenAnswer> listenAnswers, ArrayList<ReproduceAnswer> reproduceAnswers, ArrayList<FillInAnswer> fillInAnswers) {
        StringBuilder content = new StringBuilder();
        AtomicInteger index = new AtomicInteger();

        if (!chooseAnswers.isEmpty()) {
            content.append("听后选择: \n\n");
            chooseAnswers.forEach(chooseAnswer -> content.append(index.incrementAndGet()).append(": ").append(chooseAnswer.getAllAnswers()).append("\n\n"));
            content.append("====================\n");
        }
        if (!listenAnswers.isEmpty()) {
            content.append("听后回答: \n\n");
            listenAnswers.forEach(listenAnswer -> content.append(index.incrementAndGet()).append(": ").append(listenAnswer.getTheBestAnswer(index)).append("\n\n"));
            content.append("====================\n");
        }
        if (!fillInAnswers.isEmpty()) {
            content.append("听后填空: \n\n");
            fillInAnswers.forEach(fillInAnswer -> content.append(index.incrementAndGet()).append(": ").append(fillInAnswer.getAllAnswers()).append("\n\n"));
            content.append("====================\n");
        }
        if (!reproduceAnswers.isEmpty()) {
            content.append("听后转述: \n\n");
            reproduceAnswers.forEach(reproduceAnswer -> content.append(index.incrementAndGet()).append(": ").append(reproduceAnswer.getTheBestAnswer()).append("\n\n"));
        }

        return content.toString();
    }



    private static void parseAndAddAnswer(String jsonContent, ArrayList<ChooseAnswer> chooseAnswers, ArrayList<ListenAnswer> listenAnswers, ArrayList<ReproduceAnswer> reproduceAnswers, ArrayList<FillInAnswer> fillInAnswers) {
        jsonContent = jsonContent.replace("nbsp;", "");
        Log.info("jsonContent: " + jsonContent);

        Object answer;
        try {
            answer = ChooseAnswer.Companion.parse(jsonContent);
            if ("collector.choose".equals(((ChooseAnswer) answer).getStructure_type())) {
                chooseAnswers.add((ChooseAnswer) answer);
                return;
            }
        } catch (Exception ignored) {}

        try {
            answer = ListenAnswer.Companion.parse(jsonContent);
            if ("collector.listen".equals(((ListenAnswer) answer).getStructure_type())) {
                listenAnswers.add((ListenAnswer) answer);
                return;
            }
        } catch (Exception ignored) {}

        try {
            answer = FillInAnswer.Companion.parse(jsonContent);
            if ("collector.fill".equals(((FillInAnswer) answer).getStructure_type())) {
                fillInAnswers.add((FillInAnswer) answer);
                return;
            }
        } catch (Exception ignored) {}

        try {
            answer = ReproduceAnswer.Companion.parse(jsonContent);
            if ("collector.picture".equals(((ReproduceAnswer) answer).getStructure_type())) {
                reproduceAnswers.add((ReproduceAnswer) answer);
            }
        } catch (Exception ignored) {}
    }
}
