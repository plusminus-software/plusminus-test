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

import lombok.experimental.Delegate;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebElement;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import software.plusminus.selenium.Element;
import software.plusminus.selenium.Findable;
import software.plusminus.selenium.Finder;
import software.plusminus.selenium.Selenium;
import software.plusminus.selenium.model.WebTestOptions;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public abstract class SeleniumTest implements Findable {

    private static Selenium staticSelenium;
    
    @LocalServerPort
    private int port;
    @Delegate
    private Selenium selenium;
    

    @BeforeClass
    public static void setUpClass() {
        staticSelenium = new Selenium();
    }

    @AfterClass
    public static void tearDownClass() {
        if (staticSelenium.driver() != null) {
            staticSelenium.closeBrowser();
        }
        staticSelenium = null;
    }

    @Before
    public void setUp() {
        if (selenium == null) {
            selenium = staticSelenium;
            openBrowser(options());
        }
        loadPage(options(), url());
    }

    @After
    public void tearDown() {
        checkErrorsInLogs();
    }

    @Override
    public Finder find() {
        return new Finder(selenium);
    }
    
    public Element wrap(WebElement canonical) {
        return Element.of(canonical, selenium);
    }

    protected WebTestOptions options() {
        return new WebTestOptions()
                .port(port);
    }

    protected abstract String url();

}
