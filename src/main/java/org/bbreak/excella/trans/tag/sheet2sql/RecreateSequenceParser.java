/*************************************************************************
 *
 * Copyright 2009 by bBreak Systems.
 *
 * ExCella Trans - Excelファイルを利用したデータ移行支援ツール
 *
 * $Id: RecreateSequenceParser.java 64 2009-11-19 02:12:45Z akira-yokoi $
 * $Revision: 64 $
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
import org.bbreak.excella.core.tag.excel2java.ArraysParser;

/**
 * シートを解析し、シーケンスを再生成するSQLの文字列を<BR>
 * List&lt;String&gt;で返却するパーサ
 * 
 * @since 1.0
 */
public class RecreateSequenceParser extends SqlParser {

    /**
     * デフォルトタグ
     */
    public static final String DEFAULT_TAG = "@RecreateSequence";

    /**
     * SQL drop接頭句
     */
    protected static final String SQL_DROP_PREFIX = "drop sequence ";

    /**
     * SQL create接頭句
     */
    protected static final String SQL_CREATE_PREFIX = "create sequence ";

    /**
     * SQL start句
     */
    protected static final String SQL_START = " start with ";

    /**
     * SQL接尾句
     */
    protected static final String SQL_SUFFIX = ";";

    /**
     * コンストラクタ
     */
    public RecreateSequenceParser() {
        super( DEFAULT_TAG);
    }

    /**
     * コンストラクタ
     * 
     * @param tag タグ
     */
    public RecreateSequenceParser( String tag) {
        super( tag);
    }

    /**
     * パース処理
     * 
     * @param sheet 対象シート
     * @param tagCell タグが定義されたセル
     * @param data TransProcessorのprocessBook, processSheetメソッドで<BR>
     * 引数を渡した場合にTagParser.parseメソッドまで引き継がれる処理データ<BR>
     * @return パース結果
     * @throws ParseException パース例外
     */
    @Override
    public List<String> parse( Sheet sheet, Cell tagCell, Object data) throws ParseException {

        List<String> resultList = new ArrayList<String>();

        ArraysParser arraysParser = new ArraysParser( super.getTag());
        List<Object[]> objList = arraysParser.parse( sheet, tagCell, data);

        for ( Object[] obj : objList) {

            try {
                if ( obj.length != 2) {
                    throw new ParseException( tagCell, "シーケンス名と初期を設定してください");
                }
                // シーケンス名・初期値の取得
                Object nameObj = obj[0];
                Object valueObj = obj[1];

                // nullチェック
                if ( nameObj == null) {
                    throw new ParseException( tagCell, "シーケンス名がnullです");
                }
                if ( valueObj == null) {
                    throw new ParseException( tagCell, "初期値がnullです");
                }

                String name = nameObj.toString();
                Double valueDouble = Double.parseDouble( valueObj.toString());
                Integer value = valueDouble.intValue();

                // SQLを生成し、結果リストに追加
                resultList.add( createSql( name, value));

            } catch ( Exception e) {
                if ( e instanceof ParseException) {
                    throw ( ParseException) e;
                } else {
                    throw new ParseException( tagCell, e.toString());
                }
            }
        }
        return resultList;
    }

    /**
     * シーケンス再生成のSQL文字列を作成し、返却する
     * 
     * @param name シーケンス名
     * @param value 現在値
     * @return SQL文字列
     */
    private String createSql( String name, Integer value) {

        StringBuilder strBuild = new StringBuilder();
        strBuild.append( SQL_DROP_PREFIX);
        strBuild.append( name);
        strBuild.append( SQL_SUFFIX);
        strBuild.append( "\n");
        strBuild.append( SQL_CREATE_PREFIX);
        strBuild.append( name);
        strBuild.append( SQL_START);
        strBuild.append( value);
        strBuild.append( SQL_SUFFIX);

        return strBuild.toString();
    }
}
