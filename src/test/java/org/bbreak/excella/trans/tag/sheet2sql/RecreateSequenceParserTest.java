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
import static org.junit.jupiter.api.Assertions.assertThrows;

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
 * RecreateSequenceParserテストクラス
 * 
 * @since 1.0
 */
public class RecreateSequenceParserTest extends WorkbookTest {

    @ParameterizedTest
    @CsvSource( WorkbookTest.VERSIONS)
    public final void testRecreateSequenceParser( String version) throws ParseException, IOException {
        Workbook workbook = getWorkbook( version);
        Sheet sheet1 = workbook.getSheetAt( 0);
        Sheet sheet2 = workbook.getSheetAt( 1);
        Sheet sheet3 = workbook.getSheetAt( 2);
        RecreateSequenceParser recreateSequenceParser = new RecreateSequenceParser( "@RecreateSequence");
        Object data = null;
        List<String> list = null;

        // ===============================================
        // parse( Sheet sheet, Cell tagCell, Object data)
        // ===============================================
        // No.1 パラメータ無し
        Cell tagCell1 = sheet1.getRow( 10).getCell( 0);
        list = recreateSequenceParser.parse( sheet1, tagCell1, data);
        assertEquals( 3, list.size());
        assertEquals( "drop sequence table_name1;\ncreate sequence table_name1 start with 100;", list.get( 0));
        assertEquals( "drop sequence table_name2;\ncreate sequence table_name2 start with 200;", list.get( 1));
        assertEquals( "drop sequence  ;\ncreate sequence   start with 300;", list.get( 2));

        // No.2 パラメータ有り、null行を含む
        Cell tagCell2 = sheet1.getRow( 2).getCell( 4);
        list.clear();
        list = recreateSequenceParser.parse( sheet1, tagCell2, data);
        assertEquals( 3, list.size());
        assertEquals( "drop sequence table_name3;\ncreate sequence table_name3 start with 100;", list.get( 0));
        assertEquals( "drop sequence table_name4;\ncreate sequence table_name4 start with 200;", list.get( 1));
        assertEquals( "drop sequence table_name5;\ncreate sequence table_name5 start with 300;", list.get( 2));

        // No.3 シーケンス名にnullセルを含む
        Cell tagCell3 = sheet2.getRow( 5).getCell( 0);
        ParseException pe = assertThrows( ParseException.class, () -> recreateSequenceParser.parse( sheet2, tagCell3, data));
        Cell cell = pe.getCell();
        assertEquals( 5, cell.getRow().getRowNum());
        assertEquals( 0, cell.getColumnIndex());
        System.out.println( "No.3:" + pe);

        // No.4 現在値にnullセルを含む
        Cell tagCell4 = sheet2.getRow( 16).getCell( 0);
        pe = assertThrows( ParseException.class, () -> recreateSequenceParser.parse( sheet2, tagCell4, data));
        cell = pe.getCell();
        assertEquals( 16, cell.getRow().getRowNum());
        assertEquals( 0, cell.getColumnIndex());
        System.out.println( "No.3:" + pe);

        // No.5 シーケンス名・現在値がnullセル
        Cell tagCell5 = sheet2.getRow( 28).getCell( 0);
        pe = assertThrows( ParseException.class, () -> recreateSequenceParser.parse( sheet2, tagCell5, data));
        cell = pe.getCell();
        assertEquals( 28, cell.getRow().getRowNum());
        assertEquals( 0, cell.getColumnIndex());
        System.out.println( "No.3:" + pe);

        // No.6 配列のサイズ不足
        Cell tagCell6 = sheet2.getRow( 41).getCell( 0);
        pe = assertThrows( ParseException.class, () -> recreateSequenceParser.parse( sheet2, tagCell6, data));
        cell = pe.getCell();
        assertEquals( 41, cell.getRow().getRowNum());
        assertEquals( 0, cell.getColumnIndex());
        System.out.println( "No.3:" + pe);

        // No.7 配列のサイズ不足
        Cell tagCell7 = sheet2.getRow( 53).getCell( 0);
        pe = assertThrows( ParseException.class, () -> recreateSequenceParser.parse( sheet2, tagCell7, data));
        cell = pe.getCell();
        assertEquals( 53, cell.getRow().getRowNum());
        assertEquals( 0, cell.getColumnIndex());
        System.out.println( "No.3:" + pe);

        // No.8 現在値が小数
        Cell tagCell8 = sheet2.getRow( 66).getCell( 0);
        list.clear();
        list = recreateSequenceParser.parse( sheet2, tagCell8, data);
        assertEquals( 1, list.size());
        assertEquals( "drop sequence table_name11;\ncreate sequence table_name11 start with 1;", list.get( 0));

        // No.9 現在値が数値文字列
        Cell tagCell9 = sheet2.getRow( 78).getCell( 0);
        list.clear();
        list = recreateSequenceParser.parse( sheet2, tagCell9, data);
        assertEquals( 1, list.size());
        assertEquals( "drop sequence table_name12;\ncreate sequence table_name12 start with 10;", list.get( 0));

        // No.10 現在値が文字列
        Cell tagCell10 = sheet2.getRow( 90).getCell( 0);
        pe = assertThrows( ParseException.class, () -> recreateSequenceParser.parse( sheet2, tagCell10, data));
        cell = pe.getCell();
        assertEquals( 90, cell.getRow().getRowNum());
        assertEquals( 0, cell.getColumnIndex());
        System.out.println( "No.3:" + pe);

        // No.11 例外テスト（データ無し）
        Cell tagCell11 = sheet2.getRow( 102).getCell( 0);
        pe = assertThrows( ParseException.class, () -> recreateSequenceParser.parse( sheet2, tagCell11, data));
        cell = pe.getCell();
        assertEquals( 102, cell.getRow().getRowNum());
        assertEquals( 0, cell.getColumnIndex());
        System.out.println( "No.3:" + pe);

        // No.12 デフォルトタグ
        Cell tagCell12 = sheet3.getRow( 5).getCell( 0);
        RecreateSequenceParser recreateSequenceParser2 = new RecreateSequenceParser();
        list.clear();
        list = recreateSequenceParser2.parse( sheet3, tagCell12, data);
        assertEquals( "drop sequence table_name1;\ncreate sequence table_name1 start with 1000;", list.get( 0));
        assertEquals( 1, list.size());
    }
}
