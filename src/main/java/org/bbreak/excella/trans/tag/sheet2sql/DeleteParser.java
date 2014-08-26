/*************************************************************************
 *
 * Copyright 2009 by bBreak Systems.
 *
 * ExCella Trans - Excelファイルを利用したデータ移行支援ツール
 *
 * $Id: DeleteParser.java 2 2009-06-22 04:48:53Z yuta-takahashi $
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
package org.bbreak.excella.trans.tag.sheet2sql;

import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.bbreak.excella.core.exception.ParseException;

/**
 * シートを解析し、Delete文のSQL文字列を<BR>
 * List&lt;String&gt;で返却するパーサ
 * 
 * @since 1.0
 */
public class DeleteParser extends SqlParser {

    /**
     * デフォルトタグ
     */
    public static final String DEFAULT_TAG = "@Delete";

    /**
     * SQL接頭句
     */
    protected static final String SQL_PREFIX = "delete from ";

    /**
     * SQL接尾句
     */
    protected static final String SQL_SUFFIX = ";";

    /**
     * コンストラクタ
     */
    public DeleteParser() {
        super( DEFAULT_TAG);
    }

    /**
     * コンストラクタ
     * 
     * @param tag タグ
     */
    public DeleteParser( String tag) {
        super( tag);
    }

    /**
     * パース処理
     * 
     * @param sheet 対象シート
     * @param tagCell タグが定義されたセル
     * @param data TransProcessorのprocessBook, processSheetメソッドで<BR> 
     *              引数を渡した場合にTagParser.parseメソッドまで引き継がれる処理データ<BR>
     * @return パース結果
     * @throws ParseException パース例外
     */
    @Override
    public List<String> parse( Sheet sheet, Cell tagCell, Object data) throws ParseException {

        return super.parse( sheet, tagCell, data, SQL_PREFIX, SQL_SUFFIX);
    }
}
