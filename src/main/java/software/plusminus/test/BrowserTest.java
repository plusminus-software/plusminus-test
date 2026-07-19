/*
 * Copyright 2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package software.plusminus.test;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import software.plusminus.authentication.service.token.HttpTokenContext;
import software.plusminus.browser.Browser;
import software.plusminus.browser.BrowserCookies;
import software.plusminus.browser.BrowserSettings;
import software.plusminus.browser.Find;
import software.plusminus.browser.Finder;
import software.plusminus.browser.Page;
import software.plusminus.security.Security;
import software.plusminus.test.annotation.TestBrowser;
import software.plusminus.util.AnnotationUtils;

import java.util.Arrays;
import java.util.HashSet;

@Slf4j
public abstract class BrowserTest extends IntegrationTest implements Finder {

    private static Browser browser;

    @Override
    @Before
    @BeforeEach
    @SuppressFBWarnings(value = "ST_WRITE_TO_STATIC_FROM_INSTANCE_METHOD",
            justification = "One browser is shared per class and closed in @AfterClass; "
                    + "JUnit runs the class lifecycle single-threaded.")
    public void beforeEach() {
        if (browser == null) {
            browser = Browser.create(settings());
        }
        browser.go(url());
    }

    @Override
    @After
    @AfterEach
    public void afterEach() {
        super.afterEach();
        if (browser != null) {
            browser.cookies().clear();
        }
    }

    @AfterClass
    @AfterAll
    public static void closeBrowser() {
        Browser current = browser;
        browser = null;
        if (current != null) {
            current.close();
        }
    }

    @Override
    public Find find() {
        return browser.currentPage().find();
    }

    public Browser browser() {
        return browser;
    }

    public void login(String username, String... roles) {
        login(Security.builder()
                .username(username)
                .roles(new HashSet<>(Arrays.asList(roles)))
                .build());
    }

    public void login(Security security) {
        if (security().canGenerateToken()) {
            BrowserCookies cookies = browser.cookies();
            cookies.add(HttpTokenContext.COOKIE_NAME, security().generateToken(security));
        } else {
            security().login(security);
        }
    }

    protected Page go(String page) {
        return browser.go(page);
    }

    protected BrowserSettings settings() {
        BrowserSettings settings = BrowserSettings.build()
                .port(web().port());
        TestBrowser testBrowser = AnnotationUtils.findAnnotation(TestBrowser.class, this);
        if (testBrowser != null) {
            settings.headless(testBrowser.headless());
        }
        return settings;
    }

    protected abstract String url();
}
