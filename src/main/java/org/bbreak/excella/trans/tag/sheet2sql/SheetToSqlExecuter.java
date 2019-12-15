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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.bbreak.excella.core.BookController;
import org.bbreak.excella.core.SheetData;
import org.bbreak.excella.core.SheetParser;
import org.bbreak.excella.core.exception.ParseException;
import org.bbreak.excella.core.listener.SheetParseListener;
import org.bbreak.excella.core.tag.TagParser;
import org.bbreak.excella.core.util.PoiUtil;
import org.bbreak.excella.core.util.TagUtil;
import org.bbreak.excella.trans.tag.sheet2sql.converter.SheetToSqlDataConverter;
import org.bbreak.excella.trans.tag.sheet2sql.model.SheetToSqlInfo;
import org.bbreak.excella.trans.tag.sheet2sql.model.SheetToSqlParseInfo;
import org.bbreak.excella.trans.tag.sheet2sql.model.SheetToSqlSettingInfo;

/**
 * シートを解析し、Insert文の<BR>
 * SQL文字列をList&lt;String&gt;で取得する
 * 
 * @since 1.0
 */
public class SheetToSqlExecuter implements SheetParseListener {

    /**
     * 論理名パラメータ定義の開始文字
     */
    protected static final String LNAME_TAG_PARAM_PREFIX = "(";

    /**
     * 論理名パラメータ定義の終了文字
     */
    protected static final String LNAME_TAG_PARAM_SUFFIX = ")";

    /**
     * 論理名タグ
     */
    protected static final String TAG_LOGICAL_NAME_PREFIX = "@LNAME(";

    /**
     * データコンバータ
     */
    private SheetToSqlDataConverter dataConverter = null;

    /**
     * 解析前処理
     * 
     * @param sheet 対象シート
     * @param sheetParser 対象シートパーサ
     */
    public void preParse( Sheet sheet, SheetParser sheetParser) {
        // do nothing
    }

    /**
     * 解析後処理<BR>
     * 設定情報に基づき、データをSql文字列に変換し、<BR>
     * 結果をリストに入れて、SheetDataにセットする。<BR>
     * 
     * @param sheet 対象シート
     * @param sheetParser 対象シートパーサ
     * @param sheetData 解析結果シートデータ
     */
    @SuppressWarnings( "unchecked")
    public void postParse( Sheet sheet, SheetParser sheetParser, SheetData sheetData) throws ParseException {

        // 結果オブジェクトのリスト
        List<Object> results = new ArrayList<Object>();

        // 対象シートにSheetToSqlParserが存在するか
        List<TagParser<?>> tagParsers = sheetParser.getTagParsers();

        // 処理後不要になるSheetToJavaSettingParserの
        // データを判別するタグのリスト
        List<String> removeTags = new ArrayList<String>();

        // 処理をするタグの一覧を作成
        List<String> targetTags = new ArrayList<String>();
        for ( TagParser<?> tagParser : tagParsers) {
            // SheetToSqlParserのタグ
            if ( tagParser instanceof SheetToSqlParser) {
                targetTags.add( tagParser.getTag());
            }
            // SheetToSqlSettingParserのタグ
            if ( tagParser instanceof SheetToSqlSettingParser) {
                removeTags.add( tagParser.getTag());
            }
        }

        // ワークブックの取得
        Workbook workbook = sheet.getWorkbook();

        // 処理対象のタグでループ
        for ( String tag : targetTags) {

            List<SheetToSqlParseInfo> sheetInfoList = ( List<SheetToSqlParseInfo>) sheetData.get( tag);

            if ( sheetInfoList == null) {
                continue;
            }

            // 設定情報(シート)単位でループ
            for ( SheetToSqlParseInfo sheetInfo : sheetInfoList) {

                List<SheetToSqlSettingInfo> allColumnInfoList = ( List<SheetToSqlSettingInfo>) sheetData.get( sheetInfo.getSettingTagName());

                // 今回処理対象シート分の設定を取得
                List<SheetToSqlSettingInfo> targetColumnInfoList = new ArrayList<SheetToSqlSettingInfo>();
                for ( SheetToSqlSettingInfo columnInfo : allColumnInfoList) {
                    if ( columnInfo.getSheetName().equals( sheetInfo.getSheetName())) {
                        targetColumnInfoList.add( columnInfo);
                    }
                }

                // 対象シートデータの読み込み
                Sheet targetSheet = workbook.getSheet( sheetInfo.getSheetName());
                if ( targetSheet == null) {
                    throw new ParseException( "シート[" + sheetInfo.getSheetName() + "]は存在しません");
                }
                results.addAll( parseTargetSheet( targetSheet, sheetInfo, targetColumnInfoList));
            }

            // sheetDataに結果を格納
            sheetData.put( tag, results);
        }

        // 処理後に不要になるデータの削除
        for ( String removeTag : removeTags) {
            sheetData.remove( removeTag);
        }
    }

