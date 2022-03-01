package Selenium;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.sun.xml.internal.ws.policy.SimpleAssertion;

import Enums.UrlEnum.urls;
import io.cucumber.datatable.DataTable;
import junit.framework.Assert;

public class SeleniumUtils {

	WebDriver driver;

	private void createDriver(String browser) {
		if (browser.equalsIgnoreCase("chrome")) {
			System.setProperty("webdriver.chrome.driver", urls.CHROME_PATH.getLabel());
			driver = new ChromeDriver();
		} else if (browser.equalsIgnoreCase("firefox")) {
			System.setProperty("webdriver.gecko.driver", urls.FIREFOX_PATH.getLabel());
			driver = new FirefoxDriver();
		}
	}

	private void openBrowser(String url) {
		driver.get(url);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		// By element= By.xpath("//a[@title='']");
		// wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(element));
	}

	public void startup(String browser, String url) {
		createDriver(browser);
		openBrowser(url);
	}

	private String[] getIdentifierAndLocator(String expression) {
		return expression.split("#");
	}

	private WebElement getObject(String element) {

		String identifier = getIdentifierAndLocator(element)[0];
		String locator = getIdentifierAndLocator(element)[1];

		if (identifier.equalsIgnoreCase("xpath")) {
			return driver.findElement(By.xpath(locator));
		} else if (identifier.equalsIgnoreCase("id")) {
			return driver.findElement(By.id(locator));
		} else if (identifier.equalsIgnoreCase("tagName")) {
			return driver.findElement(By.tagName(locator));
		} else if (identifier.equalsIgnoreCase("css")) {
			return driver.findElement(By.cssSelector(locator));
		}

		return null;
	}

	private List<WebElement> getObjects(String expression) {

		String identifier = getIdentifierAndLocator(expression)[0];
		String locator = getIdentifierAndLocator(expression)[1];

		if (identifier.equalsIgnoreCase("xpath")) {
			return driver.findElements(By.xpath(locator));
		} else if (identifier.equalsIgnoreCase("id")) {
			return driver.findElements(By.id(locator));
		} else if (identifier.equalsIgnoreCase("tagName")) {
			return driver.findElements(By.tagName(locator));
		} else if (identifier.equalsIgnoreCase("css")) {
			return driver.findElements(By.cssSelector(locator));
		}
		return null;

	}

	public void click(String element) {
		WebElement object = getObject(element);
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].click();", object);
	}

	public void selectFromTypeAhead(String element, String sElement, String data) {
		WebElement object = getObject(element);
		object.sendKeys(data);
		List<WebElement> elements = getObjects(sElement);
		for (WebElement obj : elements) {
			if (obj.getText().equalsIgnoreCase(data)) {
				obj.click();
				break;
			}
		}
	}

	public void selectFromDropDown(String element, String sElement, String data) {

		WebElement object = getObject(element);
		object.click();
		List<WebElement> elements = getObjects(sElement);
		for (WebElement obj : elements) {
			if (obj.getText().trim().equalsIgnoreCase(data.trim())) {
				obj.click();
				break;
			}
		}

	}

	public void UpdateTable(String element, Integer row, DataTable data) {
		Map<String, String> map = data.asMaps().get(0);
		WebElement td = null;
		WebElement object = null;
		String xpath = "//tr[" + row + "]";
		int size = driver.findElements(By.xpath("//iframe")).size();
		try {
			driver.switchTo().frame(0);
			object = getObject(element).findElement(By.xpath(xpath));
		} catch (Exception e) {
			driver.switchTo().defaultContent();
			driver.switchTo().frame(1);
			object = getObject(element).findElement(By.xpath(xpath));
		}

		List<WebElement> elements = object.findElements(By.tagName("td"));
		WebElement name = object.findElement(By.tagName("th"));
		WebElement checkbox = elements.get(1);
		checkbox.click();
		for (int i = 2; i <= elements.size(); i++) {
			td = elements.get(i);
			for (String key : map.keySet()) {
				if (td.getAttribute("data-label").equals(key)) {
					td.findElement(By.tagName("Button")).click();
					td.findElement(By.tagName("input")).sendKeys(map.get(key));
					break;
				}

			}

		}

	}

	public void validateTableData(String element, DataTable data, Integer row) {
		String xpath = "//tr[" + row + "]";

		WebElement object = getObject(element).findElement(By.xpath(xpath));

		List<String> list = new ArrayList();

		String name = object.findElement(By.tagName("th")).getText();
		list.add(name);
		List<WebElement> elements = object.findElements(By.tagName("td"));
		for (int i = 0; i < elements.size(); i++) {
			if (elements.get(i).getText() != null) {
				list.add(elements.get(i).getText());
			}
		}
		List<String> actual = data.asList();

		Assert.assertEquals(list, actual);

	}

}
