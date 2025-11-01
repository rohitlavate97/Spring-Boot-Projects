package com.alchemist.scheduler;

import java.time.LocalTime;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TaskSchedulerService {

	// 1️⃣ Every minute
	@Scheduled(cron = "0 * * * * ?")
	public void runEveryMinute() {
		System.out.println("⏱ Every minute: " + LocalTime.now());
	}

	// 2️⃣ Every hour
	@Scheduled(cron = "0 0 * * * ?")
	public void runEveryHour() {
		System.out.println("🕐 Every hour: " + LocalTime.now());
	}

	// 3️⃣ Every day at noon
	@Scheduled(cron = "0 0 12 * * ?")
	public void runAtNoon() {
		System.out.println("🌞 Noon task: " + LocalTime.now());
	}

	// 4️⃣ Every day between 8 AM and 6 PM, every hour
	@Scheduled(cron = "0 0 8-18 * * ?")
	public void runBetween8and6() {
		System.out.println("💼 Working hours task: " + LocalTime.now());
	}

	// 5️⃣ 1st day of every month at 1 AM
	@Scheduled(cron = "0 0 1 1 * ?")
	public void runOnFirstDayOfMonth() {
		System.out.println("📅 First day of month task: " + LocalTime.now());
	}

	// 🔥 Manual trigger method
	public void runManualTask() {
		System.out.println("🚀 Manual trigger executed at: " + LocalTime.now());
	}
}