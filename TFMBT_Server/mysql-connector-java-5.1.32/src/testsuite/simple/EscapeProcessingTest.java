/*
  Copyright (c) 2002, 2014, Oracle and/or its affiliates. All rights reserved.

  The MySQL Connector/J is licensed under the terms of the GPLv2
  <http://www.gnu.org/licenses/old-licenses/gpl-2.0.html>, like most MySQL Connectors.
  There are special exceptions to the terms and conditions of the GPLv2 as it is applied to
  this software, see the FLOSS License Exception
  <http://www.mysql.com/about/legal/licensing/foss-exception.html>.

  This program is free software; you can redistribute it and/or modify it under the terms
  of the GNU General Public License as published by the Free Software Foundation; version 2
  of the License.

  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
  without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
  See the GNU General Public License for more details.

  You should have received a copy of the GNU General Public License along with this
  program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth
  Floor, Boston, MA 02110-1301  USA

 */

package testsuite.simple;

import java.sql.Connection;
import java.util.Properties;
import java.util.TimeZone;

import testsuite.BaseTestCase;

/**
 * Tests escape processing
 * 
 * @author Mark Matthews
 */
public class EscapeProcessingTest extends BaseTestCase {
	// ~ Constructors
	// -----------------------------------------------------------

	/**
	 * Constructor for EscapeProcessingTest.
	 * 
	 * @param name
	 *            the test to run
	 */
	public EscapeProcessingTest(String name) {
		super(name);
	}

	// ~ Methods
	// ----------------------------------------------------------------

	/**
	 * Tests the escape processing functionality
	 * 
	 * @throws Exception
	 *             if an error occurs
	 */
	public void testEscapeProcessing() throws Exception {
		String results = "select dayname (abs(now())),   -- Today    \n"
				+ "           '1997-05-24',  -- a date                    \n"
				+ "           '10:30:29',  -- a time                     \n"
				+ (versionMeetsMinimum(5, 6, 4) ?
						"           '1997-05-24 10:30:29.123', -- a timestamp  \n" :
						"           '1997-05-24 10:30:29', -- a timestamp  \n"
					)
				+ "          '{string data with { or } will not be altered'   \n"
				+ "--  Also note that you can safely include { and } in comments";

		String exSql = "select {fn dayname ({fn abs({fn now()})})},   -- Today    \n"
				+ "           {d '1997-05-24'},  -- a date                    \n"
				+ "           {t '10:30:29' },  -- a time                     \n"
				+ "           {ts '1997-05-24 10:30:29.123'}, -- a timestamp  \n"
				+ "          '{string data with { or } will not be altered'   \n"
				+ "--  Also note that you can safely include { and } in comments";

		String escapedSql = this.conn.nativeSQL(exSql);

		assertTrue(results.equals(escapedSql));

	}

	/**
	 * Runs all test cases in this test suite
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		junit.textui.TestRunner.run(EscapeProcessingTest.class);
	}

	/**
	 * JDBC-4.0 spec will allow either SQL_ or not for type in {fn convert ...}
	 * 
	 * @throws Exception
	 *             if the test fails
	 */
	public void testConvertEscape() throws Exception {
		assertEquals(conn.nativeSQL("{fn convert(abcd, SQL_INTEGER)}"), conn
				.nativeSQL("{fn convert(abcd, INTEGER)}"));
	}
	
	/**
	 * Tests that the escape tokenizer converts timestamp values
	 * wrt. timezones when useTimezone=true.
	 * 
	 * @throws Exception if the test fails.
	 */
	public void testTimestampConversion() throws Exception {
		TimeZone currentTimezone = TimeZone.getDefault();
		String[] availableIds = TimeZone.getAvailableIDs(currentTimezone.getRawOffset() + (3600 * 1000 * 2));
		String newTimezone = null;
		
		if (availableIds.length > 0) {
			newTimezone = availableIds[0];
		} else {
			newTimezone = "UTC"; // punt
		}
		
		Properties props = new Properties();
		
		props.setProperty("useTimezone", "true");
		props.setProperty("serverTimezone", newTimezone);
		Connection tzConn = null;
		
		try {
			String escapeToken = "SELECT {ts '2002-11-12 10:00:00'} {t '05:11:02'}";
			tzConn = getConnectionWithProps(props);
			assertTrue(!tzConn.nativeSQL(escapeToken).equals(this.conn.nativeSQL(escapeToken)));
		} finally {
			if (tzConn != null) {
				tzConn.close();
			}
		}
	}

	/**
	 * Tests fix for BUG#51313 - Escape processing is confused by multiple backslashes.
	 * 
	 * @throws Exception
	 */
	public void testBug51313() throws Exception {
		stmt = conn.createStatement();
		
		rs = stmt.executeQuery("SELECT {fn lcase('My{fn UCASE(sql)}} -- DATABASE')}, {fn ucase({fn lcase('SERVER')})}"
				+ " -- {escape } processing test\n -- this {fn ucase('comment') is in line 2\r\n"
				+ " -- this in line 3, and previous escape sequence was malformed\n");
		assertTrue(rs.next());
		assertEquals("my{fn ucase(sql)}} -- database", rs.getString(1));
		assertEquals("SERVER", rs.getString(2));
		rs.close();

		rs = stmt.executeQuery("SELECT 'MySQL \\\\\\' testing {long \\\\\\' escape"
				+ " -- { \\\\\\' sequences \\\\\\' } } with escape processing '");
		assertTrue(rs.next());
		assertEquals("MySQL \\\' testing {long \\\' escape -- { \\\' sequences \\\' } } with escape processing ",
				rs.getString(1));
		rs.close();

		rs = stmt.executeQuery("SELECT 'MySQL \\'', '{ testing doubled -- } ''\\\\\\''' quotes '");
		assertTrue(rs.next());
		assertEquals("MySQL \'", rs.getString(1));
		assertEquals("{ testing doubled -- } '\\\'' quotes ", rs.getString(2));
		rs.close();

		rs = stmt.executeQuery("SELECT 'MySQL \\\\\\'''', '{ testing doubled -- } ''\\''' quotes '");
		assertTrue(rs.next());
		assertEquals("MySQL \\\''", rs.getString(1));
		assertEquals("{ testing doubled -- } '\'' quotes ", rs.getString(2));
		rs.close();
	}
}