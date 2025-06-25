/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import org.json.JSONArray;
import org.json.JSONObject;

public class AIChat {

    static Colors color = new Colors();
    private static Map<String, String> qaMap = new HashMap<>();

    private static final Set<String> stopwords = new HashSet<>(Arrays.asList(
            "what", "is", "the", "a", "an", "of", "in", "on", "how", "do", "i", "to", "for", "with", "and", "you", "your"
    ));

    public static String formatAnswer(String answer) {
        int maxLineLength = 100;
        StringBuilder formattedAnswer = new StringBuilder();
        String[] words = answer.split(" ");
        int currentLineLength = 0;
        for (String word : words) {
            if (currentLineLength + word.length() + 1 > maxLineLength) {
                formattedAnswer.append("\n");
                currentLineLength = 0;
            }
            if (currentLineLength > 0) {
                formattedAnswer.append(" ");
            }
            formattedAnswer.append(word);
            currentLineLength += word.length() + 1;
        }
        return formattedAnswer.toString();
    }

    public static void loadQADatabase() {
        try (BufferedReader br = new BufferedReader(new FileReader("qa_pairs.txt"))) {
            String line, question = null;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("Q:")) {
                    question = line.substring(2).trim().toLowerCase();
                } else if (line.startsWith("A:") && question != null) {
                    String answer = line.substring(2).trim();
                    qaMap.put(question, answer);
                    question = null;
                }
            }
        } catch (IOException e) {
            System.out.println(color.RED + "Error loading Q&A database: " + e.getMessage() + color.RESET);
        }
    }

    public static int levenshteinDistance(String a, String b) {
        int[][] dp = new int[a.length() + 1][b.length() + 1];
        for (int i = 0; i <= a.length(); i++) {
            for (int j = 0; j <= b.length(); j++) {
                if (i == 0) dp[i][j] = j;
                else if (j == 0) dp[i][j] = i;
                else if (a.charAt(i - 1) == b.charAt(j - 1)) dp[i][j] = dp[i - 1][j - 1];
                else dp[i][j] = 1 + Math.min(dp[i - 1][j - 1], Math.min(dp[i - 1][j], dp[i][j - 1]));
            }
        }
        return dp[a.length()][b.length()];
    }

    public static String extractKeyword(String lastAnswer) {
        String[] tokens = lastAnswer.split("\\s+");
        StringBuilder keyword = new StringBuilder();
        for (String word : tokens) {
            if (Character.isUpperCase(word.charAt(0))) {
                keyword.append(word).append(" ");
            } else if (keyword.length() > 0) {
                break;
            }
        }
        return keyword.toString().trim();
    }

    public static String findClosestAnswer(String userQuestion, String lastAnswerContext) {
        if (lastAnswerContext != null && !lastAnswerContext.isEmpty()) {
            String keyword = extractKeyword(lastAnswerContext);
            if (!keyword.isEmpty() && !userQuestion.toLowerCase().contains(keyword.toLowerCase())) {
                userQuestion = keyword + " " + userQuestion;
            }
        }
        userQuestion = userQuestion.replaceAll("[^a-zA-Z0-9\\s]", "").toLowerCase().trim();
        int minDistance = Integer.MAX_VALUE;
        String bestMatch = null;
        for (String question : qaMap.keySet()) {
            int distance = levenshteinDistance(userQuestion, question);
            if (distance < minDistance) {
                minDistance = distance;
                bestMatch = question;
            }
        }
        double threshold = 0.4;
        if (bestMatch != null && minDistance <= userQuestion.length() * threshold) {
            String answer = qaMap.get(bestMatch);
            return formatAnswer(answer);
        }
        String[] userWordsRaw = userQuestion.split("\\s+");
        List<String> userWords = new ArrayList<>();
        for (String word : userWordsRaw) {
            if (!stopwords.contains(word)) {
                userWords.add(word);
            }
        }
        int maxMatch = 0;
        String keywordBestMatch = null;
        for (Map.Entry<String, String> entry : qaMap.entrySet()) {
            String[] qaWordsRaw = entry.getKey().split("\\s+");
            List<String> qaWords = new ArrayList<>();
            for (String word : qaWordsRaw) {
                if (!stopwords.contains(word)) {
                    qaWords.add(word);
                }
            }
            int matchCount = 0;
            for (String userWord : userWords) {
                for (String qaWord : qaWords) {
                    if (userWord.equalsIgnoreCase(qaWord)) {
                        matchCount++;
                        break;
                    }
                }
            }
            double matchRatio = (double) matchCount / Math.max(userWords.size(), qaWords.size());
            if (matchRatio >= 0.5 && matchCount > maxMatch) {
                maxMatch = matchCount;
                keywordBestMatch = entry.getKey();
            }
        }
        if (keywordBestMatch != null) {
            String answer = qaMap.get(keywordBestMatch);
            return formatAnswer(answer);
        }
        return null;
    }

    public static String getAnswerFromGemini(String message, String lastAskContext, String lastAnswerContext) {
        try {
            String fullPrompt;
            if (lastAnswerContext != null && !lastAnswerContext.isEmpty()) {
                fullPrompt = lastAnswerContext != null && !lastAnswerContext.isEmpty()
                        ? "Answer based on this previous Q&A.\nPrevious Question: " + lastAskContext +
                        "\nPrevious Answer: " + lastAnswerContext + "\nNew Question: " + message
                        : message;
            } else {
                fullPrompt = message;
            }

            String apiKey = "AIzaSyDLm4yXkRPGBHidAhU-zs29Wx3njrR6z50";
            String model = "gemini-1.5-pro-001";
            String endpoint = "https://generativelanguage.googleapis.com/v1/models/" + model + ":generateContent?key=" + apiKey;

            URL url = new URL(endpoint);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);
            con.setConnectTimeout(10000);
            con.setReadTimeout(15000);

            String jsonInput = "{\n" +
                    "  \"contents\": [\n" +
                    "    {\n" +
                    "      \"parts\": [\n" +
                    "        {\"text\": \"" + fullPrompt.replace("\"", "\\\"") + "\"}\n" +
                    "      ]\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}";

            try (OutputStream os = con.getOutputStream()) {
                byte[] input = jsonInput.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int status = con.getResponseCode();
            InputStream responseStream = (status == 200) ? con.getInputStream() : con.getErrorStream();

            StringBuilder response = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(responseStream, "utf-8"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line.trim());
                }
            }

            if (status != 200) {
                System.out.println(color.RED + "Gemini API Error: " + response.toString() + color.RESET);
                return null;
            }

            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONArray candidates = jsonResponse.getJSONArray("candidates");

            if (candidates.length() > 0) {
                JSONObject firstCandidate = candidates.getJSONObject(0);
                JSONArray parts = firstCandidate.getJSONObject("content").getJSONArray("parts");
                if (parts.length() > 0) {
                    String aiText = parts.getJSONObject(0).getString("text");
                    System.out.println(color.PURPLE + "AI Answer: " + aiText + color.RESET);
                    return aiText;
                }
            }

            System.out.println(color.RED + "No valid response from Gemini API." + color.RESET);
            return null;

        } catch (Exception e) {
            System.out.println(color.RED + "Error calling Gemini: " + e.getMessage() + color.RESET);
            return null;
        }
    }
}