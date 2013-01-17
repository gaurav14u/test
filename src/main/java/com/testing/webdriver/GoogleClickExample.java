package com.testing.webdriver;

import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class GoogleClickExample {
	private WebDriver driver;
	private String baseUrl = "http://www.google.com";
	private StringBuffer verificationErrors = new StringBuffer();
	Properties testParams;
	String browser;
	String selenium_hub_url = null;

	@BeforeClass
	public void setUpTestFF() throws Exception {
		driver = new ChromeDriver();
	}

	@Test
	public void testGoogleTest() throws Exception {

		WebElement element = driver.findElement(By.name("q"));
		element.sendKeys("Test!");
		element.submit();
		System.out.println("Page title is: " + driver.getTitle());
		(new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver d) {
				return d.getTitle().toLowerCase().startsWith("Test!");
			}
		});

		System.out.println("Page title is: " + driver.getTitle());
	}

	 @AfterClass
	public void tearDown() throws Exception {
		driver.quit();
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			// fail(verificationErrorString);
		}
	}

	
}