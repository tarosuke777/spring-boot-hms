package com.tarosuke777.hms;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/com/tarosuke777/hms/UserFlowE2ETest.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
        config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
@Sql(scripts = "/com/tarosuke777/hms/UserFlowE2ETest_cleanup.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
        config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
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
