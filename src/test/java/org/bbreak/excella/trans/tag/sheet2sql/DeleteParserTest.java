/*-
 * #%L
 * excella-trans
 * %%
 * Copyright (C) 2009 - 2019 bBreak Systems and contributors
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

package org.bbreak.excella.trans.tag.sheet2sql;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.bbreak.excella.core.exception.ParseException;
import org.bbreak.excella.trans.WorkbookTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * DeleteParserテストクラス
 *
 * @since 1.0
 */
public class DeleteParserTest extends WorkbookTest {

    @ParameterizedTest
    @CsvSource( WorkbookTest.VERSIONS)
    public final void testDeleteParser( String version) throws ParseException, IOException {
        Workbook workbook = getWorkbook( version);
        Sheet sheet = workbook.getSheetAt( 0);
        DeleteParser deleteParser = new DeleteParser( "@Delete");
        Cell tagCell = null;
        Object data = null;
        List<String> list = null;

        // ===============================================
        // parse( Sheet sheet, Cell tagCell, Object data) 
        // ===============================================
        // No.1 パラメータ無し
        tagCell = sheet.getRow( 10).getCell( 0);
        list = deleteParser.parse( sheet, tagCell, data);
        assertEquals( "delete from table_name1;", list.get( 0));
        assertEquals( "delete from table_name2;", list.get( 1));
        assertEquals( "delete from  ;", list.get( 2));
        assertEquals( 3, list.size());

        // No.2 パラメータ有り、null行を含む
        tagCell = sheet.getRow( 2).getCell( 4);
        list = deleteParser.parse( sheet, tagCell, data);
        assertEquals( "delete from table_name3;", list.get( 0));
        assertEquals( "delete from table_name4;", list.get( 1));
        assertEquals( "delete from table_name5 where code = '100';", list.get( 2));
        assertEquals( 3, list.size());

        // No.3 パラメータ有り、nullセルを含む
        tagCell = sheet.getRow( 8).getCell( 12);
        list = deleteParser.parse( sheet, tagCell, data);
        assertEquals( "delete from table_name6;", list.get( 0));
        assertEquals( "delete from table_name7;", list.get( 1));
        assertEquals( 2, list.size());

        // No.4 デフォルトタグ
        DeleteParser deleteParser2 = new DeleteParser();
        tagCell = sheet.getRow( 8).getCell( 21);
        list = deleteParser2.parse( sheet, tagCell, data);
        assertEquals( "delete from table_name8;", list.get( 0));
        assertEquals( "delete from table_name9;", list.get( 1));
    }
}
