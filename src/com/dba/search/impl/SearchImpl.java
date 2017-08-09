package com.dba.search.impl;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.dba.constants.CONSTAINTS;
import com.dba.data.Customer;
import com.dba.search.Search;
import com.dba.util.DriverHelper;
import com.dba.util.ExecuteLOG;

/**
 * Search function
 * 
 * @author LEO Step1 found all the related customer URLs with search engine
 *         Step2 for each URL of customer, enter the URL and found all the mail
 *         list
 */
public class SearchImpl implements Search {

	private String searchContent;
	private String[] countries;
	private String searchEngine;
	private int page;
	private WebDriver driver;

	public SearchImpl() {
		ExecuteLOG.info("Search process initilize ... ");
		this.searchContent = CONSTAINTS.searchContent;
		this.countries = CONSTAINTS.countries;
		this.searchEngine = CONSTAINTS.searchEngine;
		this.page = CONSTAINTS.page;
		this.driver = CONSTAINTS.driver;
	}

	@Override
	public List<String> getCustomersURLs() {
		ExecuteLOG.info("Get URLs ...");

		List<String> urlList = new ArrayList<String>();

		for (String country : countries) {
			try {
				// searching
				driver.get(searchEngine);
				Thread.sleep(3000);

				String inputXpath = "//input[@name='q']";
				DriverHelper.waitForElementPresent(By.xpath(inputXpath), driver);

				WebElement element = driver.findElement(By.xpath(inputXpath));
				element.sendKeys(searchContent + " " + country);

				DriverHelper.sendEnter(By.xpath(inputXpath), driver);

				/*
				 * DriverHelper.waitAndClick(driver,
				 * By.xpath(".//button[@name='btnG']")); Thread.sleep(3000);
				 */

				// scan searching results per page
				String currentUrl_org = driver.getCurrentUrl();

				for (int i = 0; i < page; i++) {
					String currentUrl = currentUrl_org;

					if (i > 0) {
						currentUrl = currentUrl_org + "&start=" + (i * 10);
						// LOG.info("Current URL: " + currentUrl);
					} else {
						// LOG.info("Current URL: " + currentUrl);
					}

					driver.get(currentUrl);
					Thread.sleep(3000);
					// get LINKs
					List<WebElement> list = driver
							.findElements(By.xpath("//*[@id='vs0p1c0']|//*[@id='rso']//*/h3[@class='r']/a"));

					ExecuteLOG.info("Page " + (i + 1) + ":\t has found " + list.size() + " records");

					for (WebElement welement : list) {
						String site = welement.getAttribute("href");
						// LOG.debug(welement.getText() + "-->" + site);

						urlList.add(site);
					}
					Thread.sleep(3000);

				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				ExecuteLOG.error(e.getClass().toString(), e.getMessage());
			}

		}

		ExecuteLOG.info("Number of URLs is " + urlList.size());

		return urlList;

	}

	@Override
	public List<Customer> getCustomers(List<String> urls) {

		ExecuteLOG.info("Get customers ... ");
		List<Customer> customers = new ArrayList<Customer>();

		for (int i = 0; i < urls.size(); i++) {
			ExecuteLOG.info("------------------" + i + "--------------------");
			String url = urls.get(i);
			ExecuteLOG.info(url);
			
			System.out.println("Before get:" + Thread.currentThread().getName());
			customers.addAll(getCustomerBasedonURL(url));
		}
		return customers;

	}
	
	private List<Customer> getCustomerBasedonURL(String url){
		List<Customer> customers = new ArrayList<Customer>();
		
		driver.get(url);
		System.out.println("getCustomer: " + Thread.currentThread().getName());
		/*
		try {
			// check in the home page whether there is some Email information
			String emailXpath = "//*[contains(text(),'Email')]/*"
								+ "|//*[contains(text(),'email')]/*"
								+ "|//*[contains(text(),'E-mail')]/*"
								+ "|//*[contains(text(),'E-Mail')]/*"
								+ "|//*[contains(text(),'e-mail')]/*"
								+ "|//*[contains(.,'Email')]/a"
								+ "|//*[contains(.,'email')]/a"
								+ "|//*[contains(.,'E-mail')]/a"
								+ "|//*[contains(.,'E-Mail')]/a"
								+ "|//*[contains(.,'e-mail')]/a";

			List<WebElement> welist = driver.findElements(By.xpath(emailXpath));
			
			// if not find whether have contact
			if ((welist == null) ||(welist.size() == 0)) { 
				List<WebElement> temptList = new ArrayList<WebElement>();
				String contactAboutXpath = "//*[contains(text(),'contact')]"
										+ "|//*[contains(text(),'Contact')]"
										+ "|//*[contains(text(),'About')]";
				 List<WebElement> welistContacts = driver.findElements(By.xpath(contactAboutXpath));
				
				 
				 if (welistContacts.size() > 0) { 
					 for (int j = 0; j < welistContacts.size(); j++) {
						 try { 
							 welistContacts.get(j).click();
							 Thread.sleep(3000);
							// in the content page check the Email information
							 List<WebElement> welistContact = driver.findElements(By.xpath(emailXpath));
							 temptList.addAll(welistContact);
						 } catch (Exception e) {
							 continue; 
						 }
					 }
					 welist = temptList;
				 }
			}
			
			if (welist.size() == 0) 
				Result.info("XXXXXX\t" + url);
			
			for (int j = 0; j < welist.size(); j++) {
				String mailbox = welist.get(j).getAttribute("href");
				Result.info("YYYYYY\t" + mailbox);
				Customer c = new Customer();
				c.setCustomerName("j" + j);
				c.setMailbox(mailbox);
				customers.add(c);
			}
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			ExecuteLOG.warn("Thread sleeping error.");
		}
		*/
		return customers;
	}
}
