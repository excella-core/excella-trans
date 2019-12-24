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

package org.bbreak.excella.trans.processor;

import static org.junit.Assert.assertEquals;

import java.text.DateFormat;
import java.util.List;

import org.bbreak.excella.core.BookData;
import org.bbreak.excella.core.SheetData;
import org.bbreak.excella.trans.WorkbookTest;
import org.bbreak.excella.trans.tag.sheet2java.SheetToJavaExecuter;
import org.bbreak.excella.trans.tag.sheet2java.TestChildEntityParser;
import org.bbreak.excella.trans.tag.sheet2java.entity.TestEntity1;
import org.bbreak.excella.trans.tag.sheet2java.entity.TestEntity2;
import org.bbreak.excella.trans.tag.sheet2sql.SheetToSqlExecuter;
import org.bbreak.excella.trans.tag.sheet2sql.converter.DefaultSheetToSqlDataConverter;
import org.junit.Test;

/**
 * SheetToJava、SheetToSqlの疎通テストクラス<BR>
 * 
 * SheetToSqlParser, SheetToSqlSettingParser, SheetToSqlExecuterと<BR>
 * SheetToJavaParser, SheetToJavaSettingParser, SheetToJavaExecuterの処理が<BR>
 * 正しく行われるかをテストする<BR>
 * 
 * @since 1.0
 */
public class ConnectivityTest extends WorkbookTest {

    /**
     * コンストラクタ
     * 
     * @param version Excelファイルのバージョン
     */
    public ConnectivityTest( String version) {
        super( version);
    }

