package org.makarovevg.mysbs.mysbs_app;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class MySbsAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(MySbsAppApplication.class, args);
        openBrowser();
    }

    private static void openBrowser() {

        String url = "http://localhost:8080";
        try {
            Runtime.getRuntime().exec("cmd /c start chrome " + url);
        } catch (Exception e) {
            System.err.println("Ошибка открытия URL адреса");
        }
        System.out.println("\nCервер успешно запущен! Если браузер не был запущен автоматически, тогда:" +
                           "\nПЕРЕЙДИТЕ ПО ДАННОМУ АДРЕСУ: http://localhost:8080\n");
    }
}
