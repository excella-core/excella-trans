/*************************************************************************
 *
 * Copyright 2009 by bBreak Systems.
 *
 * ExCella Trans - Excelファイルを利用したデータ移行支援ツール
 *
 * $Id: TransCreaterHelperTest.java 2 2009-06-22 04:48:53Z yuta-takahashi $
 * $Revision: 2 $
 *
 * This file is part of ExCella Trans.
 *
 * ExCella Trans is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License version 3
 * only, as published by the Free Software Foundation.
 *
 * ExCella Trans is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License version 3 for more details
 * (a copy is included in the COPYING.LESSER file that accompanied this code).
 *
 * You should have received a copy of the GNU Lesser General Public License
 * version 3 along with ExCella Trans.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0-standalone.html>
 * for a copy of the LGPLv3 License.
 *
 ************************************************************************/
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
