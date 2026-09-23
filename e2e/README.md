# Playwright E2E

## Setup

```sh
npm install
npx playwright install chromium
```

Start the Spring Boot application on `http://localhost:8080`, then run the
sample test with credentials for an existing application user:

```sh
E2E_USERNAME=your-user-name E2E_PASSWORD=your-password npm test
```

Set `TARGET_PAGE_PATH` to capture a different page. The default is `/top`.
The screenshot is written to `screenshots/target-page.png`.

To verify test discovery without starting the application:

```sh
npm test -- --list
```
