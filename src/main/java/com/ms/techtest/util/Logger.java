package com.ms.techtest.util;

import java.io.Console;

public class Logger {
	
	private Logger() {}

	public static final void log(String format, Object ...args) {
		Console console = System.console();
		if (console != null){
			console.printf(format, args);
		}
		else {
			System.out.printf(format, args);
		}
	}
}