    /**
     * 設定情報に基づき、対象シートをInsert文のSqlに<BR>
     * 変換し、リストに入れて返却する<BR>
     * 
     * @param targetSheet 解析対象シート
     * @param targetColumnInfoList 設定情報
     * @return Sqlリスト
     * @throws ParseException パース例外
     */
    protected List<Object> parseTargetSheet( Sheet targetSheet, SheetToSqlParseInfo sheetInfo, List<SheetToSqlSettingInfo> targetColumnInfoList) throws ParseException {

        // 結果オブジェクトのリスト
        List<Object> results = new ArrayList<Object>();

        int logicalRowNum = sheetInfo.getLogicalNameRowNum() - 1;
        int valueStartRowNum = sheetInfo.getValueRowNum() - 1;
        int valueEndRowNum = targetSheet.getLastRowNum();

        // 論理名、対応カラムindexのマップ
        Map<String, Integer> colLogicalNameMap = new HashMap<String, Integer>();

        // colLogicalNameMap作成
        Row row = targetSheet.getRow( logicalRowNum);
        if ( row != null) {

            // 論理名行の開始列と終了列
            int firstColIdx = row.getFirstCellNum();
            int lastColIdx = row.getLastCellNum();

            for ( int colIdx = firstColIdx; colIdx <= lastColIdx; colIdx++) {
                Cell cell = row.getCell( colIdx);
                if ( cell != null) {
                    try {
                        // 論理名
                        String logicalCellValue = cell.getStringCellValue();
                        if ( !logicalCellValue.startsWith( BookController.COMMENT_PREFIX)) {
                            colLogicalNameMap.put( logicalCellValue, colIdx);
                        }
                    } catch ( Exception e) {
                        throw new ParseException( cell, e);
                    }
                }
            }
        }

        // 定義順に処理する必要があるため、データの順番を保持するリスト
        List<String> tableNameList = new ArrayList<String>();

        // クラス、SettingInfoリストのマップ
        Map<String, List<SheetToSqlSettingInfo>> settingInfoListMap = new HashMap<String, List<SheetToSqlSettingInfo>>();
        // クラス、重複不可プロパティ名リストのマップ
        Map<String, List<String>> uniqueColumnListMap = new HashMap<String, List<String>>();
        for ( SheetToSqlSettingInfo settingInfo : targetColumnInfoList) {

            // マップからリストを取得
            String tableName = settingInfo.getTableName();
            List<SheetToSqlSettingInfo> settingInfoList = settingInfoListMap.get( tableName);
            if ( settingInfoList == null) {
                // 取得できなかった場合
                settingInfoList = new ArrayList<SheetToSqlSettingInfo>();
            }
            List<String> uniqueColumnList = uniqueColumnListMap.get( tableName);
            if ( uniqueColumnList == null) {
                // 取得できなかった場合
                uniqueColumnList = new ArrayList<String>();
            }

            // リストに追加する
            settingInfoList.add( settingInfo);
            if ( settingInfo.isUnique()) {
                uniqueColumnList.add( settingInfo.getColumnName());
            }

            // データの順番を保持
            if ( !tableNameList.contains( tableName)) {
                tableNameList.add( tableName);
            }

            // マップに詰める
            settingInfoListMap.put( tableName, settingInfoList);
            uniqueColumnListMap.put( tableName, uniqueColumnList);
        }

        // テーブル名ごとのループ
        for ( String tableName : tableNameList) {

            // SQL作成に必要な情報保持クラスのリスト
            List<SheetToSqlInfo> infoList = new ArrayList<SheetToSqlInfo>();

            SheetToSqlInfo info = null;

            // データ行ごとのループ
            for ( int valueRowIdx = valueStartRowNum; valueRowIdx <= valueEndRowNum; valueRowIdx++) {

                Map<String, String> columnValueMap = new HashMap<String, String>();
                List<String> columnNameList = new ArrayList<String>();

                // SheetToSqlInfoインスタンス生成
                info = new SheetToSqlInfo();
                info.setTableName( tableName);
                info.setColumnValueMap( columnValueMap);
                info.setColumnNameList( columnNameList);

                Row valueRow = targetSheet.getRow( valueRowIdx);
                if ( valueRow == null) {
                    continue;
                }

                // カラムごとのループ
                List<SheetToSqlSettingInfo> settingInfoList = settingInfoListMap.get( tableName);
                for ( SheetToSqlSettingInfo settingInfo : settingInfoList) {
                    // カラム名
                    String columnName = settingInfo.getColumnName();
                    // 値
                    Object value = settingInfo.getValue();
                    // データ型
                    String dataType = settingInfo.getDataType();

                    // コンバート対象はvalue
                    Object target = value;
                    Cell cell = null;

                    if ( value instanceof String) {
                        // 値が文字列の場合
                        String settingValueStr = ( String) value;
                        if ( settingValueStr.startsWith( TAG_LOGICAL_NAME_PREFIX)) {
                            // 論理名タグの場合
                            String logicalKey = TagUtil.getParam( settingValueStr, LNAME_TAG_PARAM_PREFIX, LNAME_TAG_PARAM_SUFFIX);
                            Integer logicalKeyCol = colLogicalNameMap.get( logicalKey);
                            if ( logicalKeyCol == null) {
                                throw new ParseException( settingInfo.getValueCell(), "論理名タグパラメータ不正:" + logicalKey);
                            }

                            // コンバート対象は論理名で定義されたセルの値
                            cell = valueRow.getCell( logicalKeyCol);
                            target = PoiUtil.getCellValue( cell);
                        }
                    }

                    // 対象をコンバートする
                    try{
                    	String valueStr = dataConverter.convert( target, dataType, settingInfo);

                        // マップに値を格納
                        columnValueMap.put( columnName, valueStr);

                        // リストにカラム名を格納
                        columnNameList.add( columnName);
                    }
                    catch (ParseException parseEx){
                    	// 値に変換した場合＆値のセルが分かっている場合はセル情報を上書きする
                    	if( cell != null){
                        	parseEx.setCell(cell);
                    	}
                    	throw parseEx;
                    }
                }

                List<String> uniqueColumnList = uniqueColumnListMap.get( tableName);
                if ( !isDuplicateObj( info, infoList, uniqueColumnList)) {
                    // 重複していない場合
                    infoList.add( info);
                }
            }

            // SQLのリストを作成し、結果リストに格納する
            List<String> sqlList = createInsertSqlList( infoList);
            results.addAll( sqlList);
        }

        return results;
    }

