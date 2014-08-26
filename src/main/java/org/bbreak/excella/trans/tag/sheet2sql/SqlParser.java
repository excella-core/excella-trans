/*************************************************************************
 *
 * Copyright 2009 by bBreak Systems.
 *
 * ExCella Trans - Excelファイルを利用したデータ移行支援ツール
 *
 * $Id: SqlParser.java 17 2009-06-24 04:59:23Z yuta-takahashi $
 * $Revision: 17 $
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
package org.bbreak.excella.trans.tag.sheet2sql;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.bbreak.excella.core.exception.ParseException;
import org.bbreak.excella.core.tag.excel2java.ListParser;

/**
 * シートを解析し、SQL文字列を<BR>
 * List&lt;String&gt;で返却するパーサ
 * 
 * @since 1.0
 */
public class SqlParser extends ListParser {

    /**
     * デフォルトタグ
     */
    public static final String DEFAULT_TAG = "@Sql";

    /**
     * SQL接頭句
     */
    protected static final String SQL_PREFIX = "";

    /**
     * SQL接尾句
     */
    protected static final String SQL_SUFFIX = ";";

    /**
     * コンストラクタ
     */
    public SqlParser() {
        super( DEFAULT_TAG);
    }

    /**
     * コンストラクタ
     * 
     * @param tag タグ
     */
    public SqlParser( String tag) {
        super( tag);
    }

    /**
     * パース処理
     * 
     * @param sheet 対象シート
     * @param tagCell タグが定義されたセル
     * @param data TransProcessorのprocessBook, processSheetメソッドで<BR> 
     *              引数を渡した場合にTagParserまで引き継がれる処理データ<BR>
     * @return パース結果
     * @throws ParseException パース例外
     */
    @Override
    public List<String> parse( Sheet sheet, Cell tagCell, Object data) throws ParseException {

        return parse( sheet, tagCell, data, SQL_PREFIX, SQL_SUFFIX);
    }

    /**
     * 接頭句、接尾句を付与した文字列のリストを返す。
     * 
     * @param sheet シート
     * @param tagCell タグセル
     * @param data TransProcessorのprocessBook, processSheetメソッドで<BR> 
     *              引数を渡した場合にTagParser.parseメソッドまで引き継がれる処理データ<BR>
     * @param prefix 接頭句
     * @param suffix 接尾句
     * @return SQL文字列のリスト
     * @throws ParseException パース例外
     */
    public List<String> parse( Sheet sheet, Cell tagCell, Object data, String prefix, String suffix) throws ParseException {

        List<?> objList = super.parse( sheet, tagCell, data);
        List<String> resultList = new ArrayList<String>();

        for ( Object obj : objList) {

            if ( obj == null) {
                continue;
            }

            String sql = obj.toString();
            StringBuilder strBuild = new StringBuilder();

            // 接尾句を付与する
            strBuild.append( prefix);

            strBuild.append( sql);

            // 接尾句を付与する
            strBuild.append( suffix);

            sql = strBuild.toString();
            resultList.add( sql);
        }

        return resultList;
    }
}
