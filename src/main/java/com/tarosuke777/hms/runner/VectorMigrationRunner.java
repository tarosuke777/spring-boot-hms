package com.tarosuke777.hms.runner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.tarosuke777.hms.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class VectorMigrationRunner implements CommandLineRunner {

    private final BookService bookService;

    @Override
    public void run(String... args) throws Exception {
        // コマンドライン引数に "--migrate" が含まれている時だけ実行する
        boolean shouldMigrate = false;
        for (String arg : args) {
            if ("--migrate".equals(arg)) {
                shouldMigrate = true;
                break;
            }
        }

        if (shouldMigrate) {
            log.info(">>> ベクトルデータの移行を開始します...");
            try {
                bookService.migrateAllBooksToVectorStore();
                log.info(">>> 移行が正常に完了しました。");
            } catch (Exception e) {
                log.error(">>> 移行中にエラーが発生しました: ", e);
            }
        }
    }
}
