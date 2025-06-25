/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;
import java.util.*;

public class Timers {
    private static final int MAX_LOGIN_ATTEMPTS = 3;
    private static final long BLOCK_DURATION_MINUTES = 5;
    private static final Map<String, Integer> LOGIN_ATTEMPTS = new HashMap<>();
    private static final Map<String, LocalDateTime> BLOCKED_USERS = new HashMap<>();
    private static java.util.Timer countdownTimer = new java.util.Timer();
    public static boolean countdownActive = false;

    public static void cleanup() {
        countdownTimer.cancel();
    }

    public boolean isUserBlocked(String username, boolean valid) {
        Colors color = new Colors();
        if (BLOCKED_USERS.containsKey(username)) {
            LocalDateTime blockTime = BLOCKED_USERS.get(username);
            LocalDateTime now = LocalDateTime.now();
            long secondsBlocked = ChronoUnit.SECONDS.between(blockTime, now);
            long totalBlockSeconds = BLOCK_DURATION_MINUTES * 60;
            if (valid) {
                BLOCKED_USERS.remove(username);
                LOGIN_ATTEMPTS.remove(username);
                return true;
            }
            else {
                if (secondsBlocked < totalBlockSeconds) {
                    return true;
                } else {
                    BLOCKED_USERS.remove(username);
                    LOGIN_ATTEMPTS.remove(username);
                }
            }
        }
        return false;
    }

    public static void startCountdownTimer(String username, long remainingSeconds) {
        Colors color = new Colors();
        if (countdownTimer != null) {
            countdownTimer.cancel();
        }
        countdownTimer = new Timer();
        countdownActive = true;
        TimerTask countdownTask = new TimerTask() {
            long secondsLeft = remainingSeconds;
            @Override
            public void run() {
                if (!countdownActive) {
                    this.cancel();
                    return;
                }
                if (secondsLeft <= 0) {
                    System.out.println("\n" + color.GREEN + "Account has been unblocked. Press [Enter] to return to main menu..." + color.RESET);
                    BLOCKED_USERS.remove(username);
                    LOGIN_ATTEMPTS.remove(username);
                    this.cancel();
                    return;
                }
                long minutes = secondsLeft / 60;
                long seconds = secondsLeft % 60;
                System.out.print(color.BLUE);
                System.out.printf("\rAccount is blocked. Time remaining: %02d minutes %02d seconds...", minutes, seconds);
                System.out.print(color.RESET);
                secondsLeft--;
            }
        };
        countdownTimer.scheduleAtFixedRate(countdownTask, 0, 1000);
    }

    public long getBlockRemaining(String username) {
        if (BLOCKED_USERS.containsKey(username)) {
            LocalDateTime blockTime = BLOCKED_USERS.get(username);
            long secondsPassed = ChronoUnit.SECONDS.between(blockTime, LocalDateTime.now());
            long totalBlockSeconds = BLOCK_DURATION_MINUTES * 60;
            return Math.max(0, totalBlockSeconds - secondsPassed);
        }
        return 0;
    }

    public boolean handleFailedLogin(String username) {
        Colors color = new Colors();
        int attempts = LOGIN_ATTEMPTS.getOrDefault(username, 0) + 1;
        LOGIN_ATTEMPTS.put(username, attempts);
        if (attempts >= MAX_LOGIN_ATTEMPTS) {
            BLOCKED_USERS.put(username, LocalDateTime.now());
            System.out.println(color.RED + "Too many failed attempts. Account blocked for " + BLOCK_DURATION_MINUTES + " minutes." + color.RESET);
            return true;
        } else {
            int remaining = MAX_LOGIN_ATTEMPTS - attempts;
            System.out.println(color.RED + "Invalid password. Attempts remaining: " + remaining + color.RESET);
            return false;
        }
    }
}

