import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;

import java.sql.Connection
import java.sql.ResultSetMetaData

import groovy.json.JsonBuilder

import org.apache.poi.ss.usermodel.*

import com.katalon.helpers.database.DatabaseHelper

// Load the Excel file
FileInputStream fis = new FileInputStream(new File("query_results.xlsx"))
Workbook workbook = WorkbookFactory.create(fis)
Sheet sheet = workbook.getSheetAt(0)

List<Map<String, String>> rows = []
List<String> headers = new ArrayList<>()
boolean headerRow = true

sheet.each { Row row ->
    if (headerRow) {
        row.each { Cell cell ->
            headers.add(cell.toString())
        }
        headerRow = false
    } else {
        Map<String, String> rowData = [:]
        row.eachWithIndex { Cell cell, int idx ->
            rowData[headers[idx]] = cell.toString()
        }
        rows.add(rowData)
    }
}

def expectedJson = new JsonBuilder(rows)

println(expectedJson.toPrettyString())

String query = """
	-- This query shows a list of the daily top Google Search terms.
	SELECT
	   refresh_date AS Day,
	   term AS Top_Term,
		   -- These search terms are in the top 25 in the US each day.
	FROM `bigquery-public-data.google_trends.top_terms`
	WHERE
	   rank = 1
		   -- Choose only the top term each day.
	   AND refresh_date >= DATE_SUB(CURRENT_DATE(), INTERVAL 2 WEEK)
		   -- Filter to the last 2 weeks.
	GROUP BY Day, Top_Term, rank
	ORDER BY Day DESC
	   -- Show the days in reverse chronological order.""".strip()

Connection conn = DatabaseHelper.getGlobalConnection()

def list = []
conn.createStatement().withCloseable { statement ->
	statement.executeQuery(query).withCloseable { resultSet ->
		ResultSetMetaData metaData = resultSet.metaData
		int columnCount = metaData.columnCount

		while (resultSet.next()) {
			def row = [:]
			(1..columnCount).each { col ->
				row[metaData.getColumnName(col)] = resultSet.getObject(col)
			}
			list << row
		}
	}
}

def queryResultsJson = new JsonBuilder(list)

println(queryResultsJson.toPrettyString())

assertThatJson(queryResultsJson.toString()).isEqualTo(expectedJson.toString())