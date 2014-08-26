/*************************************************************************
 *
 * Copyright 2009 by bBreak Systems.
 *
 * ExCella Trans - Excelファイルを利用したデータ移行支援ツール
 *
 * $Id: TransCreateHelper.java 52 2009-11-13 06:06:27Z akira-yokoi $
 * $Revision: 52 $
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

import java.util.ArrayList;
import java.util.List;

import org.bbreak.excella.core.listener.SheetParseListener;
import org.bbreak.excella.core.tag.TagParser;
import org.bbreak.excella.core.tag.excel2java.ArraysParser;
import org.bbreak.excella.core.tag.excel2java.ListParser;
import org.bbreak.excella.core.tag.excel2java.MapParser;
import org.bbreak.excella.core.tag.excel2java.MapsParser;
import org.bbreak.excella.core.tag.excel2java.ObjectsParser;
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
import org.bbreak.excella.trans.tag.sheet2sql.converter.DefaultSheetToSqlDataConverter;

/**
 * データ移行クラス生成ヘルパークラス
 * 
 * @since 1.0
 */
public class TransCreateHelper {
    /**
     * デフォルトタグパーサのリストを取得する
     * 
     * @return デフォルトタグパーサ
     */
    public static List<TagParser<?>> getDefaultTagParsers() {

        List<TagParser<?>> parsers = new ArrayList<TagParser<?>>();

        // コアのタグパーサ追加
        parsers.add( new MapParser( "@Map"));
        parsers.add( new ListParser( "@List"));
        parsers.add( new ObjectsParser( "@Objects"));
        parsers.add( new ArraysParser( "@Arrays"));
        parsers.add( new MapsParser( "@Maps"));

        // Sqlパーサ追加
        parsers.add( new SqlParser());
        parsers.add( new TruncateParser());
        parsers.add( new TruncateCascadeParser());
        parsers.add( new DeleteParser());
        parsers.add( new RecreateSequenceParser());

        // SheetToJava、SheetToSqlパーサ追加
        parsers.add( new SheetToJavaParser());
        parsers.add( new SheetToJavaSettingParser());
        parsers.add( new SheetToSqlParser());
        parsers.add( new SheetToSqlSettingParser());

        return parsers;
    }

    /**
     * デフォルトシート処理リスナのリストを取得する
     * 
     * @return デフォルトシート処理リスナのリスト
     */
    public static List<SheetParseListener> getDefaultSheetParseListeners() {

        List<SheetParseListener> listeners = new ArrayList<SheetParseListener>();

        // SheetToJavaExecuter
        SheetToJavaExecuter sheetToJavaExecuter = new SheetToJavaExecuter();

        // SheetToSqlExecuter（デフォルトデータコンバータを設定）
        SheetToSqlExecuter sheetToSqlExecuter = new SheetToSqlExecuter();
        sheetToSqlExecuter.setDataConverter( new DefaultSheetToSqlDataConverter());

        // リストに追加
        listeners.add( sheetToJavaExecuter);
        listeners.add( sheetToSqlExecuter);

        return listeners;
    }

    /**
     * SQLを返すパーサのデフォルトタグ一覧を取得する
     * 
     * @return SQLを返すパーサのデフォルトタグ一覧
     */
    public static List<String> getDefaultSqlTags() {

        List<String> defaultSqlTags = new ArrayList<String>();
        defaultSqlTags.add( SqlParser.DEFAULT_TAG);
        defaultSqlTags.add( TruncateParser.DEFAULT_TAG);
        defaultSqlTags.add( TruncateCascadeParser.DEFAULT_TAG);
        defaultSqlTags.add( DeleteParser.DEFAULT_TAG);
        defaultSqlTags.add( RecreateSequenceParser.DEFAULT_TAG);
        defaultSqlTags.add( SheetToSqlParser.DEFAULT_TAG);

        return defaultSqlTags;
    }
}
