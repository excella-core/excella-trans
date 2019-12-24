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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.bbreak.excella.core.listener.SheetParseListener;
import org.bbreak.excella.core.tag.TagParser;
import org.bbreak.excella.core.tag.excel2java.ArraysParser;
import org.bbreak.excella.core.tag.excel2java.ListParser;
import org.bbreak.excella.core.tag.excel2java.MapParser;
import org.bbreak.excella.core.tag.excel2java.MapsParser;
import org.bbreak.excella.core.tag.excel2java.ObjectsParser;
import org.bbreak.excella.trans.WorkbookTest;
import org.bbreak.excella.trans.tag.sheet2java.SheetToJavaExecuter;
import org.bbreak.excella.trans.tag.sheet2java.SheetToJavaParser;
import org.bbreak.excella.trans.tag.sheet2java.SheetToJavaSettingParser;
import org.bbreak.excella.trans.tag.sheet2sql.DeleteParser;
import org.bbreak.excella.trans.tag.sheet2sql.RecreateSequenceParser;
import org.bbreak.excella.trans.tag.sheet2sql.SheetToSqlExecuter;
import org.bbreak.excella.trans.tag.sheet2sql.SheetToSqlParser;
import org.bbreak.excella.trans.tag.sheet2sql.SheetToSqlSettingParser;
import org.bbreak.excella.trans.tag.sheet2sql.SqlParser;
import org.bbreak.excella.trans.tag.sheet2sql.TruncateCascadeParser;
import org.bbreak.excella.trans.tag.sheet2sql.TruncateParser;
import org.junit.Test;

/**
 * TransCreaterHelperテストクラス
 *
 * @since 1.0
 */
public class TransCreaterHelperTest extends WorkbookTest {

    /**
     * コンストラクタ
     * 
     * @param version Excelファイルのバージョン
     */
    public TransCreaterHelperTest( String version) {
        super( version);
    }

    @Test
    public final void testTransCreaterHelperProcessor() throws Exception {

        // ===============================================
        // getDefaultTagParsers()
        // ===============================================
        // デフォルトタグパーサ一覧の取得
        List<TagParser<?>> defaultTagParsers = TransCreateHelper.getDefaultTagParsers();
        assertEquals( 14, defaultTagParsers.size());
        for (TagParser<?> parser : defaultTagParsers) {
            if (parser.getTag().equals( "@Map")) {
                assertTrue( parser instanceof MapParser);
            } else if (parser.getTag().equals( "@List")) {
                assertTrue( parser instanceof ListParser);
            } else if (parser.getTag().equals( "@Objects")) {
                assertTrue( parser instanceof ObjectsParser);
            } else if (parser.getTag().equals( "@Arrays")) {
                assertTrue( parser instanceof ArraysParser);
            } else if (parser.getTag().equals( "@Maps")) {
                assertTrue( parser instanceof MapsParser);
            } else if (parser.getTag().equals( "@Sql")) {
                assertTrue( parser instanceof SqlParser);
            } else if (parser.getTag().equals( "@Truncate")) {
                assertTrue( parser instanceof TruncateParser);
            } else if (parser.getTag().equals( "@TruncateCascade")) {
                assertTrue( parser instanceof TruncateCascadeParser);
            } else if (parser.getTag().equals( "@Delete")) {
                assertTrue( parser instanceof DeleteParser);
            } else if (parser.getTag().equals( "@RecreateSequence")) {
                assertTrue( parser instanceof RecreateSequenceParser);
            } else if (parser.getTag().equals( "@SheetToJava")) {
                assertTrue( parser instanceof SheetToJavaParser);
            } else if (parser.getTag().equals( "@SheetToJavaSetting")) {
                assertTrue( parser instanceof SheetToJavaSettingParser);
            } else if (parser.getTag().equals( "@SheetToSql")) {
                assertTrue( parser instanceof SheetToSqlParser);
            } else if (parser.getTag().equals( "@SheetToSqlSetting")) {
                assertTrue( parser instanceof SheetToSqlSettingParser);
            } else {
                fail( "デフォルトでないパーサ有");
            }
        }

        // ===============================================
        // getDefaultSheetParseListeners()
        // ===============================================
        // デフォルトシート処理リスナ一覧の取得
        List<SheetParseListener> defaultSheetParseListeners = TransCreateHelper.getDefaultSheetParseListeners();
        assertEquals( 2, defaultSheetParseListeners.size());
        assertTrue( defaultSheetParseListeners.get( 0) instanceof SheetToJavaExecuter);
        assertTrue( defaultSheetParseListeners.get( 1) instanceof SheetToSqlExecuter);

        // ===============================================
        // getDefaultSqlTags()
        // ===============================================
        // デフォルトSQLタグ一覧の取得
        List<String> defaultSqlTags = TransCreateHelper.getDefaultSqlTags();
        assertEquals( 6, defaultSqlTags.size());
        assertTrue( defaultSqlTags.contains( "@Sql"));
        assertTrue( defaultSqlTags.contains( "@Truncate"));
        assertTrue( defaultSqlTags.contains( "@TruncateCascade"));
        assertTrue( defaultSqlTags.contains( "@Delete"));
        assertTrue( defaultSqlTags.contains( "@RecreateSequence"));
        assertTrue( defaultSqlTags.contains( "@SheetToSql"));
    }
}