    /**
     * SheetToSqlInfoのリストからInsert文のリストを作成する
     * 
     * @param infoList SQL作成に必要な情報のリスト
     * @return Insert文のリスト
     */
    private List<String> createInsertSqlList( List<SheetToSqlInfo> infoList) {

        // SQL文のリスト
        List<String> sqlList = new ArrayList<String>();

        for ( SheetToSqlInfo info : infoList) {

            StringBuilder strBuild = new StringBuilder();
            strBuild.append( "insert into ");
            strBuild.append( info.getTableName());
            strBuild.append( " (");

            Map<String, String> columnValueMap = info.getColumnValueMap();
            List<String> columnNameList = info.getColumnNameList();

            for ( int index = 0; index < columnNameList.size(); index++) {

                String columnName = columnNameList.get( index);

                // カラム名
                strBuild.append( columnName);

                if ( index < columnNameList.size() - 1) {
                    // カンマを付与
                    strBuild.append( ",");
                }
            }

            strBuild.append( ") ");
            strBuild.append( "values");
            strBuild.append( " (");

            for ( int index = 0; index < columnNameList.size(); index++) {

                String columnName = columnNameList.get( index);
                String value = columnValueMap.get( columnName);

                // 値
                if ( value == null) {
                    // nullの場合
                    strBuild.append( "null");
                } else {
                    strBuild.append( value);
                }

                if ( index < columnNameList.size() - 1) {
                    // カンマを付与
                    strBuild.append( ",");
                }
            }

            strBuild.append( ");");

            sqlList.add( strBuild.toString());
        }

        return sqlList;
    }

    /**
     * 重複オブジェクトが存在するかを判定する。<BR>
     * リストの中に対象オブジェクトとユニークプロパティの値が<BR>
     * 全て一致するオブジェクトが存在する場合はtrueを返す。<BR>
     * 
     * @param targetInfo 対象オブジェクト
     * @param infoList SheetToSqlInfoのリスト
     * @param uniqueColumnList ユニークカラムのリスト
     * @return result 重複オブジェクトが存在する場合はtrue、存在しない場合はfalse
     */
    private boolean isDuplicateObj( SheetToSqlInfo targetInfo, List<SheetToSqlInfo> infoList, List<String> uniqueColumnList) {

        boolean result = false;

        if ( infoList.size() == 0) {
            // SheetToSqlInfoのリストが空の場合
            return false;
        }

        if ( uniqueColumnList.size() == 0) {
            // ユニークカラムのリストが空の場合
            return false;
        }

        for ( SheetToSqlInfo info : infoList) {
            // ユニークプロパティ値が重複しているかどうか
            boolean isDuplicate = true;
            Map<String, String> targetValueMap = targetInfo.getColumnValueMap();
            Map<String, String> columnValueMap = info.getColumnValueMap();

            for ( String columnName : uniqueColumnList) {

                String targetValue = targetValueMap.get( columnName);
                String infoValue = columnValueMap.get( columnName);

                if ( targetValue == null) {
                    if ( infoValue != null) {
                        isDuplicate = false;
                    }
                } else if ( !targetValue.equals( infoValue)) {
                    isDuplicate = false;
                }
            }
            if ( isDuplicate) {
                // 重複している場合
                result = true;
                break;
            }
        }
        return result;
    }

    /**
     * データコンバータを取得する
     * 
     * @return データコンバータ
     */
    public SheetToSqlDataConverter getDataConverter() {
        return dataConverter;
    }

    /**
     * データコンバータを設定する
     * 
     * @param dataConverter データコンバータ
     */
    public void setDataConverter( SheetToSqlDataConverter dataConverter) {
        this.dataConverter = dataConverter;
    }
}
