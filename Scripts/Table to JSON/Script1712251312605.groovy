import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson

import org.openqa.selenium.By
import org.openqa.selenium.WebElement

import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import groovy.json.JsonOutput

WebUI.openBrowser('')

WebUI.navigateToUrl('https://www.w3schools.com/html/html_tables.asp')

WebElement tableElement = WebUI.findWebElement(findTestObject('Object Repository/Page_HTML Tables/Main Table'), 0)

String expectedJson = """
[
{
	"Company": "Alfreds Futterkiste",
	"Contact": "Alfred Anders",
	"Country": "Germany"
},
{
	"Company": "Centro comercial Moctezuma",
	"Contact": "Francisco Chang",
	"Country": "Mexico"
},
{
	"Company": "Ernst Handel",
	"Contact": "Roland Mendel",
	"Country": "Austria"
},
{
	"Company": "Island Trading",
	"Contact": "Helen Bennett",
	"Country": "UK"
},
{
	"Company": "Laughing Bacchus Winecellars",
	"Contact": "Yoshi Tannamuri",
	"Country": "Canada"
},
{
	"Company": "Magazzini Alimentari Riuniti",
	"Contact": "Giovanni Rovelli",
	"Country": "Italy"
}
]
"""

// Extract table headers
List<String> headers = tableElement.findElements(By.tagName("th")).collect { it.text }

// Extract table rows data
List<Map<String, String>> tableData = []
tableElement.findElements(By.tagName("tr")).each { row ->
	// Temporarily store row data
	Map<String, String> rowData = [:]
	
	row.findElements(By.tagName("td")).eachWithIndex { cell, index ->
		rowData[headers[index]] = cell.text
	}
	
	// Add row data to table data list, if it's not empty
	if (!rowData.isEmpty()) {
		tableData << rowData
	}
}

WebUI.closeBrowser()

// Convert table data to JSON
String json = JsonOutput.toJson(tableData)

assertThatJson(json).isEqualTo(expectedJson)


