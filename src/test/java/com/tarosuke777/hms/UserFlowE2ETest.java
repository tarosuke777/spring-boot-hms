package com.tarosuke777.hms;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

// @Disabled("Jenkins上でPlaywrightのブラウザが起動できないため、E2Eテストを無効化")
@Sql
@Transactional
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserFlowE2ETest {

    @LocalServerPort
    private int port;

    private static Playwright playwright;
    private Browser browser;
    private Page page;

    @BeforeAll
    static void launchBrowser() {
        playwright = Playwright.create();
    }

    @AfterAll
    static void closePlaywright() {
        playwright.close();
    }

    @BeforeEach
    void createContext() {
        // HEADLESS=false にすると、実際にブラウザが起動する様子を目視できます
        System.out.println("====== START: ENVIRONMENT VARIABLES ======");
        java.util.Map<String, String> env = System.getenv();
        env.forEach((key, value) -> {
            System.out.println(key + " = " + value);
        });
        System.out.println("====== END: ENVIRONMENT VARIABLES ======");

        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
        page = browser.newPage();
    }

    @AfterEach
    void closeContext() {
        browser.close();
    }

    @Test
    void testLoginSuccess() {
        // 起動したSpringBootのURLへ遷移
        String baseUrl = "http://localhost:" + port + "/hms";
        page.navigate(baseUrl + "/login");

        // Thymeleafの入力フォーム（name属性やid属性で指定）に値を入力
        page.fill("input[name='userName']", "admin");
        page.fill("input[name='password']", "password");

        // ログインボタンをクリック（ボタンのテキストやセレクタで指定）
        page.click("input[type='submit']");

        // 遷移後のURLや画面要素を検証
        assertEquals(baseUrl + "/music/list", page.url());
        assertEquals("Music", page.textContent(".h2"));
    }
}