    @Test
    @SuppressWarnings( "unchecked")
    public final void testConnectivity() throws Exception {

        super.getWorkbook();
        String filePath = super.getFilepath();
        TransProcessor processor = new TransProcessor( filePath);

        // デフォルトシート処理リスナを削除
        processor.clearSheetParseListeners();

        // SheetToJavaのシート処理リスナを追加
        SheetToJavaExecuter javaExecuter = new SheetToJavaExecuter();
        javaExecuter.addPropertyParser( new TestChildEntityParser());
        processor.addSheetParseListener( javaExecuter);

        // SheetToSqlのシート処理リスナを追加
        SheetToSqlExecuter sqlExecuter = new SheetToSqlExecuter();
        sqlExecuter.setDataConverter( new DefaultSheetToSqlDataConverter());
        processor.addSheetParseListener( sqlExecuter);

        // プロセス実行
        BookData bookData = processor.processBook();

        // 値の検証：SheetToJava
        SheetData sheetData = bookData.getSheetData( "Connectivity");
        List<Object> javaResults = ( List<Object>) sheetData.get( "@SheetToJava");
        assertEquals( 9, javaResults.size());
        assertEquals( "文字列1", (( TestEntity1) javaResults.get( 0)).getPropertyStr1());
        assertEquals( "文字列2", (( TestEntity1) javaResults.get( 1)).getPropertyStr1());
        assertEquals( "文字列3", (( TestEntity1) javaResults.get( 2)).getPropertyStr1());
        assertEquals( "文字列4", (( TestEntity1) javaResults.get( 3)).getPropertyStr1());
        assertEquals( "文字列1", (( TestEntity2) javaResults.get( 4)).getPropertyStr2());
        assertEquals( "文字列1", (( TestEntity2) javaResults.get( 5)).getPropertyStr2());
        assertEquals( "文字列2", (( TestEntity2) javaResults.get( 6)).getPropertyStr2());
        assertEquals( "文字列3", (( TestEntity2) javaResults.get( 7)).getPropertyStr2());
        assertEquals( "文字列4", (( TestEntity2) javaResults.get( 8)).getPropertyStr2());
        assertEquals( new Integer( 10), (( TestEntity1) javaResults.get( 0)).getPropertyInt1());
        assertEquals( new Integer( 20), (( TestEntity1) javaResults.get( 1)).getPropertyInt1());
        assertEquals( new Integer( 30), (( TestEntity1) javaResults.get( 2)).getPropertyInt1());
        assertEquals( new Integer( 40), (( TestEntity1) javaResults.get( 3)).getPropertyInt1());
        assertEquals( new Integer( 10), (( TestEntity2) javaResults.get( 4)).getPropertyInt2());
        assertEquals( new Integer( 10), (( TestEntity2) javaResults.get( 5)).getPropertyInt2());
        assertEquals( new Integer( 10), (( TestEntity2) javaResults.get( 6)).getPropertyInt2());
        assertEquals( new Integer( 10), (( TestEntity2) javaResults.get( 7)).getPropertyInt2());
        assertEquals( new Integer( 10), (( TestEntity2) javaResults.get( 8)).getPropertyInt2());
        assertEquals( DateFormat.getDateInstance().parse( "2009/06/08"), (( TestEntity1) javaResults.get( 0)).getPropertyDate1());
        assertEquals( DateFormat.getDateInstance().parse( "2009/06/08"), (( TestEntity1) javaResults.get( 1)).getPropertyDate1());
        assertEquals( DateFormat.getDateInstance().parse( "2009/06/08"), (( TestEntity1) javaResults.get( 2)).getPropertyDate1());
        assertEquals( DateFormat.getDateInstance().parse( "2009/06/08"), (( TestEntity1) javaResults.get( 3)).getPropertyDate1());
        assertEquals( DateFormat.getDateInstance().parse( "2009/06/01"), (( TestEntity2) javaResults.get( 4)).getPropertyDate2());
        assertEquals( DateFormat.getDateInstance().parse( "2009/06/02"), (( TestEntity2) javaResults.get( 5)).getPropertyDate2());
        assertEquals( DateFormat.getDateInstance().parse( "2009/06/03"), (( TestEntity2) javaResults.get( 6)).getPropertyDate2());
        assertEquals( DateFormat.getDateInstance().parse( "2009/06/04"), (( TestEntity2) javaResults.get( 7)).getPropertyDate2());
        assertEquals( DateFormat.getDateInstance().parse( "2009/06/05"), (( TestEntity2) javaResults.get( 8)).getPropertyDate2());
        assertEquals( "文字列1", (( TestEntity1) javaResults.get( 0)).getChild().getChildPropertyStr1());
        assertEquals( "文字列2", (( TestEntity1) javaResults.get( 1)).getChild().getChildPropertyStr1());
        assertEquals( "文字列3", (( TestEntity1) javaResults.get( 2)).getChild().getChildPropertyStr1());
        assertEquals( "文字列4", (( TestEntity1) javaResults.get( 3)).getChild().getChildPropertyStr1());
        assertEquals( new Integer( 10), (( TestEntity1) javaResults.get( 0)).getChild().getChildPropertyInt1());
        assertEquals( new Integer( 20), (( TestEntity1) javaResults.get( 1)).getChild().getChildPropertyInt1());
        assertEquals( new Integer( 30), (( TestEntity1) javaResults.get( 2)).getChild().getChildPropertyInt1());
        assertEquals( new Integer( 40), (( TestEntity1) javaResults.get( 3)).getChild().getChildPropertyInt1());

        // 値の検証：SheetToSql
        sheetData = bookData.getSheetData( "Connectivity 2");
        List<String> sqlResults = ( List<String>) sheetData.get( "@SheetToSql");
        assertEquals( 9, sqlResults.size());
        String sql1 = "insert into test_table1 (column_name1,column_name2,column_name3,column_name4) values ('文字列1',100.0,10,FALSE);";
        String sql2 = "insert into test_table1 (column_name1,column_name2,column_name3,column_name4) values ('文字列2',100.0,20,FALSE);";
        String sql3 = "insert into test_table1 (column_name1,column_name2,column_name3,column_name4) values ('文字列3',100.0,30,FALSE);";
        String sql4 = "insert into test_table1 (column_name1,column_name2,column_name3,column_name4) values ('文字列4',100.0,40,FALSE);";
        String sql5 = "insert into test_table2 (column_name1,column_name2,column_name3,column_name4,column_name5,column_name6,column_name7,column_name8) values ('文字列1',3.14,200,TRUE,'2009-06-01','12:13:14.0','2009-06-08 12:13:14.0',CURRENT_TIMESTAMP);";
        String sql6 = "insert into test_table2 (column_name1,column_name2,column_name3,column_name4,column_name5,column_name6,column_name7,column_name8) values ('文字列1',3.14,200,TRUE,'2009-06-02','12:13:14.0','2009-06-08 12:13:14.0',CURRENT_TIMESTAMP);";
        String sql7 = "insert into test_table2 (column_name1,column_name2,column_name3,column_name4,column_name5,column_name6,column_name7,column_name8) values ('文字列2',3.14,200,TRUE,'2009-06-03','12:13:14.0','2009-06-08 12:13:14.0',CURRENT_TIMESTAMP);";
        String sql8 = "insert into test_table2 (column_name1,column_name2,column_name3,column_name4,column_name5,column_name6,column_name7,column_name8) values ('文字列3',3.14,200,TRUE,'2009-06-04','12:13:14.0','2009-06-08 12:13:14.0',CURRENT_TIMESTAMP);";
        String sql9 = "insert into test_table2 (column_name1,column_name2,column_name3,column_name4,column_name5,column_name6,column_name7,column_name8) values ('文字列4',3.14,200,TRUE,'2009-06-05','12:13:14.0','2009-06-08 12:13:14.0',CURRENT_TIMESTAMP);";
        assertEquals( sql1, sqlResults.get( 0));
        assertEquals( sql2, sqlResults.get( 1));
        assertEquals( sql3, sqlResults.get( 2));
        assertEquals( sql4, sqlResults.get( 3));
        assertEquals( sql5, sqlResults.get( 4));
        assertEquals( sql6, sqlResults.get( 5));
        assertEquals( sql7, sqlResults.get( 6));
        assertEquals( sql8, sqlResults.get( 7));
        assertEquals( sql9, sqlResults.get( 8));

    }
}
